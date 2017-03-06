package com.tanapruk.twitterdirectmessage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import static com.tanapruk.twitterdirectmessage.Constant.OAUTH_PAGE;


public class LoginActivity extends TrustActivity {


    private static RequestToken requestToken;
    private String requestUrl;
    private static String verifier;
    /**
     * request token for accessing user account
     */

    //for error logging
    View layoutTop;
    Button btnSignin;
    WebView wvTwitterRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        layoutTop = (View) findViewById(R.id.layout_top);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        wvTwitterRedirect = (WebView) findViewById(R.id.wv_login);
        TrustTwitter.getInstance().initialize();
        btnSignin.setOnClickListener(v -> loginToTwitter());
        setupWebView();

    }


    private void setupWebView() {
        try {
            wvTwitterRedirect.getSettings().setJavaScriptEnabled(true);
        } catch (Exception e) {

        }
        wvTwitterRedirect.getSettings().setAppCacheEnabled(false);
        wvTwitterRedirect.getSettings().setSavePassword(false);
        wvTwitterRedirect.getSettings().setSaveFormData(false);
        wvTwitterRedirect.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url != null && url.startsWith(OAUTH_PAGE)) {
                    handleTwitterCallback(url);
                } else if (url != null && url.equals("https://twitter.com/")) {
                    webView.loadUrl(requestUrl);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoading();
            }
        });
    }


    private void gotoTimeline() {
        ActivityCompat.finishAffinity(this);
        Intent intent = new Intent(this, TimeLineActivity.class);
        startActivity(intent);
    }

    private void loginToTwitter() {

        btnSignin.setEnabled(false);
        Single.defer((Func0<Single<RequestToken>>) () -> {
            try {
                return Single.just(getTwitter().getOAuthRequestToken(OAUTH_PAGE));
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
                        layoutTop.setVisibility(View.GONE);
                        wvTwitterRedirect.loadUrl(requestUrl);
                        wvTwitterRedirect.setVisibility(View.VISIBLE);
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
                showLoading();
                return Single.just(getTwitter().getOAuthAccessToken(requestToken, verifier));
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
                        dismissLoading();
                        wvTwitterRedirect.setVisibility(View.GONE);
                        gotoTimeline();
                    }

                    @Override
                    public void onError(Throwable error) {
                        dismissLoading();
                        error.printStackTrace();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (wvTwitterRedirect.getVisibility() == View.VISIBLE) {
            if (wvTwitterRedirect.canGoBack()) {
                wvTwitterRedirect.goBack();
                return;
            } else {
                wvTwitterRedirect.setVisibility(View.GONE);
                btnSignin.setEnabled(true);
                layoutTop.setVisibility(View.VISIBLE);
                return;
            }
        }
    }
}
