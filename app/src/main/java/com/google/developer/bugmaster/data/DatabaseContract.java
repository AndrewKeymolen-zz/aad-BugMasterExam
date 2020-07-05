package com.google.developer.bugmaster.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

//Feature Task 1
public class DatabaseContract {
    //Database schema information
    public static final String TABLE_INSECTS = "insects";
    //Unique authority string
    public static final String CONTENT_AUTHORITY = "com.google.developer.bugmaster";
    /* Sort order constants */
    //Insect common name (ascending, alphabetically), Insect danger level (descending, most dangerous first)
    public static final String NAME_SORT = String.format("%s ASC, %s DESC",
            InsectColumns.NAME, InsectColumns.DANGER_LEVEL);
    //Insect danger level (descending, most dangerous first), Insect common name (ascending, alphabetically)
    public static final String DANGER_SORT = String.format("%s DESC, %s ASC",
            InsectColumns.DANGER_LEVEL, InsectColumns.NAME);
    //Base content Uri
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_INSECTS)
            .build();

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static final class InsectColumns implements BaseColumns {
        //Insect name
        public static final String NAME = "name";
        //Insect scientific name
        public static final String SCIENTIFIC_NAME = "scientific_name";
        //Insect classification
        public static final String CLASSIFICATION = "classification";
        //Associate image asset
        public static final String IMAGE_ASSET = "image_asset";
        //Insect danger level
        public static final String DANGER_LEVEL = "danger_level";
    }

}
