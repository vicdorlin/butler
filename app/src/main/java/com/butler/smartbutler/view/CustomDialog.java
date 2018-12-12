package com.butler.smartbutler.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.butler.smartbutler.R;

/**
 * 自定义Dialog
 */
public class CustomDialog extends Dialog {

    //定义模板
    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
                , layout, style, Gravity.CENTER);
    }

    //定义属性
    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim) {
        super(context, style);
        //设置属性
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.width = width;
        windowAttributes.height = height;
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        window.setWindowAnimations(anim);
    }

    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity) {
        this(context, width, height, layout, style, gravity, R.style.pop_anim_style);
    }

}
