package com.edu.kygroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.TiaoJiYuanXiao;

public class TiaoJiaoYuanXiaoAdapter extends BaseAdapter {
	private List<TiaoJiYuanXiao> lists;
	private Context mContext;

	public TiaoJiaoYuanXiaoAdapter(List<TiaoJiYuanXiao> lists, Context context) {
		this.lists = lists;
		this.mContext = context;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.zhaoshengyuanxiao_item, null);
			holder.txt_sname = (TextView) convertView
					.findViewById(R.id.focus_msg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_sname.setText(lists.get(arg0).getSname() + " | "
				+ lists.get(arg0).getCename() + " | "
				+ lists.get(arg0).getMname());
		return convertView;
	}

	class ViewHolder {
		TextView txt_sname;
	}
}
