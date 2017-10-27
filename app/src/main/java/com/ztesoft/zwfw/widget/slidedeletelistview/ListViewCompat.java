package com.ztesoft.zwfw.widget.slidedeletelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.ztesoft.zwfw.domain.Message;

import java.util.ArrayList;
import java.util.List;


public class ListViewCompat extends ListView {
    private float xDistance, yDistance, xLast, yLast;

    private static final String TAG = "ListViewCompat";

    private SlideView mFocusedItemView;

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

     /*   switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                int position = pointToPosition((int)xLast,(int)yLast);
                if (position != INVALID_POSITION) {
                    mFocusedItemView= (SlideView) getChildAt(position);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (xDistance > yDistance) {
                    return false;
                }
                break;

        }*/

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            int x = (int) event.getX();
            int y = (int) event.getY();
            int position = pointToPosition(x, y);
            if (position != INVALID_POSITION) {
                Message message = (Message) getItemAtPosition(position);
                //mFocusedItemView = message.slideView;
            }
        default:
            break;
        }

        if (mFocusedItemView != null) {
            mFocusedItemView.onRequireTouchEvent(event);
        }else{
            Log.d(TAG,"--null");
        }

        return super.onTouchEvent(event);
    }

}
