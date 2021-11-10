package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ca.jrvs.apps.twitter.util.JsonUtil;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

    static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

    //URI Constraints
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";
    //URI Symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    //Response code
    private static final int HTTP_OK = 200;

    private HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    /**
     * Creates a tweet object
     * @param tweet entity that is to be created
     * @return created entity
     */
    @Override
    public Tweet create(Tweet tweet) {
        URI uri;
        uri = getPostUri(tweet);

        //Execute HTTP Request
        HttpResponse response = httpHelper.httpPost(uri);
        return parseResponseBody(response, HTTP_OK);
    }

    /**
     * Find an entity(Tweet) by its id
     * @param s tweet id
     * @return found tweet entity
     */
    @Override
    public Tweet findById(String s) {
        URI uri;
        try {
            String uriStr = API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s;
            uri = new URI(uriStr);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid id input", ex);
        }

        //Execute HTTP Request
        HttpResponse response = httpHelper.httpGet(uri);
        return parseResponseBody(response, HTTP_OK);
    }

    /**
     * Deletes an entity(Tweet) from its id
     * @param s tweet id to be deleted
     * @return deleted tweet entity
     */
    @Override
    public Tweet deleteById(String s) {
        URI uri;
        try {
            String uriStr = API_BASE_URI + DELETE_PATH + "/" + s + ".json";
            uri = new URI(uriStr);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid id input", ex);
        }

        //Execute HTTP Response
        HttpResponse response = httpHelper.httpPost(uri);
        return parseResponseBody(response, HTTP_OK);
    }

    private URI getPostUri(Tweet tweet) {
        URI uri;
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        try {
            uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + percentEscaper
                    .escape(tweet.getText()) + AMPERSAND + "long" + EQUAL + tweet.getCoordinates()
                    .getCoordinates().get(0) + AMPERSAND + "lat" + EQUAL + tweet.getCoordinates()
                    .getCoordinates().get(1));
            return uri;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid input", ex);
        }
    }

    public Tweet parseResponseBody(HttpResponse response, int httpOk) {
        Tweet tweet = null;
        int status = response.getStatusLine().getStatusCode();
        if (status != httpOk) {
            try {
                logger.error(EntityUtils.toString(response.getEntity()));
            } catch (IOException ex) {
                logger.error("ERROR: Response has no entity");
            }
            throw new RuntimeException("Unexpected HTTP status code:" + status);
        }

        if (response.getEntity() == null) {
            throw new RuntimeException("Empty response body");
        }

        //Convert Response Entity to string
        String jsonStr;
        try {
            jsonStr = EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to convert entity to a string", ex);
        }

        //convert json to tweet object
        try {
            tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to convert json object to tweet object", ex);
        }

        return tweet;
    }
}