package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.developer.bugmaster.R;

//Feature Task 6
public class InsectAdapter extends InsectRecyclerAdapter {

    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public InsectAdapter(Cursor cursor) {
        super.mCursor = cursor;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    protected void postItemClick(InsectHolder insectHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(insectHolder.itemView, insectHolder.getAdapterPosition());
        }
    }

    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_insect, parent, false);

        return new InsectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {
        holder.commonNameView.setText(getItem(position).name);
        holder.scientificNameView.setText(getItem(position).scientificName);
        holder.dangerLevelView.setDangerLevel(getItem(position).dangerLevel);
    }

    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    public void swapCursor(Cursor cursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
