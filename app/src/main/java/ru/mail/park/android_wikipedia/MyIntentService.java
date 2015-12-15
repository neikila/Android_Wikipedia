package ru.mail.park.android_wikipedia;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import dbservice.DbService;
import dbservice.DbServiceStubImpl;
import wikipedia.Article;

public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private ServiceHelper serviceHelper;
    private DbService dbService;

    public MyIntentService() {
        super("MyIntentService");
        serviceHelper = new ServiceHelper();
        dbService = new DbServiceStubImpl();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ServiceHelper.ACTION_GET_ARTICLE.equals(action)) {
                final String title = intent.getStringExtra(ServiceHelper.TITLE);
                handleGetArticle(title);
            } else if (ServiceHelper.ACTION_GET_RANDOM_ARTICLE.equals(action)) {
                handleGetRandomArticle();
            }
        }
    }

    private void handleGetArticle(String title) {
        Article article = dbService.getArticleByTitle(title);
        serviceHelper.returnArticle(getApplication(), article);
    }

    private void handleGetRandomArticle() {
        Article article = dbService.getRandomArticle();
        serviceHelper.returnArticle(getApplication(), article);
    }
}
