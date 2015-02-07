package com.edu.kygroup.ui;

import java.io.Serializable;
import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ColleageAdapter;
import com.edu.kygroup.domin.Colleage;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ColleageActivity extends SelectBaseActivity implements IBindData {
	private boolean mIsEdit = false;
	private boolean mIsAddFocus = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		addItemClickListener();
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		setTitleText(R.string.s_select_colleage);
		mLists = (ArrayList<Colleage>) getIntent().getSerializableExtra(
				"colleages");
		mAdapter = new ColleageAdapter(this, mLists);
		mListView.setAdapter(mAdapter);
		mIsEdit = getIntent().getBooleanExtra("edit", false);
		mIsAddFocus = getIntent().getBooleanExtra("addfocus", false);
		if (!mIsEdit || !mIsAddFocus) {
			ActivityHolder.getInstance().addActivity(this);
		}
		if (mIsAddFocus) {
			Log.v("AAA", "2-------add");
			ActivityHolder.getInstance().addFocusActivity(this);
		}
	}

	private void addItemClickListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Colleage col = mLists.get(position);
				String url = UrlUtils.GET_MAJOR_URL + "ceid=" + col.getId();
				if (mIsEdit) {
					Intent intent = new Intent();
					intent.putExtra("url", url);
					intent.putExtra("colleage", col.getName());
					intent.putExtra("colleageid", col.getId());
					setResult(EditActivity.COLLEAGE_TAG, intent);
					finish();
				} else {
					if (!mIsAddFocus) {
						url = url + "&sid=" + mUser.getRSid();
						mUser.setRCollege(col.getName());
						mUser.setRCid(col.getId());
					} else {
						url = url + "&sid="
								+ getIntent().getStringExtra("unikey");
						getIntent().putExtra("col", col.getName());
						getIntent().putExtra("colkey", col.getId());

					}
					startWaitingDialog();
					new NetworkTask().execute(ColleageActivity.this,
							TagUtils.GET_MAJOR_TAG, url);
				}
			}
		});
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		closeWaitingDialog();
		if (null != obj) {
			ArrayList<Colleage> lists = (ArrayList<Colleage>) obj;
			if (lists.size() > 0) {
				if (mIsAddFocus) {
					startMajorActivity(getIntent(), lists);
					// finish();
				} else {
					startMajorActivity(new Intent(), lists);
				}
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		}
		return null;
	}

	private void startMajorActivity(Intent intent, ArrayList<Colleage> lists) {
		intent.setClass(this, MajorActivity.class);
		intent.putExtra("majors", (Serializable) lists);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		ActivityHolder.getInstance().removeActivity(this);
		if (mIsAddFocus) {
			Log.v("AAA", "2-------destroy");
			ActivityHolder.getInstance().removeFocusActivity(this);
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ColleageActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ColleageActivity");
		MobclickAgent.onPause(this);
	}
}
