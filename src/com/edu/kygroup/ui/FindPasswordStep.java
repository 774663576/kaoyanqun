package com.edu.kygroup.ui;

import android.content.Context;
import android.view.View;

public abstract class FindPasswordStep {
	private View contentRootView;
	protected FindPasswordActivity mActivity;
	protected onNextListener mOnNextListener;
	protected Context mContext;

	public FindPasswordStep(FindPasswordActivity activity, View contentRootView) {
		this.contentRootView = contentRootView;
		this.mContext = (Context) activity;
		this.mActivity = activity;
		initView();
		setListener();
	}

	public abstract void initView();

	public abstract void setListener();

	public View findViewById(int id) {
		return contentRootView.findViewById(id);

	}

	public void setmOnNextListener(onNextListener mOnNextListener) {
		this.mOnNextListener = mOnNextListener;
	}

	public interface onNextListener {
		void next();
	}
}
