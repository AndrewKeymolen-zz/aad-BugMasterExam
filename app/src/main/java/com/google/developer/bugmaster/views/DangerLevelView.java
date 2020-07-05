package com.google.developer.bugmaster.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

//TODO: This class should be used in the insect list to display danger level
public class DangerLevelView extends TextView {

    private int mDangerLevel;

    public DangerLevelView(Context context) {
        super(context);
    }

    public DangerLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getDangerLevel() {
        return mDangerLevel;
    }

    //Feature Taks 7
    public void setDangerLevel(int dangerLevel) {
        mDangerLevel = dangerLevel;
        setText(Integer.toString(dangerLevel));

        String[] stringArray = getResources().getStringArray(R.array.dangerColors);
        GradientDrawable bgShape = (GradientDrawable) getBackground();
        bgShape.setColor(Color.parseColor(stringArray[dangerLevel - 1]));
    }
}
