package ru.mail.park.android_wikipedia;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dbservice.DbHelper;
import dbservice.DbService;
import dbservice.DbServiceImpl;
import dbservice.DbServiceStubImpl;
import wikipedia.Article;

//import com.squareup.otto.Bus;
//import com.squareup.otto.ThreadEnforcer;

/**
 * Created by neikila on 29.09.15.
 */
public class ApplicationModified extends Application {
    private Bus bus;
    private DbService dbService;

    @Override
    public void onCreate() {
        bus = new Bus(ThreadEnforcer.ANY);
        super.onCreate();
        dbService = new DbServiceImpl(this);
        dbService.clean();
        dbService.saveArticleInHistory(new Article("Test article", "Test article/1", "qwe.com/1"));
        dbService.saveArticle(new Article("Saved test article", "Saved test article/1", "qwe.com/2"));
        dbService.saveArticleInHistory(new Article("Test article1", "Test article1/1", "qwe.com1/1"));
        dbService.saveArticle(new Article("Saved test article1", "Saved test article2/1", "qwe.com2/2"));
    }

    public DbService getDbService() {
        return dbService;
    }

    public Bus getBus() {
        return bus;
    }
}
