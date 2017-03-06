package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tanapruk.twitterdirectmessage.base.TrustFragment;
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

/**
 * Created by trusttanapruk on 3/6/2017.
 */

public class TimelineFragment extends TrustFragment {
    RecyclerView rvTimeline;
    TimeLineAdapter timelineAdapter;
    List<TimeLineItem> timeLineItemList;
    Button btnTweet;
    EditText etTweet;

    public static TimelineFragment newInstance() {

        Bundle args = new Bundle();
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        rvTimeline = (RecyclerView) view.findViewById(R.id.rv_timeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        etTweet = (EditText) view.findViewById(R.id.et_tweet);
        btnTweet = (Button) view.findViewById(R.id.btn_tweet);
        btnTweet.setOnClickListener(v -> tweet());
        syncTimeline();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeLineItemList = new ArrayList<>();


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
                        Toast.makeText(getActivity(), "You just tweeted " + status.getText(), Toast.LENGTH_SHORT).show();
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
