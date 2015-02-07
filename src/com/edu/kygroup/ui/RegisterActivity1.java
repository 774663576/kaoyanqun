package com.edu.kygroup.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ViewFlipper;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.ui.RegisterStep.onNextListener;

public class RegisterActivity1 extends BaseActivity implements onNextListener,
		OnClickListener {
	protected String phone = "";
	protected String password = "";

	private Dialog mDialog;

	private int mCurrentStepIndex = 1;
	private int second = 60;// 用于重新获取验证码时间倒计时

	private ViewFlipper mVfFlipper;

	private RegisterStep step;
	private RegisterStep1 step1;
	private RegisterStep2 step2;
	private RegisterStep3 step3;

	public Register register;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				step2.setSecond(second);
				second--;
				if (second < 0) {
					step2.setEnable();
					return;
				}
				this.sendEmptyMessageDelayed(0, 1000);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		register = (Register) getIntent().getSerializableExtra("register");
		initView();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_register_activity1, null);
	}

	private void initView() {
		setTitleText("注册");
		setLeftBg(R.drawable.btn_back_detail_normal);
		mVfFlipper = (ViewFlipper) findViewById(R.id.reg_vf_viewflipper);
		step = initStep();
		step.setmOnNextListener(this);
		setLeftBtnClickListener(this);

	}

	private RegisterStep initStep() {

		switch (mCurrentStepIndex) {
		case 1:
			if (step1 == null) {
				step1 = new RegisterStep1(this, mVfFlipper.getChildAt(0));
			}
			return step1;
		case 2:
			if (step2 == null) {
				step2 = new RegisterStep2(this, mVfFlipper.getChildAt(1));
			}
			mHandler.sendEmptyMessage(0);
			return step2;
		case 3:
			if (step3 == null) {
				step3 = new RegisterStep3(this, mVfFlipper.getChildAt(2));
			}
			return step3;
		default:
			break;
		}
		return null;
	}

	protected void postHandler() {
		second = 60;
		mHandler.sendEmptyMessage(0);
	}

	@Override
	public void next() {
		mCurrentStepIndex++;
		step = initStep();
		step.setmOnNextListener(this);
		mVfFlipper.showNext();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void startWaitingDialog() {
		try {
			if (mDialog == null) {
				mDialog = new Dialog(this, R.style.waiting);
			}
			if (!mDialog.isShowing()) {
				mDialog.setContentView(R.layout.waiting);
				mDialog.setCanceledOnTouchOutside(false);
				mDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeWaitingDialog() {
		try {
			if (mDialog != null) {
				mDialog.dismiss();
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		onBack();
	}

	private void onBack() {
		if (mCurrentStepIndex <= 1) {
			finish();
		} else if (mCurrentStepIndex == 2) {
			second = 60;
			mHandler.removeMessages(0);
			previous();
		} else {
			previous();
		}
	}

	private void previous() {
		mCurrentStepIndex--;
		step = initStep();
		mVfFlipper.showPrevious();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBack();
		}
		return false;
	}
}
