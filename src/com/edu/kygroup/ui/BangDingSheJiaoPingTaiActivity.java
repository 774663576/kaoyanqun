package com.edu.kygroup.ui;

import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.edu.kygroup.R;

public class BangDingSheJiaoPingTaiActivity extends BaseActivity implements
		OnClickListener, PlatformActionListener {
	private TextView txt_QQ;
	private TextView txt_wechat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		initView();
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText("绑定社交平台");
		txt_QQ = (TextView) findViewById(R.id.txt_QQ);
		txt_wechat = (TextView) findViewById(R.id.txt_weichat);
		Platform plat = ShareSDK.getPlatform(this, QQ.NAME);
		initPlat(plat, txt_QQ);
		plat = ShareSDK.getPlatform(this, Wechat.NAME);
		initPlat(plat, txt_wechat);
		setListener();
	}

	private void initPlat(Platform plat, TextView txt) {
		if (plat.isValid()) {
			String userName = plat.getDb().get("nickname");
			if (userName == null || userName.length() <= 0
					|| "null".equals(userName)) {
				userName = getName(plat);
			}
			txt.setText(userName);
		} else {
			txt.setText("尚未绑定");
		}
	}

	private String getName(Platform plat) {
		if (plat == null) {
			return "";
		}

		String name = plat.getName();
		if (name == null) {
			return "";
		}

		int resId = cn.sharesdk.framework.utils.R.getStringRes(this,
				plat.getName());
		return this.getString(resId);
	}

	private void setListener() {
		txt_QQ.setOnClickListener(this);
		txt_wechat.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_QQ:
			platform(ShareSDK.getPlatform(this, QQ.NAME), txt_QQ);
			break;
		case R.id.txt_weichat:

			platform(ShareSDK.getPlatform(this, Wechat.NAME), txt_wechat);
			break;
		default:
			break;
		}
	}

	private void platform(Platform plat, TextView v) {
		if (plat.isValid()) {
			plat.removeAccount();
			v.setText("尚未绑定");
			return;
		}
		plat.setPlatformActionListener(this);
		plat.authorize();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_bang_ding_she_jiao_ping_tai, null);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Platform plat = ShareSDK.getPlatform(
						BangDingSheJiaoPingTaiActivity.this, QQ.NAME);
				initPlat(plat, txt_QQ);
				plat = ShareSDK.getPlatform(
						BangDingSheJiaoPingTaiActivity.this, Wechat.NAME);
				initPlat(plat, txt_wechat);
			}
		});
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {

	}
}
