package com.edu.kygroup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class KyListView extends ListView {

	public KyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KyListView(Context context) {
		super(context);
	}

	public KyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
