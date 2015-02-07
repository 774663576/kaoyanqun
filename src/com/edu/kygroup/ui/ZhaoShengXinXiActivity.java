package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.DirectContent;
import com.edu.kygroup.domin.MajorDirect;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;

public class ZhaoShengXinXiActivity extends BaseActivity implements
		OnGroupClickListener {
	private TextView txt_zhaoshengjihua;
	private ExpandableListView mListView;

	private List<MajorDirect> directLists = new ArrayList<MajorDirect>();
	private String plan = "";

	private DirectExpandableAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentData();
		initView();
	}

	@SuppressWarnings("unchecked")
	private void getIntentData() {
		directLists = (List<MajorDirect>) getIntent().getSerializableExtra(
				"directs");
		plan = getIntent().getStringExtra("plan");
	}

	private void initView() {
		setTitleText(getIntent().getStringExtra("major"));
		txt_zhaoshengjihua = (TextView) findViewById(R.id.txt_zhaosehgnjihua);
		txt_zhaoshengjihua.setText(plan);
		mListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		adapter = new DirectExpandableAdapter();
		mListView.setAdapter(adapter);
		mListView.setGroupIndicator(null);
		mListView.setOnGroupClickListener(this);
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_zhao_sheng_xin_xi, null);
	}

	public class DirectExpandableAdapter extends BaseExpandableListAdapter {
		Activity activity;

		public Object getChild(int groupPosition, int childPosition) {
			return directLists.get(groupPosition).getDirectContent();
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(ZhaoShengXinXiActivity.this)
						.inflate(R.layout.direct_detail, null);
				holder = new ViewHolder();
				holder.daoshi = (TextView) convertView
						.findViewById(R.id.txt_daoshi);
				holder.kemu = (TextView) convertView
						.findViewById(R.id.txt_kemu);
				holder.beizhu = (TextView) convertView
						.findViewById(R.id.txt_beizhu);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			DirectContent content = directLists.get(groupPosition)
					.getDirectContent();
			if (content != null) {

				holder.daoshi.setText(content.getDaoshi());
				holder.kemu.setText("  ① " + content.getZhengzhi() + "\n  ② "
						+ content.getWaiyu() + "\n  ③ " + content.getMajor1()
						+ "\n  ④ " + content.getMajor2());
				holder.beizhu.setText(content.getBeizhu());
			}
			return convertView;

		}

		public Object getGroup(int groupPosition) {
			return directLists.get(groupPosition);
		}

		public int getGroupCount() {
			return directLists.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(ZhaoShengXinXiActivity.this)
						.inflate(R.layout.zhuanye_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = directLists.get(groupPosition).getDirect_name();
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
		TextView daoshi;
		TextView kemu;
		TextView beizhu;
	}

	private void getDirectContent(int cid, int mid, int sid, int did,
			final int groupposition) {
		String url = UrlUtils.GETDIRECTIONDETAIL + "user.email="
				+ mUser.getEmail() + "&major.sid=" + sid + "&major.ceid=" + cid
				+ "&major.mid=" + mid + "&did=" + did;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				DirectContent content = (DirectContent) result;
				if (content != null) {
					directLists.get(groupposition).setDirectContent(content);
					adapter.notifyDataSetChanged();
				}

			}
		});
		task.execute(null, TagUtils.GETDIRECTIONDETAIL, url);
	}

	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		if (directLists.get(arg2).getDirectContent() != null) {
			return false;
		}
		startWaitingDialog();
		MajorDirect direct = directLists.get(arg2);
		getDirectContent(direct.getCid(), direct.getMid(), direct.getSid(),
				direct.getDirect_id(), arg2);
		return false;
	}
}
