package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

public class MyFriendsActivity extends Activity implements IBindData,
		OnItemClickListener {
	public static final String admin = "admin@qq.com";
	public static int FRIENDS_PAGE = 1;
	public static int FRIENDS_TOTAL = 0;
	public boolean mIsGetData = false;
	private static final int SIZE = 10;
	private int mCurPage = 1;
	private User mUser;
	private ArrayList<User> mUsers;
	private UserAdapter mAdapter;
	// private ScrollListener mScrollListener;
	private PullToRefreshListView mListview;
	private LinearLayout mMoreLayout;
	private LinearLayout layout_kefu;
	public Dialog mDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_friends);
		initView();
		initData();
		initListView();
	}

	private void initData() {
		mUser = KygroupApplication.mUser;
		mUsers = new ArrayList<User>();
		mAdapter = new UserAdapter(this, mUsers, 1);
		mListview.setAdapter(mAdapter);
		startWaitingDialog();
		mListview.setVisibility(View.GONE);
		getData();
	}

	private void initView() {
		mListview = (PullToRefreshListView) findViewById(R.id.search_result_listview);
		mListview.setOnItemClickListener(this);
		// mScrollListener = new ScrollListener();
		// mListview.setOnScrollListener(mScrollListener);
		mMoreLayout = (LinearLayout) findViewById(R.id.search_loadmore_layout);
		layout_kefu = (LinearLayout) findViewById(R.id.layout_kefu);
		layout_kefu.setVisibility(View.VISIBLE);
		layout_kefu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyFriendsActivity.this,
						ChatActivity1.class);
				intent.putExtra("user_name", "研师兄");
				intent.putExtra("toChatUsername", "yan");
				startActivity(intent);
			}
		});
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
			// Intent intent = new Intent(this, UserActivity.class);
			Intent intent = new Intent(this, PersonDetailActivity.class);

			intent.putExtra("user", user);
			this.startActivity(intent);
		}
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub

		switch (tag) {
		case TagUtils.GET_FRIENDS_USER_TAG: {
			closeWaitingDialog();
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

}
