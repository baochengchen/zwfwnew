package com.ztesoft.zwfw.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by bcc on 2016/4/22.
 */
public class SearchEditText extends EditText implements View.OnKeyListener, View.OnFocusChangeListener {


    private boolean isLeft = false;

    private boolean pressSearch = false;

    private OnSearchClickListener listener;


    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }


    private void init() {

        // setOnFocusChangeListener(this);
        setOnKeyListener(this);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (isLeft) {
            super.onDraw(canvas);
        } else {
            Drawable[] drawables = getCompoundDrawables();
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            translate(drawableLeft, canvas);
            translate(drawableRight, canvas);

            super.onDraw(canvas);
        }

    }


    public void translate(Drawable drawable, Canvas canvas) {
        if (drawable != null) {
            //float textWidth = getPaint().measureText(getHint().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawable.getIntrinsicWidth();
            // float bodyWidth = textWidth + drawableWidth + drawablePadding;
            float bodyWidth = drawableWidth + drawablePadding;
            if (drawable == getCompoundDrawables()[0]) {
                canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
            } else {
                setPadding(getPaddingLeft(), getPaddingTop(), (int) (getWidth() - bodyWidth - getPaddingLeft()), getPaddingBottom());
                canvas.translate((getWidth() - bodyWidth - getPaddingLeft()) / 2, 0);
            }
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        Log.d("SearchEditText", "---------onFocusChange");
        // 恢复EditText默认的样式
        if (!pressSearch && TextUtils.isEmpty(getText().toString())) {
            isLeft = hasFocus;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        pressSearch = (keyCode == KeyEvent.KEYCODE_ENTER);
        if (pressSearch && listener != null) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            listener.onSearchClick(v);
        }
        return false;
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view);
    }


}
