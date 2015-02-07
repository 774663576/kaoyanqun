package com.edu.kygroup.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.edu.kygroup.R.color;

public class FunctionGridView extends GridView {
	public FunctionGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FunctionGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FunctionGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int count = getChildCount();
		int heightCount = count/3+1;
		int width = getMeasuredWidth();
		Paint paint = new Paint();
		paint.setColor(color.gray);
		if(count > 0){
			for(int i = 0;i < count ;i++){
				View view = getChildAt(i);
				for(int j = 1;j <= heightCount;j++){
					canvas.drawLine(0, view.getHeight()*j-1, width, view.getHeight()*j, paint);
				}
				if(i%3 == 0 || i%3 == 1){
					canvas.drawLine((i%3+1)*view.getWidth(), i/3*view.getHeight(), (i%3+1)*view.getWidth()+1, (i/3+1)*view.getHeight(), paint);
				}	
			}
		}
		super.onDraw(canvas);
	}
}
