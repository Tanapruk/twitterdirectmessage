package com.tanapruk.twitterdirectmessage;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.List;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends TrustActivity {

    public final static String TWITTER_CONSUMER_KEY = "VSH2wsA0BBW7IlDP3wqLvU951";
    public final static String TWIT_CONSUMER_SECRET = "boCM4IqCt09CvXeb1NprSqKywRZNNfb3yY04c3xs622D0rLJNu";
    public final static String OAUTH_PAGE = "oauth:///tanapruk";
    private static RequestToken requestToken;
    private String requestUrl;
    private static String verifier;
    private Twitter twitter;
    /**
     * request token for accessing user account
     */

    //for error logging

    Button btnSignin;
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignin = (Button) findViewById(R.id.btn_signin);
        mWebView = (WebView) findViewById(R.id.wv_login);
        btnSignin.setOnClickListener(v -> loginToTwitter());
        initTwitterClient();
        setupWebView();

    }

    private void setupWebView() {
        try {
            mWebView.getSettings().setJavaScriptEnabled(true);
        } catch (Exception e) {

        }
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSaveFormData(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.v("login", "url: " + url);
                if (url != null && url.startsWith(OAUTH_PAGE)) {
                    handleTwitterCallback(url);
                } else if (url != null && url.equals("https://twitter.com/")) {
                    webView.loadUrl(requestUrl);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }
        });
    }

    private void initTwitterClient() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWIT_CONSUMER_SECRET);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }


    private void loginToTwitter() {

        if (btnSignin.getText().equals("Sync Now")) {
            syncTimeline();
            return;
        }

        Single.defer((Func0<Single<RequestToken>>) () -> {
            try {
                return Single.just(twitter.getOAuthRequestToken(OAUTH_PAGE));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<RequestToken>() {
                    @Override
                    public void onSuccess(RequestToken value) {
                        requestToken = value;
                        requestUrl = requestToken.getAuthenticationURL();
                        mWebView.loadUrl(requestUrl);
                        mWebView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });


    }

    private void syncTimeline() {

        Log.d(getClass().getSimpleName(), "syncTimeline: ");
        Single.defer((Func0<Single<List<Status>>>) () -> {
            try {
                return Single.just(twitter.getHomeTimeline());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Status>>() {
                    @Override
                    public void onSuccess(List<Status> statusList) {
                        for (Status status : statusList) {
                            Log.d(getClass().getSimpleName(), "onSuccess: " + status);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });


    }


    public void handleTwitterCallback(String url) {

        // oAuth verifier
        verifier = Uri.parse(url).getQueryParameter("oauth_verifier");

        Single.defer((Func0<Single<AccessToken>>) () -> {
            try {
                return Single.just(twitter.getOAuthAccessToken(requestToken, verifier));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<AccessToken>() {

                    @Override
                    public void onSuccess(AccessToken value) {
                        mWebView.setVisibility(View.GONE);
                        btnSignin.setText("Sync Now");
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });

    }

}
