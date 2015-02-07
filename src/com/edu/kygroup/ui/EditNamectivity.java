package com.edu.kygroup.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.edu.kygroup.R;
import com.edu.kygroup.utils.ToastUtils;

public class EditNamectivity extends BaseActivity implements OnClickListener {
	private EditText edit_name;

	private String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = getIntent().getStringExtra("name");
		initView();
	}

	private void initView() {
		setTitleText("姓名");
		setRightBtnVisibility(View.VISIBLE);
		setRightBtnBg(R.drawable.submit_selector);
		edit_name = (EditText) findViewById(R.id.editText1);
		edit_name.setText(name);
		setRightBtnClickListener(this);
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_edit_namectivity, null);
	}

	@Override
	public void onClick(View v) {
		String name = edit_name.getText().toString();
		if (name.replace(" ", "").length() == 0) {
			ToastUtils.showShortToast("不能为空");
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("name", name);
		setResult(EditSelfInfoActivity.EDIT_NAME, intent);
		finish();
	}

}
