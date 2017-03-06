package com.tanapruk.twitterdirectmessage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tanapruk.twitterdirectmessage.base.TrustFragment;
import com.tanapruk.twitterdirectmessage.model.TimeLineItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by trusttanapruk on 3/6/2017.
 */

public class SearchFragment extends TrustFragment {
    RecyclerView rvTimeline;
    TimeLineAdapter timelineAdapter;
    List<TimeLineItem> timeLineItemList;
    Button btnSearch;
    EditText etSearch;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        rvTimeline = (RecyclerView) view.findViewById(R.id.rv_search_result);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        etSearch = (EditText) view.findViewById(R.id.et_search);
//        RxTextView.textChanges(etSearch)
//                .debounce(300, TimeUnit.MILLISECONDS)
//                .subscribe(action -> {
//                    Log.d(getClass().getSimpleName(), "onViewCreated: " + etSearch.getText());
//                });
        btnSearch = (Button) view.findViewById(R.id.btn_search);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeLineItemList = new ArrayList<>();


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
