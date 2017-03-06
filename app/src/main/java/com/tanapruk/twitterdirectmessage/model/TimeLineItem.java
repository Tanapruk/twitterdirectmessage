package com.tanapruk.twitterdirectmessage.model;

import java.util.Date;

/**
 * Created by trusttanapruk on 3/6/2017.
 */

public class TimeLineItem {
    public TimeLineItem(String text, Date date) {
        this.text = text;
        this.date = date;
    }

    String text;
    Date date;

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
