package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

    @Mock
    CrdDao dao;

    @InjectMocks
    TwitterService service;

    @Test
    public void postTweet() {
        when(dao.create(any())).thenReturn(new Tweet());
        String text = "Some text";
        Double lat = 1.0d;
        Double lon = 1.0d;
        Tweet tweet = service.postTweet(TweetUtil.buildTweet(text, lat, lon));
        assertNotNull(tweet);

        String invalidText = new String(new char[140]).replace('\0', ' ');

        invalidText = invalidText + " " + System.currentTimeMillis();
        Tweet invalidTweet = TweetUtil.buildTweet(invalidText, lat, lon);

        try {
            service.postTweet(invalidTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double invalidLat = 200d;
        invalidTweet = TweetUtil.buildTweet(text, invalidLat, lon);

        try {
            service.postTweet(invalidTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        Double invalidLon = 200d;
        invalidTweet = TweetUtil.buildTweet(text, lat, invalidLon);

        try {
            service.postTweet(invalidTweet);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() {
        when(dao.findById(anyString())).thenReturn(new Tweet());
        Tweet tweet = service.showTweet("123456789", new String[]{});
        assertNotNull(tweet);

        try {
            service.showTweet("hey", new String[]{});
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweets() {
        when(dao.deleteById(anyString())).thenReturn(new Tweet());
        List<Tweet> tweets = service.deleteTweets(new String[]{"123456789"});
        assertNotNull(tweets);
        assertNotNull(tweets.get(0));

        try {
            service.deleteTweets(new String[]{"hey"});
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }
}
