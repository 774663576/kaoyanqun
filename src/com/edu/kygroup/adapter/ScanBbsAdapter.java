package com.edu.kygroup.adapter;

import java.util.ArrayList;

import com.edu.kygroup.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScanBbsAdapter extends BaseAdapter {
	private ArrayList<String> mLists;
	private LayoutInflater mInflater;
	private int mPosition = 2;

	private void addLists(String[] lists) {
		if (null == mLists) {
			mLists = new ArrayList<String>();
		}
		for (String str : lists) {
			mLists.add(str);
		}
	}

	public ScanBbsAdapter(Context context, String[] lists, int position) {
		mInflater = LayoutInflater.from(context);
		addLists(lists);
		mPosition = position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null == mLists) {
			return 0;
		}
		return mLists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (null == mLists) {
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
		TextView tv = (TextView) convertView.findViewById(R.id.detail_textview);
		tv.setText(mLists.get(position));
		if (mPosition == position) {
			tv.setBackgroundColor(0xffffA500);
		} else {
			tv.setBackgroundColor(0xffeeeeee);
		}
		return convertView;
	}

	public void setPosition(int position) {
		mPosition = position;
		notifyDataSetChanged();
	}
}
