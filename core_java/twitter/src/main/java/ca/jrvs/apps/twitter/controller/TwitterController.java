package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {

    private static final String COORD_SEP = ":";
    private static final String COMMA = ",";

    private Service service;

    @Autowired
    public TwitterController(Service service) { this.service = service; }

    /**
     * Parse user argument and post a tweet by calling service classes
     *
     * @param args
     * @return a posted tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet postTweet(String[] args) throws IllegalArgumentException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Usage: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        Tweet tweet =  new Tweet();
        String text = args[1];
        String coords = args[2];
        String[] coordArr = coords.split(COORD_SEP);
        if (coordArr.length < 2) {
            throw new IllegalArgumentException("Invalid cooridnates. Usage: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        Double lat;
        Double lon;
        try {
            lat = Double.parseDouble(coordArr[0]);
            lon = Double.parseDouble(coordArr[1]);
        } catch (Exception ex){
            throw new IllegalArgumentException("Invalid cooridnates. Usage: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
        }
        tweet = TweetUtil.buildTweet(text, lat, lon);
        return service.postTweet(tweet);
    }

    /**
     * Parse user argument and search a tweet by calling service classes
     *
     * @param args
     * @return a tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet showTweet(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: TwitterCLIApp show tweet_id [field1,fields2]");
        }
        Tweet tweet =  new Tweet();
        String id = args[1];
        String[] fieldsArr = {};
        if (args.length == 3) {
            String fields = args[2];
            fieldsArr = fields.split(COMMA);
        }
        return service.showTweet(id, fieldsArr);
    }

    /**
     * Parse user argument and delete tweets by calling service classes
     *
     * @param args
     * @return a list of deleted tweets
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: TwitterApp delete [id1,id2,..]");
        }
        String[] deleteIds = args[1].split(COMMA);
        return service.deleteTweets(deleteIds);
    }
}