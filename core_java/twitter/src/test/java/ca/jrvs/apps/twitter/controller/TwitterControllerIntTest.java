package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelperTest;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerIntTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);
    TwitterDao twitterDao;
    Tweet newTweet;
    TwitterService twitterService;
    TwitterController controller;
    Tweet tweet;

    @Before
    public void setup() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        logger.debug(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        CrdDao dao = new TwitterDao(httpHelper);
        twitterService = new TwitterService(dao);
        controller = new TwitterController(twitterService);

        String hashtag = "#canada";
        String text = "Helloworld! " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        tweet = TweetUtil.buildTweet(text, lat, lon);
        tweet = twitterService.postTweet(tweet);
    }

    @After
    public void tearDown() {
        try {
            String[] tweets = new String[] {tweet.getIdStr()};
            twitterService.deleteTweets(tweets);
        } catch (RuntimeException ex) {
            logger.debug("Tweet was already deleted");
        }
    }

    @Test
    public void postTweet() {
        String hashtag = "#canada";
        String text = "Helloworld! " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = TweetUtil.buildTweet(text, lat, lon);

        String[] args = new String[]{"post",text,lat + ":" + lon};
        newTweet = controller.postTweet(args);

        assertNotNull(newTweet.getCoordinates());
        assertEquals(tweet.getText(), newTweet.getText());
        assertEquals(tweet.getCoordinates().getCoordinates().get(0), newTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(tweet.getCoordinates().getCoordinates().get(1), newTweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() {
        String[] args = new String[]{"show",tweet.getIdStr(), "id,text,retweet_count"};
        newTweet = controller.showTweet(args);

        assertEquals(tweet.getIdStr(), newTweet.getIdStr());
        assertEquals(tweet.getText(), newTweet.getText());
        assertEquals(tweet.getRetweetCount(), newTweet.getRetweetCount());
    }

    @Test
    public void deletetweet() {
        String[] args = new String[]{"delete", tweet.getIdStr()};
        List<Tweet> deletedTweets = controller.deleteTweet(args);
        assertEquals(tweet, deletedTweets.get(0));
    }

}