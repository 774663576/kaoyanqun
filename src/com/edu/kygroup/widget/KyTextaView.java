package com.edu.kygroup.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class KyTextaView extends TextView {
	
	private String mText;
	private Paint mPaint;
	private int mViewWidth;
	private int mTextWidth;

	public KyTextaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint = getPaint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
	}
	
//	/**
//	* @Description:计算出一行显示的文字
//	* @param     设定文件
//	*/
//	private String caculateOneLine(String str)
//	{
//		//对一段没有\n的文字进行换行
//		String returnStr = "";
//		int strWidth = (int) mPaint.measureText(str);
//		int len = str.length();
//		int lineNum = strWidth/620;	//大概知道分多少行
//		int oneLine;
//		int tempWidth = 0;
//		String lineStr;
//		int returnInt = 0;
//		if(lineNum == 0)
//		{
//			returnStr = str;
//			mHeight += LINE_HEIGHT;
//			return returnStr;
//		}
//		else
//		{
//			oneLine = len/(lineNum + 1);	//一行大概有多少个字
//			lineStr = str.substring(0, oneLine);
//			tempWidth = (int) mPaint.measureText(lineStr);
//			if(tempWidth < 620)	//如果小了 找到大的那个
//			{
//				while(tempWidth < 620)
//				{
//					oneLine++;
//					lineStr = str.substring(0, oneLine);
//					tempWidth = (int) mPaint.measureText(lineStr);
//				}
//				returnInt = oneLine - 1;
//				returnStr = lineStr.substring(0, lineStr.length() - 2);
//			}
//			else
//			{
//				while(tempWidth > 620)
//				{
//					oneLine--;
//					lineStr = str.substring(0, oneLine);
//					tempWidth = (int) mPaint.measureText(lineStr);
//				}
//				returnStr = lineStr.substring(0, lineStr.length() - 1);
//				returnInt = oneLine;
//			}
//			mHeight += LINE_HEIGHT;
//			returnStr += "\n" + caculateOneLine(str.substring(returnInt-1));
//		}
//		return returnStr;
//	}
//	
//	public void getText
	
	public void setShowText(String text){
		mText = text;
		mTextWidth = (int)mPaint.measureText(text);		
	}
	
	public void setWidth(int width){
		mViewWidth = width;
	}
	
	public String getSubText(){
		if(mTextWidth > mViewWidth-25){		
			char str[] = mText.toCharArray();
			StringBuffer subText = new StringBuffer();
			for(int i = 0;i < str.length;i++){
				subText.append(str[i]);				
				int w = (int)mPaint.measureText(subText.toString());
				if(w > mViewWidth-25){
					subText.deleteCharAt(subText.length()-1);
					return subText.toString();
				}
			}
		}
		return mText;
	}
}
