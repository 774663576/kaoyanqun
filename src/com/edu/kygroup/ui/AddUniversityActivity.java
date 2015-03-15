package com.edu.kygroup.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.fragment.SelectObjectFragment;
import com.edu.kygroup.fragment.SelectSchoolFragment;

public class AddUniversityActivity extends FragmentActivity implements
		OnClickListener {
	private FragmentTransaction fraTra = null;
	private FragmentManager manager;
	private SelectSchoolFragment schoolFragment;
	private SelectObjectFragment objectFragment;
	private TextView btn_school;
	private TextView btn_object;
	private int registerType = 0;
	private View line_school;
	private View line_object;
	private int register_role = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_university_activity);
		registerType = getIntent().getIntExtra("registerType", 0);
		register_role = getIntent().getIntExtra("register_role", 0);
		initView();
		initFragment();
	}

	private void initFragment() {
		manager = getSupportFragmentManager();
		fraTra = manager.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt("registerType", registerType);
		bundle.putInt("register_role", register_role);
		schoolFragment = new SelectSchoolFragment();
		schoolFragment.setArguments(bundle);
		fraTra.add(R.id.main_layout, schoolFragment);
		fraTra.commit();
	}

	private void initView() {
		line_object = (View) findViewById(R.id.line_object);
		line_school = (View) findViewById(R.id.line_school);
		line_object.setVisibility(View.GONE);
		btn_object = (TextView) findViewById(R.id.select_by_object);
		btn_school = (TextView) findViewById(R.id.select_by_school);
		setListener();
	}

	private void setListener() {
		btn_object.setOnClickListener(this);
		btn_school.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		fraTra = getSupportFragmentManager().beginTransaction();
		switch (v.getId()) {
		case R.id.select_by_school:
			if (objectFragment != null) {
				fraTra.hide(objectFragment);
			}
			fraTra.show(schoolFragment);
			line_object.setVisibility(View.GONE);
			line_school.setVisibility(View.VISIBLE);

			break;
		case R.id.select_by_object:
			if (objectFragment == null) {
				objectFragment = new SelectObjectFragment();
				fraTra.add(R.id.main_layout, objectFragment);
			} else {
				objectFragment.onResume();
			}
			if (schoolFragment != null) {
				fraTra.hide(schoolFragment);
			}
			fraTra.show(objectFragment);
			line_school.setVisibility(View.GONE);
			line_object.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		fraTra.commit();

	}
}
