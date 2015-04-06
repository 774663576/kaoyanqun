package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Task;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.GetUserInfoTask;
import com.edu.kygroup.ui.HomeActivity;
import com.edu.kygroup.ui.PersonDetailActivity;
import com.edu.kygroup.ui.TaskCommentActivity;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.CircularImage;

public class TaskAdapter extends BaseAdapter {
	private HomeActivity mContext;
	private List<Task> lists = new ArrayList<Task>();

	public TaskAdapter(HomeActivity context, List<Task> lists) {
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.task_item, null);
			holder = new ViewHolder();
			holder.avatar = (CircularImage) convertView
					.findViewById(R.id.img_avatar);
			holder.txt_content = (TextView) convertView
					.findViewById(R.id.txt_content);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.txt_school = (TextView) convertView
					.findViewById(R.id.txt_school);
			holder.txt_title = (TextView) convertView
					.findViewById(R.id.txt_title);
			holder.btn_comment = (TextView) convertView
					.findViewById(R.id.btn_comment);
			holder.btn_share = (TextView) convertView
					.findViewById(R.id.btn_share);
			holder.txt_price = (TextView) convertView
					.findViewById(R.id.txt_price);
			holder.txt_time = (TextView) convertView
					.findViewById(R.id.txt_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		holder.txt_name.getPaint().setFakeBoldText(true);// 加粗
		holder.txt_content.setText(lists.get(position).getTask_content());
		holder.txt_name.setText(lists.get(position).getUser_name());
		holder.txt_school.setText(lists.get(position).getUser_major());
		UniversalImageLoadTool.disPlay(lists.get(position).getUser_avatar(),
				holder.avatar, R.drawable.empty_photo);
		String category = "其他";
		switch (lists.get(position).getTask_category()) {
		case 1:
			category = "求资料";
			break;
		case 2:
			category = "征学长";
			break;
		case 3:
			category = "其他";
			break;
		default:
			break;
		}
		holder.txt_time.setText(lists.get(position).getTask_time());
		holder.txt_title.setText("( " + category + " ) "
				+ lists.get(position).getTask_title());
		holder.txt_price.setText("报酬:" + lists.get(position).getTask_price());
		holder.btn_comment.setOnClickListener(new Onclick(position));
		int count = lists.get(position).getTask_comment_count();
		if (count > 0) {
			holder.btn_comment.setText("回复(" + count + ")");
		} else {
			holder.btn_comment.setText("回复");

		}
		holder.avatar.setOnClickListener(new Onclick(position));
		return convertView;
	}

	class ViewHolder {
		CircularImage avatar;
		TextView txt_content;
		TextView txt_name;
		TextView txt_school;
		TextView txt_title;
		TextView btn_share;
		TextView btn_comment;
		TextView txt_price;
		TextView txt_time;
	}

	class Onclick implements OnClickListener {
		private int position;

		public Onclick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_comment:
				mContext.startActivity(new Intent(mContext,
						TaskCommentActivity.class).putExtra("task",
						lists.get(position)));
				break;
			case R.id.img_avatar:
				mContext.startWaitingDialog();
				User user = new User();
				user.setEmail(lists.get(position).getUser_id());
				getUserInfo(user);
				break;
			default:
				break;
			}

		}
	}

	private void getUserInfo(final User user) {
		GetUserInfoTask task = new GetUserInfoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				mContext.closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("信息获取失败");
					return;
				}
				Intent intent = new Intent(mContext, PersonDetailActivity.class);
				intent.putExtra("user", user);
				mContext.startActivityForResult(intent, 100);
			}
		});
		task.executeParallel(user);
	}
}
