package com.google.developer.bugmaster.data;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.views.DangerLevelView;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {

    protected Cursor mCursor;

    protected void postItemClick(InsectHolder insectHolder) {

    }

    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     * @return A new {@link Insect} filled with this position's attributes
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Feature Task 6
        public TextView commonNameView;
        public TextView scientificNameView;
        public DangerLevelView dangerLevelView;

        public InsectHolder(View itemView) {
            super(itemView);

            commonNameView = (TextView) itemView.findViewById(R.id.common_name);
            scientificNameView = (TextView) itemView.findViewById(R.id.scientific_name);
            dangerLevelView = (DangerLevelView) itemView.findViewById(R.id.danger_level);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            postItemClick(this);
        }
    }
}
