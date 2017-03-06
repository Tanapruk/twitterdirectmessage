package com.tanapruk.twitterdirectmessage;

/**
 * Created by trusttanapruk on 3/6/2017.
 */

public class AppSettings {
    private static AppSettings instance;

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
      return instance;
    }
}
