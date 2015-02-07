package com.edu.kygroup.adapter;

import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.FocusInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FocusAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<FocusInfo> mInfos;
	public FocusAdapter(Context context,ArrayList<FocusInfo> infos){
		mContext = context;
		mInfos = infos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null != mInfos){
			return mInfos.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(null != mInfos){
			return mInfos.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(mContext).inflate(R.layout.focus_text, null);
		TextView tv = (TextView)convertView.findViewById(R.id.focus_tv);
		FocusInfo info = mInfos.get(position);
		tv.setText((position+1)+" "+info.getmFocusSchool()+" "+info.getmFocusColleage()+" "+info.getmFocusMajor());
		return convertView;
	}
}
