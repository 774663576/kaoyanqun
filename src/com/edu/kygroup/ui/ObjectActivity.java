package com.edu.kygroup.ui;

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

import com.edu.kygroup.R;
import com.edu.kygroup.utils.Util;

public class ObjectActivity extends BaseActivity implements OnItemClickListener {
	private ListView mListView;

	private String[] objects;

	private int groupPosition;
	private int childPosition;

	private Adapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		groupPosition = getIntent().getIntExtra("groupPosition", 0);
		childPosition = getIntent().getIntExtra("childPosition", 0);
		int groupArr[] = Util.objectList.get(groupPosition);
		objects = getResources().getStringArray(groupArr[childPosition]);
		initView();
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText("专业");
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setOnItemClickListener(this);
		adapter = new Adapter();
		mListView.setAdapter(adapter);
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_object, null);
	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return objects.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(ObjectActivity.this).inflate(
						R.layout.object_zhuanye, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = objects[position];
			holder.text.setText(string);
			return convertView;
		}

	}

	class ViewHolder {
		TextView text;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		String majorID = getResources().getStringArray(
				Util.objec_code_tList.get(groupPosition)[childPosition])[position];
		Intent intent = new Intent();
		intent.putExtra("major_id", majorID);
		intent.setClass(this, SchoolListActivity.class);
		startActivity(intent);
	}
}
