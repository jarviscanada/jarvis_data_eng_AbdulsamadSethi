package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

public class TwitterServiceIntTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);
    private TwitterService twitterService;
    private Tweet testTweet;

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

        String hashtag = "#canada";
        String text = "Helloworld! " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = TweetUtil.buildTweet(text, lat, lon);
        testTweet = twitterService.postTweet(tweet);
    }

    @After
    public void tearDown() {
        try {
            String[] tweets = new String[] {testTweet.getIdStr()};
            twitterService.deleteTweets(tweets);
        } catch (RuntimeException ex) {
            logger.debug("Tweet was already deleted");
        }
    }

    @Test
    public void postTweet() throws IllegalArgumentException {
        String hashtag = "#canada";
        String text = "Helloworld! " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = TweetUtil.buildTweet(text, lat, lon);

        Tweet postedTweet = twitterService.postTweet(tweet);

        assertEquals(text, postedTweet.getText());

        assertNotNull(postedTweet.getCoordinates());

        assertEquals(2, postedTweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, postedTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, postedTweet.getCoordinates().getCoordinates().get(1));

        assertTrue(hashtag.contains(postedTweet.getEntities().getHashtags().get(0).getText()));

        String longText = new String(new char[140]).replace('\0', ' ');
        longText = longText + System.currentTimeMillis();
        Tweet invalidTweet = TweetUtil.buildTweet(longText, lat, lon);

        try {
            twitterService.postTweet(invalidTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double invalidLat = 200d;
        invalidTweet = TweetUtil.buildTweet(text, invalidLat, lon);

        try {
            twitterService.postTweet(invalidTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double invalidLon = 100d;
        invalidTweet = TweetUtil.buildTweet(text, lat, invalidLon);

        try {
            twitterService.postTweet(invalidTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() {
        Tweet tweet = twitterService.showTweet(testTweet.getIdStr(), new String[]{});
        assertEquals(tweet, testTweet);

        String invalidId = "Abdul";

        try {
            tweet = twitterService.showTweet(invalidId, new String[]{});
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweets() {
        String[] deleteIds = new String[]{testTweet.getIdStr()};
        List<Tweet> deletedTweets = twitterService.deleteTweets(deleteIds);

        assertEquals(testTweet, deletedTweets.get(0));

        try {
            twitterService.deleteTweets(deleteIds);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        String[] invalidIds = new String[]{"nikki"};
        try {
            twitterService.deleteTweets(invalidIds);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

}
