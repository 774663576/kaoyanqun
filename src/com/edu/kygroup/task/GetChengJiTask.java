package com.edu.kygroup.task;

import com.edu.kygroup.domin.ChengJi;

public class GetChengJiTask extends BaseAsyncTask<ChengJi, Void, Integer> {
	private ChengJi chengji;

	@Override
	protected Integer doInBackground(ChengJi... params) {
		chengji = params[0];
		chengji.getChengji();
		return 0;
	}

}
