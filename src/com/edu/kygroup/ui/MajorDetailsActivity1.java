package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.domin.DetailsInfo.Details;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;

public class MajorDetailsActivity1 extends BaseActivity implements
		OnItemClickListener, IBindData {
	private TextView marjor_name;
	private ListView listView1;
	private ListView listView2;
	private GridView mGridView;

	private List<String> marjorTypeLists = new ArrayList<String>();
	private List<String> typeListContentLists = new ArrayList<String>();
	private List<String> play = new ArrayList<String>();// 招生计划
	private List<String> directions = new ArrayList<String>();// 研究方向;
	private List<String> exams = new ArrayList<String>();// 考试科目;
	private List<String> retest = new ArrayList<String>();// 复试科目;
	private List<String> samelevel = new ArrayList<String>();// 同等学力加试;
	private List<String> note = new ArrayList<String>();// 备注;
	private List<GridViewData> gridViewData = new ArrayList<GridViewData>();
	private Details mDetails;
	private DetailsInfo mInfo;

	private TypeAdapter typeAdapter;
	private TypeContentAdapter typeContentAdapter;

	private String mFrom = "";

	private LinearLayout mScrollLayout;

	private ArrayList<User> mPostUser = new ArrayList<User>();
	private FocusInfo focusInfo;

	private RelativeLayout scroll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		getIntentData();
		initGridViewData();
		intView();
		initData();
		setAdapter();
		getPostGraduates();
	}

	private void initGridViewData() {
		// GridViewData data = new GridViewData(R.drawable.yuanxiaozixun,
		// "院校咨询");
		// gridViewData.add(data);
		// data = new GridViewData(R.drawable.zhaoshengkaoshi, "招生考试");
		// gridViewData.add(data);
		// // data = new GridViewData(R.drawable.fushitiaoji, "复试调剂");
		// gridViewData.add(data);
		// data = new GridViewData(R.drawable.kaisheyuanx, "开设院校");
		// gridViewData.add(data);
		// data = new GridViewData(R.drawable.tiaojiyuanx, "可调剂院校");
		// gridViewData.add(data);
		// data = new GridViewData(R.drawable.bbs70, "BBS(考研论坛)");
		// gridViewData.add(data);
		// data = new GridViewData(R.drawable.taolunqu, "讨论区");
		// gridViewData.add(data);

	}

	private void setAdapter() {
		typeAdapter = new TypeAdapter();
		typeContentAdapter = new TypeContentAdapter();
		listView1.setAdapter(typeAdapter);
		listView2.setAdapter(typeContentAdapter);
		setListViewHeight(listView1);
		setListViewHeight(listView2);
		mGridView.setAdapter(new GridViewAdapter());
	}

	/**
	 * 设置listview的高度
	 * 
	 * @param listView
	 */
	private void setListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView1.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int count = listAdapter.getCount();
		if (count == 0) {
			return;
		}
		int totalHeight = 0;
		View listItem = listAdapter.getView(0, null, listView);
		listItem.measure(0, 0); // 计算子项View 的宽高
		totalHeight = listItem.getMeasuredHeight(); // 统计所有子项的总高度

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = (count > 6 ? totalHeight * 6 : totalHeight * count) + 8;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	private void getIntentData() {
		mFrom = getIntent().getStringExtra("from");
		mInfo = (DetailsInfo) getIntent().getSerializableExtra("details");
		focusInfo = (FocusInfo) getIntent().getSerializableExtra("focus_info");
		mDetails = mInfo.getDetail();
	}

	private void initData() {
		marjorTypeLists.add("招生计划");
		marjorTypeLists.add("研究方向");
		marjorTypeLists.add("考试科目");
		// marjorTypeLists.add("复试科目");
		marjorTypeLists.add("导师");
		marjorTypeLists.add("备注");
		play = mDetails.getPlan();
		directions = mDetails.getDirections();
		exams = mDetails.getExams();
		retest = mDetails.getRetest();
		samelevel = mDetails.getSamelevel();
		note = mDetails.getNote();
		typeListContentLists.clear();
		if (exams == null || exams.size() == 0) {
			typeListContentLists.add("暂无数据");
		} else {
			typeListContentLists.addAll(exams);
		}
	}

	private void intView() {
		setLeftBtnVisibility(View.GONE);
		scroll = (RelativeLayout) findViewById(R.id.scrollView1);
		setTitleText(R.string.major_detail);
		marjor_name = (TextView) findViewById(R.id.major_name);
		listView1 = (ListView) findViewById(R.id.listView1);
		listView2 = (ListView) findViewById(R.id.listView2);
		listView1.setCacheColorHint(0);
		listView2.setCacheColorHint(0);
		marjor_name.setText("(" + mDetails.getYear() + ")  "
				+ mDetails.getMname());
		mScrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
		mGridView = (GridView) findViewById(R.id.gridView1);
		setListener();

	}

	private void setListener() {
		listView1.setOnItemClickListener(this);
		if ("major".equals(mFrom)) {
			setRightBtnVisibility(View.VISIBLE);
			setRightBtnText(R.string.focus);
			setRightBtnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = getIntent();
					System.out.println(intent.toString() + "bu::::::::::::::");
					intent.putExtra("addfocus", true);
					intent.setClass(MajorDetailsActivity1.this,
							YearsActivity.class);
					startActivity(intent);
				}
			});
		}
		mScrollLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPostGraduateActivity();

			}
		});
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 5:
					goToBbsActivity(focusInfo);
					break;
				case 6:
					intetnChat(focusInfo.getGroup_id());
					break;
				default:
					break;
				}

			}
		});
		listView2.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int eventAction = event.getActionMasked();
				switch (eventAction) {
				case MotionEvent.ACTION_DOWN:
					scroll.requestDisallowInterceptTouchEvent(true);

				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					scroll.requestDisallowInterceptTouchEvent(false);
					break;
				default:
					break;
				}

				return false;
			}

		});

	}

	private void intetnChat(String groupId) {
		Intent intent = new Intent();
		intent.putExtra("chatType", ChatActivity1.CHATTYPE_GROUP);
		intent.putExtra("groupId", groupId);
		intent.putExtra("user_name", mInfo.getDetail().getMname());
		intent.setClass(this, ChatActivity1.class);
		startActivity(intent);
	}

	private void startPostGraduateActivity() {
		Intent intent = new Intent(this, PostGraduateActivity.class);
		intent.putExtra("data", mPostUser);
		intent.putExtra("detail", mDetails);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	protected View setContentView() {
		return LayoutInflater.from(this).inflate(
				R.layout.activity_major_details_activity1, null);
	}

	public void getPostGraduates() {
		String url = UrlUtils.GET_POST_GRADUATES_URL + "user.email="
				+ mUser.getEmail() + "&page=" + 1 + "&rp=" + 10
				+ "&user.aim.sid=" + mDetails.getSid() + "&user.aim.ceid="
				+ mDetails.getCeid() + "&user.aim.mid=" + mDetails.getMid()
				+ "&user.longitude=" + LocationUtils.getLongtitude()
				+ "&user.latitude=" + LocationUtils.getLatitude();
		new NetworkTask().execute(this, TagUtils.GET_DETAIL_POST, url,
				mPostUser);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		typeAdapter.setSelectItem(position);
		typeAdapter.notifyDataSetChanged();
		switch (position) {
		case 0:
			typeListContentLists.clear();
			if (play == null || play.size() == 0) {
				typeListContentLists.add("暂无数据");
			} else {
				typeListContentLists.addAll(play);
			}
			break;
		case 1:
			typeListContentLists.clear();
			if (directions == null || directions.size() == 0) {
				typeListContentLists.add("暂无数据");
			} else {
				typeListContentLists.addAll(directions);
			}
			break;
		case 2:
			typeListContentLists.clear();
			if (exams == null || exams.size() == 0) {
				typeListContentLists.add("暂无数据");
			} else {
				typeListContentLists.addAll(exams);
			}
			break;
		case 3:
			typeListContentLists.clear();
			if (retest == null || retest.size() == 0) {
				typeListContentLists.add("暂无数据");
			} else {
				typeListContentLists.addAll(retest);
			}
			break;
		case 4:
			typeListContentLists.clear();
			if (samelevel == null || samelevel.size() == 0) {
				typeListContentLists.add("暂无数据");
			} else {
				typeListContentLists.addAll(samelevel);
			}
			break;
		case 5:
			typeListContentLists.clear();
			if (note == null || note.size() == 0) {
				typeListContentLists.add("暂无数据");
			} else {
				typeListContentLists.addAll(note);
			}
			break;
		default:
			break;
		}
		typeContentAdapter.notifyDataSetChanged();
	}

	class TypeContentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return typeListContentLists.size();
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
				holder = new ViewHolder();
				convertView = LayoutInflater.from(MajorDetailsActivity1.this)
						.inflate(R.layout.marjor_item, null);
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(typeListContentLists.get(position));
			return convertView;
		}

	}

	class TypeAdapter extends BaseAdapter {
		private int selectItem = 2;

		@Override
		public int getCount() {
			return marjorTypeLists.size();
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
				holder = new ViewHolder();
				convertView = LayoutInflater.from(MajorDetailsActivity1.this)
						.inflate(R.layout.major_type_item, null);
				holder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(marjorTypeLists.get(position));
			if (position == selectItem) {
				holder.text.setBackgroundColor(R.color.bbs_default_color);
				holder.text.getBackground().setAlpha(30);
			} else {
				holder.text.setBackgroundColor(Color.TRANSPARENT);
			}
			return convertView;
		}

		public int getSelectItem() {
			return selectItem;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

	}

	class ViewHolder {
		TextView text;
	}

	class GridViewData {

		int img;
		String title = "";

		public GridViewData(int img, String title) {
			this.img = img;
			this.title = title;
		}

	}

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return gridViewData.size();
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
			GridViewHolder holder = null;
			if (convertView == null) {
				holder = new GridViewHolder();
				convertView = LayoutInflater.from(MajorDetailsActivity1.this)
						.inflate(R.layout.mojar_detail_gridview_item, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.txt_title = (TextView) convertView
						.findViewById(R.id.txt_title);
				convertView.setTag(holder);
			} else {
				holder = (GridViewHolder) convertView.getTag();
			}
			holder.img.setImageResource(gridViewData.get(position).img);
			holder.txt_title.setText(gridViewData.get(position).title);
			return convertView;
		}

	}

	class GridViewHolder {
		ImageView img;
		TextView txt_title;
	}

	private void goToBbsActivity(FocusInfo info) {
		Intent intent = new Intent(this, BBSActivity1.class);
		intent.putExtra("info", info);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.GET_DETAIL_POST:
			initHScrollView(obj);
			break;
		default:
			break;
		}
		return null;
	}

	public void initHScrollView(Object obj) {
		if (null != obj) {
			mPostUser.addAll((ArrayList<User>) obj);
		}
		int width = DeviceUtils.getDisplayWidth(this);
		int size = getResources().getDimensionPixelSize(R.dimen.gallery_width);
		int minNum = (width - 5) / (size + 5);
		int posterNum = (null == mPostUser) ? 0 : mPostUser.size();
		int num = (posterNum > minNum) ? posterNum : minNum;
		for (int i = 0; i < num; i++) {
			ImageView image = new ImageView(this);
			if (i < posterNum) {
				User user = mPostUser.get(i);
				if (user != null && user.getPic() != null) {
					mImageLoader.displayImage(user.getPic(), image, mOptions);
				}
			} else {
				image.setBackgroundResource(R.drawable.non_mate);
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size,
					size);
			lp.setMargins(5, 5, 5, 5);
			image.setLayoutParams(lp);
			mScrollLayout.addView(image);
		}
	}
}
