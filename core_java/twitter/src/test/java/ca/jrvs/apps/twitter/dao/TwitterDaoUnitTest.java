package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.net.URI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

    @Mock
    HttpHelper mockHelper;

    @InjectMocks
    TwitterDao dao;

    @Test
    public void create() throws Exception {
        //test failed request
        String hashTag = "#abc";
        String text = "sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        //exception is expected here
        when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            dao.create(TweetUtil.createTweet(text, lon, lat));
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        String tweetJsonStr = "{\n"
                +"\"created_at\":\"Mon feb 18 21:24:39 +0000 2019\",\n"
                +"\"id\":1097607853932564480,\n"
                +"\"id_str\":\"1097607853932564480\",\n"
                +"\"text\":\"text with loc223\",\n"
                +"\"entities\":{\n"
                +"\"hashtags\":[],"
                +"\"user_mentions\":[]"
                +"},\n"
                +"\"coordinates\":null,"
                +"\"retweet_count\":0,\n"
                +"\"favorite_count\":0,\n"
                +"\"favorited\":false,\n"
                +"\"retweeted\":false\n"
                +"}";

        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
        //mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.create(TweetUtil.createTweet(text, lon, lat));
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }

    @Test
    public void findById() throws Exception {
        String tweetJsonStr = "{\n"
                + "  \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "  \"id\":1097607853932564480,\n"
                + "  \"id_str\":\"1097607853932564480\",\n"
                + "  \"text\":\"test with location\",\n"
                + "  \"entities\":{\n"
                + "    \"hashtags\":[],\n"
                + "    \"user_mentions\":[]\n"
                + "  },\n"
                + "  \"coordinates\":null,\n"
                + "  \"retweet_count\":0,\n"
                + "  \"favorite_count\":0,\n"
                + "  \"favorited\":false,\n"
                + "  \"retweeted\":false\n"
                + "}";

        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);

        doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.findById("1097607853932564480");
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }

    @Test
    public void deleteTweet() throws Exception {
        String tweetJsonStr = "{\n"
                + "  \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "  \"id\":1097607853932564480,\n"
                + "  \"id_str\":\"1097607853932564480\",\n"
                + "  \"text\":\"test with location\",\n"
                + "  \"entities\":{\n"
                + "    \"hashtags\":[],\n"
                + "    \"user_mentions\":[]\n"
                + "  },\n"
                + "  \"coordinates\":null,\n"
                + "  \"retweet_count\":0,\n"
                + "  \"favorite_count\":0,\n"
                + "  \"favorited\":false,\n"
                + "  \"retweeted\":false\n"
                + "}";

        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);

        doReturn(expectedTweet).doReturn(null).when(spyDao).parseResponseBody(any(), anyInt());
        Tweet tweet = spyDao.deleteById("1097607853932564480");
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
        tweet = spyDao.deleteById("1097607853932564480");
        assertNull(tweet);
    }

}