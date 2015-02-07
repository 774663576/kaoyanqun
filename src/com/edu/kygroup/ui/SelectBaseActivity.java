package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ColleageAdapter;
import com.edu.kygroup.domin.Colleage;
import com.edu.kygroup.utils.ActivityHolder;

public class SelectBaseActivity extends BaseActivity {
	protected ArrayList<Colleage> mLists;
	protected ListView mListView;
	protected ColleageAdapter mAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityHolder.getInstance().addActivity(this);
		initView();
	};

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.colleage);
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.colleage_activity, null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ActivityHolder.getInstance().removeActivity(this);
		super.onDestroy();
	}
}
