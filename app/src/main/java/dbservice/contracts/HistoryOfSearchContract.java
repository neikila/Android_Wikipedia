package dbservice.contracts;

import android.provider.BaseColumns;

/**
 * Created by ivan on 15.12.15.
 */
public final class HistoryOfSearchContract {
    public HistoryOfSearchContract() {}

    public static abstract class HistoryOfSearchEntry implements BaseColumns {
        public static final String TABLE_NAME = "HistoryOfSearch";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_LOGO = "logo";
        public static final String COLUMN_NAME_TIME = "time";
    }

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryOfSearchEntry.TABLE_NAME + " (" +
                    HistoryOfSearchEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_LOGO + TEXT_TYPE + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_TIME + INT_TYPE + " )";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXIST " + HistoryOfSearchEntry.TABLE_NAME;

    public static final String SQL_DELETE_ENTRIES = "DELETE FROM " + HistoryOfSearchEntry.TABLE_NAME;

    public static final String SQL_DELETE_NOT_LAST_50_ENTRIES = "DELETE FROM "
            + HistoryOfSearchEntry.TABLE_NAME + " WHERE " + HistoryOfSearchEntry._ID + "NOT IN (SELECT "
            + HistoryOfSearchEntry._ID + " FROM " + HistoryOfSearchEntry.TABLE_NAME + " ORDER BY "
            + HistoryOfSearchEntry.COLUMN_NAME_TIME + " DESC LIMIT 50";
}
