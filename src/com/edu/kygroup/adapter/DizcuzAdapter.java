package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Dizcuz;

public class DizcuzAdapter extends BaseAdapter {
	List<Dizcuz> lists = new ArrayList<Dizcuz>();
	Context mContext;

	public DizcuzAdapter(List<Dizcuz> lists, Context context) {
		this.mContext = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.dizcuz_item, null);
			holder.txt_author = (TextView) convertView
					.findViewById(R.id.txt_author);
			holder.txt_time = (TextView) convertView
					.findViewById(R.id.txt_time);
			holder.txt_title = (TextView) convertView
					.findViewById(R.id.txt_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_author.setText(lists.get(position).getAuthor());
		holder.txt_time.setText(lists.get(position).getTime());
		holder.txt_title.setText(lists.get(position).getTitle());
		return convertView;
	}

	class ViewHolder {
		TextView txt_title;
		TextView txt_time;
		TextView txt_author;
	}
}
