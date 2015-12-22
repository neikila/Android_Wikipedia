package ru.mail.park.android_wikipedia;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.squareup.otto.Bus;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import utils.BitmapReady;
import utils.CleanSuccess;
import utils.NoResult;
import utils.ResultArticle;
import utils.UpdateAdapter;
import wikipedia.Article;

/**
 * Created by neikila on 13.12.15.
 */
public class ServiceHelper {
    public static final String ACTION_GET_ARTICLE = "ru.mail.park.android_wikipedia.action.GET_ARTICLE";
    public static final String ACTION_SEARCH_ARTICLES_BY_TITLE = "ru.mail.park.android_wikipedia.action.SEARCH_ARTICLES_BY_TITLE";
    public static final String ACTION_GET_RANDOM_ARTICLE = "ru.mail.park.android_wikipedia.action.GET_RANDOM_ARTICLE";
    public static final String ACTION_GET_HISTORY = "ru.mail.park.android_wikipedia.action.GET_HISTORY";
    public static final String ACTION_GET_SAVED_ARTICLES = "ru.mail.park.android_wikipedia.action.GET_SAVED_ARTICLES";
    public static final String ACTION_CLEAN_DATABASE = "ru.mail.park.android_wikipedia.action.CLEAN_DATABASE";
    public static final String ACTION_GET_DEFAULT_BITMAP = "ru.mail.park.android_wikipedia.action.GET_DEFAULT_BITMAP";
    public static final String ACTION_PREPARE_TEST_DATA = "ru.mail.park.android_wikipedia.action.PREPARE_TEST_DATA";

    public static final String TITLE = "TITLE";
    public static final String AMOUNT = "AMOUNT";

    public void getArticle(Context context, String title) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_GET_ARTICLE);
        search.putExtra(TITLE, title);
        context.startService(search);
    }

    public void findArticles(Context context, String title) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_SEARCH_ARTICLES_BY_TITLE);
        search.putExtra(TITLE, title);
        context.startService(search);
    }

    public void getRandomArticle(Context context) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_GET_RANDOM_ARTICLE);
        context.startService(search);
    }

    public void getArticlesFromHistory(Context context, int amount) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_GET_HISTORY);
        search.putExtra(AMOUNT, amount);
        context.startService(search);
    }

    public void getArticlesFromHistory(Context context) {
        getArticlesFromHistory(context, -1);
    }

    public void getSavedArticles(Context context, int amount) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_GET_SAVED_ARTICLES);
        search.putExtra(AMOUNT, amount);
        context.startService(search);
    }

    public void getSavedArticles(Context context) {
        getSavedArticles(context, -1);
    }

    public void cleanDB(Context context) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_CLEAN_DATABASE);
        context.startService(search);
    }

    public void getDefaultBitmap(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_GET_DEFAULT_BITMAP);
        context.startService(intent);
    }

    public void returnArticle(Application application, Article article) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new ResultArticle(article));
    }

    public void returnArticle(Application application, List<Article> article) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new ResultArticle(article));
    }

    public void cleanSuccess(Application application) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new CleanSuccess());
    }

    public void bitmapReady(Application application, Bitmap bitmap) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new BitmapReady(bitmap));
    }

    public void updateAdapter(Application application) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new UpdateAdapter());
    }

    public void noResult(Application application) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new NoResult());
    }

    public void prepareTestData(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PREPARE_TEST_DATA);
        context.startService(intent);
    }
}
