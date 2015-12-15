package dbservice.Contracts;

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
        public static final String COLUMN_NAME_logo = "logo";
    }

    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryOfSearchEntry.TABLE_NAME + " (" +
                    HistoryOfSearchEntry._ID + " INTEGER PRIMARY KEY," + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
                    HistoryOfSearchEntry.COLUMN_NAME_logo + TEXT_TYPE + " )";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXIST " + HistoryOfSearchEntry.TABLE_NAME;
}
