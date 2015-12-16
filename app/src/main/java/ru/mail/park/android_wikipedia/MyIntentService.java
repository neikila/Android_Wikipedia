package ru.mail.park.android_wikipedia;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import dbservice.DbService;
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
        // TODO как получать базу
        dbService = ((ApplicationModified)getApplication()).getDbService();
        if (intent != null) {
            final String action = intent.getAction();
            if (ServiceHelper.ACTION_GET_ARTICLE.equals(action)) {
                final String title = intent.getStringExtra(ServiceHelper.TITLE);
                // TODO вернуть когда появится возможность проверить безопасно наличие статьи в базе
//                handleGetArticle(title);
            } else if (ServiceHelper.ACTION_GET_RANDOM_ARTICLE.equals(action)) {
                handleGetRandomArticle();
            } else if (ServiceHelper.ACTION_GET_HISTORY.equals(action)) {
                handleGetHistory(intent.getIntExtra(ServiceHelper.AMOUNT ,0));
            } else if (ServiceHelper.ACTION_GET_SAVED_ARTICLES.equals(action)) {
                handleGetSaved(intent.getIntExtra(ServiceHelper.AMOUNT ,0));
            } else if (ServiceHelper.ACTION_CLEAN_DATABASE.equals(action)) {
                handleCleanDB();
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
        for (Article article: result) {
            setBitmap(article);
        }
        serviceHelper.returnArticle(getApplication(), result);
    }

    private void handleCleanDB() {
        dbService.clean();
        serviceHelper.cleanSuccess(getApplication());
    }

    private void setBitmap(Article article) {
        try {
            Bitmap logo = Picasso.with(this)
                    .load(R.drawable.test)
                    .placeholder(R.drawable.test1)
                    .error(R.drawable.test1)
                    .resize(200, 200)
                    .get();
            article.setLogoBitmap(logo);
        } catch (IOException e) {
            Log.d("getBitmap", e.toString());
        }
    }
}
