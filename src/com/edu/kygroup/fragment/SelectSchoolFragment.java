package com.edu.kygroup.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.UniversityAdapter;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.ui.KygroupApplication;
import com.edu.kygroup.ui.SelectColleageActivity;
import com.edu.kygroup.utils.Util;

public class SelectSchoolFragment extends Fragment implements
		OnChildClickListener, OnItemClickListener, OnClickListener {
	private ExpandableListView list;
	private ListView serarchListView;

	private EditText mSearch;
	private Button btn_34, btn_211, btn_985, btn_yanjiushengyuan,
			btn_zhongkeyuan;
	private List<Button> buttons = new ArrayList<Button>();
	private List<Map<String, Integer>> mList;
	private ArrayList<HashMap<String, String>> serarchList = new ArrayList<HashMap<String, String>>();

	private UniversityAdapter mAdapter;

	private CityExpandableAdapter adapter;
	private String cityArray[];

	private String[] mUniversity;
	private String[] mKey;

	private int registerType = 0;
	private int register_role = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			list.setAdapter(adapter);

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerType = getArguments().getInt("registerType");
		register_role = getArguments().getInt("register_role");
		initArr();
	}

	private void initArr() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.select_school_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		btn_34 = (Button) getView().findViewById(R.id.btn_34);
		btn_211 = (Button) getView().findViewById(R.id.btn_211);
		btn_985 = (Button) getView().findViewById(R.id.btn_985);
		btn_yanjiushengyuan = (Button) getView().findViewById(
				R.id.btn_yanjiushengyuan);
		btn_zhongkeyuan = (Button) getView().findViewById(R.id.btn_zhongkeyuan);
		buttons.add(btn_34);
		buttons.add(btn_211);
		buttons.add(btn_985);
		buttons.add(btn_yanjiushengyuan);
		buttons.add(btn_zhongkeyuan);

		mKey = getResources().getStringArray(R.array.key_post);
		mUniversity = getResources().getStringArray(R.array.university_post);
		cityArray = getResources().getStringArray(R.array.unit);
		list = (ExpandableListView) getView().findViewById(R.id.list);
		list.setGroupIndicator(null);
		list.setOnChildClickListener(this);
		list.setCacheColorHint(0);
		adapter = new CityExpandableAdapter(getActivity());
		serarchListView = (ListView) getView().findViewById(
				R.id.search_listView);
		serarchListView.setCacheColorHint(0);
		mSearch = (EditText) getView().findViewById(R.id.university_search);
		if (registerType == 1) {
			mSearch.setHint("选择就读院校");
		} else if (registerType == 2) {
			mSearch.setHint("选择报考院校");
		}
		mAdapter = new UniversityAdapter(getActivity(), serarchList);
		serarchListView.setAdapter(mAdapter);
		setListener();
		getData();
	}

	private void setListener() {
		btn_34.setOnClickListener(this);
		btn_211.setOnClickListener(this);
		btn_985.setOnClickListener(this);
		btn_yanjiushengyuan.setOnClickListener(this);
		btn_zhongkeyuan.setOnClickListener(this);

		serarchListView.setOnItemClickListener(this);
		mSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() == 0) {
					serarchListView.setVisibility(View.GONE);
					list.setVisibility(View.VISIBLE);
					return;
				}
				search(s.toString());
				mAdapter.setSearchText(s.toString());
				mAdapter.notifyDataSetChanged();
				serarchListView.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	private void search(String str) {
		serarchList.clear();
		initSearcjList(str);
	}

	private void initSearcjList(String str) {
		System.out.println("init:::::::::::::" + mKey.length + "      "
				+ mUniversity.length);
		for (int i = 0; i < mKey.length; i++) {
			HashMap<String, String> map = null;
			if (mUniversity[i].contains(str)) {
				map = new HashMap<String, String>();
				map.put("key", mKey[i]);
				map.put("uni", mUniversity[i]);
				serarchList.add(map);
			}
		}
	}

	private void getData() {
		new Thread() {
			public void run() {
				mList = new ArrayList<Map<String, Integer>>();
				for (int i = 0; i < cityArray.length; i++) {
					Map<String, Integer> map = new HashMap<String, Integer>();
					map.put("name", Util.unitArray[i]);
					map.put("id", Util.unitCodeArray[i]);
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
			String code = getChildCode(groupPosition, childPosition);
			if (code.contains("A")) {
				holder.text.setTextColor(getResources().getColor(
						R.color.gray_title));
			} else {
				holder.text.setTextColor(getResources().getColor(
						R.color.title_text_color));
			}
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

	private void intentSelectColleageActivity(String schoolID, String schoolName) {
		Register register = null;
		System.out.println("name;:::::::::::" + schoolID + "      "
				+ schoolName);
		if (registerType != 0) {
			KygroupApplication.mUser.setRSchool(schoolName);
			KygroupApplication.mUser.setRSid(schoolID);
			register = new Register();
			register.setRegister_role(register_role + "");
			register.setRegister_School_name(schoolName);
			register.setRegister_Sid(schoolID);
		}
		Intent intent = new Intent();
		intent.putExtra("registerType", registerType);
		intent.putExtra("uni", schoolName);
		intent.putExtra("unikey", schoolID);
		intent.putExtra("register", register);

		intent.setClass(getActivity(), SelectColleageActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		intentSelectColleageActivity(
				getActivity().getResources().getStringArray(
						mList.get(groupPosition).get("id"))[childPosition],
				getResources().getStringArray(
						mList.get(groupPosition).get("name"))[childPosition]);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		intentSelectColleageActivity(serarchList.get(position).get("key"),
				serarchList.get(position).get("uni"));
	}

	@Override
	public void onClick(View v) {
		Map<String, Integer> map = null;
		switch (v.getId()) {
		case R.id.btn_211:
			cityArray = new String[] { "“211工程”院校" };
			map = new HashMap<String, Integer>();
			map.put("name", R.array.arr_211);
			map.put("id", R.array.arr_211_code);
			setBtnBg(1);

			break;
		case R.id.btn_34:
			cityArray = new String[] { "34所自划线院校" };
			map = new HashMap<String, Integer>();
			map.put("name", R.array.arr_34);
			map.put("id", R.array.arr_34_code);
			setBtnBg(0);

			break;
		case R.id.btn_985:
			cityArray = new String[] { "“985工程”院校" };
			map = new HashMap<String, Integer>();
			map.put("name", R.array.arr_985);
			map.put("id", R.array.arr_985_code);
			setBtnBg(2);

			break;
		case R.id.btn_yanjiushengyuan:
			cityArray = new String[] { "设立研究生院的招生院校 " };
			map = new HashMap<String, Integer>();
			map.put("name", R.array.arr_yanjiushengyuan);
			map.put("id", R.array.arr_yanjiushengyuan_code);
			setBtnBg(3);

			break;
		case R.id.btn_zhongkeyuan:
			cityArray = new String[] { "中国科学院大学（含各研究所）" };
			map = new HashMap<String, Integer>();
			map.put("name", R.array.arr_zhongkeyuan);
			map.put("id", R.array.arr_zhongkeyuan_code);
			setBtnBg(4);
			break;
		default:
			break;
		}
		mList.clear();
		mList.add(map);
		adapter.notifyDataSetChanged();
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i);
		}
	}

	private void setBtnBg(int index) {

		for (Button btn : buttons) {
			btn.setBackgroundResource(R.drawable.category_select);
		}
		buttons.get(index).setBackgroundResource(R.drawable.category);
	}
}
