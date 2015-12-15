package dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dbservice.contracts.ArticleContract;
import dbservice.contracts.HistoryOfSearchContract;

/**
 * Created by ivan on 15.12.15.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Wikipedia.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticleContract.SQL_CREATE_ENTRIES);
        db.execSQL(HistoryOfSearchContract.SQL_CREATE_ENTRIES);
    }

    //Пока оставим вот так, затем, когда версия бд станет более менее стабильной, переделаем TODO remove
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ArticleContract.SQL_DROP_TABLE);
        db.execSQL(HistoryOfSearchContract.SQL_DROP_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void cleanArticle() {
        getWritableDatabase().execSQL(ArticleContract.SQL_DELETE_ENTRIES);
        getWritableDatabase().execSQL(HistoryOfSearchContract.SQL_DELETE_ENTRIES);
    }
}
