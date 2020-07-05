package com.google.developer.bugmaster;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;

import java.io.IOException;
import java.io.InputStream;

public class InsectDetailsActivity extends AppCompatActivity {

    public static final String INSECT_ID = "INSECT_ID";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Feature Task 9
        Bundle extras = getIntent().getExtras();
        id = extras.getInt(INSECT_ID);
        setContentView(R.layout.activity_insect_details);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        TextView commonNameDetailView = (TextView) findViewById(R.id.common_name_detail);
        TextView scientificNameDetailView = (TextView) findViewById(R.id.scientific_name_detail);
        TextView classification = (TextView) findViewById(R.id.classification);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        Cursor mCursor = DatabaseManager.getInstance(getApplicationContext()).queryInsectsById(id);
        mCursor.moveToFirst();
        Insect insect = new Insect(mCursor);
        AssetManager assetManager = getAssets();

        try {
            InputStream ims = assetManager.open(insect.imageAsset);
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }

        commonNameDetailView.setText(insect.name);
        scientificNameDetailView.setText(insect.scientificName);
        classification.setText(insect.classification);
        ratingBar.setRating(insect.dangerLevel);

    }
}
