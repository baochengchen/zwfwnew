package com.ztesoft.zwfw.widget;

import org.xmlpull.v1.XmlPullParser;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztesoft.zwfw.R;

public class SegmentView extends LinearLayout {

	public SegmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}

	public SegmentView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	private TextView textView1;
	private TextView textView2;
	private OnSegmentViewClickListener listener;

	public void setOnSegmentViewClickListener(OnSegmentViewClickListener listener) {
		this.listener = listener;
	}

	private void init() {
		textView1 = new TextView(getContext());
		textView2 = new TextView(getContext());
		textView1.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
		textView2.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
		textView1.setText("text1");
		textView2.setText("text2");
		XmlPullParser parser = getResources().getXml(R.drawable.seg_color_selector);
		try {
			ColorStateList list = ColorStateList.createFromXml(getResources(), parser);
			textView1.setTextColor(list);
			textView2.setTextColor(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textView1.setGravity(Gravity.CENTER);
		textView2.setGravity(Gravity.CENTER);
		textView1.setPadding(10, 6, 10, 6);
		textView2.setPadding(10, 6, 10, 6);
		setSegmentTextSize(16);
		this.setBackgroundResource(R.drawable.seg_bg);
		textView1.setBackgroundResource(R.drawable.seg_left);
		textView2.setBackgroundResource(R.drawable.seg_right);
		textView1.setSelected(true);
		this.removeAllViews();
		this.addView(textView1);
		this.addView(textView2);
		this.invalidate();

		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (textView1.isSelected()) {
					return;
				}
				textView1.setSelected(true);
				textView2.setSelected(false);
				if (listener != null) {
					listener.onSegmentViewClick(textView1, 0);
				}
			}
		});

		textView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (textView2.isSelected()) {
					return;
				}
				textView1.setSelected(false);
				textView2.setSelected(true);
				if (listener != null) {
					listener.onSegmentViewClick(textView2, 1);
				}
			}
		});
	}

	public void setSegmentTextSize(int dp) {
		textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
		textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
	}

	public void setSegmentText(String text1, String text2) {
		textView1.setText(text1);
		textView2.setText(text2);
		this.postInvalidate();
	}

	public void setSelected(int position) {
		switch (position) {
		case 0:
			textView1.setSelected(true);
			textView2.setSelected(false);
			break;
		case 1:
			textView1.setSelected(false);
			textView2.setSelected(true);
			break;
		default:
			break;
		}
	}

	public static interface OnSegmentViewClickListener {
		public void onSegmentViewClick(View view, int position);
	}
}
