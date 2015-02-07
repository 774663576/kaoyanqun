/**
 * 工程名: KyGroup
 * 文件名: SettingView.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-11-24上午9:10:34
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Announce;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.widget.RoundAngleImageView;

/**
 * 类名: SettingView <br/>
 * 功能: TODO 设置页面. <br/>
 * 日期: 2013-11-24 上午9:10:34 <br/>
 * 
 * @author lx
 * @version
 */
public class SettingView implements OnClickListener, Callback {
	private static final String TAG = "SettingView";

	private HomeActivity mContext;

	private View mView;
	private TextView mNickName;
	private TextView mHometownName;
	private TextView mSchollName;
	private RoundAngleImageView mPic;
	private User mUser;
	private Bitmap mPicBitmap;

	public SettingView(HomeActivity context) {
		this.mContext = context;
		mView = LayoutInflater.from(mContext).inflate(
				R.layout.setting_activity, null);
		initView();
	}

	public View getView() {
		initData();
		mContext.setTitleBarVisibility(View.GONE);
		return mView;
	}

	private void initView() {
		/*
		 * 需要从服务端获取绑定状态 并相应显示
		 */
		mUser = KygroupApplication.getApplication().mUser;
		((TextView) mView.findViewById(R.id.edit)).setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.modify_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.quit_app_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.help_app_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.info_response_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.blacklist_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.post_modify_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.school_modify_rl))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.check_update))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.my_post))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.annoucement))
				.setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.share_rl))
				.setOnClickListener(this);
		mNickName = (TextView) mView.findViewById(R.id.nick_name);
		mHometownName = (TextView) mView.findViewById(R.id.home_modify);
		mSchollName = (TextView) mView.findViewById(R.id.school_modify);
		mPic = (RoundAngleImageView) mView.findViewById(R.id.user_pic);
		initAuth();
		// mGender = (ImageView)mView.findViewById(R.id.user_gender);
	}

	private void initData() {
		if (null == mUser) {
			return;
		}
		mNickName.setText(mUser.getNickName());
		if (mUser.getGender().equals("M")) {
			mNickName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.p_male, 0);
		} else {
			mNickName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.p_female, 0);
		}
		updateMessage();
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.modify_rl: {
			Intent intent = new Intent(mContext, PasswordActivity.class);
			mContext.startActivity(intent);
			break;
		}
		case R.id.quit_app_rl:
			new AlertDialog.Builder(mContext)
					.setTitle(R.string.login_out)
					.setMessage(R.string.tips_login_out)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									KygroupApplication.mUser = new User();
									removePersonMsg();
									mContext.finish();
									mContext.stopService();
									Intent intent = new Intent(mContext,
											LoginActivity.class);
									mContext.startActivity(intent);

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
			break;
		case R.id.school_modify_rl:
		case R.id.edit: {
			Intent intent = new Intent(mContext, EditActivity.class);
			mContext.startActivityForResult(intent, 1);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		}
			break;
		case R.id.help_app_rl: {
			Intent intent = new Intent(mContext, HelpActivity.class);
			mContext.startActivity(intent);
		}
			break;
		case R.id.info_response_rl: {
			Intent intent = new Intent(mContext, FeedBackActivity.class);
			mContext.startActivity(intent);
		}
			break;
		case R.id.blacklist_rl: {
			Intent intent = new Intent(mContext, BlacklistActivity.class);
			mContext.startActivity(intent);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		}
		case R.id.post_modify_rl: {
			Intent intent = new Intent(mContext, AuthActivity.class);
			mContext.startActivityForResult(intent, 0);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		}
		case R.id.check_update: {
			Intent intent = new Intent(mContext, CheckUpgradeActivity.class);
			mContext.startActivityForResult(intent, 0);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		}
		case R.id.my_post: {
			Intent intent = new Intent(mContext, MyPostActivity.class);
			mContext.startActivity(intent);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		}
		case R.id.annoucement: {
			Announce announce = KygroupApplication.mAnnounce;
			if (announce != null && announce.getAnnounces().size() > 0) {
				Intent intent = new Intent(mContext, AnnounceActivity.class);
				intent.putExtra("announce", announce);
				mContext.startActivity(intent);
				mContext.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				ToastUtils.showShortToast(R.string.no_announce);
			}
			break;
		}
		case R.id.share_rl: {
			ToastUtils.showShortToast(R.string.no_announce);
			showShare(true, null);
			break;
		}
		default:
			break;
		}

	}

	/**
	 * 
	 * removePersonMsg:(清空个人数据). <br/>
	 * 
	 */
	private void removePersonMsg() {
		SharedPreferences prefs = mContext.getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, mContext.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

	public void updateMessage() {
		mNickName.setText(mUser.getNickName());
		if (mUser.getProvince().equals(mUser.getCity())) {
			mHometownName.setText(mUser.getProvince());
		} else {
			mHometownName.setText(mUser.getProvince() + " " + mUser.getCity());
		}
		initSchoolMsg();
		if (!StringUtils.isEmpty(mUser.getPic())) {
			if (mUser.getPic().contains("http")) {
				mContext.mImageLoader.displayImage(mUser.getPic(), mPic,
						mContext.mOptions);
			} else {
				if (null != mPicBitmap) {
					mPicBitmap.recycle();
					mPicBitmap = null;
				}
				if (null == mPicBitmap) {
					mPicBitmap = FileUtils.getLoacalBitmap(mUser.getPic());
				}
				mPic.setImageBitmap(mPicBitmap);
			}
		}
	}

	private void initSchoolMsg() {
		if (mUser.getRole() == 1) {
			mSchollName.setText(mUser.getRSchool());
			mSchollName.setTextColor(Color.BLACK);
		} else if (!StringUtils.isEmpty(mUser.getESchool())) {
			mSchollName.setText(mUser.getESchool());
			mSchollName.setTextColor(Color.BLACK);
		}
	}

	public void initAuth() {
		TextView tv = (TextView) mView.findViewById(R.id.post_modify);
		int role = mUser.getRole();
		int confirm = mUser.getConfirm();
		if (confirm == 2) {
			tv.setText(R.string.auth_post3);
			((RelativeLayout) mView.findViewById(R.id.post_modify_rl))
					.setClickable(false);
			((TextView) mView.findViewById(R.id.right_arrow))
					.setVisibility(View.GONE);
		} else {
			if (confirm == 1) {
				tv.setText(R.string.authing);
				((RelativeLayout) mView.findViewById(R.id.post_modify_rl))
						.setClickable(false);
				tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				((TextView) mView.findViewById(R.id.right_arrow))
						.setVisibility(View.GONE);
			} else if (role == 0) {
				tv.setText(R.string.auth_post1);
			} else if (role == 1) {
				tv.setText(R.string.auth_post2);
			}
		}
	}

	public void showShare(boolean silent, String platform) {
		// final OnekeyShare oks = new OnekeyShare();
		// oks.setNotification(R.drawable.ic_launcher,
		// mContext.getString(R.string.app_name));
		// //oks.setAddress("12345678901");
		// oks.setTitle(mContext.getString(R.string.evenote_title));
		// //oks.setTitleUrl("http://sharesdk.cn");
		// oks.setText(mContext.getString(R.string.share_content));
		// //oks.setImagePath(MainActivity.TEST_IMAGE);
		// //oks.setImageUrl(MainActivity.TEST_IMAGE_URL);
		// oks.setUrl("http://www.sharesdk.cn");
		// //oks.setFilePath(MainActivity.TEST_IMAGE);
		// //oks.setComment(getContext().getString(R.string.share));
		// oks.setSite(mContext.getString(R.string.app_name));
		// //oks.setSiteUrl("http://sharesdk.cn");
		// //oks.setVenueName("ShareSDK");
		// //oks.setVenueDescription("This is a beautiful place!");
		// KyLocation location =
		// KygroupApplication.getApplication().getLocation();
		// oks.setLatitude((float)location.getLatitude());
		// oks.setLongitude((float)location.getLatitude());
		// oks.setSilent(silent);
		// if (platform != null) {
		// oks.setPlatform(platform);
		// }
		//
		// // 取消注释，可以实现对具体的View进行截屏分享
		// // oks.setViewToShare(getPage());
		//
		// // 去除注释，可令编辑页面显示为Dialog模式
		// // oks.setDialogMode();
		//
		// // 去除注释，在自动授权时可以禁用SSO方式
		// // oks.disableSSOWhenAuthorize();
		//
		// // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		// // oks.setCallback(new OneKeyShareCallback());
		// // oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());
		//
		// // 去除注释，演示在九宫格设置自定义的图标
		// // Bitmap logo = BitmapFactory.decodeResource(menu.getResources(),
		// R.drawable.ic_launcher);
		// // String label = menu.getResources().getString(R.string.app_name);
		// // OnClickListener listener = new OnClickListener() {
		// // public void onClick(View v) {
		// // String text = "Customer Logo -- ShareSDK " +
		// ShareSDK.getSDKVersionName();
		// // Toast.makeText(menu.getContext(), text,
		// Toast.LENGTH_SHORT).show();
		// // oks.finish();
		// // }
		// // };
		// // oks.setCustomerLogo(logo, label, listener);
		//
		// // 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// // oks.addHiddenPlatform(SinaWeibo.NAME);
		// // oks.addHiddenPlatform(TencentWeibo.NAME);
		//
		// oks.show(mContext);
	}
}
