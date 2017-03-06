package com.tanapruk.twitterdirectmessage.base;


import android.support.v4.app.Fragment;

import com.tanapruk.twitterdirectmessage.TrustDialog;
import com.tanapruk.twitterdirectmessage.TrustTwitter;

import twitter4j.Twitter;

/**
 * Created by trusttanapruk on 3/6/2017.
 */

abstract public class TrustFragment extends Fragment {
    public Twitter getTwitter() {
        return TrustTwitter.getInstance().getTwitter();
    }
    public void showLoading() {
        TrustDialog.getInstance().showLoadingDialog(getFragmentManager());
    }

    public void dismissLoading() {
        TrustDialog.getInstance().dismissDialog();
    }
}
