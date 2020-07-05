package com.google.developer.bugmaster;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.BugsDbHelper;
import com.google.developer.bugmaster.data.CustomCursorLoader;
import com.google.developer.bugmaster.data.DatabaseContract;
import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectAdapter;

import java.util.ArrayList;
import java.util.Random;

import static com.google.developer.bugmaster.InsectDetailsActivity.INSECT_ID;
import static com.google.developer.bugmaster.QuizActivity.ANSWER_COUNT;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_INSECTS;

//Feature Task 2, 4, 5, 6, 8, 10 & 12
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, InsectAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final Uri CONTENT_URI = Uri.parse("content://" + DatabaseContract.CONTENT_AUTHORITY + "/" + DatabaseContract.TABLE_INSECTS);
    BugsDbHelper mDbHelper;
    private InsectAdapter mAdapter;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mAdapter = new InsectAdapter(null);
        mAdapter.setOnItemClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //For the creation of the database if it's not yet done
        mDbHelper = new BugsDbHelper(getApplicationContext());
        mDbHelper.getWritableDatabase();

        getLoaderManager().initLoader(0, null, this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                restartLoader();
            }
        };

    }

    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String sortOrderPref = prefs.getString(getResources().getString(R.string.pref_sortBy_key), getResources().getString(R.string.pref_sortBy_name));

                if (sortOrderPref.equals(getString(R.string.pref_sortBy_danger))) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(getResources().getString(R.string.pref_sortBy_key), getResources().getString(R.string.pref_sortBy_name));
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(getResources().getString(R.string.pref_sortBy_key), getResources().getString(R.string.pref_sortBy_danger));
                    editor.apply();
                }
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        Intent action = new Intent(this, QuizActivity.class);
        ArrayList<Insect> insects = new ArrayList<>();
        Cursor mCursor = DatabaseManager.getInstance(getApplicationContext()).queryAllInsects(null);
        int cursorSize = mCursor.getCount();
        final Random rand = new Random();
        int position;
        Insect insect;

        for (int i = 0; i < ANSWER_COUNT; i++) {
            position = rand.nextInt(cursorSize);
            mCursor.moveToPosition(position);
            insect = new Insect(mCursor);
            insects.add(insect);
        }

        action.putParcelableArrayListExtra(EXTRA_INSECTS, insects);
        action.putExtra(EXTRA_ANSWER, insects.get(rand.nextInt(ANSWER_COUNT)));
        startActivity(action);
    }

    /* Click events in RecyclerView items */
    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(this, InsectDetailsActivity.class);
        intent.putExtra(INSECT_ID, mAdapter.getItem(position).id);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CustomCursorLoader(getBaseContext(), CONTENT_URI,
                null, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }
}
