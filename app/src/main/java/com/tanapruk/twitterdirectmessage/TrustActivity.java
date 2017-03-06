package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import twitter4j.Twitter;

public class TrustActivity extends AppCompatActivity {



    public Twitter getTwitter() {
        return TrustTwitter.getInstance().getTwitter();
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
