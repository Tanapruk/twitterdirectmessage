package com.tanapruk.twitterdirectmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TrustActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust);
    }

    public void showLoading() {
        TrustDialog.getInstance().showLoadingDialog(getSupportFragmentManager());
    }

    public void dismissDialog() {
        TrustDialog.getInstance().dismissDialog();
    }
}
