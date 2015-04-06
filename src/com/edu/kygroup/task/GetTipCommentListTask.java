package com.edu.kygroup.task;

import com.edu.kygroup.domin.TipCommentList;

public class GetTipCommentListTask extends
		BaseAsyncTask<TipCommentList, Void, Integer> {
	private TipCommentList list;

	@Override
	protected Integer doInBackground(TipCommentList... params) {
		list = params[0];
		return list.getCommentLists();
	}

}
