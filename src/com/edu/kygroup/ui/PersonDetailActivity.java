package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.fragment.PersonDynamicFragment;
import com.edu.kygroup.fragment.PersonInfoFragment;
import com.edu.kygroup.fragment.PersonKaoYanFragment;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.Constant;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.CircularImage;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

public class PersonDetailActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnClickListener, IBindData {
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private PersonInfoFragment infoFragment;
	private PersonDynamicFragment dyFragment;
	private PersonKaoYanFragment kyFragment;
	private int currentTabIndex = -1;
	private User user;
	private Dialog mDialog = null;
	// private ImageView img_renzheng;

	public ImageLoader mImageLoader;
	public DisplayImageOptions mOptions;

	private CircularImage img_avatar;
	private TextView txt_name;
	// private TextView txt_renzheng;

	private RelativeLayout mMsgLayout;
	private RelativeLayout mFocusLayout;
	private RelativeLayout mForbidLayout;
	private TextView txt_focus_tv;
	private TextView txt_focus_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person_detail);
		user = (User) getIntent().getSerializableExtra("user");
		mCurRelation = user.getRelation();
		initImageOptions();
		initView();
		setLisetener();
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

	public User getUser() {
		return user;
	}

	private void initView() {
//		img_renzheng = (ImageView) findViewById(R.id.img_renzheng);

		txt_focus_id = (TextView) findViewById(R.id.focus_id);
		txt_focus_tv = (TextView) findViewById(R.id.focus_tv);
		mForbidLayout = (RelativeLayout) findViewById(R.id.forbid_layout);
		mFocusLayout = (RelativeLayout) findViewById(R.id.focus_layout);
		mMsgLayout = (RelativeLayout) findViewById(R.id.msg_layout);
		infoFragment = new PersonInfoFragment();
		dyFragment = new PersonDynamicFragment();
		kyFragment = new PersonKaoYanFragment();
		fragmentList.add(infoFragment);
		fragmentList.add(dyFragment);
		fragmentList.add(kyFragment);
		// RadioGroup rg = (RadioGroup) this.findViewById(R.id.tab);
		// rg.setOnCheckedChangeListener(this);
		showTab(0);
		img_avatar = (CircularImage) findViewById(R.id.img_avatar);
		mImageLoader.displayImage(user.getPic(), img_avatar, mOptions);
		System.out.println("user::::::::::::::" + user.getPic());
		txt_name = (TextView) findViewById(R.id.txt_name);
//		txt_renzheng = (TextView) findViewById(R.id.txt_renzheng);
		txt_name.setText(user.getNickName());
		Drawable drawable;
		String year = "";
		if (user.getRole() == 0) {
			year = user.getEYear();
		} else {
			year = user.getRYear();
		}
		String postMsg = user.getRSchool() + " | " + user.getRCollege() + " | "
				+ user.getRMajor() + " 专业 " + year + "级研究生";
		// if (user.getRole() == 2) {
		// txt_renzheng.setText(postMsg);
		// img_renzheng.setImageResource(R.drawable.certification);
		//
		// } else if (user.getRole() == 1) {
		// img_renzheng.setImageResource(R.drawable.un_certifiaction);
		// txt_renzheng.setText(postMsg);
		//
		// } else {
		// img_renzheng.setImageResource(R.drawable.un_certifiaction);
		// txt_renzheng.setText(postMsg);
		// img_renzheng.setVisibility(View.INVISIBLE);
		// txt_renzheng.setVisibility(View.INVISIBLE);
		// }

		if (mCurRelation == 0) {
			txt_focus_tv.setText(R.string.add);
			txt_focus_id
					.setBackgroundResource(R.drawable.ic_profilebar_follow_normal);
		} else {
			txt_focus_tv.setText(R.string.remove);
			txt_focus_id
					.setBackgroundResource(R.drawable.ic_profilebar_unfollow_normal);
		}
	}

	private void setLisetener() {
		mForbidLayout.setOnClickListener(this);
		mFocusLayout.setOnClickListener(this);
		mMsgLayout.setOnClickListener(this);
	}

	public void showTab(int tabIndex) {
		// RadioGroup group = (RadioGroup) this.findViewById(R.id.tab);
		// if (tabIndex < 0 || tabIndex >= group.getChildCount())
		// return;
		if (currentTabIndex == tabIndex)
			return;
		if (currentTabIndex >= 0) {
			fragmentList.get(currentTabIndex).onPause();
		}
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		// for (int i = 0; i < group.getChildCount(); i++) {
		Fragment fg = fragmentList.get(0);
		// RadioButton tabItem = (RadioButton) group.getChildAt(i);
		// if (i == tabIndex) {
		// if (fg.isAdded()) {
		// fg.onResume();
		// } else {
		ft.add(R.id.realtabcontent, fg);
		// }
		ft.show(fg);
		// tabItem.setTextColor(getResources().getColor(R.color.tab_select_color));
		// } else {
		// ft.hide(fg);
		// tabItem.setTextColor(getResources().getColor(R.color.tab_color));
		// }
		// }
		ft.commit();
		// currentTabIndex = tabIndex;
		// RadioButton rb = (RadioButton) group.getChildAt(tabIndex);
		// if (!rb.isChecked())
		// rb.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
			if (group.getChildAt(i).getId() == checkedId) {
				showTab(i);
				break;
			}
		}
	}

	/**
	 * 显示弹框加黑还是举报
	 */
	public void showDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.add_to_black,
				null);
		view.findViewById(R.id.addtoblack).setOnClickListener(this);
		view.findViewById(R.id.report).setOnClickListener(this);
		mDialog = new Dialog(this, R.style.no_title_dialog);
		mDialog.setContentView(view);
		mDialog.setCancelable(true);
		mDialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addtoblack:
			if (null != mDialog && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			break;
		case R.id.report:
			if (null != mDialog && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			break;
		case R.id.forbid_layout:
			showDialog();
			break;
		case R.id.msg_layout:
			startChatActivity();
			break;
		case R.id.focus_layout:
			if (mCurRelation == 0) {
				getPostAddFriends();
			} else {
				getPostRemoveFriends();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 发送请求添加好友
	 */
	private void getPostAddFriends() {
		startWaitingDialog();
		String url = UrlUtils.ADD_FRIENDs_URL + "user.email="
				+ KygroupApplication.mUser.getEmail() + "&" + "friend.email="
				+ user.getEmail();
		startWaitingDialog();
		new NetworkTask().execute(this, TagUtils.REPORT_ADD_FRIENDS, url);
	}

	/**
	 * 发送请求删除好友
	 */
	private void getPostRemoveFriends() {
		startWaitingDialog();
		String url = UrlUtils.REMOVE_FRIENDS_URL + "user.email="
				+ KygroupApplication.mUser.getEmail() + "&" + "friend.email="
				+ user.getEmail();
		startWaitingDialog();
		new NetworkTask().execute(this, TagUtils.REPORT_REMOVE_FRIENDS, url);
	}

	private void startChatActivity() {
		Intent intent = new Intent(PersonDetailActivity.this,
				ChatActivity1.class);
		intent.putExtra("user_name", user.getNickName());
		intent.putExtra("toChatUsername", user.getEmail());
		startActivity(intent);
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

	private int mCurRelation = -1;

	private void dealFriendsRelation(int tip, int str, int pic, int relation) {
		ToastUtils.showShortToast(this, tip);
		((TextView) findViewById(R.id.focus_tv)).setText(str);
		((TextView) findViewById(R.id.focus_id)).setBackgroundResource(pic);
		mCurRelation = relation;
	}

	@Override
	public Object bindData(int tag, Object obj) {
		switch (tag) {
		case TagUtils.REPORT_ADD_FRIENDS:
			closeWaitingDialog();
			if ((Boolean) obj) {
				dealFriendsRelation(R.string.add_friend_success,
						R.string.remove,
						R.drawable.ic_profilebar_unfollow_normal, 1);
			} else {
				dealFriendsRelation(R.string.add_friend_failure, R.string.add,
						R.drawable.ic_profilebar_follow_normal, 0);
			}
			sendBroadcast(new Intent(Constant.REFUSH_FRRIENDS));
			break;
		case TagUtils.REPORT_REMOVE_FRIENDS:
			closeWaitingDialog();
			if ((Boolean) obj) {
				dealFriendsRelation(R.string.remove_friend_success,
						R.string.add, R.drawable.ic_profilebar_follow_normal, 0);
			} else {
				dealFriendsRelation(R.string.remove_friend_failure,
						R.string.remove,
						R.drawable.ic_profilebar_unfollow_normal, 1);
			}
			break;
		case TagUtils.ADDTO_BLACKLIST_TAG:

			break;
		case TagUtils.DETAILS_TAG:

			break;
		default:
			break;
		}
		return null;
	}
}
