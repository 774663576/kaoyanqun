package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.umeng.analytics.MobclickAgent;

public class BlacklistActivity extends BaseActivity implements IBindData,
		OnItemClickListener {
	public static int POST_PAGE = 1;
	public static int POST_TOTAL = 0;
	private int mCurPage = 1;
	private PullToRefreshListView mBlackListView;
	private UserAdapter mBlacklistAdapter;
	private ArrayList<User> mBlackUsers;
	private User mCancelUser;
	private boolean mIsGetData = false;
	private LinearLayout mMoreLayout;

	// private ScrollListener mScrollListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListView();
	}

	private void initView() {
		setTitleText(R.string.blacklist);
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		mBlackListView = (PullToRefreshListView) findViewById(R.id.search_result_listview);
		mMoreLayout = (LinearLayout) findViewById(R.id.search_loadmore_layout);
		// mScrollListener = new ScrollListener();
		// mBlackListView.setOnScrollListener(mScrollListener);
		mBlackUsers = new ArrayList<User>();
		mBlacklistAdapter = new UserAdapter(this, mBlackUsers);
		mBlackListView.setAdapter(mBlacklistAdapter);
		mBlackListView.setOnItemClickListener(this);
		mBlackListView.setVisibility(View.GONE);
	}

	private void initData() {
		mUser = KygroupApplication.mUser;
		getBlacklist();
	}

	private void getBlacklist() {
		startWaitingDialog();
		String url = UrlUtils.BLACKLIST_URL + "user.email=" + mUser.getEmail()
				+ "&page=" + mCurPage + "&rp=" + 10;
		new NetworkTask().execute(this, TagUtils.BLACKLIST_TAG, url);
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(this).inflate(R.layout.viewlayout, null);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		if (tag == TagUtils.BLACKLIST_TAG) {
			showBlacklist(obj);
		} else if (tag == TagUtils.CANCEL_BLACK) {
			closeWaitingDialog();
			if (obj instanceof Boolean && ((Boolean) obj) == true) {
				mBlackUsers.remove(mCancelUser);
				mBlacklistAdapter.notifyDataSetChanged();
			}
		}
		return null;
	}

	private void showBlacklist(Object obj) {
		closeWaitingDialog();
		mMoreLayout.setVisibility(View.GONE);
		if (null != obj) {
			ArrayList<User> users = (ArrayList<User>) obj;
			mBlackUsers.addAll(users);
			mBlacklistAdapter.notifyDataSetChanged();
			mBlackListView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mCancelUser = (User) parent.getAdapter().getItem(position);
		showDialog();
	}

	public void cancelBlack() {
		if (null != mCancelUser) {
			String url = UrlUtils.CANCEL_BLACK + "?user.email="
					+ mUser.getEmail() + "&friend.email="
					+ mCancelUser.getEmail();
			startWaitingDialog();
			new NetworkTask().execute(this, TagUtils.CANCEL_BLACK, url);
		}
	}

	public void showDialog() {
		Builder builder = new Builder(this);
		builder.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				cancelBlack();
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.make_sure_cancel);
		builder.create().show();
	}

	private void initListView() {
		mBlackListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						// TODO Auto-generated method stub
						if (!mIsGetData && mCurPage <= POST_PAGE) {
							mIsGetData = true;
							getBlacklist();
							mMoreLayout.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("BlacklistActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("BlacklistActivity");
		MobclickAgent.onPause(this);
	}
}
