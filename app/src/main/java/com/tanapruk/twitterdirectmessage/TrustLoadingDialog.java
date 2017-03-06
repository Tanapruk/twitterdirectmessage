package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


/**
 * Created by Akexorcist on 8/3/15 AD.
 */
public class TrustLoadingDialog extends DialogFragment {

    public static TrustLoadingDialog newInstance() {
        return new TrustLoadingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);
        return inflater.inflate(R.layout.my_ais_library_dialog_loading, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setupView();
    }

    private void bindView(View view) {
    }

    private void setupView() {
    }

    public static class Builder {

        public Builder() {
        }

        public TrustLoadingDialog build() {
            return TrustLoadingDialog.newInstance();
        }
    }
}
