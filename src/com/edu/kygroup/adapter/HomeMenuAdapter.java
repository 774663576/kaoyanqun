package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeMenuAdapter extends BaseAdapter{
	
	private static final String TAG = "home_menu_adapter";
	
	private Context mContext;
	private ArrayList<HashMap<String,Object>> contents;	// �˵�����	
	
	public HomeMenuAdapter(Context context,ArrayList<HashMap<String,Object>> contents){
		this.mContext = context;
		this.contents = contents;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents == null ? 0:contents.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contents == null ? null:contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	static class ViewHolder{
		ImageView icon;
		TextView content;
	}
}
