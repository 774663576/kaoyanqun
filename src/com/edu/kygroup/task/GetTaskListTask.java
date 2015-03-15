package com.edu.kygroup.task;

import com.edu.kygroup.domin.TaskList;

public class GetTaskListTask extends BaseAsyncTask<TaskList, Void, Integer> {
	private TaskList list;

	@Override
	protected Integer doInBackground(TaskList... params) {
		list = params[0];
		int res = list.refush();
		return res;
	}

}
