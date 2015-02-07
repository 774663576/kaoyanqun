package com.edu.kygroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.edu.kygroup.R;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.MyImageView;

public class BBSImgAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> listData;

	public BBSImgAdapter(Context context, List<String> data) {
		this.mContext = context;
		this.listData = data;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		String path = listData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bbs_img_gridview_item, null);
			holder.img = (MyImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setTag(path);
		UniversalImageLoadTool
				.disPlay(path, holder.img, R.drawable.empty_photo);

		return convertView;
	}

	class ViewHolder {
		MyImageView img;
	}
}
