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

import com.edu.kygroup.R;
import com.edu.kygroup.domin.ColleageInfo;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class SchoolListActivity extends BaseActivity implements
		OnItemClickListener {
	private String major_id = "";
	private List<ColleageInfo> lists = new ArrayList<ColleageInfo>();

	private ListView mListView;

	private Adapter adapter;
	private ColleageInfo colleageInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		major_id = getIntent().getStringExtra("major_id");
		initView();
		getList();
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText("学校");
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setOnItemClickListener(this);
		adapter = new Adapter();
		mListView.setAdapter(adapter);
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_school_list, null);
	}

	private void getList() {
		startWaitingDialog();
		String url = UrlUtils.GET_SCHOOL_LIST_BY_MAJOR + "majorid=" + major_id;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				lists = (List<ColleageInfo>) result;
				adapter.notifyDataSetChanged();
				if (lists.size() == 0) {
					ToastUtils.showShortToast("没有数据");
				}
			}
		});
		task.execute(null, TagUtils.GET_SCHOOL_LIST_BY_MAJOR, url);
	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
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
				convertView = LayoutInflater.from(SchoolListActivity.this)
						.inflate(R.layout.object_zhuanye, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = lists.get(position).getSname() + " | "
					+ lists.get(position).getCename() + " | "
					+ lists.get(position).getMname();
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
		colleageInfo = lists.get(position);
		getMarjorDetail(colleageInfo.getSid() + "",
				colleageInfo.getCeid() + "", colleageInfo.getMid() + "");
	};

	/**
	 * 
	 * @param colleageID
	 *            学院ID
	 * @param majorId
	 *            专业ID
	 */
	private void getMarjorDetail(String schoolID, String colleageID,
			String majorId) {
		startWaitingDialog();
		String url = UrlUtils.DETAILS_URL + "major.sid=" + schoolID
				+ "&major.ceid=" + colleageID + "&major.mid=" + majorId;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				closeWaitingDialog();

				if (null != result) {
					MajorDetail detail = (MajorDetail) result;
					startDetailActivity(detail);
				} else {
					ToastUtils.showShortToast(R.string.request_failed);
				}
			}
		});
		task.execute(null, TagUtils.DETAILS_TAG, url);
	}

	private void startDetailActivity(MajorDetail info) {
		Intent intent = new Intent();
		intent.setClass(this, MajorDetailsActivity2.class);
		intent.putExtra("major", colleageInfo.getMname());
		intent.putExtra("from", "major");
		intent.putExtra("details", info);
		intent.putExtra("uni", colleageInfo.getSname());
		intent.putExtra("col", colleageInfo.getCename());
		intent.putExtra("maj", colleageInfo.getMname());
		intent.putExtra("unikey", colleageInfo.getSid() + "");
		intent.putExtra("colkey", colleageInfo.getCeid() + "");
		intent.putExtra("majkey", colleageInfo.getMid() + "");
		FocusInfo focusInfo = new FocusInfo();
		focusInfo.setmSid(colleageInfo.getSid() + "");
		focusInfo.setmCid(colleageInfo.getCeid() + "");
		focusInfo.setmMid(colleageInfo.getMid() + "");
		focusInfo.setmFocusSchool(colleageInfo.getSname());
		focusInfo.setmFocusColleage(colleageInfo.getSname());
		focusInfo.setmFocusMajor(colleageInfo.getMname());
		intent.putExtra("focus_info", focusInfo);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
