/**
 * 工程名: KyGroup
 * 文件名: HelpActivity.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-12-15下午2:22:56
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.edu.kygroup.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 类名: HelpActivity <br/>
 * 功能: TODO 帮助页面. <br/>
 * 日期: 2013-12-15 下午2:22:56 <br/>
 * 
 * @author lx
 * @version
 */
public class HelpActivity extends BaseActivity implements OnClickListener {

	private View mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText(R.string.help_tip);
		setLeftBtnVisibility(View.GONE);
		setBottomBarVisibility(View.GONE);
		mView = LayoutInflater.from(HelpActivity.this).inflate(
				R.layout.help_activity, null);
		addView(mView);
	}

	@Override
	protected View setContentView() {
		return mView;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("HelpActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("HelpActivity");
		MobclickAgent.onPause(this);
	}
}
