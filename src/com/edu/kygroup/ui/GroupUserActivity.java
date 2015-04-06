package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.UserAdapter;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.pullrefresh.PullToRefreshListView;

public class GroupUserActivity extends BaseActivity implements IBindData,
		OnItemClickListener {

	private PullToRefreshListView mPostListView;
	private ArrayList<User> mPostUser;
	private UserAdapter mPostGraduateAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		getPostGraduates();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_group_user, null);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText(R.string.search_post_graduate);
		mPostListView = (PullToRefreshListView) findViewById(R.id.search_result_listview);
	}

	private void initData() {
		mPostUser = new ArrayList<User>();
		mPostListView.setOnItemClickListener(this);
		mPostGraduateAdapter = new UserAdapter(this, mPostUser, 1);
		mPostListView.setAdapter(mPostGraduateAdapter);
	}

	public void getPostGraduates() {
		startWaitingDialog();
		String url = UrlUtils.GET_POST_GRADUATES_URL + "user.email="
				+ mUser.getEmail() + "&page=" + 1 + "&rp=" + 8
				+ "&user.aim.sid=" + mUser.getRSid() + "&user.aim.ceid="
				+ mUser.getRCid() + "&user.aim.mid=" + mUser.getRmid()
				+ "&user.longitude=" + LocationUtils.getLongtitude()
				+ "&user.latitude=" + LocationUtils.getLatitude() + "&target="
				+ 1;
		new NetworkTask().execute(this, TagUtils.GET_DETAIL_POST, url);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent(this, PersonDetailActivity.class);
		intent.putExtra("user", mPostUser.get(position));
		startActivityForResult(intent, 100);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object bindData(int tag, Object obj) {
		closeWaitingDialog();
		switch (tag) {
		case TagUtils.GET_DETAIL_POST: {
			if (null != obj) {
				mPostUser.addAll((ArrayList<User>) obj);
				mPostGraduateAdapter.notifyDataSetChanged();
			}
			break;
		}
		default:
			break;
		}
		return null;
	}
}
