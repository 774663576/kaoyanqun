package com.edu.kygroup.task;

import com.edu.kygroup.domin.TaskComment;

public class AddTaskCommentTask extends
		BaseAsyncTask<TaskComment, Void, Integer> {
	private TaskComment comment;

	@Override
	protected Integer doInBackground(TaskComment... params) {
		comment = params[0];
		return comment.addTaskComment();
	}

}
