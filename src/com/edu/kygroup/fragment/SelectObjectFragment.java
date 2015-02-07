package com.edu.kygroup.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.ui.ObjectActivity;
import com.edu.kygroup.utils.Util;

public class SelectObjectFragment extends Fragment implements
		OnChildClickListener {
	private ExpandableListView list;

	private List<Map<String, Integer>> mList;
	private CityExpandableAdapter adapter;
	private String cityArray[];

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			list.setAdapter(adapter);

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.select_object_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		cityArray = getResources().getStringArray(R.array.subject);
		list = (ExpandableListView) getView().findViewById(R.id.list);
		list.setGroupIndicator(null);
		list.setOnChildClickListener(this);
		list.setCacheColorHint(0);
		adapter = new CityExpandableAdapter(getActivity());
		getData();
	}

	private void getData() {
		new Thread() {
			public void run() {
				mList = new ArrayList<Map<String, Integer>>();
				for (int i = 0; i < cityArray.length; i++) {
					Map<String, Integer> map = new HashMap<String, Integer>();
					map.put("name", Util.subjectArray[i]);
					map.put("id", Util.subjectCodeArray[i]);
					mList.add(map);
				}
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	public class CityExpandableAdapter extends BaseExpandableListAdapter {
		Activity activity;

		public CityExpandableAdapter(Activity a) {
			activity = a;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return getActivity().getResources().getStringArray(
					mList.get(groupPosition).get("name"))[childPosition];
		}

		public String getChildCode(int groupPosition, int childPosition) {
			return getActivity().getResources().getStringArray(
					mList.get(groupPosition).get("id"))[childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return getActivity().getResources().getStringArray(
					mList.get(groupPosition).get("name")).length;
		}

		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.xuanyuan_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = (String) getChild(groupPosition, childPosition);

			holder.text.setText(string);
			return convertView;

		}

		public Object getGroup(int groupPosition) {
			return cityArray[groupPosition];
		}

		public int getGroupCount() {
			return cityArray.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.zhuanye_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = cityArray[groupPosition];
			holder.text.setText(string);
			return convertView;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	class ViewHolder {
		TextView text;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Intent intent = new Intent();
		intent.putExtra("groupPosition", groupPosition);
		intent.putExtra("childPosition", childPosition);
		intent.setClass(getActivity(), ObjectActivity.class);
		startActivity(intent);
		return false;
	}
}
