package ru.mail.park.android_wikipedia;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;

import java.util.List;

import processor.Processor;
import wikipedia.Article;

public class MyIntentService extends IntentService {
    private ServiceHelper serviceHelper;
//    private DbService dbService;
    private Processor processor;

    public MyIntentService() {
        super("MyIntentService");
        serviceHelper = new ServiceHelper();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO как получать базу
//        dbService = ((ApplicationModified)getApplication()).getDbService();
        processor = new Processor(this);
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
            } else if (ServiceHelper.ACTION_GET_DEFAULT_BITMAP.equals(action)) {
                handleGetDefaultBitmap();
            }
        }
    }

    private void handleGetArticle(String title) {
        Article article = processor.getArticleByTitle(title);
        serviceHelper.returnArticle(getApplication(), article);
    }

    private void handleGetRandomArticle() {
        Article article = processor.getRandomArticle();
        serviceHelper.returnArticle(getApplication(), article);
    }

    private void handleGetHistory(int amount) {
        List<Article> result = processor.getHistory(amount);
        serviceHelper.returnArticle(getApplication(), result);
    }

    private void handleGetSaved(int amount) {
        List<Article> result = processor.getSaved(amount);
//        processor.setBitmap(result);
        serviceHelper.returnArticle(getApplication(), result);
    }

    private void handleCleanDB() {
        processor.clean();
        serviceHelper.cleanSuccess(getApplication());
    }

    private void handleGetDefaultBitmap() {
        Bitmap bitmap= processor.getDefaultBitmap();
        serviceHelper.bitmapReady(getApplication(), bitmap);
    }
}
