package com.labs.jangkriek.qoutesandwallpaper;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utility {

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 100);
        return noOfColumns;
    }
}
