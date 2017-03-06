package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.List;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TimeLineActivity extends TrustActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        setTitle("Time Line");
        initTwitterClient();
        syncTimeline();
    }

    private void syncTimeline() {

        showLoading();
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

                        Log.d(getClass().getSimpleName(), new GsonBuilder().setPrettyPrinting().create().toJson(statusList.get(0)));
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable error) {
                        dismissLoading();
                        error.printStackTrace();
                    }
                });


    }
}
