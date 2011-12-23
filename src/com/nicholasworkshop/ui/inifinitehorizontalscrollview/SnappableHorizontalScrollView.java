package com.nicholasworkshop.ui.inifinitehorizontalscrollview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
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
	private int mActiveColor = Color.WHITE;
	private int mInactiveColor = Color.GRAY;

	// private int mCurrent = 0;

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

	public void setActiveColor(int activeColor) {
		mActiveColor = activeColor;
	}

	public void setInactiveColor(int inactiveColor) {
		mInactiveColor = inactiveColor;
	}

	public void updateTextColor(int position) {
		for (int i = 0; i < mContainer.getChildCount(); i++) {
			if (i != position) ((TextView) mContainer.getChildAt(i)).setTextColor(mInactiveColor);
		}
		((TextView) mContainer.getChildAt(position)).setTextColor(mActiveColor);
	}

	public void smoothScrollToItemAt(int position) {
		int itemPosition = 0;
		for (int i = 0; i < position; i++) {
			itemPosition += mContainer.getChildAt(i).getWidth();
		}
		smoothScrollTo(itemPosition, getScrollY());
	}

	public void setDataSet(String[] dataset) {
		mDataSet = dataset;
		mContainer.removeAllViews();
		for (int i = 0; i < mDataSet.length; i++) {
			TextView textView = new TextView(getContext());
			textView.setText(mDataSet[i]);
			textView.setPadding(0, 0, 16, 0);
			textView.setTextColor(mInactiveColor);
			textView.setTextSize(mFontSize);
			textView.setTypeface(mTypeface);
			mContainer.addView(textView);
		}
		Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
		mContainer.getChildAt(mDataSet.length - 1).getLayoutParams().width = display.getWidth();
		updateTextColor(0);
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
						if (Math.abs(nextItemPosition - getScrollX()) > Math.abs(lastItemPosition - getScrollX()) || i == mContainer.getChildCount() - 1) {
							Log.d(TAG, "Snap to last " + lastItemPosition);
							smoothScrollTo(lastItemPosition, getScrollY());
							updateTextColor(i);
						}
						else {
							Log.d(TAG, "Snap to next " + nextItemPosition);
							smoothScrollTo(nextItemPosition, getScrollY());
							updateTextColor(i + 1);
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