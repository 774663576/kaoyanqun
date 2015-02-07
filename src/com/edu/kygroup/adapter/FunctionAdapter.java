package com.edu.kygroup.adapter;

import java.util.ArrayList;

import com.edu.kygroup.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FunctionAdapter extends BaseAdapter {
	private ArrayList<String> mLists;
	private LayoutInflater mInflater;
	
	private void addLists(String []lists){
		if(null == mLists){
			mLists = new ArrayList<String>();
		}
		for(String str:lists){
			mLists.add(str);
		}
	}
	
	public FunctionAdapter(Context context,String []lists){
		mInflater = LayoutInflater.from(context);
		addLists(lists);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null == mLists){
			return 0;
		}
		return mLists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(null == mLists){
			return null;
		}
		return mLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.scan_item, null);
		TextView tv = (TextView)convertView.findViewById(R.id.detail_textview);
		tv.setText(mLists.get(position));
		return convertView;
	}
}
