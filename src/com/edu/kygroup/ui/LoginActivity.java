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
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.LoginInfo;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.domin.PersonInfo;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.ErrorUtils;
import com.edu.kygroup.utils.MD5;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity implements OnClickListener,
		IBindData {
	private static final String TAG = "LoginActivity";
	private Button mLoginBtn;
	private TextView mForgetPwd;
	private EditText mLoginName;
	private EditText mLoginPwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityHolder.getInstance().addActivity(this);
		isLogin();
		initView();
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText(R.string.kygroup);
		setBottomBarVisibility(View.GONE);
		mLoginBtn = (Button) findViewById(R.id.btn_login);
		mLoginBtn.setOnClickListener(this);
		setRightBtnClickListener(this);
		mForgetPwd = (TextView) findViewById(R.id.txt_forgot_psw);
		mForgetPwd.setOnClickListener(this);
		mLoginName = (EditText) findViewById(R.id.edit_num);
		mLoginPwd = (EditText) findViewById(R.id.edit_psw);
		((TextView) findViewById(R.id.txt_yanjiushegn_regisiter))
				.setOnClickListener(this);
		((TextView) findViewById(R.id.txt_kaosheng_regisiter))
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.txt_kaosheng_regisiter: {
			// startMessageAcitivty(0);
			KygroupApplication.mFlag = 0;
			startMessageAcitivty(2, 0);

			break;
		}
		case R.id.txt_yanjiushegn_regisiter: {
			KygroupApplication.mFlag = 1;
			startMessageAcitivty(1, 1);
			break;
		}
		case R.id.btn_login: {
			login();
			break;
		}
		case R.id.txt_forgot_psw: {
			// if ("".equals(mLoginName.getText().toString())) {
			// ToastUtils.showShortToast(LoginActivity.this,
			// R.string.mail_input);
			// } else {
			// postForResetPassword();
			// }
			startActivity(new Intent(this, FindPasswordActivity.class));
			break;
		}
		default:
			break;
		}
	}

	private void startMessageAcitivty(int register, int role) {
		// Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
		// intent.putExtra("role", role);
		// startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		Intent intent = new Intent(LoginActivity.this,
				AddUniversityActivity.class);
		intent.putExtra("registerType", register);
		intent.putExtra("register_role", role);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	// AddUniversityActivity
	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.activity_login, null);
	}

	public void setLoginName(String suffix) {
		String pre = mLoginName.getText().toString();
		String mail = pre + suffix;
		mLoginName.setText(mail);
		mLoginName.setSelection(mLoginName.length());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ActivityHolder.getInstance().removeActivity(this);
		super.onDestroy();
	}

	private void isLogin() {
		String email = mUser.getEmail();
		if (!TextUtils.isEmpty(email)) {
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void login() {
		String email = mLoginName.getText().toString();
		String pwd = mLoginPwd.getText().toString();
		// if (TextUtils.isEmpty(email)) {
		// ToastUtils.showShortToast(this, R.string.mail_input);
		// return;
		// }
		if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
			ToastUtils.showShortToast(this, R.string.pwd_input);
			return;
		}
		// if (StringUtils.isMailCorrect(email)) {
		startWaitingDialog();
		String url = UrlUtils.LOGIN_URL + "user.email=" + MD5.Md5_16(email)
				+ "&user.password=" + pwd;
		new NetworkTask().execute(this, TagUtils.LOGIN_TAG, url);
		// } else {
		// ToastUtils.showShortToast(this, R.string.mail_error);
		// }
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.LOGIN_TAG:
			System.out.println("obj::::::::::::" + obj);
			if (null != obj) {
				LoginInfo info = (LoginInfo) obj;
				System.out.println("obj::::::::::::==" + info.getResult());
				if (info.getResult() == 200) {
					savePersonMsg(info);
					loginHuanxin(mLoginName.getText().toString(),
							"5991cbe94e304ce7dd7b333e66767037");
				} else {
					closeWaitingDialog();
					ToastUtils.showShortToast(ErrorUtils.getErrorMsg(info
							.getResult()));
				}
			} else {
				closeWaitingDialog();
				ToastUtils.showShortToast("登陆失败");
			}
			break;
		case TagUtils.LOSE_PASSWORD: {
			closeWaitingDialog();
			if (obj != null && !(obj instanceof Boolean)) {
				if ("200".equals(obj.toString())) {
					ToastUtils.showShortToast(LoginActivity.this,
							R.string.tips_send_password_email);
				} else if ("-1".equals(obj.toString())) {
					ToastUtils.showShortToast(LoginActivity.this,
							R.string.email_error);
				} else {
				}
			}
			break;
		}
		}
		return null;
	}

	private void loginHuanxin(final String mRegister, final String password) {
		new Thread() {
			public void run() {
				// 调用sdk登陆方法登陆聊天服务器
				EMChatManager.getInstance().login(MD5.Md5_16(mRegister),
						password, new EMCallBack() {
							@Override
							public void onSuccess() {
								runOnUiThread(new Runnable() {
									public void run() {
										closeWaitingDialog();
										Toast.makeText(getApplicationContext(),
												"登录成功", 0).show();
										Intent intent = new Intent(
												LoginActivity.this,
												HomeActivity.class);
										startActivity(intent);
										finish();
									}
								});
							}

							@Override
							public void onProgress(int progress, String status) {

							}

							@Override
							public void onError(int code, final String message) {
								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(getApplicationContext(),
												"登录失败: " + message, 0).show();

									}
								});

							}
						});
			}
		}.start();
	}

	private void savePersonMsg(final LoginInfo info) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				saveMsg(info);
			}
		}.start();
	}

	private void saveMsg(LoginInfo info) {
		PersonInfo pinfo = info.getPersoninfo();
		SharedPreferences prefs = getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putLong("timestamp", info.getTimestamp());
		editor.putString("email", pinfo.getEmail());
		// 报考院校信息
		editor.putString("regcol", pinfo.getAim().getCename());
		editor.putString("regmaj", pinfo.getAim().getMname());
		editor.putString("regyea", pinfo.getSession() + "");
		editor.putString("reguni", pinfo.getAim().getSname());
		editor.putString("group_id", pinfo.getAim().getGroupid());
		editor.putString("reguniid", pinfo.getAim().getSid() + "");
		editor.putString("regcolid", pinfo.getAim().getCeid() + "");
		editor.putString("regmajid", pinfo.getAim().getMid() + "");

		editor.putString("nickname", pinfo.getNickname());
		editor.putString("gender", pinfo.getGender());
		editor.putString("province", pinfo.getPname());
		editor.putString("proid", pinfo.getPid());
		editor.putString("city", pinfo.getCname());
		editor.putString("cityid", pinfo.getCity());

		// 母校信息
		editor.putString("majoruni", pinfo.getMajor().getSname());
		editor.putString("majorcol", pinfo.getMajor().getCename());
		editor.putString("majormaj", pinfo.getMajor().getMname());

		editor.putString("majoruniid", pinfo.getMajor().getSid() + "");
		editor.putString("majorcolid", pinfo.getMajor().getCeid() + "");
		editor.putString("majormajid", pinfo.getMajor().getMid() + "");

		editor.putString("majoryear", pinfo.getEnterTime());
		editor.putString("pic", pinfo.getImage());

		editor.putString("announce", pinfo.getDeclaration());// 个人宣言
		editor.putString("howgoing", pinfo.getHowgoing());
		editor.putString("state", pinfo.getStatus());
		editor.putInt("role", pinfo.getRole());
		editor.putString("score", pinfo.getScores());
		editor.putInt("confirm", pinfo.getConfirm());
		editor.commit();
		KygroupApplication.mFlag = pinfo.getRole();
		User user = KygroupApplication.mUser;
		user.setEmail(pinfo.getEmail());
		user.setRSchool(pinfo.getAim().getSname());
		user.setRCollege(pinfo.getAim().getCename());
		user.setRMajor(pinfo.getAim().getMname());
		user.setRSid(pinfo.getAim().getSid() + "");
		user.setRCid(pinfo.getAim().getCeid() + "");
		user.setRMid(pinfo.getAim().getMid() + "");
		user.setRYear(pinfo.getSession() + "");
		user.setCity(pinfo.getCname());
		user.setPic(pinfo.getImage());
		user.setAnnounce(pinfo.getDeclaration());
		user.setHowGoing(pinfo.getHowgoing());
		user.setECollege(pinfo.getMajor().getCename());
		user.setESchool(pinfo.getMajor().getSname());
		user.setEYear(pinfo.getEnterTime());
		user.setNickName(pinfo.getNickname());
		user.setRole(pinfo.getRole());
		user.setGender(pinfo.getGender());
		user.setProvince(pinfo.getPname());
		user.setEMajor(pinfo.getMajor().getMname());
		user.setEColleageid(pinfo.getMajor().getCeid() + "");
		user.setEMajorid(pinfo.getMajor().getMid() + "");
		user.setESchoolid(pinfo.getMajor().getSid() + "");
		user.setProid(pinfo.getPid());
		user.setCityid(pinfo.getCity());
		user.setScore(pinfo.getScores());
		user.setConfirm(pinfo.getConfirm());
		addAdminUser();
	}

	/**
	 * 字符在字符串中出现的次数
	 */
	public int occurTimes(String string, String ch) {
		int pos = -2;
		int n = 0;
		while (pos != -1) {
			if (pos == -2) {
				pos = -1;
			}
			pos = string.indexOf(ch, pos + 1);
			if (pos != -1) {
				n++;
			}
		}
		return n;
	}

	private void postForResetPassword() {
		String url = UrlUtils.LOSE_PASSWORD_URL + "user.email="
				+ mLoginName.getText().toString();
		startWaitingDialog();
		new NetworkTask().execute(LoginActivity.this, TagUtils.LOSE_PASSWORD,
				url);
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
		MobclickAgent.onPageStart("LoginActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("LoginActivity");
		MobclickAgent.onPause(this);
	}
}
