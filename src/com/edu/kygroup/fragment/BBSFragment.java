package com.edu.kygroup.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.sharesdk.framework.ShareSDK;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.PostersAdater;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.Louzhu;
import com.edu.kygroup.domin.Poster;
import com.edu.kygroup.domin.Poster.Topic;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.ui.BBSActivity1;
import com.edu.kygroup.ui.KygroupApplication;
import com.edu.kygroup.ui.PublicshBbsActivity;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;

public class BBSFragment extends Fragment implements IBindData {

	private static final int MAJOR_FLAG = 3;
	private static final int COLLEAGE_FLAG = 2;
	private static final int SCHOOL_FLAG = 1;

	private ListView mPostListView;
	private LinearLayout mMoreLayout;
	private FocusInfo mFocusInfo;

	private ArrayList<Topic> mMajorTopics;
	private HashMap<String, Topic> mMajorMap;
	private int mMajorTotalPage;
	private int mMajorPage = 1;

	private ArrayList<Topic> mColleageTopics;
	private int mColleageTotalPage;
	private int mColleagePage = 1;

	private ArrayList<Topic> mSchoolTopics;
	private int mSchoolTotalPage;
	private int mSchoolPage = 1;
	private PostersAdater mMajorAdapter;
	private PostersAdater mColleageAdapter;
	private PostersAdater mSchoolAdapter;

	private LinearLayout layout_tishi;

	private User mUser;
	private int mPostion = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.bbs_fragment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFocusInfo = ((BBSActivity1) getActivity()).getmFocusInfo();
		mUser = KygroupApplication.mUser;
		initView();
		setValue();
		getPosters(1);
		initShareSDk();
	}

	private void initShareSDk() {
		ShareSDK.initSDK(getActivity());
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
	}

	private void initView() {
		mPostListView = (ListView) getView().findViewById(R.id.bbs_listview);
		mPostListView.setCacheColorHint(0);
		mMoreLayout = (LinearLayout) getView().findViewById(
				R.id.search_loadmore_layout);
		layout_tishi = (LinearLayout) getView().findViewById(R.id.tishi);
		layout_tishi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("focusInfo", mFocusInfo);
				intent.setClass(getActivity(), PublicshBbsActivity.class);
				startActivityForResult(intent, 100);
			}
		});

	}

	private void setValue() {
		mMajorTopics = new ArrayList<Poster.Topic>();
		mMajorAdapter = new PostersAdater(getActivity(), mMajorTopics);
		mColleageTopics = new ArrayList<Poster.Topic>();
		mColleageAdapter = new PostersAdater(getActivity(), mColleageTopics);
		mSchoolTopics = new ArrayList<Poster.Topic>();
		mSchoolAdapter = new PostersAdater(getActivity(), mSchoolTopics);
		mPostListView.setAdapter(mMajorAdapter);
	}

	public void setPoster(int index) {
		mPostion = index;
		switch (index) {
		case 0:
			if (null == mSchoolTopics || mSchoolTopics.size() <= 0) {
				getPosters(1);
			}
			mPostListView.setAdapter(mSchoolAdapter);
			break;
		case 1:
			if (null == mColleageTopics || mColleageTopics.size() <= 0) {
				getPosters(1);
			}
			mPostListView.setAdapter(mColleageAdapter);
			break;
		case 2:
			if (null == mMajorTopics || mMajorTopics.size() <= 0) {
				getPosters(1);
			}
			mPostListView.setAdapter(mMajorAdapter);
			break;
		default:
			break;
		}
	}

	private void getPosters(int page) {
		if (page == 1) {
			((BBSActivity1) getActivity()).startWaitingDialog();
		}
		StringBuffer buf = new StringBuffer(UrlUtils.GET_POSTER);
		buf.append("&topic.sid=" + mFocusInfo.getmSid());
		if (mPostion > 0) {
			buf.append("&topic.ceid=" + mFocusInfo.getmCid());
		}
		if (mPostion > 1) {
			buf.append("&topic.mid=" + mFocusInfo.getmMid());
		}
		buf.append("&page=" + page);
		buf.append("&rp=10&kind=3");
		String url = buf.toString();
		new NetworkTask().execute(this, TagUtils.GET_POSTER_TAG, url);
	}

	private void showPosters(Object obj) {
		((BBSActivity1) getActivity()).closeWaitingDialog();
		if (null != obj) {
			Poster poster = (Poster) obj;
			if (poster.getFlag() == MAJOR_FLAG) {
				int pos = mMajorTopics.size();
				mMajorTopics.addAll(poster.getTopics());
				mMajorAdapter.notifyDataSetChanged();
				mPostListView.setAdapter(mMajorAdapter);
				mMajorTotalPage = poster.getTotalpage();
				mPostListView.setSelection(pos);
			} else if (poster.getFlag() == COLLEAGE_FLAG) {
				int pos = mColleageTopics.size();
				mColleageTopics.addAll(poster.getTopics());
				mColleageAdapter.notifyDataSetChanged();
				mPostListView.setAdapter(mColleageAdapter);
				mColleageTotalPage = poster.getTotalpage();
				mPostListView.setSelection(pos);
			} else if (poster.getFlag() == SCHOOL_FLAG) {

				int pos = mSchoolTopics.size();
				mSchoolTopics.addAll(poster.getTopics());
				mSchoolAdapter.notifyDataSetChanged();
				mPostListView.setAdapter(mSchoolAdapter);
				mSchoolTotalPage = poster.getTotalpage();
				mPostListView.setSelectionFromTop(pos - 1, 0);
			}
		}
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.TOPIC_RET_TAG:
			break;
		case TagUtils.GET_POSTER_TAG:
			showPosters(obj);
			break;
		default:
			break;
		}
		return null;
	}

	public void addSucPoster(int tid, String time, String content,
			ArrayList<String> picList) {
		Poster poster = new Poster();
		Topic topic = poster.new Topic();
		topic.setCeid(Integer.parseInt(mFocusInfo.getmCid()));
		topic.setCename(mFocusInfo.getmFocusColleage());
		topic.setMid(Integer.parseInt(mFocusInfo.getmMid()));
		topic.setMname(mFocusInfo.getmFocusMajor());
		topic.setSid(Integer.parseInt(mFocusInfo.getmSid()));
		topic.setSname(mFocusInfo.getmFocusSchool());
		topic.setTitle(content);
		topic.setPicsList(picList);
		topic.setTid(tid);
		topic.setTimestamp(time);
		Louzhu louzhu = new Louzhu();
		louzhu.setBatchelorschool(mUser.getESchool());
		louzhu.setEmail(mUser.getEmail());
		louzhu.setGender(mUser.getGender());
		louzhu.setImage(mUser.getPic());
		louzhu.setNickname(mUser.getNickName());
		topic.setLouzhu(louzhu);
		if (null != mMajorTopics) {
			mMajorTopics.add(0, topic);
			mMajorAdapter.notifyDataSetChanged();
		}
		if (null != mColleageTopics) {
			mColleageTopics.add(0, topic);
			mColleageAdapter.notifyDataSetChanged();
		}
		if (null != mSchoolTopics) {
			mSchoolTopics.add(0, topic);
			mSchoolAdapter.notifyDataSetChanged();
		}
	}
}
