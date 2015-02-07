package com.edu.kygroup.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.utils.StringUtils;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity {
	public DisplayImageOptions mOptions;
	public ImageLoader mImageLoader;
	public User mUser;
	public RelativeLayout mLayout;
	public LinearLayout mBottomBar;
	protected LayoutInflater mInflater;
	private LinearLayout mDividerLine;
	private Button mLeftBtn;
	private Button mRightBtn;
	private TextView mTitle;
	private View mChildView;
	private LinearLayout mParentView;
	public Dialog mDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_layout);
		initImageOptions();
		initView();
		initData();
	}

	private void initView() {
		mInflater = LayoutInflater.from(this);
		mLayout = (RelativeLayout) findViewById(R.id.title_layout);
		mDividerLine = (LinearLayout) findViewById(R.id.title_divider);
		mLeftBtn = (Button) findViewById(R.id.left_btn);
		mRightBtn = (Button) findViewById(R.id.right_btn);
		mTitle = (TextView) findViewById(R.id.title);
		mParentView = (LinearLayout) findViewById(R.id.child_parent_view);
		mChildView = setContentView();
		mBottomBar = (LinearLayout) findViewById(R.id.bottom_bar);
		if (null != mChildView) {
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			mParentView.addView(mChildView, params);
		}
	}

	private void initData() {
		mUser = KygroupApplication.mUser;
	}

	protected abstract View setContentView();

	protected View inflate(int resource, ViewGroup root) {
		return mInflater.inflate(resource, root);
	}

	public View getParentView() {
		return mParentView;
	}

	protected void setLeftBtnVisibility(int type) {
		mLeftBtn.setVisibility(type);
	}

	protected void addView(View view) {
		mParentView.removeAllViews();
		if (null != view) {
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			mParentView.addView(view, params);
		}
	}

	protected void setRightBtnVisibility(int type) {
		mRightBtn.setVisibility(type);
	}

	protected void setRightBtnBg(int id) {
		mRightBtn.setText("");
		mRightBtn.setBackgroundResource(id);
	}

	public void setRightBtnText(int id) {
		mRightBtn.setBackgroundColor(0x00000000);
		mRightBtn.setText(id);
	}

	public void setRightBtnText(String str) {
		mRightBtn.setText(str);
	}

	public void setRightBtnBg() {
		mRightBtn.setBackgroundResource(R.drawable.category_selector);
	}

	protected void setTitleText(int id) {
		mTitle.setText(id);
	}

	protected void setTitleTextColor(int id) {
		mTitle.setTextColor(id);
	}

	protected void setTitleText(String title) {
		mTitle.setText(title);
	}

	protected void setTitleTextSize(int size) {
		mTitle.setTextSize(size);
	}

	protected void setTitleBarVisibility(int type) {
		mLayout.setVisibility(type);
		mDividerLine.setVisibility(type);
	}

	protected void setRightBg(int id) {
		mRightBtn.setText("");
		mRightBtn.setBackgroundResource(id);
	}

	protected void setLeftBtnText(int id) {
		mLeftBtn.setText(id);
	}

	protected void setRightBtnClickListener(OnClickListener listener) {
		mRightBtn.setOnClickListener(listener);
	}

	protected void setLeftBtnClickListener(OnClickListener listener) {
		mLeftBtn.setOnClickListener(listener);
	}

	public void setLeftBtnDrawable(int... ids) {
		if (ids.length >= 4) {
			mLeftBtn.setCompoundDrawablesWithIntrinsicBounds(ids[0], ids[1],
					ids[2], ids[3]);
			mLeftBtn.setBackgroundDrawable(null);
		}
	}

	protected void setLeftSize(float size) {
		mLeftBtn.setTextSize(size);
	}

	protected void setRightSize(float size) {
		mRightBtn.setTextSize(size);
	}

	public void setLeftBg(int id) {
		mLeftBtn.setBackgroundResource(id);
	}

	protected void setBottomBarVisibility(int id) {
		mBottomBar.setVisibility(id);
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
			// TODO: handle exception
		}
	}

	private void initImageOptions() {
		if (null == mImageLoader) {
			mImageLoader = ImageLoader.getInstance();
		}

		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.pic_boy)
				.showImageForEmptyUri(R.drawable.pic_boy)
				.showImageOnFail(R.drawable.pic_boy).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void setString(TextView tv, String str) {
		if (!StringUtils.isEmpty(str)) {
			tv.setText(str);
		}
	}

	public void setLeftBtnClickable(boolean flag) {
		mLeftBtn.setClickable(flag);
	}
}
