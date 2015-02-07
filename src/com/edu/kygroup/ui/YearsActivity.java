package com.edu.kygroup.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.ui.SelectBaseActivity;
import com.edu.kygroup.utils.ActivityHolder;
import com.umeng.analytics.MobclickAgent;

public class YearsActivity extends SelectBaseActivity {
	private boolean mIsEdit = false;
	private boolean mIsAddFocus = false;
	private Register register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		initView();
		initListView();
		addItemClickListener();
	}

	private void initView() {
		setTitleText(R.string.s_select_year);
		mIsEdit = getIntent().getBooleanExtra("edit", false);
		mIsAddFocus = getIntent().getBooleanExtra("addfocus", false);
		System.out.println("year:::::::::::" + mIsEdit + "     " + mIsAddFocus);
		if (!mIsEdit || !mIsAddFocus) {
			ActivityHolder.getInstance().addActivity(this);
		}
	}

	private void initListView() {
		String[] years = null;
		if (KygroupApplication.mFlag == 0) {
			years = getResources().getStringArray(R.array.aim_years);
		} else if (KygroupApplication.mFlag == 1) {
			years = getResources().getStringArray(R.array.graduate_years);
		} else {
			years = getResources().getStringArray(R.array.bachelor_years);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.university_item, R.id.university_name, years);
		mListView.setAdapter(adapter);

	}

	private void addItemClickListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String year = (String) parent.getAdapter().getItem(position);
				if (mIsEdit) {
					Intent intent = new Intent();
					intent.putExtra("year", year);
					setResult(4, intent);
					finish();
				} else if (mIsAddFocus) {
					Log.v("AAA", "============ years");
					Intent intent = getIntent();
					Bundle bundle = intent.getExtras();
					Intent newIntent = new Intent();
					newIntent.setAction("com.kygroup.addfocus");
					newIntent.putExtras(bundle);
					newIntent.putExtra("year", year);
					sendBroadcast(newIntent);
					ActivityHolder.getInstance().finishAllFocusActivity();
					finish();
					KygroupApplication.exitFoucsActivity();

					// finishFocusActivity();
				} else {
					KygroupApplication.mUser.setRYear(year);
					register = (Register) getIntent().getSerializableExtra(
							"register");
					register.setRegister_year(year);
					Intent intent = new Intent(YearsActivity.this,
							RegisterActivity1.class);
					intent.putExtra("from", "yearsactivity");
					intent.putExtra("register", register);
					startActivity(intent);
					finish();
					KygroupApplication.exitFoucsActivity();

				}
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
	}

	public void finishFocusActivity() {
		new Thread() {
			@Override
			public void run() {
				super.run();

			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		ActivityHolder.getInstance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("YearsActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("YearsActivity");
		MobclickAgent.onPause(this);
	}
}
