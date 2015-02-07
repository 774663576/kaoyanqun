package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.funshion.video.mobile.imageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class UserActivity extends BaseActivity implements IBindData,
		OnClickListener {
	private RelativeLayout mMsgLayout;
	private RelativeLayout mFocusLayout;
	private RelativeLayout mForbidLayout;
	private ImageView mPic;
	private TextView mNickName;
	private TextView mHometownView;
	private TextView mEUniversityView;// 母校;
	private TextView mRegUniversityView;// 报考学校
	private TextView mEnYearView;// 入学年份
	private TextView mRegYearView;// 报考年份
	private TextView mAnnounceText;
	private TextView mAnnounceTip;
	private TextView mAuthView;
	private TextView mAuthTipView;
	private Dialog mDialog;
	private User mFriendUser;

	private Boolean isMaster = true; // 是否研究生

	/** 研究生本科生区别显示部分 **/
	private RelativeLayout bachelor_rl; // 本科生备考布局
	private RelativeLayout master_rl; // 研究生成绩布局
	private TextView baokao_tip_tv; // 本科生报考信息&研究生院校信息
	private TextView beikao_tip_tv; // 备考&研究生成绩

	/** 研究生考试信息 **/
	private TextView mScroeView;
 	private TextView mLocationView;
	private Intent mRetIntent;// 携带返回值
	private int mCurRelation = -1;
	private DetailsInfo mDetailsInfo;
	private ListView mFocusUniListView;
	private TextView mViewFocusUniView;
	private ArrayList<String> mFocusUnilist;
	private ArrayList<String> mFocusUniIdList;
	private ArrayAdapter<String> foucsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mImageLoader = ImageLoader.getInstance();
		initView();
		initData();
		addListener();
		getFoucsList();
	}

	@Override
	protected View setContentView() {
		return mInflater.inflate(R.layout.user_activity, null);
	}

	private void getIntentData() {
		mFriendUser = (User) getIntent().getSerializableExtra("user");
	}

	private void getRole() {
		if (mFriendUser.getRole() == 0) {
			isMaster = false;
		} else {
			isMaster = true;
		}
	}

	private void initData() {
		getIntentData();
		if (null == mFriendUser)
			return;
		mRetIntent = new Intent();
		setTitleText(mFriendUser.getNickName());
		mImageLoader.displayImage(mFriendUser.getPic(), mPic, mOptions);
		mNickName.setText(mFriendUser.getNickName());
		mCurRelation = mFriendUser.getRelation();
		String postMsg = mFriendUser.getRSchool() + ">"
				+ mFriendUser.getRCollege() + ">" + mFriendUser.getRMajor();
		getRole();
		if (isMaster) { // 研究生
			baokao_tip_tv.setText(getResources().getString(
					R.string.master_school_tip));
			mRegUniversityView.setText(postMsg); // 研究生院校
			beikao_tip_tv
					.setText(getResources().getString(R.string.exam_point));
			bachelor_rl.setVisibility(View.GONE);
			master_rl.setVisibility(View.VISIBLE);
			mAnnounceTip.setText(R.string.sign);
		} else {
			baokao_tip_tv.setText(getResources().getString(R.string.reg_msg)); // 报考信息
			mRegUniversityView.setText(postMsg); // 报考院校信息
			beikao_tip_tv.setText(getResources().getString(R.string.remark));
			bachelor_rl.setVisibility(View.VISIBLE);
			master_rl.setVisibility(View.GONE);
		}
		mHometownView.setText(hometownString());
		StringBuffer emsg = new StringBuffer();
		if (StringUtils.isEmpty(mFriendUser.getESchool())) {
			try {
				String str[] = getResources().getStringArray(R.array.unis);
				int pos = (int) (Math.random() * 10) % 3;
				emsg.append(str[pos]);
			} catch (Exception e) {
				emsg.append(getString(R.string.local_uni));
			}
		} else {
			emsg.append(mFriendUser.getESchool());
			if (!StringUtils.isEmpty(mFriendUser.getECollege())) {
				appendString(emsg, ">", mFriendUser.getECollege());
			}
			if (!StringUtils.isEmpty(mFriendUser.getEMajor())) {
				appendString(emsg, ">", mFriendUser.getEMajor());
			}
		}
		mEUniversityView.setText(emsg);
		if (!StringUtils.isEmpty(mFriendUser.getEYear())) {
			mEnYearView.setText(mFriendUser.getEYear()
					+ getString(R.string.grade_tip));
		}
		if (!StringUtils.isEmpty(mFriendUser.getRYear())) {
			if (mFriendUser.getRole() == 0) {
				mRegYearView.setText(mFriendUser.getRYear()
						+ getString(R.string.year_tip));
			} else {
				mRegYearView.setText(mFriendUser.getRYear()
						+ getString(R.string.grade_tip));
			}
		}
		if (!StringUtils.isEmpty(mFriendUser.getAnnounce())) {
			mAnnounceText.setText(mFriendUser.getAnnounce());
		}
		if (mFriendUser.getRelation() == 1) { // haoyou
			((TextView) findViewById(R.id.focus_tv)).setText(R.string.remove);
			((TextView) findViewById(R.id.focus_id))
					.setBackgroundResource(R.drawable.ic_profilebar_unfollow_normal);
		} else { // 陌生人
			((TextView) findViewById(R.id.focus_tv)).setText(R.string.add);
			((TextView) findViewById(R.id.focus_id))
					.setBackgroundResource(R.drawable.ic_profilebar_follow_normal);
		}
		setLocation();
		setScore();
		initAuth(postMsg + mFriendUser.getRYear());

		/**************************************************/
		mFocusUnilist = new ArrayList<String>();
		mFocusUniIdList = new ArrayList<String>();
		initListView();
	}

	/**
	 * 获取关注院校列表
	 */
	private void getFoucsList() {
		if (DeviceUtils.isNetAvailable(this)) {
			String url = UrlUtils.GET_CONCERN + "user.email="
					+ mFriendUser.getEmail();
			NetworkTask task = new NetworkTask();
			task.setmFinish(new GetFinish() {

				@Override
				public void finish(Object result) {
					if (null != result) {
						ArrayList<FocusInfo> infos = (ArrayList<FocusInfo>) result;
						if (null != infos && infos.size() > 0) {

							for (FocusInfo fi : infos) {
								mFocusUnilist.add(fi.getmFocusSchool() + ">"
										+ fi.getmFocusColleage() + ">"
										+ fi.getmFocusMajor());
							}
							foucsAdapter.notifyDataSetChanged();
						}
					}
				}
			});
			task.execute(null, TagUtils.GET_CONCERN, url);
		}
	}

	private void appendString(StringBuffer sb, String... strs) {
		if (null != sb && null != strs) {
			for (String str : strs) {
				sb.append(str);
			}
		}
	}

	private String hometownString() {
		if (StringUtils.isEmpty(mFriendUser.getProvince())) {
			return "";
		} else if (mFriendUser.getProvince().equals(mFriendUser.getCity())) {
			return mFriendUser.getProvince();
		} else {
			return (mFriendUser.getProvince() + " " + mFriendUser.getCity());
		}
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		setBottomBarVisibility(View.GONE);
		mPic = (ImageView) findViewById(R.id.user_pic);
		mNickName = (TextView) findViewById(R.id.nick_name);
		mForbidLayout = (RelativeLayout) findViewById(R.id.forbid_layout);
		mFocusLayout = (RelativeLayout) findViewById(R.id.focus_layout);
		mMsgLayout = (RelativeLayout) findViewById(R.id.msg_layout);
		mHometownView = (TextView) findViewById(R.id.hometown_text);
		mEUniversityView = (TextView) findViewById(R.id.uni_text);
		mRegUniversityView = (TextView) findViewById(R.id.reg_text);
		mEnYearView = (TextView) findViewById(R.id.uni_year);
		mRegYearView = (TextView) findViewById(R.id.reg_year);
		mAnnounceText = (TextView) findViewById(R.id.announce_text);
		mLocationView = (TextView) findViewById(R.id.location);
		mAnnounceTip = (TextView) findViewById(R.id.personal_announce_tip);
		mAuthView = (TextView) findViewById(R.id.authenticate);
		mAuthTipView = (TextView) findViewById(R.id.authenticate_tip);
		mFocusUniListView = (ListView) findViewById(R.id.focus_uni_list);
		mViewFocusUniView = (TextView) findViewById(R.id.focus_uni_text);

		/** 研究生本科生区别显示部分 **/
		bachelor_rl = (RelativeLayout) findViewById(R.id.remark_rl); // 本科生备考布局
		master_rl = (RelativeLayout) findViewById(R.id.master_rl); // 研究生成绩布局
		baokao_tip_tv = (TextView) findViewById(R.id.reg_msg); // 本科生报考信息&研究生院校信息
		beikao_tip_tv = (TextView) findViewById(R.id.remark_msg); // 备考&研究生成绩
		mScroeView = (TextView) findViewById(R.id.score);
	}

	private void initListView() {
		foucsAdapter = new ArrayAdapter<String>(this, R.layout.focus_uni_list,
				R.id.focus_text, mFocusUnilist);
		mFocusUniListView.setAdapter(foucsAdapter);
	}

	private void addListener() {
		mForbidLayout.setOnClickListener(this);
		mFocusLayout.setOnClickListener(this);
		mMsgLayout.setOnClickListener(this);
		mRegUniversityView.setOnClickListener(this);
		mViewFocusUniView.setOnClickListener(this);
	}

	private void setLocation() {
		String loca = "";
		if (!StringUtils.isEmpty(mFriendUser.getDistance())) {
			loca = mFriendUser.getDistance();
		}
		if (!StringUtils.isEmpty(mFriendUser.getAddress())) {
			loca = loca + "|" + mFriendUser.getAddress();
		}
		if (StringUtils.isEmpty(loca)) {
			mLocationView.setVisibility(View.GONE);
		} else {
			mLocationView.setText(loca);
		}
	}

	public void addToBlackList() {
		startWaitingDialog();
		String url = UrlUtils.ADDTO_BLACKLIST_URL + "user.email="
				+ mUser.getEmail() + "&friend.email=" + mFriendUser.getEmail();
		new NetworkTask().execute(this, TagUtils.ADDTO_BLACKLIST_TAG, url);
	}

	public void addReport() {
		startWaitingDialog();
		String url = UrlUtils.ADDTO_BLACKLIST_URL + "user.email="
				+ mUser.getEmail() + "&friend.email=" + mFriendUser.getEmail();
		new NetworkTask().execute(this, TagUtils.ADDTO_BLACKLIST_TAG, url);
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
				addToBlackList();
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
		case R.id.reg_text:
			if (null == mDetailsInfo) {
				startWaitingDialog();
				String url = UrlUtils.DETAILS_URL + "major.sid="
						+ mFriendUser.getRSid() + "&major.ceid="
						+ mFriendUser.getRCid() + "&major.mid="
						+ mFriendUser.getRmid();
				new NetworkTask().execute(this, TagUtils.DETAILS_TAG, url);
			} else {
				startDetailActivity();
			}
			break;
		case R.id.focus_uni_text: {
			mFocusUniListView
					.setVisibility(mFocusUniListView.isShown() ? View.GONE
							: View.VISIBLE);
			break;
		}
		default:
			break;
		}
	}

	private void startChatActivity() {
		// Intent intent = new Intent(UserActivity.this, ChatActivity.class);
		// intent.putExtra("friendEmail", mFriendUser.getEmail());
		// intent.putExtra("friendNickName", mFriendUser.getNickName());
		// startActivity(intent);
		Intent intent = new Intent(UserActivity.this, ChatActivity1.class);
		intent.putExtra("user_name", mFriendUser.getNickName());
		intent.putExtra("toChatUsername", mFriendUser.getEmail());
		startActivity(intent);
	}

	private void startDetailActivity() {
		Intent intent = new Intent(this, MajorDetailsActivity.class);
		intent.putExtra("details", mDetailsInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 发送请求添加好友
	 */
	private void getPostAddFriends() {
		startWaitingDialog();
		String url = UrlUtils.ADD_FRIENDs_URL + "user.email="
				+ mUser.getEmail() + "&" + "friend.email="
				+ mFriendUser.getEmail();
		startWaitingDialog();
		new NetworkTask().execute(UserActivity.this,
				TagUtils.REPORT_ADD_FRIENDS, url);
	}

	/**
	 * 发送请求删除好友
	 */
	private void getPostRemoveFriends() {
		startWaitingDialog();
		String url = UrlUtils.REMOVE_FRIENDS_URL + "user.email="
				+ mUser.getEmail() + "&" + "friend.email="
				+ mFriendUser.getEmail();
		startWaitingDialog();
		new NetworkTask().execute(this, TagUtils.REPORT_REMOVE_FRIENDS, url);
	}

	private void dealFriendsRelation(int tip, int str, int pic, int relation) {
		ToastUtils.showShortToast(this, tip);
		((TextView) findViewById(R.id.focus_tv)).setText(str);
		((TextView) findViewById(R.id.focus_id)).setBackgroundResource(pic);
		mCurRelation = relation;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCurRelation != -1 && mCurRelation != mFriendUser.getRelation()) {
			mFriendUser.setRelation(mCurRelation);
			notifyRelationChanged();
		}
		MobclickAgent.onPageEnd("UserActivity");
		MobclickAgent.onPause(this);
	}

	private void notifyRelationChanged() {
		Intent intent = new Intent();
		intent.putExtra("ruser", mFriendUser);
		intent.setAction("com.kygroup.changed");
		sendBroadcast(intent);
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
			closeWaitingDialog();
			if ((Boolean) obj) {
				mRetIntent.putExtra("black", mFriendUser);
				setResult(1, mRetIntent);
				mFriendUser.setRelation(0);
				notifyRelationChanged();
				finish();
				ToastUtils.showShortToast(R.string.addtoblack_success);
			} else {
				ToastUtils.showShortToast(R.string.addtoblack_failed);
			}
			break;
		case TagUtils.DETAILS_TAG:
			if (null != obj) {
				mDetailsInfo = (DetailsInfo) obj;
				startDetailActivity();
			} else {
				ToastUtils.showShortToast(R.string.request_failed);
			}
			closeWaitingDialog();
			break;
		default:
			break;
		}
		return null;
	}

	private void setScore() {
		String score = mFriendUser.getScore();
		if (!StringUtils.isEmpty(score)) {
			String[] scores = score.split(",");
			String scoresStr = "";
			if (null != scores) {
				for (String sc : scores) {
					if (!StringUtils.isEmpty(sc)) {
						scoresStr += sc + " ";
					}
				}
				mScroeView.setText(scoresStr);
			}
		}
	}

	private void initAuth(String msg) {
		switch (mFriendUser.getRole()) {
		case 0:
			setVisiablity(View.GONE, "", 0);
			break;
		case 1:
			setVisiablity(View.VISIBLE, msg + getString(R.string.post_suffix),
					R.drawable.un_certifiaction);
			break;
		case 2:
			setVisiablity(View.VISIBLE, msg + getString(R.string.post_suffix),
					R.drawable.certification);
			break;
		}
	}

	private void setVisiablity(int flag, String text, int id) {
		mAuthView.setVisibility(flag);
		mAuthTipView.setVisibility(flag);
		mAuthTipView.setBackgroundResource(id);
		mAuthView.setText(text);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("UserActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

}
