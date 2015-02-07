package com.edu.kygroup.ui;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class RegisterStep2 extends RegisterStep implements OnClickListener {
	private TextView txt_show;
	private Button btn_next;
	private EditText edit_code;
	private Button btn_get_code;

	public RegisterStep2(RegisterActivity1 activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		txt_show = (TextView) findViewById(R.id.txt_show);
		btn_get_code = (Button) findViewById(R.id.btn_getCode);
		btn_next = (Button) findViewById(R.id.btn_next);
		edit_code = (EditText) findViewById(R.id.edit_code);
		txt_show.setText(Html.fromHtml("验证码已发送到  <font color=\"red\">"
				+ mActivity.getPhone() + "</font>"));
	}

	@Override
	public void setListener() {
		btn_get_code.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	protected void setValue() {
	}

	protected void setSecond(int second) {
		btn_get_code.setText("重新获取" + second);
		btn_get_code.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			checkCode(edit_code.getText().toString());
			break;
		case R.id.btn_getCode:
			getCode(mActivity.getPhone());
			break;
		default:
			break;
		}
	}

	public void setEnable() {
		btn_get_code.setText("获取验证码");
		btn_get_code.setEnabled(true);
	}

	private void checkCode(String code) {
		mActivity.startWaitingDialog();
		String url = UrlUtils.CHECK_CODE + "user.email=" + mActivity.getPhone()
				+ "&valid=" + code;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				mActivity.closeWaitingDialog();
				boolean err = (Boolean) result;
				if (err) {
					mOnNextListener.next();
				} else {
					ToastUtils.showShortToast("验证码输入错误");
				}
			}
		});
		task.execute(null, TagUtils.CHECK_CODE, url);
	}

	private void getCode(String phoneNum) {
		mActivity.startWaitingDialog();
		String url = UrlUtils.GET_VERIFY_CODE + "user.email=" + phoneNum;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				mActivity.closeWaitingDialog();
				btn_get_code.setEnabled(true);
				mActivity.postHandler();

			}
		});
		task.execute(null, TagUtils.GET_VERIFY_CODE, url);
	}
}
