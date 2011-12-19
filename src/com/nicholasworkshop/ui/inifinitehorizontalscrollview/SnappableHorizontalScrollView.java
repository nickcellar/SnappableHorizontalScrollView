package com.nicholasworkshop.ui.inifinitehorizontalscrollview;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SnappableHorizontalScrollView extends HorizontalScrollView {

	private static final String TAG = "InifiniteHorizontalScrollView";

	private LinearLayout mContainer;

	public SnappableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContainer = new LinearLayout(context);
		mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setHorizontalScrollBarEnabled(false);
		this.setVerticalScrollBarEnabled(false);
		this.setScrollbarFadingEnabled(false);
		this.addView(mContainer);

		for (int i = 0; i < 10; i++) {
			TextView textView = new TextView(context);
			textView.setText("Bonjour" + i);
			textView.setPadding(10, 10, 10, 10);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(30);
			mContainer.addView(textView);
		}
		setOnTouchListener(mOnTouchListener);
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