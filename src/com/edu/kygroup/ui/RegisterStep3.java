package com.edu.kygroup.ui;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.edu.kygroup.R;
import com.edu.kygroup.utils.ToastUtils;

public class RegisterStep3 extends RegisterStep implements OnClickListener {
	private EditText edit_password;
	private Button btn_next;

	public RegisterStep3(RegisterActivity1 activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_password = (EditText) findViewById(R.id.edit_passord);
		btn_next = (Button) findViewById(R.id.btn_next);

	}

	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String pswd = edit_password.getText().toString().replaceAll(" ", "");
		if (pswd.length() < 6) {
			ToastUtils.showShortToast("密码必须为6位以上");
			return;
		}
		register(pswd);
	}

	private void register(String pswd) {
		Intent intent = new Intent(mContext, PersonalMsgActivity1.class);
		intent.putExtra("phone", mActivity.getPhone());
		intent.putExtra("password", pswd);
		mActivity.register.setRegister_email(mActivity.getPhone());
		mActivity.register.setRegister_passwrod(pswd);
		intent.putExtra("register", mActivity.register);
		mContext.startActivity(intent);
		mActivity.finish();
	}
}
