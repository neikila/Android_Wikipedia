package dbservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dbservice.contracts.ArticleContract.ArticleEntry;
import dbservice.contracts.HistoryOfSearchContract;
import dbservice.contracts.HistoryOfSearchContract.HistoryOfSearchEntry;
import wikipedia.Article;

/**
 * Created by ivan on 15.12.15.
 */
public class DbServiceImpl implements DbService {
    private DbHelper dbHelper;
    private String HISTORY_TIME_DESC_SORT = HistoryOfSearchEntry.COLUMN_NAME_TIME + " DESC";
    private String ARTICLE_TIME_DESC_SORT = HistoryOfSearchEntry.COLUMN_NAME_TIME + " DESC";

    public DbServiceImpl(Context context) {
        dbHelper = new DbHelper(context);
    }

    @Override
    public void clean() {
        dbHelper.cleanArticle();
    }


    @Override
    public List<Article> getArticlesFromHistory() {
        return getArticlesFromHistory(null);
    }

    @Override
    public List<Article> getArticlesFromHistory(Integer length) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                HistoryOfSearchEntry.COLUMN_NAME_TITLE,
                HistoryOfSearchEntry.COLUMN_NAME_LOGO,
                HistoryOfSearchEntry.COLUMN_NAME_LINK
        };

        Cursor c;
        if(length != null) {
            c = db.query(HistoryOfSearchEntry.TABLE_NAME, projection,
                    null, null, null, null, HISTORY_TIME_DESC_SORT, Integer.toString(length));
        } else {
            c = db.query(HistoryOfSearchEntry.TABLE_NAME, projection,
                        null, null, null, null, HISTORY_TIME_DESC_SORT);
        }
        List<Article> articles = new ArrayList<>();

        if(c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Article article = new Article(
                        c.getString(c.getColumnIndex(HistoryOfSearchEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndex(HistoryOfSearchEntry.COLUMN_NAME_LOGO)),
                        c.getString(c.getColumnIndex(HistoryOfSearchEntry.COLUMN_NAME_LINK))
                );
                articles.add(article);
                c.moveToNext();
            }
        }

        c.close();
        return articles;
    }

    @Override
    public List<Article> getSavedArticles() {
        return getSavedArticles(null);
    }

    @Override
    public List<Article> getSavedArticles(Integer length) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ArticleEntry.COLUMN_NAME_TITLE,
                ArticleEntry.COLUMN_NAME_LOGO,
                ArticleEntry.COLUMN_NAME_LINK,
                ArticleEntry.COLUMN_NAME_BODY
        };

        Cursor c;
        if(length != null) {
            c = db.query(ArticleEntry.TABLE_NAME, projection,
                    null, null, null, null, ARTICLE_TIME_DESC_SORT, Integer.toString(length));
        } else {
            c = db.query(ArticleEntry.TABLE_NAME, projection,
                    null, null, null, null, ARTICLE_TIME_DESC_SORT);
        }
        List<Article> articles = new ArrayList<>();
        if(c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Article article = new Article(
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LOGO)),
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LINK)),
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_BODY))
                );
                articles.add(article);
                c.moveToNext();
            }
        }
        c.close();
        return articles;
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
        db.execSQL(HistoryOfSearchContract.SQL_DELETE_NOT_LAST_50_ENTRIES);
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
        Article article = null;

        if (c.getCount() > 0) {
            c.moveToFirst();
            article = new Article(
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_TITLE)),
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LOGO)),
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LINK)),
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_BODY))
            );
        }

        c.close();
        return  article;
    }

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
        Article article = null;

        if(c.getCount() > 0) {
            c.moveToFirst();
            article = new Article(
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_TITLE)),
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LOGO)),
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LINK)),
                    c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_BODY))
            );
        }

        c.close();
        return  article;
    }

    @Override
    public List<Article> getArticleLikeByTitle(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ArticleEntry.COLUMN_NAME_TITLE,
                ArticleEntry.COLUMN_NAME_LOGO,
                ArticleEntry.COLUMN_NAME_LINK,
                ArticleEntry.COLUMN_NAME_BODY
        };

        String selection = ArticleEntry.COLUMN_NAME_TITLE + " LIKE '?'";
        String selectionArgs[] = {
                title
        };

        Cursor c = db.query(ArticleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        List<Article> articles = new ArrayList<>();
        if(c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Article article = new Article(
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_TITLE)),
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LOGO)),
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_LINK)),
                        c.getString(c.getColumnIndex(ArticleEntry.COLUMN_NAME_BODY))
                );
                articles.add(article);
                c.moveToNext();
            }
        }

        c.close();
        return  articles;
    }
}
