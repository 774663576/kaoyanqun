package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.TaskAdapter;
import com.edu.kygroup.domin.Task;
import com.edu.kygroup.domin.TaskList;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.GetTaskListTask;
import com.edu.kygroup.utils.ToastUtils;

public class TaskView {
	private Context mContext;
	private HomeActivity mActivity;
	private List<Task> lists = new ArrayList<Task>();

	private View mView;

	private ListView mListView;
	private TaskAdapter adapter;

	private TaskList tList = new TaskList();

	public TaskView(Context context, HomeActivity activcity) {
		mView = LayoutInflater.from(context).inflate(R.layout.task_view_layout,
				null);
		mContext = context;
		mActivity = activcity;
		tList.setSid(Integer.valueOf(KygroupApplication.getmApplication().mUser
				.getRSid()));
		initView();
		setValue();
	}

	public View getView() {
		((BaseActivity) mContext).setTitleBarVisibility(View.VISIBLE);
		return mView;
	}

	private void initView() {
		mListView = (ListView) mView.findViewById(R.id.mListView);
	}

	private void setValue() {
		adapter = new TaskAdapter(mContext, lists);
		mListView.setAdapter(adapter);
		refush();
	}

	private void refush() {
		mActivity.startWaitingDialog();
		GetTaskListTask task = new GetTaskListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				mActivity.closeWaitingDialog();
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
}
