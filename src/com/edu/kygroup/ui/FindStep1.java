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
import com.edu.kygroup.utils.Util;

public class FindStep1 extends FindPasswordStep implements OnClickListener {
	private EditText edit_num;
	private Button btn_next;

	public FindStep1(FindPasswordActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_num = (EditText) findViewById(R.id.edit_num);
		btn_next = (Button) findViewById(R.id.btn_next);

	}

	@Override
	public void setListener() {
		btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String phone = edit_num.getText().toString();
		mActivity.setPhone(phone);
		if (!Util.isPhoneNum(phone)) {
			ToastUtils.showShortToast("手机号格式不正确");
			return;
		}
		getCode(phone);
	}

	private void getCode(String phoneNum) {
		mActivity.startWaitingDialog();
		String url = UrlUtils.LOSE_PASSWORD_URL + "user.email=" + phoneNum;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				mActivity.closeWaitingDialog();
				String res = "";
				try {
					JSONObject obj = new JSONObject((String) result);
					res = obj.optString("result");
				} catch (Exception e) {
				}
				if (res.equals("200")) {
					mActivity.closeWaitingDialog();
					mOnNextListener.next();
				} else if (res.equals("-1")) {
					ToastUtils.showShortToast("手机号码不存在");
				} else {
					ToastUtils.showShortToast("操作失败");
				}
			}
		});
		task.execute(null, TagUtils.GET_VERIFY_CODE, url);
	}
}
