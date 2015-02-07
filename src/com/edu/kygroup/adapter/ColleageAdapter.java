package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.UniversityAdapter.ViewHolder;
import com.edu.kygroup.domin.Colleage;

public class ColleageAdapter extends BaseAdapter {
	private Activity mContext;
	private ArrayList<Colleage> mList;
	private LayoutInflater mInflater;
	
	
	public ColleageAdapter(Activity context,ArrayList<Colleage> list){
		mContext = context;
		mList = list;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.university_item, null);
		TextView textView = (TextView)convertView.findViewById(R.id.university_name);
		Colleage col = mList.get(pos);
		if(null != col){
			textView.setText(col.getName());
		}
		return convertView;
	}
}
