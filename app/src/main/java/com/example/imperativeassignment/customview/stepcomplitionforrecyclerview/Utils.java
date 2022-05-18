package com.example.imperativeassignment.customview.stepcomplitionforrecyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {

    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    private static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static float pxToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / context.getResources().getDisplayMetrics().DENSITY_DEFAULT);
    }
}
