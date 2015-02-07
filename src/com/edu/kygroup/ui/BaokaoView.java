package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.FocusAdapter;
import com.edu.kygroup.db.DBUtils;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.EditParse;
import com.edu.kygroup.net.MessageParse;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.CircularImage;

public class BaokaoView implements IBindData, OnClickListener,
		OnLongClickListener, OnItemClickListener {
	private static final int SIZE = 10;
	public boolean mIsDelete = false;
	private ArrayList<User> mPostUser;
	private ArrayList<FocusInfo> mFocusInfos = new ArrayList<FocusInfo>();
	private ArrayList<View> mViews;

	private HomeActivity mContext;
	private User mUser;
	private CreateFocusBroadcastReceiver mReceiver;
	private FocusInfo mInfo = new FocusInfo();// 添加关注的信息
	// private FocusUniDao mDao;
	private FocusInfo mAimInfo;// 关注更改为目标院校
	private Dialog mFocusDialog;
	private IChangeAim mChangeAim;
	private View mView;
	private TextView mLoadView;
	// private TextView mPostFriendsView;
	private TextView mFriendsNumView;
	private TextView mBaokaoView;
	private LinearLayout mFocusLayout;
	private LinearLayout mScrollLayout;
	private LinearLayout add_layout;
	private ScrollView mScrollView;
	private TextView major_detail;
	private TextView txt_bbs;
	private TextView txt_chat;
	private RelativeLayout friend_layout;
	private HorizontalScrollView scrollView;

	private int mBottomHeight;
	private int mScreenHeight;
	private FocusInfo mDelInfo;

	private ListView fourceListView;

	private MyAdapter adapter;

	private FocusInfo focusInfo;

	public BaokaoView(HomeActivity context) {
		mContext = context;
		mView = LayoutInflater.from(mContext).inflate(R.layout.baokao_view,
				null);
		initView();
		initData();
	}

	public View getView() {
		mContext.setTitleBarVisibility(View.VISIBLE);
		setRightBtn();
		return mView;
	}

	private void initView() {
		scrollView = (HorizontalScrollView) mView
				.findViewById(R.id.horizontal_scrollview);
		scrollView.setOnClickListener(this);
		friend_layout = (RelativeLayout) mView
				.findViewById(R.id.friends_layout);
		friend_layout.setOnClickListener(this);
		txt_bbs = (TextView) mView.findViewById(R.id.txt_bbs);
		txt_chat = (TextView) mView.findViewById(R.id.txt_chat);
		txt_bbs.setOnClickListener(this);
		txt_chat.setOnClickListener(this);
		major_detail = (TextView) mView.findViewById(R.id.major_detail);
		major_detail.setOnClickListener(this);
		add_layout = (LinearLayout) mView.findViewById(R.id.add_layout);
		add_layout.setOnLongClickListener(this);
		fourceListView = (ListView) mView.findViewById(R.id.listView1);
		adapter = new MyAdapter();
		fourceListView.setAdapter(adapter);
		mUser = KygroupApplication.getApplication().mUser;
		mScrollView = (ScrollView) mView.findViewById(R.id.scroll_view);
		mBaokaoView = (TextView) mView.findViewById(R.id.baokao_msg);
		mLoadView = (TextView) mView.findViewById(R.id.postfriend_load);
		mFocusLayout = (LinearLayout) mView.findViewById(R.id.focus_layout);
		mFriendsNumView = (TextView) mView.findViewById(R.id.friends);
		mScrollLayout = (LinearLayout) mView.findViewById(R.id.scroll_layout);

		mLoadView.setOnClickListener(this);
		// mPostFriendsView.setOnClickListener(this);
		mScrollLayout.setOnClickListener(this);
		if (mUser.getRole() == 1) {
			((TextView) mView.findViewById(R.id.per_tip))
					.setText(R.string.graduate_university);
		}
		fourceListView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				ToastUtils.showShortToast("setOnLongClickListener");
				return false;
			}
		});
		// fourceListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// mContext.startWaitingDialog();
		// FocusInfo info = mFocusInfos.get(position);
		// String url = UrlUtils.DETAILS_URL + "major.sid="
		// + info.getmSid() + "&major.ceid=" + info.getmCid()
		// + "&major.mid=" + info.getmMid();
		// new NetworkTask().execute(this, TagUtils.DETAILS_TAG, url);
		// }
		// });
	}

	private void setRightBtn() {
		// mContext.setRightBtnVisibility(View.VISIBLE);
		mContext.setRightBtnBg(R.drawable.add_focus_tip);
		mContext.setRightBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// showAddIcon();
				mScrollView.smoothScrollTo(0, mScrollView.getBottom());
			}
		});
	}

	private void initData() {
		// mDao = FocusUniDao.getInstance(mContext);
		mPostUser = new ArrayList<User>();
		mViews = new ArrayList<View>();
		mReceiver = new CreateFocusBroadcastReceiver();
		mScreenHeight = DeviceUtils.getDisplayHeight(mContext);
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.kygroup.addfocus");
		mContext.registerReceiver(mReceiver, filter);
		initBaokaoView();
		getConcern();
	}

	private void initBaokaoView() {
		String msg = mUser.getRYear() + " " + mUser.getRSchool() + " | "
				+ mUser.getRCollege() + " | " + mUser.getRMajor();
		mBaokaoView.setText(msg);
		getPostGraduates();
	}

	private void getConcern() {
		if (DeviceUtils.isNetAvailable(mContext)) {
			String url = UrlUtils.GET_CONCERN + "user.email="
					+ mUser.getEmail();
			new NetworkTask().execute(this, TagUtils.GET_CONCERN, url);
		}
	}

	private void addConcern(FocusInfo info) {
		System.out.println("focus::::::::::::::::::::========");
		mContext.startWaitingDialog();
		String url = UrlUtils.ADD_CONCERN + "user.email=" + mUser.getEmail()
				+ "&major.sid=" + info.getmSid() + "&major.ceid="
				+ info.getmCid() + "&major.mid=" + info.getmMid()
				+ "&major.year=" + info.getmFocusYear() + "&major.timestamp="
				+ info.getmTime();
		new NetworkTask().execute(this, TagUtils.ADD_CONCERN, url);
	}

	private void delConcern(FocusInfo info) {
		mContext.startWaitingDialog();
		String url = UrlUtils.DEL_CONCERN + "user.email=" + mUser.getEmail()
				+ "&major.sid=" + info.getmSid() + "&major.ceid="
				+ info.getmCid() + "&major.mid=" + info.getmMid()
				+ "&major.year=" + info.getmFocusYear() + "&major.timestamp="
				+ info.getmTime();
		new NetworkTask().execute(this, TagUtils.DEL_CONCERN, url);
	}

	public void getPostGraduates() {
		User user = KygroupApplication.mUser;
		String url = UrlUtils.GET_POST_GRADUATES_URL + "user.email="
				+ user.getEmail() + "&page=" + 1 + "&rp=" + SIZE
				+ "&user.aim.sid=" + user.getRSid() + "&user.aim.ceid="
				+ user.getRCid() + "&user.aim.mid=" + user.getRmid()
				+ "&user.longitude=" + LocationUtils.getLongtitude()
				+ "&user.latitude=" + LocationUtils.getLatitude();
		if (mContext.mDialog == null) {
			mContext.startWaitingDialog();
		}
		new NetworkTask().execute(this, TagUtils.GET_POST_GRADUATE_TAG, url,
				mPostUser);
	}

	public void initHScrollView() {
		mScrollLayout.removeAllViews();
		for (int i = 0; i < mPostUser.size(); i++) {
			User user = mPostUser.get(i);
			CircularImage image = new CircularImage(mContext);
			if (user != null
					&& !StringUtils.isEmpty(user.getPic())
					&& !user.getPic().contains(
							FileUtils.SAVE_FILE_PATH_DIRECTORY)) {
				mContext.mImageLoader.displayImage(user.getPic(), image,
						mContext.mOptions);
			} else {
				if (StringUtils.isEmpty(user.getPic())
						&& !StringUtils.isEmpty(user.getGender())) {
					if (user.getGender().equals("F")) {
						image.setImageResource(R.drawable.pic_girl);
					} else {
						image.setImageResource(R.drawable.pic_boy);
					}
				} else {
					if (StringUtils.isEmpty(user.getPic())) {
						mContext.mImageLoader.displayImage(user.getPic(),
								image, mContext.mOptions);
					} else {
						Bitmap bitmap = BitmapFactory.decodeFile(user.getPic());
						if (null != bitmap) {
							image.setImageBitmap(bitmap);
						}
					}
				}
			}
			int size = mContext.getResources().getDimensionPixelSize(
					R.dimen.gallery_width);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(60, 60);
			lp.setMargins(5, 10, 5, 10);
			image.setLayoutParams(lp);
			mScrollLayout.addView(image);
		}
	}

	public void updataPersonalPic() {
		if (null != mScrollLayout && mScrollLayout.getChildCount() > 0) {
			ImageView view = (ImageView) mScrollLayout.getChildAt(0);
			Bitmap bitmap = BitmapFactory.decodeFile(mUser.getPic());
			if (null != bitmap) {
				view.setImageBitmap(bitmap);
			}
		}
	}

	private void writeFocus(final List<FocusInfo> lists) {
		new Thread() {
			public void run() {
				FocusInfo info = new FocusInfo();
				info.write(DBUtils.getDBsa(2), lists);
			}
		}.start();
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.GET_POST_GRADUATE_TAG: {
			mContext.closeWaitingDialog();
			if (null != obj) {
				mPostUser.clear();
				mPostUser.add(KygroupApplication.mUser);
				if (GraduateView.POST_TOTAL > 0) {
					String text = mContext
							.getString(R.string.search_post_graduate)
							+ " ("
							+ GraduateView.POST_TOTAL + ")";
					mFriendsNumView.setText(text);
				}
				mPostUser.addAll((ArrayList<User>) obj);
				initHScrollView();
				mLoadView.setVisibility(View.GONE);
			} else {
				mLoadView.setText(R.string.post_friends_loading_failed);
			}
			break;
		}
		case TagUtils.CHANGE_AIM_TAG: {
			Boolean bool = (Boolean) obj;
			if (bool) {
				if (null != mAimInfo) {
					changeAim();
				}
			}
			break;
		}
		case TagUtils.DETAILS_TAG: {
			if (null != obj) {
				MajorDetail detail = (MajorDetail) obj;
				goToDetailActivity(detail);
				System.out.println("maror::::::::::::" + detail.toString());

			} else {
				ToastUtils.showShortToast(R.string.request_failed);
			}
			mContext.closeWaitingDialog();
			break;
		}

		case TagUtils.GET_CONCERN: {
			if (null != obj) {
				ArrayList<FocusInfo> infos = (ArrayList<FocusInfo>) obj;
				if (null != infos && infos.size() > 0) {

					mFocusInfos.addAll(infos);
					adapter.notifyDataSetChanged();
					writeFocus(mFocusInfos);

				}
			}
			break;
		}
		case TagUtils.ADD_CONCERN: {
			if (null != obj) {
				Boolean result = (Boolean) obj;
				if (result) {
					// createFocusLayout(mInfo);
					mFocusInfos.add(mInfo);
					adapter.notifyDataSetChanged();
					mContext.closeWaitingDialog();
					writeFocus(mFocusInfos);
					return null;
				}
			}

			mContext.closeWaitingDialog();
			ToastUtils.showShortToast(R.string.add_concern_failed);
			break;
		}
		case TagUtils.DEL_CONCERN: {
			if (null != obj) {
				Boolean result = (Boolean) obj;
				if (result) {
					// System.out.println("size::::::::::" +
					// mFocusInfos.size());
					// mFocusLayout.removeAllViews();
					// mFocusInfos.remove(mDelInfo);
					// addFocus();
					// System.out
					// .println("size::::::::::===" + mFocusInfos.size());

					mFocusInfos.remove(mDelInfo);
					mIsDelete = false;
					adapter.notifyDataSetChanged();
					mContext.closeWaitingDialog();
					// loadFocusView();
					return null;
				}
			}
			mContext.closeWaitingDialog();
			ToastUtils.showShortToast(R.string.del_concern_failed);
		}
			break;
		default:
			break;
		}
		return null;
	}

	private boolean delFocus() {
		String url = UrlUtils.DEL_CONCERN + "user.email=" + mUser.getEmail()
				+ "&major.sid=" + mAimInfo.getmSid() + "&major.ceid="
				+ mAimInfo.getmCid() + "&major.mid=" + mAimInfo.getmMid()
				+ "&major.year=" + mAimInfo.getmFocusYear()
				+ "&major.timestamp=" + mAimInfo.getmTime();
		boolean res = MessageParse.getInstance().delConcern(url);
		return res;
	}

	private boolean addFocus() {
		System.out.println("focus::::::::::::::::::::");
		String url = UrlUtils.ADD_CONCERN + "user.email=" + mUser.getEmail()
				+ "&major.sid=" + mInfo.getmSid() + "&major.ceid="
				+ mInfo.getmCid() + "&major.mid=" + mInfo.getmMid()
				+ "&major.year=" + mInfo.getmFocusYear() + "&major.timestamp="
				+ mInfo.getmTime();
		boolean res = MessageParse.getInstance().addConcern(url);
		return res;
	}

	private boolean change() {
		System.out.println("user::::::::::::((((((((((((("
				+ mAimInfo.getmFocusSchool());
		String url = UrlUtils.CHANGE_AIM_URL + "user.email=" + mUser.getEmail()
				+ "&major.sid=" + mAimInfo.getmSid() + "&major.ceid="
				+ mAimInfo.getmCid() + "&major.mid=" + mAimInfo.getmMid()
				+ "&user.session=" + mUser.getRYear();
		boolean res = EditParse.getInstance().alterBaokaoAim(url);
		return res;
	}

	private void changeAim() {
		new Thread() {
			public void run() {
				boolean res = change();
				if (!res) {
					mHandler.sendEmptyMessage(4);
					mHandler.sendEmptyMessage(7);

					return;
				}
				res = delFocus();
				if (res) {
					mFocusInfos.remove(mAimInfo);
					mHandler.sendEmptyMessage(5);
				} else {
					mHandler.sendEmptyMessage(4);
					mHandler.sendEmptyMessage(7);

					return;
				}
				if (mInfo == null) {
					mInfo = new FocusInfo();
				}
				mInfo.setmCid(mUser.getRCid());
				mInfo.setmFocusColleage(mUser.getRCollege());
				mInfo.setmSid(mUser.getRSid());
				mInfo.setmFocusSchool(mUser.getRSchool());
				mInfo.setmMid(mUser.getRmid());
				mInfo.setmFocusMajor(mUser.getRMajor());
				mInfo.setmTime(System.currentTimeMillis() + "");
				mInfo.setmFocusYear(mUser.getRYear());
				System.out.println("user::::::::::::==" + mUser.getRSchool());
				res = addFocus();
				if (res) {
					System.out.println("del::::::::::::"
							+ mInfo.getmFocusSchool());
					mFocusInfos.add(mInfo);
					mHandler.sendEmptyMessage(5);
				} else {
					mHandler.sendEmptyMessage(4);
					mHandler.sendEmptyMessage(7);
					return;
				}
				System.out.println("user::::::::::::------"
						+ mAimInfo.getmFocusSchool());
				mUser.setRCid(mAimInfo.getmCid());
				mUser.setRMid(mAimInfo.getmMid());
				mUser.setRSid(mAimInfo.getmSid());
				mUser.setRCollege(mAimInfo.getmFocusColleage());
				mUser.setRSchool(mAimInfo.getmFocusSchool());
				System.out.println("user::::::::::::" + mUser.getRSchool());
				mUser.setRMajor(mAimInfo.getmFocusMajor());
				mUser.setRYear(mAimInfo.getmFocusYear());
				saveRegisterMsg();
				mHandler.sendEmptyMessage(6);
				// if (null != mChangeAim) {
				// mChangeAim.changeAim();
				// }
				mHandler.sendEmptyMessage(4);
				mAimInfo = null;
				mInfo = null;
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.horizontal_scrollview:
			getPostGraduates();
			break;
		case R.id.friends_layout:
			getPostGraduates();
			break;
		// case R.id.postfriends_more:
		// mContext.performClickFriends(1);
		// break;
		case R.id.delete_focus: {
			FocusInfo info = (FocusInfo) v.getTag();
			mDelInfo = info;
			// mDao.delete(info);
			delConcern(info);
			break;
		}
		case R.id.scroll_layout:
			mContext.performClickFriends(1);
			break;
		case R.id.major_detail: {
			mContext.startWaitingDialog();
			String url = UrlUtils.DETAILS_URL + "major.sid=" + mUser.getRSid()
					+ "&major.ceid=" + mUser.getRCid() + "&major.mid="
					+ mUser.getRmid();
			focusInfo = new FocusInfo();
			focusInfo.setmSid(mUser.getRSid());
			focusInfo.setmCid(mUser.getRCid());
			focusInfo.setmMid(mUser.getRmid());
			focusInfo.setmFocusSchool(mUser.getRSchool());
			focusInfo.setmFocusColleage(mUser.getRCollege());
			focusInfo.setmFocusMajor(mUser.getRMajor());
			new NetworkTask().execute(this, TagUtils.DETAILS_TAG, url);
			break;
		}
		case R.id.txt_bbs:
			FocusInfo info = new FocusInfo();
			info.setmSid(mUser.getRSid());
			info.setmCid(mUser.getRCid());
			info.setmMid(mUser.getRmid());
			info.setmFocusSchool(mUser.getRSchool());
			info.setmFocusColleage(mUser.getRCollege());
			info.setmFocusMajor(mUser.getRMajor());
			goToBbsActivity(info);
			break;

		case R.id.focus_msg: {
			mContext.startWaitingDialog();
			info = (FocusInfo) v.getTag();
			System.out.println("info:::::::::::" + info);
			String url = UrlUtils.DETAILS_URL + "major.sid=" + info.getmSid()
					+ "&major.ceid=" + info.getmCid() + "&major.mid="
					+ info.getmMid();
			new NetworkTask().execute(this, TagUtils.DETAILS_TAG, url);
			focusInfo = info;
			break;
		}
		// case R.id.txt_bbs: {
		// FocusInfo info = (FocusInfo) v.getTag();
		// goToBbsActivity(info);
		// break;
		// }
		case R.id.txt_chat:
			intetnChat(mUser.getChatid());
			break;
		default:
			break;
		}
	}

	private void intetnChat(String groupId) {
		Intent intent = new Intent();
		intent.putExtra("chatType", ChatActivity1.CHATTYPE_GROUP);
		intent.putExtra("groupId", KygroupApplication.getGroupId());
		System.out.println("group::::::::::::::::" + groupId);
		intent.putExtra("user_name", mUser.getRMajor());
		intent.setClass(mContext, ChatActivity1.class);
		mContext.startActivity(intent);
	}

	private void goToDetailActivity(MajorDetail details) {
		Intent intent = new Intent(mContext, MajorDetailsActivity2.class);
		intent.putExtra("details", details);
		intent.putExtra("focus_info", focusInfo);
		intent.putExtra("major", focusInfo.getmFocusMajor());

		mContext.startActivity(intent);
		mContext.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
		focusInfo = null;
	}

	private void goToBbsActivity(FocusInfo info) {
		Intent intent = new Intent(mContext, BBSActivity1.class);
		intent.putExtra("info", info);
		mContext.startActivity(intent);
		mContext.overridePendingTransition(R.anim.push_left_in,
				R.anim.push_left_out);
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_layout:
			showDialog();
		}
		return false;
	}

	private void showDialog() {
		Builder builder = new Builder(mContext);
		if (null != mViews && mViews.size() <= 10)
			builder.setPositiveButton(R.string.dia_add_foucs,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							// createFocusLayout();
							gotoSelectActivity();
						}
					});
		if (null != mFocusInfos && mFocusInfos.size() > 0) {
			if (mUser.getRole() != 1) {
				// builder.setNegativeButton(R.string.dia_change_aim,
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// // TODO Auto-generated method stub
				// showChangeAimDialog();
				// }
				// });
			}
			builder.setNeutralButton(R.string.dia_delete_focus,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							deleteFocus();
						}
					});
		}
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void deleteFocus() {
		mIsDelete = true;
		adapter.notifyDataSetChanged();
		// for (int i = 0; mViews != null && i < mViews.size(); i++) {
		// View view = mViews.get(i);
		// TextView tv = (TextView) view.findViewById(R.id.delete_focus);
		// tv.setVisibility(View.VISIBLE);
		// tv.setOnClickListener(this);
		// tv.setTag(view.getTag());
		// mIsDelete = true;
		// }
	}

	public void cleanDeleteFocus() {
		for (int i = 0; mViews != null && i < mViews.size(); i++) {
			View view = mViews.get(i);
			TextView tv = (TextView) view.findViewById(R.id.delete_focus);
			tv.setVisibility(View.GONE);
			mIsDelete = false;
		}
	}

	private void gotoSelectActivity() {
		KygroupApplication.mFlag = 0;
		Intent intent = new Intent(mContext, AddUniversityActivity.class);
		intent.putExtra("addfocus", true);
		mContext.startActivity(intent);
		mIsDelete = false;
	}

	private void submitConcern() {
		if (!isAim()) {
			// if(!mDao.isExistinDb(mInfo)){
			addConcern(mInfo);
			// }else{
			// ToastUtils.showShortToast(R.string.baokao_exist);
			// }
		} else {
			ToastUtils.showShortToast(R.string.baokao_exist);
		}
	}

	class CreateFocusBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mInfo = new FocusInfo();
			mInfo.setmTime(System.currentTimeMillis() + "");
			mInfo.setmFocusSchool(intent.getStringExtra("uni"));
			mInfo.setmFocusColleage(intent.getStringExtra("col"));
			mInfo.setmFocusMajor(intent.getStringExtra("maj"));
			mInfo.setmSid(intent.getStringExtra("unikey"));
			mInfo.setmCid(intent.getStringExtra("colkey"));
			mInfo.setmMid(intent.getStringExtra("majkey"));
			mInfo.setmFocusYear(intent.getStringExtra("year"));
			System.out.println("id:::::::::::::::::::::::"
					+ intent.getStringExtra("unikey"));
			submitConcern();
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 3) {
				mContext.startWaitingDialog();
			}
			if (msg.what == 4) {
				mContext.closeWaitingDialog();
			}
			if (msg.what == 5) {
				adapter.notifyDataSetChanged();
			}
			if (msg.what == 6) {
				initBaokaoView();
			}
			if (msg.what == 7) {
				ToastUtils.showShortToast("操作失败");
			}
			super.handleMessage(msg);
		}
	};

	public void onDestory() {
		if (null != mReceiver) {
			mContext.unregisterReceiver(mReceiver);
		}
	}

	private void showChangeAimDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle(R.string.dia_change_aim);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.mail_listview, null);
		ListView focusView = (ListView) view.findViewById(R.id.mail_listview);
		FocusAdapter adapter = new FocusAdapter(mContext, mFocusInfos);
		focusView.setAdapter(adapter);
		focusView.setOnItemClickListener(this);
		builder.setView(view);
		mFocusDialog = builder.create();
		mFocusDialog.show();
		mIsDelete = false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// TODO Auto-generated method stub
		// mAimInfo = (FocusInfo) parent.getAdapter().getItem(position);
		mAimInfo = mFocusInfos.get(position);
		mContext.startWaitingDialog();
		if (null != mFocusDialog) {
			mFocusDialog.dismiss();
		}
		changeAim();
	}

	private void saveRegisterMsg() {
		SharedPreferences prefs = mContext.getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, mContext.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("regcol", mAimInfo.getmFocusColleage());
		editor.putString("regmaj", mAimInfo.getmFocusMajor());
		editor.putString("reguni", mAimInfo.getmFocusSchool());
		editor.putString("regcolid", mAimInfo.getmCid());
		editor.putString("regmajid", mAimInfo.getmMid());
		editor.putString("reguniid", mAimInfo.getmSid());
		editor.commit();
	}

	private boolean isAim() {
		boolean ret = false;
		if (null != mInfo
				&& mUser.getRCollege().equals(mInfo.getmFocusColleage())
				&& mUser.getRMajor().equals(mInfo.getmFocusMajor())
				&& mUser.getRSchool().equals(mInfo.getmFocusSchool())) {
			ret = true;
		}
		return ret;
	}

	public abstract interface IChangeAim {
		public void changeAim();
	}

	public void setChangeAim(IChangeAim aim) {
		mChangeAim = aim;
	}

	// private void showAddIcon(){
	// ViewTreeObserver vto = mAddFoucsLyoaut.getViewTreeObserver();
	// vto.addOnScrollChangedListener(new OnScrollChangedListener() {
	// @Override
	// public void onScrollChanged() {
	// // TODO Auto-generated method stub
	// setRightVisibility();
	// }
	// });
	//
	// }

	private void setRightVisibility() {
		// int maxHeight = mScreenHeight - mBottomHeight;
		// int loc[] = new int[2];
		// mAddFoucsLyoaut.getLocationOnScreen(loc);
		// Log.v("AAA", "-- " + maxHeight + "- " + loc[1] + " - "
		// + mAddFoucsLyoaut.getTop());
		// if (loc[1] > maxHeight) {
		// mContext.setRightBtnVisibility(View.VISIBLE);
		// } else {
		// mContext.setRightBtnVisibility(View.GONE);
		// }
	}

	// private void getBottomHeight(){
	// final ViewTreeObserver vto = mAddFoucsLyoaut.getViewTreeObserver();
	// vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	// @Override
	// public void onGlobalLayout() {
	// // TODO Auto-generated method stub
	// mBottomHeight = mContext.getBottomHeight();
	// setRightVisibility();
	// showAddIcon();
	// mAddFoucsLyoaut.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	// }
	// });
	// }
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mFocusInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.focus_item_layout, null);
				holder = new ViewHolder();
				holder.txt_del = (TextView) convertView
						.findViewById(R.id.delete_focus);
				holder.txt_focus_msg = (TextView) convertView
						.findViewById(R.id.focus_msg);
				holder.img_arrow_right = (ImageView) convertView
						.findViewById(R.id.img_arrow_right);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			FocusInfo info = mFocusInfos.get(position);
			holder.txt_focus_msg.setText(info.getmFocusYear() + " "
					+ info.getmFocusSchool() + " | " + info.getmFocusColleage()
					+ " | " + info.getmFocusMajor());
			holder.txt_focus_msg.setOnClickListener(BaokaoView.this);
			holder.txt_focus_msg.setTag(info);
			holder.txt_del.setOnClickListener(BaokaoView.this);
			holder.txt_del.setTag(info);
			if (mIsDelete) {
				holder.txt_del.setVisibility(View.VISIBLE);
				holder.img_arrow_right.setVisibility(View.GONE);

			} else {
				holder.txt_del.setVisibility(View.GONE);
				holder.img_arrow_right.setVisibility(View.VISIBLE);

			}

			return convertView;
		}
	}

	class ViewHolder {
		ImageView img_arrow_right;
		TextView txt_del;
		TextView txt_focus_msg;;
	}
}
