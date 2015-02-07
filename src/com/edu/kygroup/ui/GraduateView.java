package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ScanBbsAdapter;
import com.edu.kygroup.adapter.UserAdapter;
import com.edu.kygroup.db.DBUtils;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.ui.BaokaoView.IChangeAim;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.SearchViewPageAdapter;
import com.edu.pullrefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.edu.pullrefresh.PullToRefreshListView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class GraduateView implements IBindData, OnClickListener,
		OnItemClickListener, IChangeAim {
	private HomeActivity mContext;
	private View mView;
	private static final int SIZE = 8;

	public static int POST_PAGE = 1;
	public static int MATES_PAGE = 1;
	public static int FELLOW_PAGE = 1;
	public static int BROWSER_PAGE = 1;// 总页数

	public static int SAME_MAJOE_PAGE = 1;
	public static int SAME_COLLEAGE_PAGE = 1;
	public static int POST_GRADUATE_PAGE = 1;

	public static int POST_TOTAL = 0;

	public static int SAME_MAJOE_TOTAL = 0;
	public static int SAME_COLLEAGE_TOTAL = 0;
	public static int POST_GRADUATE_TOTAL = 0;

	private TextView mSearchBrowser;
	private TextView mSearchClassmates;
	private TextView mSearchPost;
	private TextView mSearchFellow;
	private ViewPager mViewPager;

	private View mBrowserView;
	private PullToRefreshListView mBrowserListview;// 浏览
	private UserAdapter mBrowserAdapter;
	private List<User> mBrowserUser;
	private int mBrowserPage = 1;
	private boolean mBrowserGetData = false;
	private LinearLayout linearLayout1;
	private View mFellowView;
	private boolean mFellowGetData = false;
	private PullToRefreshListView mFellowListview;// 同乡
	private UserAdapter mFellowAdapter;
	private List<User> mFellowUser;
	private int mFellowPage = 1;
	private LinearLayout layout_qq;
	private LinearLayout layout_weixin;
	// public boolean mIsGetData = false;
	private View mPostView;
	private PullToRefreshListView mPostListview;// 研友
	private UserAdapter mPostAdapter;
	private List<User> mPostUser;
	private int mPostPage = 1;
	private boolean mPostGetData = false;

	// private PullToRefreshListView mSameMajorListView;
	private UserAdapter mSameMajorAdapter;
	private List<User> mSameMajorUser;
	private int mSameMajorPage = 1;
	private boolean mMajorGetData = false;
	private LinearLayout layout_kefu;

	// private PullToRefreshListView mSameColleageListView;
	private UserAdapter mSameColleageAdapter;
	private List<User> mSameColleageUser;
	private int mSameColleagePage = 1;
	private boolean mColleageGetData = false;

	// private PullToRefreshListView mPostGraduateListView;
	private UserAdapter mPostGraduateAdapter;
	private List<User> mPostGraduateUser;
	private int mPostGraduatePage = 1;
	private boolean mGraduateGetData = false;
	// /////////////////////////////////////////////////////
	private View mMatesView;
	private PullToRefreshListView mMatesListview;// 校友
	private UserAdapter mMatesAdapter;
	private List<User> mMatesUser;
	private int mMatesPage = 1;
	private boolean mMateGetData = false;

	private ArrayList<View> mViews;
	private int mbmpWidth;// 动画图片宽度
	private int mCurrIndex = 0;// 当前页卡编号
	private ImageView imageView;// 动画图片

	private LinearLayout mLoadMore;

	private int mTipCount = 0;

	private PopupWindow mPopupWindow;
	private ListView mSelectListView;
	private ScanBbsAdapter mAdapter;
	private int mPosition = 0;

	//
	// public boolean mIsGetData = false;
	// private View mPostView;
	// private PullToRefreshListView mPostListview;//研友
	// private UserAdapter mPostAdapter;
	// private List<User> mPostUser;
	// private int mPostPage = 1;
	// private boolean mPostGetData = false;

	private boolean mCurPostGetData = mPostGetData;
	private int mCurPostPage = mPostPage;
	private int CUR_POST_PAGE = POST_PAGE;

	private User mUser;

	public GraduateView(HomeActivity context) {
		mUser = KygroupApplication.mUser;
		mContext = context;
		mView = LayoutInflater.from(mContext).inflate(R.layout.search_activity,
				null);
		initView();
		initData();
		initShareSDk();
	}

	private void initShareSDk() {
		ShareSDK.initSDK(mContext);
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
	}

	public View getView() {
		mContext.setTitleBarVisibility(View.VISIBLE);
		if (null != mFellowAdapter) {
			// mFellowAdapter.notifyDataSetChanged();
			mFellowListview.setAdapter(mFellowAdapter);
		}
		if (null != mBrowserAdapter) {
			// mBrowserAdapter.notifyDataSetChanged();
			mBrowserListview.setAdapter(mBrowserAdapter);
		}
		if (null != mMatesAdapter) {
			// mMatesAdapter.notifyDataSetChanged();
			mMatesListview.setAdapter(mMatesAdapter);
		}
		if (null != mPostAdapter) {
			// mPostAdapter.notifyDataSetChanged();
			if (mPosition == 0) {
				mPostListview.setAdapter(mPostAdapter);
			} else if (mPosition == 1) {
				mPostListview.setAdapter(mSameMajorAdapter);
			} else if (mPosition == 2) {
				mPostListview.setAdapter(mSameColleageAdapter);
			} else if (mPosition == 3) {
				mPostListview.setAdapter(mPostGraduateAdapter);
			}

		}
		setRightBtn();
		return mView;
	}

	private void initView() {
		layout_qq = (LinearLayout) mView.findViewById(R.id.layout_qq);
		layout_weixin = (LinearLayout) mView.findViewById(R.id.layout_weixin);
		layout_qq.setOnClickListener(this);
		layout_weixin.setOnClickListener(this);
		linearLayout1 = (LinearLayout) mView.findViewById(R.id.linearLayout1);
		mViewPager = (ViewPager) mView.findViewById(R.id.vpager);
		mSearchBrowser = (TextView) mView.findViewById(R.id.search_browser);
		mSearchBrowser.setOnClickListener(this);
		mSearchClassmates = (TextView) mView
				.findViewById(R.id.search_classmates);
		mSearchClassmates.setOnClickListener(this);
		mSearchFellow = (TextView) mView.findViewById(R.id.search_fellow);
		mSearchFellow.setOnClickListener(this);
		mSearchPost = (TextView) mView.findViewById(R.id.search_post);
		mSearchPost.setOnClickListener(this);
		mLoadMore = (LinearLayout) mView
				.findViewById(R.id.search_loadmore_layout);
		initViewPage();
		initImageView();
		initPopupWindow();
	}

	private void initViewPage() {
		mViews = new ArrayList<View>();
		initListView();
		mViewPager.setAdapter(new SearchViewPageAdapter(mViews));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new SearchOnPageChangeListener());
		getBrowser();
	}

	private void initData() {
		POST_PAGE = 1;
		MATES_PAGE = 1;
		FELLOW_PAGE = 1;
		SAME_MAJOE_PAGE = 1;
		SAME_COLLEAGE_PAGE = 1;
		POST_GRADUATE_PAGE = 1;
		POST_TOTAL = 0;
	}

	private void writeUser(final List<User> lists) {
		new Thread() {
			public void run() {
				User user = new User();
				System.out.println("size:::::::::::::" + lists.size());
				// for (User user : lists) {
				// user.write(DBUtils.getDBsa(2));
				// }
				user.write(DBUtils.getDBsa(2), lists);
			}
		}.start();
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.GET_BROWSER_USER_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mBrowserGetData = false;
			if (null != obj) {
				mBrowserPage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mBrowserUser.addAll(users);
				mBrowserUser.remove(mBrowserUser.size() - 1);
				mBrowserAdapter.notifyDataSetChanged();
				writeUser(mBrowserUser);
			}
			break;
		}
		case TagUtils.GET_POST_GRADUATE_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mCurPostGetData = false;
			if (null != obj) {
				mPostPage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mPostUser.addAll(users);
				mPostAdapter.notifyDataSetChanged();
				writeUser(mPostUser);

			}
			if (mPosition == 0) {
				mCurPostPage = mPostPage;
				CUR_POST_PAGE = POST_PAGE;
			}
			break;
		}
		case TagUtils.GET_FELLOW_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mFellowGetData = false;
			if (null != obj) {
				mFellowPage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mFellowUser.addAll(users);
				mFellowAdapter.notifyDataSetChanged();
				writeUser(mFellowUser);

			}
			break;
		}
		case TagUtils.GET_MATES_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mMateGetData = false;
			if (null != obj) {
				mMatesPage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mMatesUser.addAll(users);
				mMatesAdapter.notifyDataSetChanged();
				writeUser(mMatesUser);

			}
			break;
		}

		case TagUtils.GET_SAME_MAJOR_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mCurPostGetData = false;
			if (null != obj) {
				mSameMajorPage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mSameMajorUser.addAll(users);
				mSameMajorAdapter.notifyDataSetChanged();
			}
			if (mPosition == 1) {
				mCurPostPage = mSameMajorPage;
				CUR_POST_PAGE = SAME_MAJOE_PAGE;
			}
			break;
		}
		case TagUtils.GET_SAME_COLLEAGE_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mCurPostGetData = false;
			if (null != obj) {
				mSameColleagePage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mSameColleageUser.addAll(users);
				mSameColleageAdapter.notifyDataSetChanged();
			}
			if (mPosition == 2) {
				mCurPostPage = mSameColleagePage;
				CUR_POST_PAGE = SAME_COLLEAGE_PAGE;
			}
			break;
		}
		case TagUtils.GET_GRADUATE_TAG: {
			mContext.closeWaitingDialog();
			setLoadMore(View.GONE);
			mCurPostGetData = false;
			if (null != obj) {
				mPostGraduatePage++;
				ArrayList<User> users = (ArrayList<User>) obj;
				mPostGraduateUser.addAll(users);
				mPostGraduateAdapter.notifyDataSetChanged();
			}
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

	private void initImageView() {
		imageView = (ImageView) mView.findViewById(R.id.cursor);
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mbmpWidth = dm.widthPixels / 4;
		LayoutParams params = imageView.getLayoutParams();
		params.width = mbmpWidth;
		imageView.setLayoutParams(params);
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	public class SearchOnPageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int arg0) {
			imageAni(arg0);
			linearLayout1.setVisibility(View.GONE);
			if (arg0 == 1) {
				linearLayout1.setVisibility(View.VISIBLE);
				getPostGraduates();
			} else if (arg0 == 2) {
				getFellow();
			} else if (arg0 == 0) {
				getBrowser();
			} else if (arg0 == 3) {
				getClassmates();
			}
		}
	}

	private void imageAni(int arg0) {
		Animation animation = new TranslateAnimation(mbmpWidth * mCurrIndex,
				mbmpWidth * arg0, 0, 0);
		mCurrIndex = arg0;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(100);
		imageView.startAnimation(animation);
		if (arg0 == 1) {
			mContext.setRightBtnVisibility(View.VISIBLE);
			// mContext.setRightBg(R.drawable.postgraduate_selected);
			mContext.setRightBtnText(R.string.select);
		} else {
			mContext.setRightBtnVisibility(View.GONE);
		}
	}

	private void initListView() {
		User user = KygroupApplication.getApplication().mUser;
		LayoutInflater inflater = mContext.getLayoutInflater();
		mBrowserView = inflater.inflate(R.layout.viewlayout, null);
		mBrowserListview = (PullToRefreshListView) mBrowserView
				.findViewById(R.id.search_result_listview);

		mViews.add(mBrowserView);
		mBrowserUser = new ArrayList<User>();
		mBrowserUser.add(user);
		mBrowserAdapter = new UserAdapter(mContext, mBrowserUser);
		mBrowserListview.setAdapter(mBrowserAdapter);
		mBrowserListview.setOnItemClickListener(this);

		initBrowserListView();

		mPostView = inflater.inflate(R.layout.search_viewlayout, null);
		mPostListview = (PullToRefreshListView) mPostView
				.findViewById(R.id.search_result_listview);
		mViews.add(mPostView);
		mPostUser = new ArrayList<User>();
		mPostUser.add(user);
		mPostAdapter = new UserAdapter(mContext, mPostUser);
		mPostListview.setAdapter(mPostAdapter);
		mPostListview.setOnItemClickListener(this);
		initPostListView();

		// mSameMajorListView =
		// (PullToRefreshListView)mPostView.findViewById(R.id.same_major_listview);
		mSameMajorUser = new ArrayList<User>();
		mSameMajorUser.add(user);
		mSameMajorAdapter = new UserAdapter(mContext, mSameMajorUser);
		// mSameMajorListView.setAdapter(mSameMajorAdapter);
		// mSameMajorListView.setOnItemClickListener(this);
		// initSameMajorListView();

		// mSameColleageListView =
		// (PullToRefreshListView)mPostView.findViewById(R.id.same_colleage_listview);
		mSameColleageUser = new ArrayList<User>();
		mSameColleageUser.add(user);
		mSameColleageAdapter = new UserAdapter(mContext, mSameColleageUser);
		// mSameColleageListView.setAdapter(mSameColleageAdapter);
		// mSameColleageListView.setOnItemClickListener(this);
		// initSameColleageListView();

		// mPostGraduateListView =
		// (PullToRefreshListView)mPostView.findViewById(R.id.post_graduate_listview);
		mPostGraduateUser = new ArrayList<User>();
		if (mUser.getRole() != 0) {
			mPostGraduateUser.add(user);
		}
		mPostGraduateAdapter = new UserAdapter(mContext, mPostGraduateUser);
		// mPostGraduateListView.setAdapter(mPostGraduateAdapter);
		// mPostGraduateListView.setOnItemClickListener(this);
		// initPostGraduateListView();

		mFellowView = inflater.inflate(R.layout.viewlayout, null);
		mFellowListview = (PullToRefreshListView) mFellowView
				.findViewById(R.id.search_result_listview);
		mViews.add(mFellowView);
		mFellowUser = new ArrayList<User>();
		mFellowUser.add(user);
		mFellowAdapter = new UserAdapter(mContext, mFellowUser);
		mFellowListview.setAdapter(mFellowAdapter);
		mFellowListview.setOnItemClickListener(this);
		initFellowListView();

		mMatesView = inflater.inflate(R.layout.viewlayout, null);
		mMatesListview = (PullToRefreshListView) mMatesView
				.findViewById(R.id.search_result_listview);
		mViews.add(mMatesView);
		mMatesUser = new ArrayList<User>();
		mMatesUser.add(user);
		mMatesAdapter = new UserAdapter(mContext, mMatesUser);
		mMatesListview.setAdapter(mMatesAdapter);
		mMatesListview.setOnItemClickListener(this);
		initMatesListView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_browser:
			if (mCurrIndex != 0) {
				imageAni(0);
				mViewPager.setCurrentItem(0);
			}
			break;
		case R.id.search_classmates:
			if (mCurrIndex != 3) {
				imageAni(3);
				mViewPager.setCurrentItem(3);
			}
			break;
		case R.id.search_fellow:
			if (mCurrIndex != 2) {
				imageAni(2);
				mViewPager.setCurrentItem(2);
			}
			break;
		case R.id.search_post:
			if (mCurrIndex != 1) {
				imageAni(1);
				mViewPager.setCurrentItem(1);

			}
			break;
		case R.id.layout_qq:
			Platform plat = ShareSDK.getPlatform(QQ.NAME);
			share(plat);
			break;
		case R.id.layout_weixin:
			Platform plat1 = ShareSDK.getPlatform(Wechat.NAME);
			shareWe(plat1);
			break;
		default:
			break;
		}
	}

	private void share(final Platform plat) {
		new Thread() {
			public void run() {
				ShareParams sp = new ShareParams();
				sp.setTitle("邀请你加入\n  考研人一起来吧");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText("[超级考研群app]\n大视野的考研神器,最实用的考研助手");
				sp.setSite("超级考研群");
				sp.setImageUrl("http://www.yifulou.cn:8080/picture/logo.png");
				sp.setSiteUrl("http://www.yifulou.cn:8180/");
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("邀请你加入\n  考研人一起来吧");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText("[超级考研群app]\n大视野的考研神器,最实用的考研助手");
				sp.setUrl("http://www.yifulou.cn:8180/");
				sp.shareType = Platform.SHARE_TEXT;
				plat.share(sp);
			}
		}.start();

	}

	public void getPostGraduates() {
		if (mPostUser.size() <= 1) {
			getPostGraduates(1, 0);
		}
	}

	// target 0同专业 1同学院 2研究生3全部
	public void getPostGraduates(int page, int target) {
		String url = UrlUtils.GET_POST_GRADUATES_URL + "user.email="
				+ mUser.getEmail() + "&page=" + page + "&rp=" + SIZE
				+ "&user.aim.sid=" + mUser.getRSid() + "&user.aim.ceid="
				+ mUser.getRCid() + "&user.aim.mid=" + mUser.getRmid()
				+ "&user.longitude=" + LocationUtils.getLongtitude()
				+ "&user.latitude=" + LocationUtils.getLatitude() + "&target="
				+ target;
		if (1 == page) {
			mContext.startWaitingDialog();
		}
		switch (target) {
		case 1:
			new NetworkTask().execute(this, TagUtils.GET_SAME_MAJOR_TAG, url);
			break;
		case 2:
			new NetworkTask()
					.execute(this, TagUtils.GET_SAME_COLLEAGE_TAG, url);
			break;
		case 3:
			new NetworkTask().execute(this, TagUtils.GET_GRADUATE_TAG, url);
			break;
		case 0:
			new NetworkTask()
					.execute(this, TagUtils.GET_POST_GRADUATE_TAG, url);
			break;
		default:
			break;
		}
	}

	public void getFellow() {
		if (mFellowUser.size() <= 1) {
			getFellow(1);
		}
	}

	public void getBrowser() {
		if (mBrowserUser.size() <= 1) {
			getBrowser(1);
		}
	}

	/**
	 * 获取校友
	 */
	public void getClassmates() {
		if (mMatesUser.size() <= 1) {
			getMates(1);
		}
	}

	public void getBrowser(int page) {
		User user = KygroupApplication.mUser;
		String url = UrlUtils.GET_BROWSER_URL + "user.email=" + user.getEmail()
				+ "&page=" + page + "&rp=" + SIZE;
		if (1 == page) {
			mContext.startWaitingDialog();
		}
		new NetworkTask().execute(this, TagUtils.GET_BROWSER_USER_TAG, url);
	}

	public void getFellow(int page) {
		User user = KygroupApplication.mUser;
		String url = UrlUtils.GET_FELLOW_URL + "user.email=" + user.getEmail()
				+ "&page=" + page + "&rp=" + SIZE;
		if (1 == page) {
			mContext.startWaitingDialog();
		}
		new NetworkTask().execute(this, TagUtils.GET_FELLOW_TAG, url);
	}

	public void getMates(int page) {
		User user = KygroupApplication.mUser;
		if (!StringUtils.isEmpty(user.getECollege())) {
			String url = UrlUtils.GET_MATES_URL + "user.email="
					+ user.getEmail() + "&page=" + page + "&rp=" + SIZE;
			if (1 == page) {
				mContext.startWaitingDialog();
			}
			new NetworkTask().execute(this, TagUtils.GET_MATES_TAG, url);
		} else if (mTipCount < 3) {
			mTipCount++;
			Toast.makeText(mContext, R.string.add_msg, 2500).show();
		}
	}

	public void setLoadMore(int flag) {
		mLoadMore.setVisibility(flag);
	}

	public int getFellowPage() {
		return mFellowPage;
	}

	public void setFellowPage(int fellowPage) {
		mFellowPage = fellowPage;
	}

	public int getPostPage() {
		return mPostPage;
	}

	public void setPostPage(int postPage) {
		mPostPage = postPage;
	}

	public int getMatesPage() {
		return mMatesPage;
	}

	public void setMatesPage(int matesPage) {
		mMatesPage = matesPage;
	}

	public void setBrowserPage(int browserPage) {
		mBrowserPage = browserPage;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Object obj = parent.getAdapter().getItem(position);
		if (obj instanceof User) {
			User user = (User) obj;
			if (user.getEmail().equals(KygroupApplication.mUser.getEmail())) {
				mContext.performClickFriends(4);
			} else {
				Intent intent = new Intent(mContext, PersonDetailActivity.class);
				intent.putExtra("user", user);
				mContext.startActivityForResult(intent, 100);
			}
		} else if (obj instanceof String) {
			if (mPosition != position) {
				mPosition = position;
				if (mPosition == 1) {
					mCurPostGetData = mMajorGetData;
					mCurPostPage = mSameMajorPage;
					CUR_POST_PAGE = SAME_MAJOE_PAGE;
					if (null == mSameMajorUser || mSameMajorUser.size() <= 1) {
						getPostGraduates(mSameMajorPage, 1);
					}
					mPostListview.setAdapter(mSameMajorAdapter);
				} else if (mPosition == 2) {
					mCurPostGetData = mColleageGetData;
					mCurPostPage = mSameColleagePage;
					CUR_POST_PAGE = SAME_COLLEAGE_PAGE;
					if (null == mSameColleageUser
							|| mSameColleageUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 2);
					}
					mPostListview.setAdapter(mSameColleageAdapter);
				} else if (mPosition == 3) {
					mCurPostGetData = mGraduateGetData;
					mCurPostPage = mPostGraduatePage;
					CUR_POST_PAGE = POST_GRADUATE_PAGE;
					if (null == mPostGraduateUser
							|| mPostGraduateUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 3);
					}
					mPostListview.setAdapter(mPostGraduateAdapter);
				} else if (mPosition == 0) {
					mCurPostGetData = mPostGetData;
					mCurPostPage = mPostPage;
					CUR_POST_PAGE = POST_PAGE;
					if (null == mPostUser || mPostUser.size() <= 1) {
						getPostGraduates(mCurPostPage, 0);
					}
					mPostListview.setAdapter(mPostAdapter);
				}
				mPopupWindow.dismiss();
				mAdapter.setPosition(mPosition);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	public void notifyAdapterChanged(User user) {
		if (null != user) {
			String email = user.getEmail();
			int relation = user.getRelation();
			dealRelation(mBrowserUser, mBrowserAdapter, email, relation);
			dealRelation(mPostUser, mPostAdapter, email, relation);
			dealRelation(mFellowUser, mFellowAdapter, email, relation);
			dealRelation(mMatesUser, mMatesAdapter, email, relation);
		}
	}

	public void setPos(int pos) {
		mViewPager.setCurrentItem(pos);
		imageAni(pos);
	}

	@Override
	public void changeAim() {
		// TODO Auto-generated method stub
		mFellowUser.clear();
		mPostUser.clear();
		mMatesUser.clear();
		mFellowUser.add(KygroupApplication.mUser);
		mPostUser.add(KygroupApplication.mUser);
		mMatesUser.add(KygroupApplication.mUser);
		mPostAdapter.notifyDataSetChanged();
		mFellowAdapter.notifyDataSetChanged();
		mMatesAdapter.notifyDataSetChanged();
		POST_PAGE = 1;
		MATES_PAGE = 1;
		FELLOW_PAGE = 1;
		POST_TOTAL = 0;
		SAME_MAJOE_PAGE = 1;
		SAME_COLLEAGE_PAGE = 1;
		POST_GRADUATE_PAGE = 1;
		mSameMajorUser.clear();
		mSameColleageUser.clear();
		mPostGraduateUser.clear();
		mSameMajorUser.add(KygroupApplication.mUser);
		mSameColleageUser.add(KygroupApplication.mUser);
		mPostGraduateUser.add(KygroupApplication.mUser);
		mPostAdapter.notifyDataSetChanged();
		mFellowAdapter.notifyDataSetChanged();
		mMatesAdapter.notifyDataSetChanged();
		setPos(0);
	}

	private void removeUser(List<User> users, UserAdapter adapter, String email) {
		if (null != users) {
			for (User user : users) {
				if (user.getEmail().equals(email)) {
					users.remove(user);
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}

	public void deleteBlackList(String email) {
		removeUser(mBrowserUser, mBrowserAdapter, email);
		removeUser(mPostUser, mPostAdapter, email);
		removeUser(mFellowUser, mFellowAdapter, email);
		removeUser(mMatesUser, mMatesAdapter, email);
		removeUser(mSameMajorUser, mSameMajorAdapter, email);
		removeUser(mSameColleageUser, mSameColleageAdapter, email);
		removeUser(mPostGraduateUser, mPostGraduateAdapter, email);
	}

	public void dealRelation(List<User> users, UserAdapter adapter,
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

	public void regetMessage() {
		MATES_PAGE = 1;
		FELLOW_PAGE = 1;
		mMatesUser.clear();
		mFellowUser.clear();
		mFellowPage = 1;
		mMatesPage = 1;
		mFellowUser.add(KygroupApplication.mUser);
		mMatesUser.add(KygroupApplication.mUser);
		// mFellowListview.setPullLoadEnable(true);
		// mMatesListview.setPullLoadEnable(true);
		if (null != mFellowAdapter) {
			mFellowAdapter.notifyDataSetChanged();
		}
		if (null != mMatesAdapter) {
			mMatesAdapter.notifyDataSetChanged();
		}
	}

	private void initBrowserListView() {
		mBrowserListview
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (!mBrowserGetData && mBrowserPage <= BROWSER_PAGE) {
							mBrowserGetData = true;
							// getBrowser(mBrowserPage);
						}
					}
				});
	}

	// private void initPostListView(){
	// mPostListview.setOnLastItemVisibleListener(new
	// OnLastItemVisibleListener() {
	// @Override
	// public void onLastItemVisible() {
	// // TODO Auto-generated method stub
	// if(!mPostGetData && mPostPage <= POST_PAGE){
	// mPostGetData = true;
	// getPostGraduates(mPostPage,0);
	// setLoadMore(View.VISIBLE);
	// }
	// }
	// });
	// }

	private void initPostListView() {
		mPostListview
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (!mCurPostGetData && mCurPostPage <= CUR_POST_PAGE) {
							mCurPostGetData = true;
							getPostGraduates(mCurPostPage, mPosition);
							setLoadMore(View.VISIBLE);
						}
					}
				});
	}

	private void initMatesListView() {
		mMatesListview
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (!mMateGetData && mMatesPage <= MATES_PAGE) {
							mMateGetData = true;
							getMates(mMatesPage);
							setLoadMore(View.VISIBLE);
							;
						}
					}
				});
	}

	private void initFellowListView() {
		mFellowListview
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (!mFellowGetData && mFellowPage <= FELLOW_PAGE) {
							mFellowGetData = true;
							getFellow(mFellowPage);
							setLoadMore(View.VISIBLE);
						}
					}
				});
	}

	// private void initSameMajorListView(){
	// mSameMajorListView.setOnLastItemVisibleListener(new
	// OnLastItemVisibleListener() {
	// @Override
	// public void onLastItemVisible() {
	// // TODO Auto-generated method stub
	// if(!mMajorGetData && mSameMajorPage <= SAME_MAJOE_PAGE){
	// mMajorGetData = true;
	// getPostGraduates(mSameMajorPage,1);
	// setLoadMore(View.VISIBLE);
	// }
	// }
	// });
	// }

	// private void initSameColleageListView(){
	// mSameColleageListView.setOnLastItemVisibleListener(new
	// OnLastItemVisibleListener() {
	// @Override
	// public void onLastItemVisible() {
	// // TODO Auto-generated method stub
	// if(!mColleageGetData && mSameColleagePage <= SAME_COLLEAGE_PAGE){
	// mColleageGetData = true;
	// getPostGraduates(mSameColleagePage,2);
	// setLoadMore(View.VISIBLE);
	// }
	// }
	// });
	// }

	// private void initPostGraduateListView(){
	// mPostGraduateListView.setOnLastItemVisibleListener(new
	// OnLastItemVisibleListener() {
	// @Override
	// public void onLastItemVisible() {
	// // TODO Auto-generated method stub
	// if(!mGraduateGetData && mPostGraduatePage <= POST_GRADUATE_PAGE){
	// mGraduateGetData = true;
	// getPostGraduates(mPostGraduatePage,3);
	// setLoadMore(View.VISIBLE);
	// }
	// }
	// });
	// }

	private void initPopupWindow() {
		String[] lists = mContext.getResources().getStringArray(
				R.array.select_post);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.mail_listview, null);
		view.setBackgroundColor(Color.WHITE);
		mSelectListView = (ListView) view.findViewById(R.id.mail_listview);
		mSelectListView.setOnItemClickListener(this);
		mAdapter = new ScanBbsAdapter(mContext, lists, 0);
		mSelectListView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(view, 180, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	private void setRightBtn() {
		mContext.setRightBtnText(R.string.select);
		mContext.setRightBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int width = DeviceUtils.getDisplayWidth(mContext) - 180;
				mPopupWindow.showAsDropDown(mContext.mLayout, width, 0);
			}
		});
	}

}
