package com.edu.kygroup.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.utils.ActivityHolder;
import com.umeng.analytics.MobclickAgent;

public class MessageActivity extends BaseActivity implements OnClickListener {
	private int mRole;
	private LinearLayout mSelectLayout;
	private TextView mSkipRegister;
	private TextView mLogin;
	private TextView mUniversityView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityHolder.getInstance().addActivity(this);
		initData();
		initView();
	}

	private void initData() {
		mRole = getIntent().getIntExtra("role", 0);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		setTitleText(R.string.university_msg);
		mSelectLayout = (LinearLayout) findViewById(R.id.selectlayout);
		mLogin = (TextView) findViewById(R.id.goto_login);
		mSkipRegister = (TextView) findViewById(R.id.skip_register);
		mSkipRegister.setOnClickListener(this);
		mSelectLayout.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mUniversityView = (TextView) findViewById(R.id.university_tip);
		if (mRole == 1) {
			mUniversityView.setText(R.string.graduate_university);
			((TextView) findViewById(R.id.select_year))
					.setText(R.string.select_graduate_year);
			KygroupApplication.mFlag = 1;
		} else {
			handleText();
			KygroupApplication.mFlag = 0;
		}
		KygroupApplication.mUser.setRole(mRole);
	}

	private void handleText() {
		SpannableString university = new SpannableString(
				getString(R.string.kaoyan_university));
		university.setSpan(new ForegroundColorSpan(0xffbb2222), 0, 2,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		university.setSpan(new ForegroundColorSpan(0xff707070), 2,
				university.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mUniversityView.setText(university);
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.message_activity, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// case R.id.skip_register:{
		// Intent intent = new
		// Intent(MessageActivity.this,RegisterActivity.class);
		// startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		// break;
		// }
		case R.id.selectlayout: {
			Intent intent = new Intent(MessageActivity.this,
					SelectActivity.class);
			intent.putExtra("role", mRole);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.goto_login: {
			finish();
		}
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ActivityHolder.getInstance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MessageActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MessageActivity");
		MobclickAgent.onPause(this);
	}
}
