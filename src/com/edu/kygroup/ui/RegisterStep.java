package com.edu.kygroup.ui;

import android.content.Context;
import android.view.View;

import com.edu.kygroup.domin.Register;

public abstract class RegisterStep {
	private View contentRootView;
	protected RegisterActivity1 mActivity;
	protected onNextListener mOnNextListener;
	protected Context mContext;
	protected Register mRegister;

	public RegisterStep(RegisterActivity1 activity, View contentRootView) {
		this.contentRootView = contentRootView;
		this.mActivity = activity;
		this.mContext = (Context) activity;
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
