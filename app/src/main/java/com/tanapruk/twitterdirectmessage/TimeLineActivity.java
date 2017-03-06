package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tanapruk.twitterdirectmessage.model.TimeLineItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.TwitterException;

public class TimeLineActivity extends TrustActivity {

    RecyclerView rvTimeline;
    List<TimeLineItem> timeLineItemList;
    TimeLineAdapter timelineAdapter;
    Toolbar toolbar;
    Button btnTweet;
    Spinner spnSwitch;
    EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        rvTimeline = (RecyclerView) findViewById(R.id.rv_timeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        toolbar = (Toolbar) findViewById(R.id.toolbar_timeline);
        spnSwitch = (Spinner) findViewById(R.id.spn_switch_fragment);
        etTweet = (EditText) findViewById(R.id.et_tweet);
        btnTweet = (Button) findViewById(R.id.btn_tweet);
        btnTweet.setOnClickListener(v -> tweet());
        List<String> spinnerItemList = new ArrayList<>();
        spinnerItemList.add("Timeline");
        spinnerItemList.add("Direct Message");
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItemList);

        spnSwitch.setAdapter(arrayAdapter);
        spnSwitch.setOnItemSelectedListener(onSpnSwitchListener());
        setSupportActionBar(toolbar);
        setTitle("");
        timeLineItemList = new ArrayList<>();
        syncTimeline();
    }

    private void tweet() {
        String statusText = etTweet.getText().toString();
        etTweet.setText("");
        showLoading();
        Single.defer((Func0<Single<Status>>) () -> {
            try {
                return Single.just(getTwitter().updateStatus(statusText));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Status>() {
                    @Override
                    public void onSuccess(Status status) {
                        updateTimeline(status.getText(), status.getCreatedAt());
                        Toast.makeText(TimeLineActivity.this, "You just tweeted " + status.getText(), Toast.LENGTH_SHORT).show();
                        dismissLoading();
                        delayAndScroll();
                    }

                    @Override
                    public void onError(Throwable error) {
                        dismissLoading();
                        error.printStackTrace();
                    }
                });

    }

    @NonNull
    private AdapterView.OnItemSelectedListener onSpnSwitchListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    private void syncTimeline() {

        showLoading();
        Single.defer((Func0<Single<List<Status>>>) () -> {
            try {
                Log.d(getClass().getSimpleName(), "syncTimeline: requesting");
                return Single.just(getTwitter().getHomeTimeline());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            Log.d(getClass().getSimpleName(), "syncTimeline: null");
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<Status>>() {
                    @Override
                    public void onSuccess(List<Status> statusList) {
                        dismissLoading();
                        for (Status status : statusList) {
                            TimeLineItem timeLineItem = new TimeLineItem(status.getText(), status.getCreatedAt());
                            timeLineItemList.add(timeLineItem);
                        }
                        timelineAdapter = new TimeLineAdapter(timeLineItemList);
                        rvTimeline.setAdapter(timelineAdapter);


                    }

                    @Override
                    public void onError(Throwable error) {
                        dismissLoading();
                        error.printStackTrace();
                    }
                });


    }

    public void scrollToTop() {
        rvTimeline.scrollToPosition(0);
    }

    public void delayAndScroll() {
        Observable.empty()
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(this::scrollToTop)
                .subscribe();
    }

    public void updateTimeline(String text, Date date) {
        timeLineItemList.add(0, new TimeLineItem(text, date));
        timelineAdapter.notifyItemInserted(0);

    }
}
