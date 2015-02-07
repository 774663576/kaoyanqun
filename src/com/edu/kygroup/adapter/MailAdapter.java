package com.edu.kygroup.adapter;

import java.util.ArrayList;

import com.edu.kygroup.R;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MailAdapter extends BaseAdapter {
	private Activity mContext;
	private String []mMails;
	private String mInputText;
	private LayoutInflater mInflater;
	private int mHeight = 0;
	
	public MailAdapter(Activity context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		
		initMailList();
		mInflater = LayoutInflater.from(mContext);
	}
	
	private void initMailList(){
		Resources resources = mContext.getResources();
		mMails = resources.getStringArray(R.array.mails);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMails.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMails[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setHeight(int height){
		mHeight = height;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(null != mMails && mMails.length>0){
			convertView = mInflater.inflate(R.layout.mail_item, null);
			TextView mail = (TextView)convertView.findViewById(R.id.mail_text);
			if(0 != mHeight){
				mail.setHeight(mHeight);
			}
			mail.setText(mMails[position]);
		}
		return convertView;
	}
}
