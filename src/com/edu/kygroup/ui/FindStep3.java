package com.edu.kygroup.ui;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.edu.kygroup.R;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class FindStep3 extends FindPasswordStep implements OnClickListener {
	private EditText edit_password;
	private Button btn_next;

	public FindStep3(FindPasswordActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_password = (EditText) findViewById(R.id.edit_passord);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setText("完成");

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
		postForModifyPassword(pswd);
	}

	private void postForModifyPassword(String password) {
		String url = UrlUtils.MODIFY_PASSWORD_URL + "user.email="
				+ mActivity.getPhone() + "&user.password=" + password;
		mActivity.startWaitingDialog();
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				System.out.println("e:::::::::::::::::" + result);
				if ((Boolean) result) {
					ToastUtils.showShortToast(mActivity,
							R.string.tips_password_modify_success);
					mActivity.finish();
				} else {
					ToastUtils.showShortToast(mActivity,
							R.string.tips_password_modify_failure);
				}
				mActivity.closeWaitingDialog();

			}
		});
		task.execute(null, TagUtils.MODIFY_PASSWORD, url);
	}
}
