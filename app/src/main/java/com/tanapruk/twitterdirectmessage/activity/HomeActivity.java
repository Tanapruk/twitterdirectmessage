package com.tanapruk.twitterdirectmessage.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tanapruk.twitterdirectmessage.DirectMessageFragment;
import com.tanapruk.twitterdirectmessage.R;
import com.tanapruk.twitterdirectmessage.SearchFragment;
import com.tanapruk.twitterdirectmessage.TimelineFragment;
import com.tanapruk.twitterdirectmessage.base.FragmentHelperDelegate;
import com.tanapruk.twitterdirectmessage.base.TrustActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends TrustActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    Spinner spnSwitch;
    ArrayAdapter arrayAdapter;

    FragmentHelperDelegate fragmentDelegate = new FragmentHelperDelegate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar_timeline);
        spnSwitch = (Spinner) findViewById(R.id.spn_switch_fragment);
        fragmentDelegate.bind(R.id.layout_container);

        List<String> spinnerItemList = new ArrayList<>();
        spinnerItemList.add("Timeline");
        spinnerItemList.add("Direct Message");
        spinnerItemList.add("Search");
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItemList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSwitch.setAdapter(arrayAdapter);
        spnSwitch.setOnItemSelectedListener(this);
        setSupportActionBar(toolbar);
        setTitle("");


    }


    private void openTimeline() {
        fragmentDelegate.openFragmentAndWontComeBack(TimelineFragment.newInstance());
    }

    private void openSearch() {
        fragmentDelegate.openFragmentAndWontComeBack(SearchFragment.newInstance());
    }

    private void openDirectMessage() {
        fragmentDelegate.openFragmentAndWontComeBack(DirectMessageFragment.newInstance());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(getClass().getSimpleName(), "onItemSelected: " + position);

        Object item = arrayAdapter.getItem(position);
        if ("Timeline".equals(item)) {
            openTimeline();
            return;
        }

        if ("Direct Message".equals(item)) {
            openDirectMessage();
            return;
        }

        if ("Search".equals(item)) {
            openSearch();
            return;
        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(getClass().getSimpleName(), "onNothingSelected: ");
    }
}
