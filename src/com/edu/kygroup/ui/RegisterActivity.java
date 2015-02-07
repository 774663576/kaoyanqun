package com.edu.kygroup.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.utils.Util;
import com.umeng.analytics.MobclickAgent;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText edit_phone;
	private EditText mPwdEdit;
	private EditText mPwdAgainEdit;
	private Button mRegisterBtn;
	private String mPhone;
	private String mPwd;
	private Button btn_getcode;
	private EditText edit_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		// initData();
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		setTitleText(R.string.new_user_register);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		mPwdEdit = (EditText) findViewById(R.id.pwd_input);
		mPwdAgainEdit = (EditText) findViewById(R.id.pwd_again_input);
		mRegisterBtn = (Button) findViewById(R.id.register_now);
		btn_getcode = (Button) findViewById(R.id.btn_getcode);
		edit_code = (EditText) findViewById(R.id.edit_code);
		setListener();
	}

	private void setListener() {
		mRegisterBtn.setOnClickListener(this);
		btn_getcode.setOnClickListener(this);
	}

	// private void initData(){
	// mRegister = new Register();
	// }

	@Override
	protected View setContentView() {
		return mInflater.inflate(R.layout.register_activity, null);
	}

	private void getCode(String phoneNum) {
		startWaitingDialog();
		String url = UrlUtils.GET_VERIFY_CODE + "user.email=" + phoneNum;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				closeWaitingDialog();

			}
		});
		task.execute(null, TagUtils.GET_VERIFY_CODE, url);
	}

	private void checkCode(String code) {
		startWaitingDialog();
		String url = UrlUtils.CHECK_CODE + "user.email="
				+ edit_phone.getText().toString() + "&valid=" + code;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				boolean err = (Boolean) result;
				if (err) {
					register();
				} else {
					ToastUtils.showShortToast("验证码输入错误");
				}
			}
		});
		task.execute(null, TagUtils.CHECK_CODE, url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_now:
			String code = edit_code.getText().toString();
			mPhone = edit_phone.getText().toString();
			mPwd = mPwdEdit.getText().toString();
			if (TextUtils.isEmpty(mPhone)) {
				ToastUtils.showShortToast(this, R.string.mail_input);
				return;
			}
			if (TextUtils.isEmpty(mPwd) || mPwd.length() < 6
					|| mPwd.length() > 18) {
				ToastUtils.showShortToast(this, R.string.pwd_length_error);
				return;
			}
			String pwd2 = mPwdAgainEdit.getText().toString();
			if (StringUtils.isEmpty(pwd2) || !pwd2.equals(mPwd)) {
				ToastUtils.showShortToast(this, R.string.pwd_not_same);
				return;
			}
			checkCode(code);
			// register();
			break;
		case R.id.btn_getcode:
			String phoneNum = edit_phone.getText().toString();
			if (!Util.isPhoneNum(phoneNum)) {
				ToastUtils.showShortToast("手机号格式不正确");
				return;
			}
			getCode(phoneNum);
			break;
		default:
			break;
		}
	}

	private void register() {
		mUser.setEmail(mPhone);
		mUser.setPassword(mPwdAgainEdit.getText().toString());
		Intent intent = new Intent(RegisterActivity.this,
				PersonalMsgActivity1.class);
		intent.putExtra("phone", mPhone);
		intent.putExtra("password", mPwd);
		startActivity(intent);
		finish();

	}

	public void setLoginName(String suffix) {
		String pre = edit_phone.getText().toString();
		String mail = pre + suffix;
		edit_phone.setText(mail);
		edit_phone.setSelection(edit_phone.length());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void saveRegisterMsg(final long timestamp) {
		new Thread() {
			public void run() {
				SharedPreferences prefs = getSharedPreferences(
						ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.putString("email", mUser.getEmail());
				editor.putString("regcol", mUser.getRCollege());
				editor.putString("regmaj", mUser.getRMajor());
				editor.putString("regyea", mUser.getRYear());
				editor.putString("reguni", mUser.getRSchool());
				editor.putString("reguniid", mUser.getRSid());
				editor.putString("regmajid", mUser.getRmid());
				editor.putString("regcolid", mUser.getRCid());
				editor.putInt("role", mUser.getRole());
				editor.putLong("timestamp", timestamp);
				editor.commit();
				ActivityHolder.getInstance().finishAllActivity();
				addAdminUser();// 添加
			};
		}.start();

	}

	public void addAdminUser() {
		MessageBean bean = new MessageBean();
		Context context = KygroupApplication.getApplication();
		bean.setFriendName(context.getString(R.string.admin_user));
		bean.setFriends_email(context.getString(R.string.admin_mail));
		bean.setMsg_content(context.getString(R.string.admin_support));
		bean.setMsg_count(0);
		bean.setImg("admin");
		bean.setRead("0");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("RegisterActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("RegisterActivity");
		MobclickAgent.onPause(this);
	}

}
