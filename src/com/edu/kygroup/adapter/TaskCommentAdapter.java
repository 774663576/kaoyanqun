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
import com.edu.kygroup.domin.TaskComment;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.RoundAngleImageView;

public class TaskCommentAdapter extends BaseAdapter {
	private List<TaskComment> lists = new ArrayList<TaskComment>();
	private Context mContext;

	public TaskCommentAdapter(Context context, List<TaskComment> lists) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null == convertView) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.task_comment_item, null);
			holder.nickView = (TextView) convertView
					.findViewById(R.id.nickname);
			holder.picView = (RoundAngleImageView) convertView
					.findViewById(R.id.pic);
			holder.resConView = (TextView) convertView
					.findViewById(R.id.response_content);
			holder.timeView = (TextView) convertView.findViewById(R.id.time);
			holder.school = (TextView) convertView.findViewById(R.id.school);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		TaskComment comment = lists.get(position);
		holder.nickView.setText(comment.getUser_name());
		holder.resConView.setText(comment.getTask_comment_content());
		holder.timeView.setText(comment.getTask_comment_time());
		holder.school.setText(comment.getUser_major());
		UniversalImageLoadTool.disPlay(comment.getUser_avatar(),
				holder.picView, R.drawable.empty_photo);
		return convertView;
	}

	class Holder {
		RoundAngleImageView picView;
		TextView nickView;
		TextView timeView;
		TextView resConView;// 内容
		TextView school;
	}

}
