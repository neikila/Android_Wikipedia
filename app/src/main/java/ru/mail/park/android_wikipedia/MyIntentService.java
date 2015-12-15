package ru.mail.park.android_wikipedia;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import dbservice.DbService;
import dbservice.DbServiceStubImpl;
import wikipedia.Article;

public class MyIntentService extends IntentService {
    private ServiceHelper serviceHelper;
    private DbService dbService;

    public MyIntentService() {
        super("MyIntentService");
        serviceHelper = new ServiceHelper();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        dbService = ((ApplicationModified)getApplication()).getDbService();
        if (intent != null) {
            final String action = intent.getAction();
            if (ServiceHelper.ACTION_GET_ARTICLE.equals(action)) {
                final String title = intent.getStringExtra(ServiceHelper.TITLE);
                handleGetArticle(title);
            } else if (ServiceHelper.ACTION_GET_RANDOM_ARTICLE.equals(action)) {
                handleGetRandomArticle();
            } else if (ServiceHelper.ACTION_GET_HISTORY.equals(action)) {
                handleGetHistory(intent.getIntExtra(ServiceHelper.AMOUNT ,0));
            } else if (ServiceHelper.ACTION_GET_SAVED_ARTICLES.equals(action)) {
                handleGetSaved(intent.getIntExtra(ServiceHelper.AMOUNT ,0));
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

    private void handleGetHistory(int amount) {
        List<Article> result;
        if (amount > 0) {
            result = dbService.getArticlesFromHistory(amount);
        } else {
            result = dbService.getArticlesFromHistory();
        }
        serviceHelper.returnArticle(getApplication(), result);
    }

    private void handleGetSaved(int amount) {
        List<Article> result;
        if (amount > 0) {
            result = dbService.getSavedArticles(amount);
        } else {
            result = dbService.getSavedArticles();
        }
        serviceHelper.returnArticle(getApplication(), result);
    }
}