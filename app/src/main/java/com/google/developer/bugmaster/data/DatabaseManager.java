package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;
    private BugsDbHelper mBugsDbHelper;

    private DatabaseManager(Context context) {
        mBugsDbHelper = new BugsDbHelper(context);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    //Feature Task 3
    /**
     * Return a {@link Cursor} that contains every insect in the database.
     *
     * @param sortOrder Optional sort order string for the query, can be null
     * @return {@link Cursor} containing all insect results.
     */
    public Cursor queryAllInsects(String sortOrder) {
        SQLiteDatabase db = mBugsDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.TABLE_INSECTS);
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = DatabaseContract.NAME_SORT;
        }
        Cursor cursor =
                builder.query(
                        db,
                        null,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
        return cursor;
    }

    //Feature Task 3
    /**
     * Return a {@link Cursor} that contains a single insect for the given unique id.
     *
     * @param id Unique identifier for the insect record.
     * @return {@link Cursor} containing the insect result.
     */
    public Cursor queryInsectsById(int id) {
        SQLiteDatabase db = mBugsDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.TABLE_INSECTS);
        builder.appendWhere(DatabaseContract.InsectColumns._ID + " = " +
                id);
        Cursor cursor =
                builder.query(
                        db,
                        null,
                        null,
                        null,
                        null,
                        null,
                        DatabaseContract.NAME_SORT);
        return cursor;
    }
}
