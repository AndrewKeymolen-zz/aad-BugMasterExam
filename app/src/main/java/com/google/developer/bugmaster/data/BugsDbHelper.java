package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.DatabaseContract.InsectColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 1;

    //Feature Task 1
    private static final String SQL_CREATE_TABLE_TASKS = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
            DatabaseContract.TABLE_INSECTS,
            InsectColumns._ID,
            InsectColumns.NAME,
            InsectColumns.SCIENTIFIC_NAME,
            InsectColumns.CLASSIFICATION,
            InsectColumns.IMAGE_ASSET,
            InsectColumns.DANGER_LEVEL
    );

    //Used to read data from res/ and assets/
    private Resources mResources;

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mResources = context.getResources();
    }

    //Feature Task 2
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TASKS);
        try {
            readInsectsFromResources(db);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //Feature Task 2

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_INSECTS);
        onCreate(db);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        //Feature Task 2
        JSONObject mJSONObject = new JSONObject(rawJson);
        JSONArray insects = mJSONObject.getJSONArray("insects");
        ContentValues values;
        JSONObject insect;

        for (int i = 0; i < insects.length(); i++) {
            insect = insects.getJSONObject(i);
            values = new ContentValues();

            values.put(InsectColumns.NAME, insect.getString("friendlyName"));
            values.put(InsectColumns.SCIENTIFIC_NAME, insect.getString("scientificName"));
            values.put(InsectColumns.CLASSIFICATION, insect.getString("classification"));
            values.put(InsectColumns.IMAGE_ASSET, insect.getString("imageAsset"));
            values.put(InsectColumns.DANGER_LEVEL, insect.getInt("dangerLevel"));

            db.insertOrThrow(DatabaseContract.TABLE_INSECTS, null, values);
        }
    }
}
