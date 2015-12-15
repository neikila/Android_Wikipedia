package dbservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.List;

import dbservice.Contracts.ArticleContract.ArticleEntry;
import dbservice.Contracts.HistoryOfSearchContract.HistoryOfSearchEntry;
import wikipedia.Article;

/**
 * Created by ivan on 15.12.15.
 */
public class DbServiceImpl implements DbService {
    private DbHelper dbHelper;

    public DbServiceImpl(Context context) {
        dbHelper = new DbHelper(context);
    }

    @Override
    public void clean() {
        dbHelper.cleanArticle();
    }

    @Override
    public List<String> getArticlesNameFromHistory() {
        return null;
    }

    @Override
    public List<String> getArticlesNameFromHistory(int length) {
        return null;
    }

    @Override
    public List<String> getSavedArticlesName() {
        return null;
    }

    @Override
    public List<String> getSavedArticlesNames(int length) {
        Calendar calendar =  Calendar.getInstance();
        calendar.getTimeInMillis();
        return null;
    }

    @Override
    public void saveArticleInHistory(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HistoryOfSearchEntry.COLUMN_NAME_TITLE, article.getTitle());
        values.put(HistoryOfSearchEntry.COLUMN_NAME_LINK, article.getLink());
        values.put(HistoryOfSearchEntry.COLUMN_NAME_LOGO, article.getLogo());
        values.put(HistoryOfSearchEntry.COLUMN_NAME_TIME, Calendar.getInstance().getTimeInMillis());

        db.insert(HistoryOfSearchEntry.TABLE_NAME, null, values);
    }

    @Override
    public void saveArticle(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ArticleEntry.COLUMN_NAME_TITLE, article.getTitle());
        values.put(ArticleEntry.COLUMN_NAME_BODY, article.getBody());
        values.put(ArticleEntry.COLUMN_NAME_LOGO, article.getLogo());
        values.put(ArticleEntry.COLUMN_NAME_LINK, article.getLink());
        values.put(ArticleEntry.COLUMN_NAME_TIME, Calendar.getInstance().getTimeInMillis());

        db.insert(ArticleEntry.TABLE_NAME, null, values);
    }

    @Override
    public Article getArticleByTitle(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ArticleEntry.COLUMN_NAME_TITLE,
                ArticleEntry.COLUMN_NAME_LOGO,
                ArticleEntry.COLUMN_NAME_LINK,
                ArticleEntry.COLUMN_NAME_BODY
        };

        String selection = ArticleEntry.COLUMN_NAME_TITLE + " = ?";
        String selectionArgs[] = {
                title
        };

        Cursor c = db.query(ArticleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        c.moveToFirst();
        Article article = new  Article(
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_TITLE)),
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LOGO)),
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LINK)),
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_BODY))
        );

        c.close();
        return  article;
    }

    //todo
    @Override
    public Article getRandomArticle() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ArticleEntry.COLUMN_NAME_TITLE,
                ArticleEntry.COLUMN_NAME_LOGO,
                ArticleEntry.COLUMN_NAME_LINK,
                ArticleEntry.COLUMN_NAME_BODY
        };

        String orderBy = "RANDOM()";
        String limit = "1";

        Cursor c = db.query(ArticleEntry.TABLE_NAME, projection, null, null, null, null, orderBy, limit);
        c.moveToFirst();
        Article article = new Article(
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_TITLE)),
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LOGO)),
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LINK)),
                c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_BODY))
        );

        c.close();
        return  article;
    }
}
