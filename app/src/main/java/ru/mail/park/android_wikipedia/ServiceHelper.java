package ru.mail.park.android_wikipedia;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.squareup.otto.Bus;

import java.util.concurrent.atomic.AtomicLong;

import utils.ResultArticle;
import wikipedia.Article;

/**
 * Created by neikila on 13.12.15.
 */
public class ServiceHelper {
    public static final String EXTRA_PARAM1 = "ru.mail.park.android_wikipedia.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "ru.mail.park.android_wikipedia.extra.PARAM2";
    public static final String ACTION_GET_ARTICLE = "ru.mail.park.android_wikipedia.action.GET_ARTICLE";
    public static final String ACTION_GET_RANDOM_ARTICLE = "ru.mail.park.android_wikipedia.action.GET_RANDOM_ARTICLE";

    public static final String TITLE = "TITLE";
    public AtomicLong id = new AtomicLong(0);

    public long getArticle(Context context, String title) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_GET_ARTICLE);
        search.putExtra(TITLE, title);
        context.startService(search);
        return id.incrementAndGet();
    }

    public long getRandomArticle(Context context) {
        Intent search = new Intent(context, MyIntentService.class);
        search.setAction(ACTION_GET_RANDOM_ARTICLE);
        context.startService(search);
        return id.incrementAndGet();
    }

    public void returnArticle(Application application, Article article) {
        Bus bus = ((ApplicationModified)application).getBus();
        bus.post(new ResultArticle(article));
    }
}
