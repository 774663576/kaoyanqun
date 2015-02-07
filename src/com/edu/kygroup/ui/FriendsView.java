/**
 * 工程名: KyGroup
 * 文件名: FriendsView.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-11-24上午9:48:10
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.UserAdapter;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.pullrefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.edu.pullrefresh.PullToRefreshListView;

/**
 * 类名: FriendsView <br/>
 * 功能: TODO 好友. <br/>
 * 日期: 2013-11-24 上午9:48:10 <br/>
 * 
 * @author lx
 * @version
 */
public class FriendsView implements IBindData, OnItemClickListener {
	public static final String admin = "admin@qq.com";
	public static int FRIENDS_PAGE = 1;
	public static int FRIENDS_TOTAL = 0;
	public boolean mIsGetData = false;
	private static final int SIZE = 10;
	private int mCurPage = 1;
	private User mUser;
	private ArrayList<User> mUsers;
	private HomeActivity mContext;
	private View mView;
	private UserAdapter mAdapter;
	// private ScrollListener mScrollListener;
	private PullToRefreshListView mListview;
	private LinearLayout mMoreLayout;
	private LinearLayout layout_kefu;

	public FriendsView(HomeActivity context) {
		mContext = context;
		initView();
		initData();
		initListView();
	}

	private void initData() {
		mUser = KygroupApplication.mUser;
		mUsers = new ArrayList<User>();
		mAdapter = new UserAdapter(mContext, mUsers);
		mListview.setAdapter(mAdapter);
		mContext.startWaitingDialog();
		mListview.setVisibility(View.GONE);
		getData();
	}

	private void initView() {
		mView = LayoutInflater.from(mContext)
				.inflate(R.layout.viewlayout, null);
		mListview = (PullToRefreshListView) mView
				.findViewById(R.id.search_result_listview);
		mListview.setOnItemClickListener(this);
		// mScrollListener = new ScrollListener();
		// mListview.setOnScrollListener(mScrollListener);
		mMoreLayout = (LinearLayout) mView
				.findViewById(R.id.search_loadmore_layout);
		layout_kefu = (LinearLayout) mView.findViewById(R.id.layout_kefu);
		layout_kefu.setVisibility(View.VISIBLE);
		layout_kefu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, ChatActivity1.class);
				intent.putExtra("user_name", "研师兄");
				intent.putExtra("toChatUsername", "yan");
				mContext.startActivity(intent);
			}
		});
	}

	public View getView() {
		mListview.setAdapter(mAdapter);
		mContext.setTitleBarVisibility(View.VISIBLE);
		return mView;
	}

	public void getData() {
		String url = UrlUtils.GET_FRIENDS_USER + "user.email="
				+ mUser.getEmail() + "&page=" + mCurPage + "&rp=" + SIZE;
		new NetworkTask().execute(this, TagUtils.GET_FRIENDS_USER_TAG, url);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		User user = (User) parent.getAdapter().getItem(position);
		if (null != user && !user.getEmail().equals(admin)) {
			// Intent intent = new Intent(mContext, UserActivity.class);
			Intent intent = new Intent(mContext, PersonDetailActivity.class);

			intent.putExtra("user", user);
			mContext.startActivity(intent);
		}
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub

		switch (tag) {
		case TagUtils.GET_FRIENDS_USER_TAG: {
			((HomeActivity) mContext).closeWaitingDialog();
			mMoreLayout.setVisibility(View.GONE);
			mIsGetData = false;
			if (null != obj) {
				ArrayList<User> user = (ArrayList<User>) obj;
				if (user.size() >= 10) {
					mCurPage++;
				}
				if (mCurPage == 1) {
					mUsers.clear();
				}
				mUsers.addAll(user);
				mAdapter.notifyDataSetChanged();
				mListview.setVisibility(View.VISIBLE);
			}
		}
			break;
		default:
			break;
		}
		return null;
	}

	public void dealRelation(ArrayList<User> users, UserAdapter adapter,
			String email, int relation, User u) {
		if (null != users) {
			if (relation == 0) {
				for (User user : users) {
					if (user.getEmail().equals(email)) {
						users.remove(user);
						adapter.notifyDataSetChanged();
						break;
					}
				}
			} else if (relation == 1) {
				for (User user : users) {
					if (user.getEmail().equals(email)) {
						return;
					}
				}
				users.add(u);
				adapter.notifyDataSetChanged();
			}
		}
	}

	public void notifyAdapterChanged(User user) {
		String email = user.getEmail();
		int relation = user.getRelation();
		dealRelation(mUsers, mAdapter, email, relation, user);
	}

	public void deleteBlackList(String email) {
		dealRelation(mUsers, mAdapter, email, 0, null);
	}

	private void initListView() {
		mListview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				if (!mIsGetData && mCurPage <= FRIENDS_PAGE) {
					mIsGetData = true;
					getData();
					mMoreLayout.setVisibility(View.VISIBLE);
				}
			}
		});
	}
}
