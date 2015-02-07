package com.edu.kygroup.adapter;

import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Announce.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnnounceAdapter extends BaseAdapter {
	private ArrayList<Item> mItems;
	private LayoutInflater mInflater;

	public AnnounceAdapter(Context context, ArrayList<Item> items) {
		mItems = items;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (mItems == null) ? 0 : mItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (mItems == null) ? 0 : mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.announce_item, null);
		((TextView) convertView.findViewById(R.id.announce_text))
				.setText(mItems.get(position).getMsg());
		((TextView) convertView.findViewById(R.id.announce_time))
				.setText(mItems.get(position).getSendTime());
		return convertView;
	}
}
