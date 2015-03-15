package com.edu.kygroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.TaskComment;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.CircularImage;

public class CommentAvatarAdapter extends BaseAdapter {
	private List<TaskComment> lists;
	private Context mContext;

	public CommentAvatarAdapter(Context context, List<TaskComment> lists) {
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_avatar_item, null);
			holder = new ViewHolder();
			holder.user_avatar = (CircularImage) convertView
					.findViewById(R.id.img_user_avatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UniversalImageLoadTool.disPlay(lists.get(position).getUser_avatar(),
				holder.user_avatar, R.drawable.default_avatar);

		return convertView;
	}

	class ViewHolder {
		CircularImage user_avatar;
	}
}
