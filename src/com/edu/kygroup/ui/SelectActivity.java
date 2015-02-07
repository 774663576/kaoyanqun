package com.edu.kygroup.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.keygroup.selectshcool.XueYuan;
import com.edu.kygroup.R;
import com.edu.kygroup.adapter.UniversityAdapter;
import com.edu.kygroup.domin.Colleage;
import com.edu.kygroup.domin.University;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

public class SelectActivity extends Activity implements IBindData,
		OnGroupClickListener, OnChildClickListener, OnItemClickListener {
	private String[] mUniversity;
	private String[] mKey;
	private ExpandableListView list;
	private ListView mUniversityView;
	private EditText mSearch;
	private TextView mTipView;
	private ArrayList<HashMap<String, String>> mList;
	private UniversityAdapter mAdapter;
	private List<University> uLists = new ArrayList<University>();
	private ExpandableAdapter adapter;
	private String mUniversityName = "";
	private String mUniID = "";
	private String colleage_id = "";
	String mYear = "";

	private Dialog mDialog = null;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_activity);
		initView();
		initData();
		initListviewData();
		addListener();
	}

	@Override
	public Object bindData(int tag, Object obj) {
		closeWaitingDialog();
		if (null != obj) {
			ArrayList<Colleage> lists = (ArrayList<Colleage>) obj;
			if (lists.size() > 0) {

				Intent intent = new Intent(SelectActivity.this,
						ColleageActivity.class);
				intent.putExtra("colleages", (Serializable) lists);
				startActivity(intent);

				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				Toast.makeText(SelectActivity.this, R.string.data_not_enough,
						1500).show();
			}
		} else {
			Toast.makeText(SelectActivity.this, R.string.request_failed, 1500)
					.show();
		}
		return null;
	}

	private void initView() {
		mUniversityView = (ListView) findViewById(R.id.universities);
		list = (ExpandableListView) findViewById(R.id.list);
		list.setGroupIndicator(null);
		list.setOnGroupClickListener(this);
		list.setOnChildClickListener(this);
		mSearch = (EditText) findViewById(R.id.university_search);
		mTipView = (TextView) findViewById(R.id.select_tip);
	}

	private void initListviewData() {
		mAdapter = new UniversityAdapter(this, mList);
		mUniversityView.setAdapter(mAdapter);

	}

	private void initData() {
		adapter = new ExpandableAdapter();
		list.setAdapter(adapter);
		mKey = getResources().getStringArray(R.array.key);
		mUniversity = getResources().getStringArray(R.array.university);
		mTipView.setText(R.string.u_tip1);
		mList = new ArrayList<HashMap<String, String>>();
		initList("");
		new Thread() {
			public void run() {
				for (int i = 0; i < mKey.length; i++) {
					University u = new University();
					u.setuCode(mKey[i]);
					u.setuName(mUniversity[i]);
					uLists.add(u);
				}
				mHandler.sendEmptyMessage(0);
			}
		}.start();

	}

	private void initList(String str) {
		if (null != mKey && null != mUniversity) {
			for (int i = 0; i < mKey.length; i++) {
				HashMap<String, String> map = null;
				if (TextUtils.isEmpty(str)) {
					map = new HashMap<String, String>();
					map.put("key", mKey[i]);
					map.put("uni", mUniversity[i]);
					mList.add(map);
				} else if (mUniversity[i].contains(str)) {
					map = new HashMap<String, String>();
					map.put("key", mKey[i]);
					map.put("uni", mUniversity[i]);
					mList.add(map);
				}
			}
		}
	}

	private void addListener() {
		mSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() == 0) {
					list.setVisibility(View.VISIBLE);
					mUniversityView.setVisibility(View.GONE);
					return;
				}
				search(s.toString());
				mAdapter.setSearchText(s.toString());
				mAdapter.notifyDataSetChanged();
				list.setVisibility(View.GONE);
				mUniversityView.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mUniversityView.setOnItemClickListener(this);
	}

	private void search(String str) {
		mList.clear();
		initList(str);
	}

	public class ExpandableAdapter extends BaseExpandableListAdapter {
		Activity activity;

		public Object getChild(int groupPosition, int childPosition) {
			return uLists.get(groupPosition).getcList().get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return uLists.get(groupPosition).getcList().size();
		}

		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(SelectActivity.this).inflate(
						R.layout.xuanyuan_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = uLists.get(groupPosition).getcList()
					.get(childPosition).getXueYuanName();
			holder.text.setText(string);
			return convertView;

		}

		public Object getGroup(int groupPosition) {
			return uLists.get(groupPosition);
		}

		public int getGroupCount() {
			return uLists.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(SelectActivity.this).inflate(
						R.layout.zhuanye_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = uLists.get(groupPosition).getuName();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("SelectActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("SelectActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		if (uLists.get(arg2).getcList().size() > 0) {
			return false;
		}
		String url = UrlUtils.GET_COLLEAGE_URL + "sid="
				+ uLists.get(arg2).getuCode() + "&kind=1";
		getXueYuan(url, arg2);
		mUniversityName = uLists.get(arg2).getuName();
		mUniID = uLists.get(arg2).getuCode();
		return false;
	}

	private void getXueYuan(String url, final int position) {
		startWaitingDialog();
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				if (null != result) {
					ArrayList<XueYuan> lists = (ArrayList<XueYuan>) result;
					if (lists.size() > 0) {
						uLists.get(position).setcList(lists);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtils.showShortToast(R.string.request_failed);
					}
				} else {
					ToastUtils.showShortToast(R.string.request_failed);
				}
			}
		});
		task.execute(null, TagUtils.GET_COLLEAGE_TAG, url);

	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		Intent intent = new Intent(this, YearsActivity.class);
		intent.putExtra("edit", true);
		startActivityForResult(intent, 4);
		colleage_id = uLists.get(arg2).getcList().get(arg3).getmId();
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (requestCode == 5) {
			colleage_id = data.getStringExtra("colleage_id");
		}
		Intent intent = new Intent();
		intent.putExtra("uname", mUniversityName);
		intent.putExtra("sid", mUniID);
		intent.putExtra("cid", colleage_id);
		intent.putExtra("year", data.getStringExtra("year"));
		setResult(300, intent);
		finish();
	}

	private void intentEditBiYeYuanXiaoActivity(String schoolID,
			String schoolName) {

		Intent intent = new Intent();
		intent.putExtra("uni", schoolName);
		intent.putExtra("unikey", schoolID);
		intent.setClass(this, EditBiYeYuanXiaoActivity.class);
		startActivityForResult(intent, 5);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		mUniversityName = mList.get(position).get("uni");
		intentEditBiYeYuanXiaoActivity(mList.get(position).get("key"),
				mUniversityName);
		mUniID = mList.get(position).get("key");
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
