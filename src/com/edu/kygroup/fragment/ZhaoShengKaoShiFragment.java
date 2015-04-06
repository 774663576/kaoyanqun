package com.edu.kygroup.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.DizcuzAdapter;
import com.edu.kygroup.domin.Dizcuz;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.ui.BBSActivity1;
import com.edu.kygroup.ui.DizcuzWebViewActivity;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class ZhaoShengKaoShiFragment extends Fragment implements
		OnItemClickListener, IBindData {
	private ListView mListView;

	private List<Dizcuz> lists = new ArrayList<Dizcuz>();

	private FocusInfo mFocusInfo;

	private DizcuzAdapter adpater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.yuanxiaozixun_fragment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFocusInfo = ((BBSActivity1) getActivity()).getmFocusInfo();
		initView();
		setValue();
		getData(1);
	}

	private void initView() {
		mListView = (ListView) getView().findViewById(R.id.listView1);
		setListener();
	}

	private void setListener() {
		mListView.setOnItemClickListener(this);

	}

	private void setValue() {
		adpater = new DizcuzAdapter(lists, getActivity());
		mListView.setAdapter(adpater);
	}

	private void getData(int page) {
		if (page == 1) {
			((BBSActivity1) getActivity()).startWaitingDialog();
		}
		StringBuffer buf = new StringBuffer(UrlUtils.DIZCUZ);
		buf.append("&major.sid=" + mFocusInfo.getmSid());
		buf.append("&major.ceid=" + mFocusInfo.getmCid());
		buf.append("&major.mid=" + mFocusInfo.getmMid());
		buf.append("&page=" + page);
		buf.append("&rp=10&kind=2");
		String url = buf.toString();
		new NetworkTask().execute(this, TagUtils.DIZCUZ, url);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("url", lists.get(position).getUrl());
		intent.putExtra("title", lists.get(position).getTitle());
		intent.setClass(getActivity(), DizcuzWebViewActivity.class);
		startActivity(intent);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object bindData(int tag, Object obj) {
		((BBSActivity1) getActivity()).closeWaitingDialog();

		if (obj == null) {
			ToastUtils.showShortToast("获取失败");
			return null;
		}
		lists.addAll((List<Dizcuz>) obj);
		adpater.notifyDataSetChanged();
		return null;
	}
}
