package dbservice.contracts;

import android.provider.BaseColumns;

/**
 * Created by ivan on 15.12.15.
 */
public final class ArticleContract {

    public ArticleContract() {}

    public static abstract class ArticleEntry implements BaseColumns {
        public static final String TABLE_NAME = "Article";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BODY = "body";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_TIME = "time";
    }

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
            ArticleEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_BODY + TEXT_TYPE + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_LOGO + TEXT_TYPE + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
            ArticleEntry.COLUMN_NAME_TIME + INT_TYPE + " )";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST " + ArticleEntry.TABLE_NAME;

    public static final String SQL_DELETE_ENTRIES = "DELETE FROM " + ArticleEntry.TABLE_NAME;
}
