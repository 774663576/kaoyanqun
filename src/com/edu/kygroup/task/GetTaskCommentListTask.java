package com.edu.kygroup.task;

import com.edu.kygroup.domin.TaskCommentList;

public class GetTaskCommentListTask extends
		BaseAsyncTask<TaskCommentList, Void, Integer> {
	private TaskCommentList list;

	@Override
	protected Integer doInBackground(TaskCommentList... params) {
		list = params[0];
		return list.refush();
	}

}
