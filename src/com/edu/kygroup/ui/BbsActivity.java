//package com.edu.kygroup.ui;
//
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.edu.kygroup.R;
//import com.edu.kygroup.adapter.PostersAdater;
//import com.edu.kygroup.adapter.PostersAdater.OnShareResponseListener;
//import com.edu.kygroup.adapter.ScanBbsAdapter;
//import com.edu.kygroup.domin.FocusInfo;
//import com.edu.kygroup.domin.Louzhu;
//import com.edu.kygroup.domin.Poster;
//import com.edu.kygroup.domin.Poster.Topic;
//import com.edu.kygroup.domin.TopicRetInfo;
//import com.edu.kygroup.domin.User;
//import com.edu.kygroup.iface.IBindData;
//import com.edu.kygroup.net.NetworkTask;
//import com.edu.kygroup.utils.StringUtils;
//import com.edu.kygroup.utils.TagUtils;
//import com.edu.kygroup.utils.ToastUtils;
//import com.edu.kygroup.utils.UrlUtils;
//import com.umeng.analytics.MobclickAgent;
//
////import cn.sharesdk.onekeyshare.OnekeyShare;
//
//public class BbsActivity extends BaseActivity implements OnClickListener,
//		OnItemClickListener, IBindData, OnShareResponseListener {
//	private static final int MAX_INPUT = 140;
//	private static final int MAJOR_FLAG = 3;
//	private static final int COLLEAGE_FLAG = 2;
//	private static final int SCHOOL_FLAG = 1;
//	private FocusInfo mFocusInfo;
//	private PopupWindow mPopupWindow;
//	private PopupWindow mSharePopupWindow;
//
//	private ListView mPostListView;
//	private ListView mScanListView;
//	private TextView mCountView;
//	private EditText mInputText;
//	private Dialog mDialog;
//
//	private TextView mQQWeiboView;
//	private TextView mSinaWeiboView;
//
//	private ScanBbsAdapter mAdapter;
//	private PostersAdater mMajorAdapter;
//	private PostersAdater mColleageAdapter;
//	private PostersAdater mSchoolAdapter;
//	private int mPostion = 2;
//
//	private User mUser;
//
//	private ArrayList<Topic> mMajorTopics;
//	private HashMap<String, Topic> mMajorMap;
//	private int mMajorTotalPage;
//	private int mMajorPage = 1;
//
//	private ArrayList<Topic> mColleageTopics;
//	private int mColleageTotalPage;
//	private int mColleagePage = 1;
//
//	private ArrayList<Topic> mSchoolTopics;
//	private int mSchoolTotalPage;
//	private int mSchoolPage = 1;
//
//	private boolean mIsGetData = false;
//	private LinearLayout mMoreLayout;
//	private ScrollListener mScrollListener;
//	private Topic mShareTopic;
//
//	@Override
//	protected View setContentView() {
//		// TODO Auto-generated method stub
//		return mInflater.inflate(R.layout.bbsactivity, null);
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		initData();
//		initView();
//		addListener();
//		getPosters(1);
//		// showPopWindow();
//		// ShareSDK.initSDK(this, "19b1b5571cf2");
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		// ShareSDK.stopSDK(this);
//	}
//
//	public void initView() {
//		setLeftSize(getResources().getDimension(R.dimen.size_11));
//		setRightSize(getResources().getDimension(R.dimen.size_11));
//		setTitleTextSize(13);
//		setTitleTextColor(getResources().getColor(R.color.nomal_font_color));
//		setTitleText(mFocusInfo.getmFocusMajor());
//		setLeftBtnDrawable(0, 0, R.drawable.abs_spinner, 0);
//		setLeftBtnVisibility(View.VISIBLE);
//		setLeftBtnText(R.string.major);
//		setRightBtnText(R.string.posts);
//		mPostListView = (ListView) findViewById(R.id.bbs_listview);
//		mPostListView.setCacheColorHint(0);
//		mMoreLayout = (LinearLayout) findViewById(R.id.search_loadmore_layout);
//		mScrollListener = new ScrollListener();
//	}
//
//	private void addListener() {
//		setLeftBtnClickListener(this);
//		setRightBtnClickListener(this);
//		mPostListView.setOnScrollListener(mScrollListener);
//		mPostListView.setOnItemClickListener(this);
//	}
//
//	public void initData() {
//		Intent intent = getIntent();
//		mFocusInfo = (FocusInfo) intent.getSerializableExtra("info");
//		mUser = KygroupApplication.mUser;
//		initPopupWindow();
//		mMajorTopics = new ArrayList<Poster.Topic>();
//		mMajorAdapter = new PostersAdater(this, mMajorTopics);
//		mColleageTopics = new ArrayList<Poster.Topic>();
//		mColleageAdapter = new PostersAdater(this, mColleageTopics);
//		mSchoolTopics = new ArrayList<Poster.Topic>();
//		mSchoolAdapter = new PostersAdater(this, mSchoolTopics);
//	}
//
//	private void initPopupWindow() {
//		View view = LayoutInflater.from(this).inflate(R.layout.mail_listview,
//				null);
//		view.setBackgroundColor(Color.WHITE);
//		mScanListView = (ListView) view.findViewById(R.id.mail_listview);
//		mScanListView.setOnItemClickListener(this);
//		String[] lists = getResources().getStringArray(R.array.select_bbs);
//		mAdapter = new ScanBbsAdapter(this, lists, 2);
//		mScanListView.setAdapter(mAdapter);
//		mPopupWindow = new PopupWindow(view, 180, LayoutParams.WRAP_CONTENT);
//		mPopupWindow.setFocusable(true);
//		mPopupWindow.setOutsideTouchable(true);
//		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//		View shareView = LayoutInflater.from(this).inflate(
//				R.layout.share_layout, null);
//		shareView.setBackgroundColor(Color.WHITE);
//		mQQWeiboView = (TextView) shareView.findViewById(R.id.qq_weibo);
//		mSinaWeiboView = (TextView) shareView.findViewById(R.id.sina_weibo);
//		mSharePopupWindow = new PopupWindow(shareView,
//				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		mSharePopupWindow.setFocusable(true);
//		mSharePopupWindow.setOutsideTouchable(true);
//		mSharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
//		mQQWeiboView.setOnClickListener(this);
//		mSinaWeiboView.setOnClickListener(this);
//	}
//
//	private void initTitle() {
//		String title = "";
//		if (mPostion == 1) {
//			title = mFocusInfo.getmFocusColleage();
//		} else if (mPostion == 2) {
//			title = mFocusInfo.getmFocusMajor();
//		} else {
//			title = mFocusInfo.getmFocusSchool();
//		}
//		setTitleText(title);
//
//		title = null;
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.left_btn:
//			showPopupwindow();
//			break;
//		case R.id.right_btn:
//			Intent intent = new Intent();
//			intent.putExtra("focusInfo", mFocusInfo);
//			intent.setClass(this, PublicshBbsActivity.class);
//			startActivityForResult(intent, 100);
//			break;
//		case R.id.qq_weibo:
//			// startActivity(mShareTopic, TencentWeibo.NAME);
//			break;
//		case R.id.sina_weibo:
//			// startActivity(mShareTopic, SinaWeibo.NAME);
//		default:
//			break;
//		}
//	}
//
//	private void startActivity(Topic topic, String plat) {
//		// Intent intent = new Intent(this, ShareActivity.class);
//		// intent.putExtra("topic", topic);
//		// intent.putExtra("plat", plat);
//		// startActivity(intent);
//		// if (mSharePopupWindow.isShowing()) {
//		// mSharePopupWindow.dismiss();
//		// }
//	}
//
//	public void showInputMethod() {
//		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//	}
//
//	public void showPopupwindow() {
//		if (!isFinishing()) {
//			mPopupWindow.showAsDropDown(mLayout, 0, 0);
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		mPopupWindow.dismiss();
//		Object obj = parent.getAdapter().getItem(position);
//		if (obj instanceof String) {
//			if (mPostion != position) {
//				mPostion = position;
//				if (mPostion == 0) {
//					if (null == mSchoolTopics || mSchoolTopics.size() <= 0) {
//						getPosters(1);
//					}
//					setLeftBtnText(R.string.university);
//					mPostListView.setAdapter(mSchoolAdapter);
//				} else if (mPostion == 1) {
//					if (null == mColleageTopics || mColleageTopics.size() <= 0) {
//						getPosters(1);
//					}
//					mPostListView.setAdapter(mColleageAdapter);
//					setLeftBtnText(R.string.colleage);
//				} else if (mPostion == 2) {
//					if (null == mMajorTopics || mMajorTopics.size() <= 0) {
//						getPosters(1);
//					}
//					mPostListView.setAdapter(mMajorAdapter);
//					setLeftBtnText(R.string.major);
//
//				}
//				mAdapter.setPosition(mPostion);
//				initTitle();
//			}
//		} else if (obj instanceof Topic) {
//			Topic topic = (Topic) obj;
//			startResponseActivity(topic);
//		}
//	}
//
//	private void startResponseActivity(Topic topic) {
//		Intent intent = new Intent(this, ResponseActivity.class);
//		intent.putExtra("topic", topic);
//		startActivity(intent);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (data == null || requestCode != 100) {
//			return;
//		}
//
//		String content = data.getStringExtra("content");
//		ArrayList<String> picList = new ArrayList<String>();
//		picList = data.getStringArrayListExtra("picList");
//		int tid = data.getIntExtra("tid", 0);
//		String time = data.getStringExtra("time");
//		addSucPoster(tid, time, content, picList);
//	}
//
//	public void showPosterDialog() {
//		if (null == mDialog) {
//			Builder builder = new Builder(this);
//			builder.setTitle(R.string.my_poster);
//			View view = LayoutInflater.from(this).inflate(
//					R.layout.poster_layout, null);
//			initDialogView(view);
//			builder.setView(view);
//			builder.setPositiveButton(R.string.ok,
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//							addPoster();
//						}
//					});
//			builder.setNegativeButton(R.string.cancel,
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//							mInputText.setText("");
//						}
//					});
//			mDialog = builder.create();
//			Window window = mDialog.getWindow();
//			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		}
//		if (!mDialog.isShowing()) {
//			mDialog.show();
//		}
//	}
//
//	private void initDialogView(View view) {
//		mInputText = (EditText) view.findViewById(R.id.poster_input);
//		mCountView = (TextView) view.findViewById(R.id.input_count);
//		mInputText.setGravity(Gravity.TOP);
//		addEditListener();
//	}
//
//	private void addEditListener() {
//		mInputText.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence seq, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//				if (null != seq) {
//					String inputStr = seq.toString();
//					int cou = inputStr.length();
//					if (cou > MAX_INPUT) {
//						mCountView.setText(R.string.speakalot);
//					} else {
//						mCountView.setText(cou + "");
//					}
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//
//	private void addPoster() {
//		String str = mInputText.getText().toString();
//		if (!StringUtils.isEmpty(str.trim())) {
//			StringBuffer buf = new StringBuffer(UrlUtils.ADD_POSTER);
//			buf.append("email=" + mUser.getEmail());
//			buf.append("&topic.sid=" + mFocusInfo.getmSid());
//			buf.append("&topic.ceid=" + mFocusInfo.getmCid());
//			buf.append("&topic.mid=" + mFocusInfo.getmMid());
//			buf.append("&topic.title=" + URLEncoder.encode(str.trim()));
//			String url = buf.toString();
//			startWaitingDialog();
//			new NetworkTask().execute(this, TagUtils.TOPIC_RET_TAG, url);
//		} else {
//			Toast.makeText(this, R.string.speak_none, 500).show();
//		}
//	}
//
//	@Override
//	public Object bindData(int tag, Object obj) {
//		// TODO Auto-generated method stub
//		switch (tag) {
//		case TagUtils.TOPIC_RET_TAG:
//			addTopicResult(obj);
//			break;
//		case TagUtils.GET_POSTER_TAG:
//			showPosters(obj);
//			break;
//		default:
//			break;
//		}
//		return null;
//	}
//
//	private void addTopicResult(Object obj) {
//		closeWaitingDialog();
//		if (null != obj) {
//			TopicRetInfo info = (TopicRetInfo) obj;
//			int id = info.getTopicid();
//			String time = info.getSendtime();
//			if (id > 0) {
//				Toast.makeText(this, R.string.send_success, 500).show();
//				// addSucPoster(id, time);
//				mInputText.setText("");
//				return;
//			}
//		}
//		Toast.makeText(this, R.string.tips_send_msg_failed, 500).show();
//	}
//
//	private void showPosters(Object obj) {
//		closeWaitingDialog();
//		if (null != obj) {
//			Poster poster = (Poster) obj;
//			if (poster.getFlag() == MAJOR_FLAG) {
//				int pos = mMajorTopics.size();
//				mMajorTopics.addAll(poster.getTopics());
//				mMajorAdapter.notifyDataSetChanged();
//				mPostListView.setAdapter(mMajorAdapter);
//				mMajorTotalPage = poster.getTotalpage();
//				mPostListView.setSelection(pos);
//			} else if (poster.getFlag() == COLLEAGE_FLAG) {
//				int pos = mColleageTopics.size();
//				mColleageTopics.addAll(poster.getTopics());
//				mColleageAdapter.notifyDataSetChanged();
//				mPostListView.setAdapter(mColleageAdapter);
//				mColleageTotalPage = poster.getTotalpage();
//				mPostListView.setSelection(pos);
//			} else if (poster.getFlag() == SCHOOL_FLAG) {
//
//				int pos = mSchoolTopics.size();
//				mSchoolTopics.addAll(poster.getTopics());
//				mSchoolAdapter.notifyDataSetChanged();
//				mPostListView.setAdapter(mSchoolAdapter);
//				mSchoolTotalPage = poster.getTotalpage();
//				mPostListView.setSelectionFromTop(pos - 1, 0);
//			}
//		}
//		setLeftBtnClickable(true);
//		mIsGetData = false;
//	}
//
//	private void getPosters(int page) {
//		if (page == 1) {
//			startWaitingDialog();
//		}
//		setLeftBtnClickable(false);
//		StringBuffer buf = new StringBuffer(UrlUtils.GET_POSTER);
//		buf.append("&topic.sid=" + mFocusInfo.getmSid());
//		if (mPostion > 0) {
//			buf.append("&topic.ceid=" + mFocusInfo.getmCid());
//		}
//		if (mPostion > 1) {
//			buf.append("&topic.mid=" + mFocusInfo.getmMid());
//		}
//		buf.append("&page=" + page);
//		buf.append("&rp=10");
//		String url = buf.toString();
//		new NetworkTask().execute(this, TagUtils.GET_POSTER_TAG, url);
//	}
//
//	private void addSucPoster(int tid, String time, String content,
//			ArrayList<String> picList) {
//		Poster poster = new Poster();
//		Topic topic = poster.new Topic();
//		topic.setCeid(Integer.parseInt(mFocusInfo.getmCid()));
//		topic.setCename(mFocusInfo.getmFocusColleage());
//		topic.setMid(Integer.parseInt(mFocusInfo.getmMid()));
//		topic.setMname(mFocusInfo.getmFocusMajor());
//		topic.setSid(Integer.parseInt(mFocusInfo.getmSid()));
//		topic.setSname(mFocusInfo.getmFocusSchool());
//		topic.setTitle(content);
//		topic.setPicsList(picList);
//		topic.setTid(tid);
//		topic.setTimestamp(time);
//		Louzhu louzhu = new Louzhu();
//		louzhu.setBatchelorschool(mUser.getESchool());
//		louzhu.setEmail(mUser.getEmail());
//		louzhu.setGender(mUser.getGender());
//		louzhu.setImage(mUser.getPic());
//		louzhu.setNickname(mUser.getNickName());
//		topic.setLouzhu(louzhu);
//		if (null != mMajorTopics) {
//			mMajorTopics.add(0, topic);
//			mMajorAdapter.notifyDataSetChanged();
//		}
//		if (null != mColleageTopics) {
//			mColleageTopics.add(0, topic);
//			mColleageAdapter.notifyDataSetChanged();
//		}
//		if (null != mSchoolTopics) {
//			mSchoolTopics.add(0, topic);
//			mSchoolAdapter.notifyDataSetChanged();
//		}
//	}
//
//	class ScrollListener implements OnScrollListener {
//		private int mLastVisiblePosition = 0;// 记录下拉到最后一个item的位置
//		private int mLastVisiblePositionY = 0;// 记录最后一个item的Y坐标
//		private int mLastItem = 0;
//		private boolean mIsTip = false;
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			// TODO Auto-generated method stub
//			if (mPostion == 1) {
//				if (mColleagePage < mColleageTotalPage
//						&& scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//					int count = mPostListView.getCount();
//					if (mLastItem >= count && !mIsGetData) {
//						mColleagePage++;
//						mIsGetData = true;
//						getPosters(mColleagePage);
//					}
//				}
//			} else if (mPostion == 2) {
//				if (mMajorPage < mMajorTotalPage
//						&& scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//					int count = mPostListView.getCount();
//					if (mLastItem >= count && !mIsGetData) {
//						mMajorPage++;
//						mIsGetData = true;
//						getPosters(mMajorPage);
//					}
//				}
//			} else if (mPostion == 0) {
//				if (mSchoolPage < mSchoolTotalPage
//						&& scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//					int count = mPostListView.getCount();
//					if (mLastItem >= count && !mIsGetData) {
//						mSchoolPage++;
//						mIsGetData = true;
//						getPosters(mSchoolPage);
//					}
//				}
//			}
//
//			if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
//					&& mIsGetData) {
//				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {// 滚动到底部
//					View v = (View) view.getChildAt(view.getChildCount() - 1);// 取得最后一个Item的View
//					int[] location = new int[2];// 保存最后一个Item的绝对坐标数组
//					v.getLocationOnScreen(location);// 获取最后一个Item在整个屏幕内的绝对坐标
//					int y = location[1];// 取得最后一个Item的Y坐标
//					if (view.getLastVisiblePosition() != mLastVisiblePosition
//							&& mLastVisiblePositionY != y) {// 第一次拖至底部
//						mLastVisiblePosition = view.getLastVisiblePosition();
//						mLastVisiblePositionY = y;
//						mMoreLayout.setVisibility(View.VISIBLE);
//						return;
//					} else if (view.getLastVisiblePosition() == mLastVisiblePosition
//							&& mLastVisiblePositionY == y) {// 第二次拖至底部
//						mMoreLayout.setVisibility(View.VISIBLE);
//					}
//				}
//				// 未滚动到底部，第二次拖至底部都初始化
//				mLastVisiblePosition = 0;
//				mLastVisiblePositionY = 0;
//			}
//			// 判断可见的数量是否等于总数量如果等于，则提示没有数据了
//			if (mPostion == 1) {
//				if (mLastItem == mColleageTotalPage && !mIsTip) {
//					ToastUtils.showShortToast(R.string.havenodata);
//					mIsTip = true;
//				}
//			} else if (mPostion == 2) {
//				if (mLastItem == mMajorTotalPage && !mIsTip) {
//					ToastUtils.showShortToast(R.string.havenodata);
//					mIsTip = true;
//				}
//			} else if (mPostion == 0) {
//				if (mLastItem == mSchoolTotalPage && !mIsTip) {
//					ToastUtils.showShortToast(R.string.havenodata);
//					mIsTip = true;
//				}
//			}
//		}
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem,
//				int visibleItemCount, int totalItemCount) {
//			// TODO Auto-generated method stub
//			mLastItem = firstVisibleItem + visibleItemCount;
//		}
//	}
//
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			showPopupwindow();
//			super.handleMessage(msg);
//		}
//	};
//
//	public void showPopWindow() {
//		handler.sendEmptyMessageDelayed(0, 400);
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		MobclickAgent.onPageStart("BbsActivity"); // 统计页面
//		MobclickAgent.onResume(this);
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		MobclickAgent.onPageEnd("BbsActivity");
//		MobclickAgent.onPause(this);
//	}
//
//	@Override
//	public void OnResponse(Topic topic) {
//		// TODO Auto-generated method stub
//		startResponseActivity(topic);
//	}
//
//	@Override
//	public void OnShare(Topic topic) {
//		// TODO Auto-generated method stub
//		if (null != mSharePopupWindow && !mSharePopupWindow.isShowing()) {
//			mSharePopupWindow.showAtLocation(getParentView(), Gravity.BOTTOM,
//					0, 0);
//			mShareTopic = topic;
//		}
//	}
// }
