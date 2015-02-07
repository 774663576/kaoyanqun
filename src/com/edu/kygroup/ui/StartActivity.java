package com.edu.kygroup.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;

import com.edu.kygroup.R;

public class StartActivity extends Activity {

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_bg);
		stop2Seconds();
		initHandler();
	}

	private void stop2Seconds() {
		new Thread() {
			public void run() {
				try {
					sleep(2000);
					mHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				startActivity();
				super.handleMessage(msg);
			}
		};
	}

	private void startActivity() {
		Intent intent;
		String email = KygroupApplication.mUser.getEmail();
		if (!TextUtils.isEmpty(email)) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		} else {
			intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
		}
		finish();

	}
}
