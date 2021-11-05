package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TwitterHttpHelperTest {

    @Test
    public void httpGet() throws Exception {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        HttpResponse httpResponse = httpHelper
                .httpGet(new URI("https://api.twitter.com/1.1/users/search.json?q=nikkidaly15"));
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Test
    public void httpPost() throws Exception {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        HttpResponse httpResponse = httpHelper
                .httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=first_tweet2"));
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }
}
