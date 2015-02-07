package com.edu.kygroup.ui;

import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.PostersAdater;
import com.edu.kygroup.adapter.PostersAdater.OnShareResponseListener;
import com.edu.kygroup.domin.Poster;
import com.edu.kygroup.domin.Poster.Topic;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyPostActivity extends BaseActivity implements
		OnItemClickListener, IBindData, OnShareResponseListener {
	private ListView mPostListView;
	private TextView mCountView;
	private PostersAdater mMajorAdapter;
	private User mUser;
	private ArrayList<Topic> mTopics;
	private int mTotalPage;
	private int mPage = 1;
	private boolean mIsGetData = false;
	private LinearLayout mMoreLayout;
	private ScrollListener mScrollListener;

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.bbsactivity, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		addListener();
		initData();
		getPosters(1);
	}

	public void initView() {
		setTitleText(R.string.my_posts);
		mPostListView = (ListView) findViewById(R.id.bbs_listview);
		mMoreLayout = (LinearLayout) findViewById(R.id.search_loadmore_layout);
		mScrollListener = new ScrollListener();
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);

	}

	private void addListener() {
		mPostListView.setOnScrollListener(mScrollListener);
		// mPostListView.setOnItemClickListener(this);
	}

	public void initData() {
		mUser = KygroupApplication.mUser;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Object obj = parent.getAdapter().getItem(position);
		if (obj instanceof Topic) {
			Topic topic = (Topic) obj;
			startResponseActivity(topic);
		}
	}

	private void startResponseActivity(Topic topic) {
		Intent intent = new Intent(this, ResponseActivity.class);
		intent.putExtra("topic", topic);
		startActivity(intent);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.GET_MY_POSTER_TAG:
			showPosters(obj);
			break;
		default:
			break;
		}
		return null;
	}

	private void showPosters(Object obj) {
		closeWaitingDialog();
		if (null != obj) {
			Poster poster = (Poster) obj;
			if (null != poster) {
				if (null == mTopics) {
					mTopics = new ArrayList<Poster.Topic>();
				}
				int pos = mTopics.size();
				ArrayList<Topic> topics = poster.getTopics();
				for (Topic topic : topics) {
					if (null != topic) {
						topic.getLouzhu().setImage(mUser.getPic());
						topic.getLouzhu().setNickname(mUser.getNickName());
						topic.getLouzhu().setGender(mUser.getGender());
						topic.getLouzhu()
								.setBatchelorschool(mUser.getESchool());
						topic.getLouzhu().setEmail(mUser.getEmail());
					}
				}
				mTopics.addAll(poster.getTopics());
				if (null == mMajorAdapter) {
					mMajorAdapter = new PostersAdater(this, mTopics);
				} else {
					mMajorAdapter.notifyDataSetChanged();
				}
				mPostListView.setAdapter(mMajorAdapter);
				mTotalPage = poster.getTotalpage();
				mPostListView.setSelection(pos);
			}
		}
		mIsGetData = false;
	}

	private void getPosters(int page) {
		if (page == 1) {
			startWaitingDialog();
		}
		StringBuffer buf = new StringBuffer(UrlUtils.GET_MY_POSTER);
		buf.append("email=" + mUser.getEmail());
		buf.append("&page=" + page);
		buf.append("&rp=10");
		String url = buf.toString();
		new NetworkTask().execute(this, TagUtils.GET_MY_POSTER_TAG, url);
	}

	class ScrollListener implements OnScrollListener {
		private int mLastVisiblePosition = 0;// 记录下拉到最后一个item的位置
		private int mLastVisiblePositionY = 0;// 记录最后一个item的Y坐标
		private int mLastItem = 0;
		private boolean mIsTip = false;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if (mPage < mTotalPage
					&& scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				int count = mPostListView.getCount();
				if (mLastItem >= count && !mIsGetData) {
					mPage++;
					mIsGetData = true;
					getPosters(mPage);
				}
			}

			if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
					&& mIsGetData) {
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {// 滚动到底部
					View v = (View) view.getChildAt(view.getChildCount() - 1);// 取得最后一个Item的View
					int[] location = new int[2];// 保存最后一个Item的绝对坐标数组
					v.getLocationOnScreen(location);// 获取最后一个Item在整个屏幕内的绝对坐标
					int y = location[1];// 取得最后一个Item的Y坐标
					if (view.getLastVisiblePosition() != mLastVisiblePosition
							&& mLastVisiblePositionY != y) {// 第一次拖至底部
						mLastVisiblePosition = view.getLastVisiblePosition();
						mLastVisiblePositionY = y;
						mMoreLayout.setVisibility(View.VISIBLE);
						return;
					} else if (view.getLastVisiblePosition() == mLastVisiblePosition
							&& mLastVisiblePositionY == y) {// 第二次拖至底部
						mMoreLayout.setVisibility(View.VISIBLE);
					}
				}
				// 未滚动到底部，第二次拖至底部都初始化
				mLastVisiblePosition = 0;
				mLastVisiblePositionY = 0;
			}
			// 判断可见的数量是否等于总数量如果等于，则提示没有数据了
			if (mLastItem == mTotalPage && !mIsTip) {
				ToastUtils.showShortToast(R.string.havenodata);
				mIsTip = true;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			mLastItem = firstVisibleItem + visibleItemCount;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MyPostActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MyPostActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public void OnResponse(Topic topic) {
		// TODO Auto-generated method stub
		startResponseActivity(topic);
	}

	@Override
	public void OnShare(Topic topic) {
		// TODO Auto-generated method stub

	}
}
