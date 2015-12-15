package dbservice.Contracts;

import android.provider.BaseColumns;

/**
 * Created by ivan on 15.12.15.
 */
public final class ArticleContract {

    public ArticleContract() {}

    private static abstract class ArticleEntry implements BaseColumns {
        public static final String TABLE_NAME = "Article";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_logo = "logo";
    }

    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
            ArticleEntry._ID + " INTEGER PRIMARY KEY," + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_BODY + TEXT_TYPE + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_logo + TEXT_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXIST " + ArticleEntry.TABLE_NAME;
}
