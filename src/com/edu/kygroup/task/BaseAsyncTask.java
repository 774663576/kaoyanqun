package com.edu.kygroup.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	private AbstractTaskPostCallBack<Result> mCallBack;

	public void setmCallBack(AbstractTaskPostCallBack<Result> mCallBack) {
		this.mCallBack = mCallBack;
	}

	@SuppressLint("NewApi")
	public void executeParallel(Params... params) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			super.execute(params);
		} else {
			super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		if (mCallBack != null) {
			mCallBack.taskFinish(result);
		}
	}
}
