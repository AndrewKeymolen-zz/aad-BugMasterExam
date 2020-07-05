package com.google.developer.bugmaster.data;

import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.google.developer.bugmaster.R;

import static android.content.Context.MODE_PRIVATE;
import static com.google.developer.bugmaster.MainActivity.MY_PREFS_NAME;

public class CustomCursorLoader extends CursorLoader {
    private final ForceLoadContentObserver mObserver = new ForceLoadContentObserver();

    public CustomCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Cursor loadInBackground() {
        String sortOrder = "";
        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sortOrderPref = prefs.getString(getContext().getResources().getString(R.string.pref_sortBy_key), getContext().getResources().getString(R.string.pref_sortBy_name));
        if (sortOrderPref.equals(getContext().getString(R.string.pref_sortBy_danger))) {
            sortOrder = DatabaseContract.DANGER_SORT;
        }
        Cursor cursor = DatabaseManager.getInstance(getContext()).queryAllInsects(sortOrder);

        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            cursor.registerContentObserver(mObserver);
        }

        return cursor;
    }
}
