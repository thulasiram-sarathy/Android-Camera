package com.thul.androidcamera;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class PushTouchView extends View {

    @Nullable
    private View forwardTo;

    public PushTouchView(Context context) {
        super(context);
    }

    public PushTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PushTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setForwardTo(@Nullable View v) {
        this.forwardTo = v;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (forwardTo != null) {
            if(forwardTo.getId() == R.id.draw_view){
                return forwardTo.onTouchEvent(event);
            }else if(forwardTo.getId() == R.id.wishText){
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    /*
                     * Drag details: we only need default behavior
                     * - clip data could be set to pass data as part of drag
                     * - shadow can be tailored
                     */
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(forwardTo);
                    //start dragging the item touched
                    if (forwardTo != null){
                        forwardTo.startDrag(data, shadowBuilder, forwardTo, 0);
                    }

                    return true;
                }else{
                    return false;
                }
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}


