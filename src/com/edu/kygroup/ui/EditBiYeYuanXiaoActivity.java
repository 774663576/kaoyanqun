package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.keygroup.selectshcool.XueYuan;
import com.edu.kygroup.R;
import com.edu.kygroup.R.id;
import com.edu.kygroup.R.layout;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class EditBiYeYuanXiaoActivity extends BaseActivity implements
		OnItemClickListener, IBindData {
	private ListView list;
	private String schoolID = "";// 学校ID
	private List<XueYuan> xueyuanLists = new ArrayList<XueYuan>();

	private MyAdapter adapter;

	private String colleage_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		schoolID = getIntent().getStringExtra("unikey");
		setLeftBtnVisibility(View.GONE);
		list = (ListView) findViewById(R.id.list);
		adapter = new MyAdapter();
		list.setAdapter(adapter);
		setTitleText(getIntent().getStringExtra("uni"));
		setListener();
		getData();
	}

	private void setListener() {
		list.setOnItemClickListener(this);
	}

	private void getData() {

		String url = UrlUtils.GET_COLLEAGE_URL + "sid=" + schoolID;
		url += "&kind=1";
		startWaitingDialog();
		new NetworkTask().execute(this, TagUtils.GET_COLLEAGE_TAG, url);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		closeWaitingDialog();
		if (null != obj) {
			@SuppressWarnings("unchecked")
			ArrayList<XueYuan> lists = (ArrayList<XueYuan>) obj;
			if (lists.size() > 0) {
				xueyuanLists.clear();
				xueyuanLists = lists;
				adapter.notifyDataSetChanged();
			} else {
				ToastUtils.showShortToast("暂时没有数据");

			}
		} else {
			ToastUtils.showShortToast("失败");

		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		KygroupApplication.mFlag = 3;
		Intent intent = new Intent(this, YearsActivity.class);
		intent.putExtra("edit", true);
		startActivityForResult(intent, 4);
		colleage_id = xueyuanLists.get(position).getmId();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		data.putExtra("colleage_id", colleage_id);
		setResult(300, data);
		finish();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_edit_bi_ye_yuan_xiao, null);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return xueyuanLists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater
						.from(EditBiYeYuanXiaoActivity.this).inflate(
								R.layout.zhuanye_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = xueyuanLists.get(position).getXueYuanName();
			holder.text.setText(string);
			return convertView;
		}

	}

	class ViewHolder {
		TextView text;
	}
}
