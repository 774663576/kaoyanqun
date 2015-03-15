package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
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
import com.edu.kygroup.adapter.TiaoJiaoYuanXiaoAdapter;
import com.edu.kygroup.domin.TiaoJiYuanXiao;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class Tiaoji2015Fragment extends Fragment {
	private ListView mListView;
	private TiaoJiaoYuanXiaoAdapter adapter;
	private List<TiaoJiYuanXiao> lists = new ArrayList<TiaoJiYuanXiao>();
	private StringBuffer share_content = new StringBuffer();
	private String mid = "";
	private String major = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mid = getArguments().getString("mid");
		major = getArguments().getString("major");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_tiao_ji_yuan_xiao, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setValue();
		getData(mid);
	}

	private void initView() {
		mListView = (ListView) getView().findViewById(R.id.listView1);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), WebActivity.class);
				intent.putExtra("url", lists.get(arg2).getUrl());
				intent.putExtra("title", "调剂内容");
				startActivity(intent);
			}
		});
	}

	private void setValue() {
		adapter = new TiaoJiaoYuanXiaoAdapter(lists, getActivity());
		mListView.setAdapter(adapter);
	}

	public String getShareContent() {
		return share_content.toString();

	}

	private void getData(String mid) {
		startWaitingDialog();
		String url = UrlUtils.TIAOJIYUANXIAO + "mid=" + mid + "&year=" + 2015;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@SuppressWarnings("unchecked")
			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				if (result != null) {
					lists.addAll((List<TiaoJiYuanXiao>) result);
					adapter.notifyDataSetChanged();
					share_content.append("【超级考研群app】2015年考研招生[" + major
							+ "]接收调剂院校名单：");
					for (TiaoJiYuanXiao info : lists) {
						share_content.append(info.getSname() + " | "
								+ info.getCename());
						share_content.append("\n");
					}
					share_content.append("【超级考研群app】大视野的考研神器，最实用的考研助手。");

				} else {
					ToastUtils.showShortToast("获取失败");
				}
			}
		});
		task.execute(null, TagUtils.TIAOJIYUANXIAO, url);
	}

	public Dialog mDialog = null;

	public void startWaitingDialog() {
		try {
			if (mDialog == null) {
				mDialog = new Dialog(getActivity(), R.style.waiting);
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
