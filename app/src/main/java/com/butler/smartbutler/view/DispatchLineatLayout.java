package com.butler.smartbutler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import lombok.Getter;
import lombok.Setter;

/**
 * 事件分发
 */
@Getter
@Setter
public class DispatchLineatLayout extends LinearLayout {
    private DispatchKeyEventListener dispatchKeyEventListener;

    public DispatchLineatLayout(Context context) {
        super(context);
    }

    public DispatchLineatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLineatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //接口
    public static interface DispatchKeyEventListener{
        boolean dispatchKeyEvent(KeyEvent keyEvent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空说明调用了  去获取事件
        if(dispatchKeyEventListener != null){
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
