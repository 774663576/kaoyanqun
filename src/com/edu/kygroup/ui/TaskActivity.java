package com.edu.kygroup.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.edu.kygroup.R;

public class TaskActivity extends Activity implements OnClickListener {
	private ImageView img_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		initView();
	}

	private void initView() {
		img_add = (ImageView) findViewById(R.id.img_add);
		setListener();
	}

	private void setListener() {
		img_add.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_add:
			break;

		default:
			break;
		}
	}
}
