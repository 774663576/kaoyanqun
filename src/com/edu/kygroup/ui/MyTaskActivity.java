package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.MyTaskAdapter;
import com.edu.kygroup.adapter.MyTaskAdapter.DelTask;
import com.edu.kygroup.domin.Task;
import com.edu.kygroup.domin.TaskList;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.DelMyTask;
import com.edu.kygroup.task.GetMyTask;
import com.edu.kygroup.utils.ToastUtils;

public class MyTaskActivity extends BaseActivity implements DelTask {
	private List<Task> lists = new ArrayList<Task>();

	private ListView mListView;
	private MyTaskAdapter adapter;

	private TaskList tList = new TaskList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tList.setUser_id(KygroupApplication.getmApplication().mUser.getEmail());
		initView();
		refush();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_my_task, null);
	}

	private void initView() {
		setTitleText("我的任务");
		setLeftBtnVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.mListView);
		adapter = new MyTaskAdapter(this, lists);
		adapter.setmCallBack(this);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				startActivity(new Intent(MyTaskActivity.this,
						TaskCommentActivity.class).putExtra("task",
						lists.get(position)));
			}
		});
	}

	private void refush() {
		startWaitingDialog();
		GetMyTask task = new GetMyTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("获取失败");
					return;
				}
				lists.addAll(tList.getLists());
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(tList);
	}

	@Override
	public void delTask(final int position) {
		startWaitingDialog();
		DelMyTask task = new DelMyTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("删除失败");
					return;
				}
				lists.remove(position);
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(lists.get(position));
	}
}
