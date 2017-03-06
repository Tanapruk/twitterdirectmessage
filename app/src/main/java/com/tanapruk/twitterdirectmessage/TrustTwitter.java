package com.tanapruk.twitterdirectmessage;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static com.tanapruk.twitterdirectmessage.Constant.TWITTER_CONSUMER_KEY;
import static com.tanapruk.twitterdirectmessage.Constant.TWIT_CONSUMER_SECRET;

/**
 * Created by trusttanapruk on 3/6/2017.
 */

public class TrustTwitter {
    private static TrustTwitter instance;

    public static TrustTwitter getInstance() {
        if (instance == null) {
            instance = new TrustTwitter();
        }
      return instance;
    }

    Twitter twitter;

    public void initialize() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWIT_CONSUMER_SECRET);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public Twitter getTwitter() {
        return twitter;
    }
}
