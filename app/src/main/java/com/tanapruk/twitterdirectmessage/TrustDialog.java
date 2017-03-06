package com.tanapruk.twitterdirectmessage;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Akexorcist on 9/2/2016 AD.
 */

public class TrustDialog {
    private static final String TAG_AIS_DIALOG = "tag_ais_dialog";
    private static TrustDialog trustDialog;

    public static TrustDialog getInstance() {
        if (trustDialog == null) {
            trustDialog = new TrustDialog();
        }
        return trustDialog;
    }

    private DialogFragment dialog;

    public void dismissDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void showLoadingDialog(FragmentManager fragmentManager) {
        dismissDialog();
        dialog = new TrustLoadingDialog.Builder()
                .build();
        dialog.show(fragmentManager, TAG_AIS_DIALOG);
    }


}
