package ru.mail.park.android_wikipedia;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dbservice.DbService;
import dbservice.DbServiceImpl;
import wikipedia.Article;

/**
 * Created by neikila on 29.09.15.
 */
public class ApplicationModified extends Application {
    private Bus bus;

    @Override
    public void onCreate() {
        bus = new Bus(ThreadEnforcer.ANY);
        super.onCreate();
    }

    public Bus getBus() {
        return bus;
    }
}
