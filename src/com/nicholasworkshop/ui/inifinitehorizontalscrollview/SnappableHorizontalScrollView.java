package com.nicholasworkshop.ui.inifinitehorizontalscrollview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SnappableHorizontalScrollView extends HorizontalScrollView {

	private static final String TAG = "InifiniteHorizontalScrollView";

	private LinearLayout mContainer;
	private String[] mDataSet;
	
	private int mFontSize = 30;
	private Typeface mTypeface;
	
	public SnappableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContainer = new LinearLayout(context);
		mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setHorizontalScrollBarEnabled(false);
		this.setVerticalScrollBarEnabled(false);
		this.setScrollbarFadingEnabled(false);
		this.addView(mContainer);
		this.setOnTouchListener(mOnTouchListener);
	}

	public void setTypeface(Typeface typeface) {
		mTypeface = typeface;
	}

	public void setFontSize(int fontsize) {
		mFontSize = fontsize;
	}
	
	public void setDataSet(String[] dataset) {
		mDataSet = dataset;
		mContainer.removeAllViews();
		for (int i = 0; i < mDataSet.length; i++) {
			TextView textView = new TextView(getContext());
			textView.setText(mDataSet[i]);
			textView.setPadding(10, 10, 10, 10);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(mFontSize);
			textView.setTypeface(mTypeface);
			mContainer.addView(textView);
		}
	}

	private OnTouchListener mOnTouchListener = new View.OnTouchListener() {
		@Override public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
				int lastItemPosition = 0;
				int nextItemPosition = 0;
				for (int i = 0; i < mContainer.getChildCount(); i++) {
					lastItemPosition = nextItemPosition;
					nextItemPosition += mContainer.getChildAt(i).getWidth();
					if (lastItemPosition < getScrollX() && getScrollX() < nextItemPosition) {
						if (Math.abs(nextItemPosition - getScrollX()) < Math.abs(lastItemPosition - getScrollX())) {
							Log.d(TAG, "Snap to next " + nextItemPosition);
							smoothScrollTo(nextItemPosition, getScrollY());
						}
						else {
							Log.d(TAG, "Snap to last " + lastItemPosition);
							smoothScrollTo(lastItemPosition, getScrollY());
						}
						break;
					}
				}
				return true;
			}
			else {
				return false;
			}
		}
	};
}