package com.edu.kygroup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MTextView  extends TextView{
	
    public MTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

	@Override
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }

}
