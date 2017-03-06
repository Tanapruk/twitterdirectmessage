package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TrustActivity extends AppCompatActivity {

    Twitter twitter;
    public final static String TWITTER_CONSUMER_KEY = "VSH2wsA0BBW7IlDP3wqLvU951";
    public final static String TWIT_CONSUMER_SECRET = "boCM4IqCt09CvXeb1NprSqKywRZNNfb3yY04c3xs622D0rLJNu";
    public final static String OAUTH_PAGE = "oauth:///tanapruk";

    public void initTwitterClient() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWIT_CONSUMER_SECRET);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust);
    }

    public void showLoading() {
        TrustDialog.getInstance().showLoadingDialog(getSupportFragmentManager());
    }

    public void dismissLoading() {
        TrustDialog.getInstance().dismissDialog();
    }
}
