package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.CommentAvatarAdapter;
import com.edu.kygroup.adapter.TaskCommentAdapter;
import com.edu.kygroup.domin.Task;
import com.edu.kygroup.domin.TaskComment;
import com.edu.kygroup.domin.TaskCommentList;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.AddTaskCommentTask;
import com.edu.kygroup.task.GetTaskCommentListTask;
import com.edu.kygroup.task.GetUserInfoTask;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.CircularImage;
import com.edu.kygroup.widget.HorizontalListView;
import com.edu.kygroup.widget.KyListView;

public class TaskCommentActivity extends BaseActivity implements
		OnClickListener {
	private CircularImage avatar;
	private TextView txt_content;
	private TextView txt_name;
	private TextView txt_school;
	private TextView txt_title;
	private TextView txt_price;
	private TextView btn_send;
	private EditText edit_content;
	private TextView txt_time;

	private KyListView mListView;
	private HorizontalListView avatar_listView;

	private CommentAvatarAdapter avatar_adapter;

	private Task task;

	private List<TaskComment> lists = new ArrayList<TaskComment>();

	private TaskCommentAdapter adapter;

	private TaskCommentList cList = new TaskCommentList();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
		setValue();
		getCommentList();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_task_comment, null);
	}

	private void initView() {
		setTitleText("回复");
		setLeftBtnVisibility(View.GONE);
		avatar = (CircularImage) findViewById(R.id.img_avatar);
		txt_content = (TextView) findViewById(R.id.txt_content);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_school = (TextView) findViewById(R.id.txt_school);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_price = (TextView) findViewById(R.id.txt_price);
		btn_send = (TextView) findViewById(R.id.btn_send);
		edit_content = (EditText) findViewById(R.id.edit_content);
		mListView = (KyListView) findViewById(R.id.mlistview);
		avatar_listView = (HorizontalListView) findViewById(R.id.avatar_listView);
		txt_time = (TextView) findViewById(R.id.txt_time);
		btn_send.setOnClickListener(this);
		if (KygroupApplication.getmApplication().mUser.getEmail().equals(
				task.getUser_id())) {
			avatar_listView.setVisibility(View.GONE);
		} else {
			mListView.setVisibility(View.GONE);
		}
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startWaitingDialog();
				User user = new User();
				user.setEmail(task.getUser_id());
				getUserInfo(user);
			}
		});
	}

	private void setValue() {
		txt_content.setText(task.getTask_content());
		txt_name.setText(task.getUser_name());
		txt_school.setText(task.getUser_major());
		UniversalImageLoadTool.disPlay(task.getUser_avatar(), avatar,
				R.drawable.empty_photo);
		String category = "其他";
		switch (task.getTask_category()) {
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
		txt_title.setText("( " + category + " ) " + task.getTask_title());
		txt_price.setText("报酬:" + task.getTask_price());
		txt_time.setText(task.getTask_time());
		adapter = new TaskCommentAdapter(this, lists);
		mListView.setAdapter(adapter);
		avatar_adapter = new CommentAvatarAdapter(this, lists);
		avatar_listView.setAdapter(avatar_adapter);

	}

	private void getCommentList() {
		startWaitingDialog();
		GetTaskCommentListTask cTask = new GetTaskCommentListTask();
		cTask.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("获取回复失败");
					return;
				}
				lists.addAll(cList.getLists());
				adapter.notifyDataSetChanged();
			}
		});
		cList.setTask_id(task.getTask_id());
		cTask.executeParallel(cList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			addTaskComment();
			break;

		default:
			break;
		}
	}

	private void addTaskComment() {
		String comment_content = edit_content.getText().toString().trim();
		if (comment_content.length() == 0) {
			return;
		}
		startWaitingDialog();
		final TaskComment comment = new TaskComment();
		comment.setTask_id(task.getTask_id());
		comment.setTask_comment_content(comment_content);
		comment.setUser_id(KygroupApplication.getmApplication().mUser
				.getEmail());
		comment.setUser_avatar(KygroupApplication.getmApplication().mUser
				.getPic());
		comment.setUser_name(KygroupApplication.getmApplication().mUser
				.getNickName());
		comment.setUser_major(KygroupApplication.getmApplication().mUser
				.getRMajor());
		AddTaskCommentTask task = new AddTaskCommentTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("操作失败");
					return;
				}
				ToastUtils.showShortToast("回复成功");
				edit_content.setText("");
				lists.add(comment);
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(comment);
	}

	private void getUserInfo(final User user) {
		GetUserInfoTask task = new GetUserInfoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("信息获取失败");
					return;
				}
				Intent intent = new Intent(TaskCommentActivity.this,
						PersonDetailActivity.class);
				intent.putExtra("user", user);
				startActivityForResult(intent, 100);
			}
		});
		task.executeParallel(user);
	}
}
