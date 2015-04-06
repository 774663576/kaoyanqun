package com.edu.kygroup.task;

import com.edu.kygroup.domin.TipsComment;

public class AddTipCommentTask extends
		BaseAsyncTask<TipsComment, Void, Integer> {
	private TipsComment comment;

	@Override
	protected Integer doInBackground(TipsComment... params) {
		comment = params[0];
		return comment.uploadComment();
	}

}
