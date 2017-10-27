package com.ztesoft.zwfw.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by BaoChengchen on 2017/8/8.
 */

public class HomeComponentView extends LinearLayout{

    public HomeComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        ImageView imageView = new ImageView(context);
        addView(null);
    }

}
