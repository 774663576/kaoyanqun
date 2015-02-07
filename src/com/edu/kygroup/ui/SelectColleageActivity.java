package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.edu.keygroup.selectshcool.XueYuan;
import com.edu.keygroup.selectshcool.ZhuanYe;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class SelectColleageActivity extends BaseActivity implements IBindData {
	private ExpandableListView list;
	// private TextView schoolName;

	private String schoolID = "";// 学校ID
	private String schollName = "";
	private String cNname = "";
	private String cID = "";
	private String mName = "";
	private String mID = "";

	private int registerType = 0;

	private List<XueYuan> xueyuanLists = new ArrayList<XueYuan>();

	private XueYuanExpandableAdapter adapter;
	private Register register = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		ActivityHolder.getInstance().addActivity(this);
		schoolID = getIntent().getStringExtra("unikey");
		registerType = getIntent().getIntExtra("registerType", registerType);
		register = (Register) getIntent().getSerializableExtra("register");
		initView();
		getData();
	}

	private void getData() {
		int kind = 0;
		if (getIntent().getBooleanExtra("edit", false)) {
			kind = 1;
		}
		String url = UrlUtils.GET_COLLEAGE_URL + "sid=" + schoolID;
		url += "&kind=" + kind;
		startWaitingDialog();
		new NetworkTask().execute(this, TagUtils.GET_COLLEAGE_TAG, url);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		list = (ExpandableListView) findViewById(R.id.list);
		list.setGroupIndicator(null);
		adapter = new XueYuanExpandableAdapter(this);
		list.setAdapter(adapter);
		// schoolName = (TextView) findViewById(R.id.schoolName);
		// schoolName.setText(getIntent().getStringExtra("schoolName"));
		schollName = getIntent().getStringExtra("uni");
		setTitleText(schollName);

		setListener();
	}

	private void setListener() {
		list.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (registerType != 0) {
					KygroupApplication.mUser.setRMajor(xueyuanLists
							.get(groupPosition).getZhuanYeList()
							.get(childPosition).getZhuanYeName());
					KygroupApplication.mUser.setRMid(xueyuanLists
							.get(groupPosition).getZhuanYeList()
							.get(childPosition).getZhuanyeID());
					register.setRegister_Colleage_name(xueyuanLists.get(
							groupPosition).getXueYuanName());
					register.setRegister_Cid(xueyuanLists.get(groupPosition)
							.getmId());
					System.out.println("cid:::::::::::::"
							+ register.getRegister_Cid());
					register.setRegister_Major_name(xueyuanLists
							.get(groupPosition).getZhuanYeList()
							.get(childPosition).getZhuanYeName());
					register.setRegister_Mid(xueyuanLists.get(groupPosition)
							.getZhuanYeList().get(childPosition).getZhuanyeID());
					Intent intent = new Intent(SelectColleageActivity.this,
							YearsActivity.class);
					intent.putExtra("register", register);
					startActivity(intent);
					// startActivity(new Intent(SelectColleageActivity.this,
					// RegisterActivity.class));
					return true;
				}
				getMarjorDetail(
						xueyuanLists.get(groupPosition).getmId(),
						xueyuanLists.get(groupPosition).getZhuanYeList()
								.get(childPosition).getZhuanyeID());
				mName = xueyuanLists.get(groupPosition).getZhuanYeList()
						.get(childPosition).getZhuanYeName();
				mID = xueyuanLists.get(groupPosition).getZhuanYeList()
						.get(childPosition).getZhuanyeID();
				getIntent().putExtra(
						"maj",
						xueyuanLists.get(groupPosition).getZhuanYeList()
								.get(childPosition).getZhuanYeName());
				getIntent().putExtra(
						"majkey",
						xueyuanLists.get(groupPosition).getZhuanYeList()
								.get(childPosition).getZhuanyeID());
				return true;
			}
		});
		list.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				if (xueyuanLists.get(groupPosition).getZhuanYeList().size() > 0) {
					return false;
				}
				if (registerType != 0) {
					mUser.setRCollege(xueyuanLists.get(groupPosition)
							.getXueYuanName());
					mUser.setRCid(xueyuanLists.get(groupPosition).getmId());
				}
				xueyuanOnclick(xueyuanLists.get(groupPosition).getmId(),
						groupPosition);
				getIntent().putExtra("col",
						xueyuanLists.get(groupPosition).getXueYuanName());
				getIntent().putExtra("colkey",
						xueyuanLists.get(groupPosition).getmId());
				cNname = xueyuanLists.get(groupPosition).getXueYuanName();
				cID = xueyuanLists.get(groupPosition).getmId();
				return false;
			}
		});
	}

	/**
	 * 
	 * @param colleageID
	 *            学院ID
	 * @param majorId
	 *            专业ID
	 */
	private void getMarjorDetail(String colleageID, String majorId) {
		startWaitingDialog();
		String url = UrlUtils.DETAILS_URL + "major.sid=" + schoolID
				+ "&major.ceid=" + colleageID + "&major.mid=" + majorId;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				closeWaitingDialog();

				if (null != result) {
					MajorDetail info = (MajorDetail) result;
					startDetailActivity(info);
				} else {
					ToastUtils.showShortToast(R.string.request_failed);
				}
			}
		});
		task.execute(null, TagUtils.DETAILS_TAG, url);
	}

	private void startDetailActivity(MajorDetail info) {
		Intent intent = getIntent();
		intent.setClass(this, MajorDetailsActivity2.class);
		intent.putExtra("from", "major");
		intent.putExtra("major", mName);
		intent.putExtra("details", info);
		FocusInfo focusInfo = new FocusInfo();
		focusInfo.setmSid(schoolID);
		focusInfo.setmCid(cID);
		focusInfo.setmMid(mID);
		focusInfo.setmFocusSchool(schollName);
		focusInfo.setmFocusColleage(cNname);
		focusInfo.setmFocusMajor(mName);
		intent.putExtra("focus_info", focusInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
	protected View setContentView() {
		return inflate(R.layout.activity_select_colleage, null);
	}

	public class XueYuanExpandableAdapter extends BaseExpandableListAdapter {
		Activity activity;

		public XueYuanExpandableAdapter(Activity a) {
			activity = a;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return xueyuanLists.get(groupPosition).getZhuanYeList()
					.get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return xueyuanLists.get(groupPosition).getZhuanYeList().size();
		}

		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(SelectColleageActivity.this)
						.inflate(R.layout.xuanyuan_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = xueyuanLists.get(groupPosition).getZhuanYeList()
					.get(childPosition).getZhuanYeName();
			holder.text.setText(string);
			return convertView;

		}

		public Object getGroup(int groupPosition) {
			return xueyuanLists.get(groupPosition);
		}

		public int getGroupCount() {
			return xueyuanLists.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(SelectColleageActivity.this)
						.inflate(R.layout.zhuanye_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = xueyuanLists.get(groupPosition).getXueYuanName();
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

	private void xueyuanOnclick(String colleageID, final int xueyuanPosition) {
		String url = UrlUtils.GET_MAJOR_URL + "ceid=" + colleageID;
		url = url + "&sid=" + schoolID;
		startWaitingDialog();
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				if (null != result) {
					@SuppressWarnings("unchecked")
					ArrayList<ZhuanYe> lists = (ArrayList<ZhuanYe>) result;
					if (lists.size() == 0) {
						ToastUtils.showShortToast("暂时获取不到数据");
						return;
					}
					xueyuanLists.get(xueyuanPosition).setZhuanYeList(lists);
					adapter.notifyDataSetChanged();

				} else {
					ToastUtils.showShortToast("失败");

				}
			}
		});

		task.execute(null, TagUtils.GET_MAJOR_TAG, url);
	}
}
