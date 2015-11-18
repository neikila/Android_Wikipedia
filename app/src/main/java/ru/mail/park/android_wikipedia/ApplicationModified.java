package ru.mail.park.android_wikipedia;

import android.app.Application;

import dbservice.DbService;
import dbservice.DbServiceStubImpl;

//import com.squareup.otto.Bus;
//import com.squareup.otto.ThreadEnforcer;

/**
 * Created by neikila on 29.09.15.
 */
public class ApplicationModified extends Application {
//    private Bus bus;
    private DbService dbService;

    @Override
    public void onCreate() {
//        bus = new Bus(ThreadEnforcer.ANY);
        dbService = new DbServiceStubImpl();
        super.onCreate();
    }

    public DbService getDbService() {
        return dbService;
    }

//    public Bus getBus() {
//        return bus;
//    }
}
