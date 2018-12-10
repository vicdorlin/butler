package com.butler.smartbutler.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class UtilTools {

    /**
     * 设置字体
     */
    public static void setFont(Context context, TextView view){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/FONT.TTF");
        view.setTypeface(typeface);
    }
}
