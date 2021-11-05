package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterService implements Service {

    private CrdDao dao;

    //@Autowired
    public TwitterService(CrdDao dao) { this.dao = dao; }

    @Override
    public Tweet postTweet(Tweet tweet) {
        validatePostTweet(tweet);

        //create tweet via dao
        return (Tweet) dao.create(tweet);
    }

    @Override
    public Tweet showTweet(String id, String[] fields) {
        validateId(id);

        //find the tweet via dao
        return (Tweet) dao.findById(id);
    }

    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        Arrays.stream(ids).forEach(id -> validateId(id));
        List<Tweet> deletedTweets = new ArrayList<>();

        //delete each tweet with corresponding ids
        Arrays.stream(ids).forEach(id->deletedTweets.add((Tweet) dao.deleteById(id)));
        return deletedTweets;
    }

    private void validatePostTweet(Tweet tweet) throws IllegalArgumentException {
        if (tweet.getText().length() > 140) {
            throw new IllegalArgumentException("Tweet cannot exceed 140 characters");
        }
        if (tweet.getCoordinates().getCoordinates().get(0) > 180 ||
                tweet.getCoordinates().getCoordinates().get(0) < -180) {
            throw new IllegalArgumentException("Longitude must fall within the range [-180, 180]");
        }
        if (tweet.getCoordinates().getCoordinates().get(1) > 90 ||
                tweet.getCoordinates().getCoordinates().get(1) < -90) {
            throw new IllegalArgumentException("Latitude must fall within the range [90, 90]");
        }
    }

    private void validateId(String id) throws IllegalArgumentException {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Id value is invalid");
        }
    }
}
