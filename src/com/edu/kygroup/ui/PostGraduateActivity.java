package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ScanBbsAdapter;
import com.edu.kygroup.adapter.UserAdapter;
import com.edu.kygroup.domin.DetailsInfo.Details;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.pullrefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.edu.pullrefresh.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

public class PostGraduateActivity extends BaseActivity implements IBindData,
		OnItemClickListener {
	public static int POST_PAGE = 1;
	public static int MAJOR_PAGE = 1;
	public static int COLLEAGE_PAGE = 1;
	public static int POST_GRADUATE_PAGE = 1;
	public static int POST_GRADUATE_TOTAL = 2;

	private PullToRefreshListView mPostListView;
	private boolean mPostData = false;
	private ArrayList<User> mPostUser;
	private UserAdapter mPostAdapter;
	private int mPostPage = 2;

	private boolean mMajorData = false;
	private ArrayList<User> mMajorUser;
	private UserAdapter mMajorAdapter;
	private int mMajorPage = 1;

	private boolean mColleageData = false;
	private ArrayList<User> mColleageUser;
	private UserAdapter mColleageAdapter;
	private int mColleagePage = 1;

	private boolean mPostGraduateData = false;
	private ArrayList<User> mPostGraduateUser;
	private UserAdapter mPostGraduateAdapter;
	private int mPostGraduatePage = 1;

	private LinearLayout mMoreLayout;
	private MajorDetail mDetails;

	private PopupWindow mPopupWindow;
	private ListView mSelectListView;
	private ScanBbsAdapter mAdapter;
	private int mPosition = 0;

	private boolean mCurPostGetData = mPostData;
	private int mCurPostPage = mPostPage;
	private int CUR_POST_PAGE = POST_PAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initPopupWindow();
		initData();
		regisiterRecevier();
		initListView();
		getPostGraduates(mCurPostPage, 0);
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(this).inflate(R.layout.viewlayout, null);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText(R.string.search_post_graduate);
		setRightBtnText(R.string.select);
		mPostListView = (PullToRefreshListView) findViewById(R.id.search_result_listview);
		mPostListView.setVisibility(View.GONE);
		mMoreLayout = (LinearLayout) findViewById(R.id.search_loadmore_layout);
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		mPostUser = (ArrayList<User>) getIntent().getSerializableExtra("data");
		mDetails = (MajorDetail) getIntent().getSerializableExtra("detail");
		if (null == mPostUser) {
			mPostUser = new ArrayList<User>();
		}
		mMajorUser = new ArrayList<User>();
		mColleageUser = new ArrayList<User>();
		mPostGraduateUser = new ArrayList<User>();
		mPostAdapter = new UserAdapter(this, mPostUser, 1);
		mMajorAdapter = new UserAdapter(this, mMajorUser, 0);
		mColleageAdapter = new UserAdapter(this, mColleageUser, 0);
		mPostGraduateAdapter = new UserAdapter(this, mPostGraduateUser, 1);
		mPostListView.setAdapter(mPostAdapter);
		if (mPostUser.size() > 0) {
			mPostListView.setVisibility(View.VISIBLE);
		}
		mPostListView.setOnItemClickListener(this);
	}

	// target 0同专业 1同学院 2研究生3全部
	public void getPostGraduates(int page, int target) {
		String url = UrlUtils.GET_POST_GRADUATES_URL + "user.email="
				+ mUser.getEmail() + "&page=" + page + "&rp=" + 10
				+ "&user.aim.sid=" + mDetails.getSid() + "&user.aim.ceid="
				+ mDetails.getCid() + "&user.aim.mid=" + mDetails.getMid()
				+ "&user.longitude=" + LocationUtils.getLongtitude()
				+ "&user.latitude=" + LocationUtils.getLatitude() + "&target="
				+ target;
		if (1 == page) {
			startWaitingDialog();
		}
		switch (target) {
		case 1:
			new NetworkTask().execute(this, TagUtils.GET_DETAIL_MAJOR, url);
			break;
		case 2:
			new NetworkTask().execute(this, TagUtils.GET_DETAIL_COLLEAGE, url);
			break;
		case 3:
			new NetworkTask().execute(this, TagUtils.GET_DETAIL_POST_GRADUATE,
					url);
			break;
		case 0:
			new NetworkTask().execute(this, TagUtils.GET_DETAIL_POST, url);
			break;
		default:
			break;
		}
	}

	private void initListView() {
		mPostListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (!mCurPostGetData && mCurPostPage <= CUR_POST_PAGE) {
							mCurPostGetData = true;
							getPostGraduates(mCurPostPage, mPosition);
							mMoreLayout.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		closeWaitingDialog();
		switch (tag) {
		case TagUtils.GET_DETAIL_POST: {
			mMoreLayout.setVisibility(View.GONE);
			if (null != obj) {
				mPostListView.setVisibility(View.VISIBLE);
				mPostUser.addAll((ArrayList<User>) obj);
				mPostAdapter.notifyDataSetChanged();
				mPostPage++;
			}
			mCurPostGetData = false;
			if (mPosition == 0) {
				mCurPostPage = mPostPage;
				CUR_POST_PAGE = POST_PAGE;
			}
			break;
		}
		case TagUtils.GET_DETAIL_MAJOR: {
			mMoreLayout.setVisibility(View.GONE);
			if (null != obj) {
				mPostListView.setVisibility(View.VISIBLE);
				mMajorUser.addAll((ArrayList<User>) obj);
				mMajorAdapter.notifyDataSetChanged();
				mMajorPage++;
			}
			mCurPostGetData = false;
			if (mPosition == 1) {
				mCurPostPage = mMajorPage;
				CUR_POST_PAGE = MAJOR_PAGE;
			}
			break;
		}
		case TagUtils.GET_DETAIL_COLLEAGE: {
			mMoreLayout.setVisibility(View.GONE);
			if (null != obj) {
				mPostListView.setVisibility(View.VISIBLE);
				mColleageUser.addAll((ArrayList<User>) obj);
				mColleageAdapter.notifyDataSetChanged();
				mColleagePage++;
			}
			mCurPostGetData = false;
			if (mPosition == 2) {
				mCurPostPage = mColleagePage;
				CUR_POST_PAGE = COLLEAGE_PAGE;
			}
			break;
		}
		case TagUtils.GET_DETAIL_POST_GRADUATE: {
			mMoreLayout.setVisibility(View.GONE);
			if (null != obj) {
				mPostListView.setVisibility(View.VISIBLE);
				mPostGraduateUser.addAll((ArrayList<User>) obj);
				mPostGraduateAdapter.notifyDataSetChanged();
				mPostGraduatePage++;
			}
			mCurPostGetData = false;
			if (mPosition == 3) {
				mCurPostPage = mPostGraduatePage;
				CUR_POST_PAGE = POST_GRADUATE_PAGE;
			}
			break;
		}
		default:
			break;
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// User user = (User)parent.getAdapter().getItem(position);
		// Intent intent = new Intent(this,UserActivity.class);
		// intent.putExtra("user", user);
		// startActivityForResult(intent, 100);

		Object obj = parent.getAdapter().getItem(position);
		if (obj instanceof User) {
			User user = (User) obj;
			Intent intent = new Intent(this, PersonDetailActivity.class);
			intent.putExtra("user", user);
			startActivityForResult(intent, 100);
		} else if (obj instanceof String) {
			if (mPosition != position) {
				mPosition = position;
				if (mPosition == 1) {
					mCurPostGetData = mMajorData;
					mCurPostPage = mMajorPage;
					CUR_POST_PAGE = MAJOR_PAGE;
					if (null == mMajorUser || mMajorUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 1);
					}
					mPostListView.setAdapter(mMajorAdapter);
				} else if (mPosition == 2) {
					mCurPostGetData = mColleageData;
					mCurPostPage = mColleagePage;
					CUR_POST_PAGE = COLLEAGE_PAGE;
					if (null == mColleageUser || mColleageUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 2);
					}
					mPostListView.setAdapter(mColleageAdapter);
				} else if (mPosition == 3) {
					mCurPostGetData = mPostGraduateData;
					mCurPostPage = mPostGraduatePage;
					CUR_POST_PAGE = POST_GRADUATE_PAGE;
					if (null == mPostGraduateUser
							|| mPostGraduateUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 3);
					}
					mPostListView.setAdapter(mPostGraduateAdapter);
				} else if (mPosition == 0) {
					mCurPostGetData = mPostData;
					mCurPostPage = mPostPage;
					CUR_POST_PAGE = POST_PAGE;
					if (null == mPostUser || mPostUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 0);
					}
					mPostListView.setAdapter(mPostAdapter);
				}
				mPopupWindow.dismiss();
				mAdapter.setPosition(mPosition);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void regisiterRecevier() {
		IntentFilter filter = new IntentFilter("com.kygroup.changed");
		registerReceiver(receiver, filter);
	}

	public void dealRelation(ArrayList<User> users, UserAdapter adapter,
			String email, int relation) {
		if (null != users) {
			for (User user : users) {
				if (user.getEmail().equals(email)) {
					user.setRelation(relation);
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String email = intent.getStringExtra("email");
			int relation = intent.getIntExtra("relation", 0);
			try {
				dealRelation(mPostUser, mPostAdapter, email, relation);
				dealRelation(mMajorUser, mMajorAdapter, email, relation);
				dealRelation(mColleageUser, mColleageAdapter, email, relation);
				dealRelation(mPostGraduateUser, mPostGraduateAdapter, email,
						relation);
			} catch (Exception e) {
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("PostGraduateActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("PostGraduateActivity");
		MobclickAgent.onPause(this);
	}

	private void initPopupWindow() {
		String[] lists = getResources().getStringArray(R.array.select_post);
		View view = LayoutInflater.from(this).inflate(R.layout.mail_listview,
				null);
		view.setBackgroundColor(Color.WHITE);
		mSelectListView = (ListView) view.findViewById(R.id.mail_listview);
		mSelectListView.setOnItemClickListener(this);
		mAdapter = new ScanBbsAdapter(this, lists, 0);
		mSelectListView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(view, 180, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		setRightBtnListener();
	}

	private void setRightBtnListener() {
		setRightBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int width = DeviceUtils
						.getDisplayWidth(PostGraduateActivity.this);
				mPopupWindow.showAsDropDown(mLayout, width, 0);
			}
		});
	}
}
