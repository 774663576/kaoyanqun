package com.edu.kygroup.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.domin.DetailsInfo.Details;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.DetailLinearLayout;
import com.umeng.analytics.MobclickAgent;

public class MajorDetailsActivity extends BaseActivity implements IBindData,
		OnClickListener {
	private DetailsInfo mInfo;
	private TextView mMajorView;
	private TextView mYearView;
	private Details mDetails;
	private DetailLinearLayout mPlanLayout;
	private DetailLinearLayout mNoteLayout;
	private DetailLinearLayout mRetestLayout;
	private LinearLayout mLineLayout;
	private DetailLinearLayout mTeacherLayout;
	private DetailLinearLayout mDirectionLayout;
	private DetailLinearLayout mExamLayout;
	private ArrayList<User> mPostUser;
	private DetailLinearLayout mSameLevelLayout;
	private DetailLinearLayout mReferencesLayout;
	private LinearLayout mScrollLayout;
	private String mFrom;
	private boolean mIsAddFocus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(this).inflate(
				R.layout.major_details_activity, null);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		setTitleText(R.string.major_detail);
		mMajorView = (TextView) findViewById(R.id.major_name);
		mYearView = (TextView) findViewById(R.id.year_name);
		// mPlanView = (TextView)findViewById(R.id.plan_name);
		mPlanLayout = (DetailLinearLayout) findViewById(R.id.plan_layout);
		mNoteLayout = (DetailLinearLayout) findViewById(R.id.extra_layout);
		mRetestLayout = (DetailLinearLayout) findViewById(R.id.subject_layout);
		mScrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
		mLineLayout = (LinearLayout) findViewById(R.id.line_layout);
		mTeacherLayout = (DetailLinearLayout) findViewById(R.id.teacher_layout);
		mExamLayout = (DetailLinearLayout) findViewById(R.id.exams_layout);
		mSameLevelLayout = (DetailLinearLayout) findViewById(R.id.samelevel_layout);
		mReferencesLayout = (DetailLinearLayout) findViewById(R.id.references_layout);
		mDirectionLayout = (DetailLinearLayout) findViewById(R.id.directions_layout);
		// mGraduatesLayout =
		// (LinearLayout)findViewById(R.id.post_graduate_layout);
		findViewById(R.id.scroll_layout).setOnClickListener(this);
	}

	private void setView(LinearLayout layout, TextView view, String str) {
		if (StringUtils.isEmpty(str)) {
			layout.setVisibility(View.GONE);
		} else {
			view.setText(str);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mIsAddFocus) {
			ActivityHolder.getInstance().removeFocusActivity(this);
		}
	}

	private void initData() {
		mInfo = (DetailsInfo) getIntent().getSerializableExtra("details");
		mIsAddFocus = (Boolean) getIntent().getBooleanExtra("addfocus", false);
		if (mIsAddFocus) {
			ActivityHolder.getInstance().addFocusActivity(this);
		}
		mFrom = getIntent().getStringExtra("from");
		mDetails = mInfo.getDetail();
		mMajorView.setText(mDetails.getMname());
		mYearView.setText(mDetails.getYear());
		mPostUser = new ArrayList<User>();
		initFocus();
		initDirections();
		initExams();
		initTeacher();
		initRetest();
		initLine();
		initNote();
		getPostGraduates();
		initSameLevel();
		initReferences();
		initPlan();

	}

	public static void setListViewHeightBasedOnChildren(
			ExpandableListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	private void addView(LinearLayout layout, SpannableString str) {
		View view = LayoutInflater.from(this).inflate(R.layout.details_child,
				null);
		TextView tv = (TextView) view.findViewById(R.id.textview);
		tv.setText(str);
		layout.addView(view);
	}

	private void addView(LinearLayout layout, String str) {
		View view = LayoutInflater.from(this).inflate(R.layout.details_child,
				null);
		TextView tv = (TextView) view.findViewById(R.id.textview);
		tv.setText(str);
		layout.addView(view);
	}

	private void initLinearLayout(LinearLayout parent, ArrayList<String> lists,
			int id) {
		LinearLayout layout = (LinearLayout) findViewById(id);
		if (null != lists && lists.size() > 0) {
			for (String str : lists) {
				addView(layout, str);
			}
		} else {
			parent.setVisibility(View.GONE);
		}
	}

	private void initUrlLinearLayout(LinearLayout parent,
			ArrayList<String> lists, int id) {
		LinearLayout layout = (LinearLayout) findViewById(id);
		if (null != lists && lists.size() > 0) {
			for (String str : lists) {
				if (str.contains("http://")) {
					String url = str.substring(str.indexOf("http://"),
							str.length());
					String alterStr = str.replace(url,
							getString(R.string.details_click));
					int index = alterStr
							.indexOf(getString(R.string.details_click));
					SpannableString spanStr = new SpannableString(alterStr);
					spanStr.setSpan(new ForegroundColorSpan(0xff0000ff), index,
							alterStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					layout.setTag(url);
					layout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String urlString = v.getTag().toString();
							String url = urlString.substring(
									urlString.indexOf("http://"),
									urlString.length());
							Intent intent = new Intent(
									MajorDetailsActivity.this,
									WebActivity.class);
							intent.putExtra("url", url);
							startActivity(intent);
						}
					});
					addView(layout, spanStr);
				} else {
					addView(layout, str);
				}
			}
		} else {
			parent.setVisibility(View.GONE);
		}
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

	private void initLine() {
		ArrayList<String> lists = mDetails.getLines();
		initUrlLinearLayout(mLineLayout, lists, R.id.line);
	}

	private void initRetest() {
		ArrayList<String> lists = mDetails.getRetest();
		if (null != lists && lists.size() > 0) {
			mRetestLayout.setHeader(R.string.subject_content);
			mRetestLayout.setList(lists);
		} else {
			mRetestLayout.setVisibility(View.GONE);
		}
	} 

	private void initNote() {
		ArrayList<String> lists = mDetails.getNote();
		if (null != lists && lists.size() > 0) {
			mNoteLayout.setHeader(R.string.extra);
			mNoteLayout.setList(lists);
		} else {
			mNoteLayout.setVisibility(View.GONE);
		}
	}

	private void initDirections() {
		ArrayList<String> lists = mDetails.getDirections();
		if (null != lists && lists.size() > 0) {
			mDirectionLayout.setHeader(R.string.directions);
			mDirectionLayout.setList(lists);
		} else {
			mDirectionLayout.setVisibility(View.GONE);
		}

	}

	private void initExams() {
		ArrayList<String> lists = mDetails.getExams();
		if (null != lists && lists.size() > 0) {
			mExamLayout.setHeader(R.string.exams);
			mExamLayout.setList(lists);
		} else {
			mExamLayout.setVisibility(View.GONE);
		}
	}

	private void initPlan() {
		ArrayList<String> lists = mDetails.getPlan();
		if (null != lists && lists.size() > 0) {
			mPlanLayout.setHeader(R.string.plan);
			mPlanLayout.setList(lists);
		} else {
			mPlanLayout.setVisibility(View.GONE);
		}
	}

	private void initTeacher() {
		ArrayList<String> lists = mDetails.getTeachers();
		if (null != lists && lists.size() > 0) {
			mTeacherLayout.setHeader(R.string.teacher);
			mTeacherLayout.setList(lists);
		} else {
			mTeacherLayout.setVisibility(View.GONE);
		}
	}

	private void initSameLevel() {
		ArrayList<String> lists = mDetails.getSamelevel();
		if (null != lists && lists.size() > 0) {
			mSameLevelLayout.setHeader(R.string.same_level);
			mSameLevelLayout.setList(lists);
		} else {
			mSameLevelLayout.setVisibility(View.GONE);
		}
	}

	private void initReferences() {
		ArrayList<String> lists = mDetails.getReferences();
		if (null != lists && lists.size() > 0) {
			mReferencesLayout.setHeader(R.string.references_books);
			mReferencesLayout.setList(lists);
		} else {
			mReferencesLayout.setVisibility(View.GONE);
		}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.scroll_layout:
			startPostGraduateActivity();
			break;
		default:
			break;
		}
	}

	private void startPostGraduateActivity() {
		Intent intent = new Intent(this, PostGraduateActivity.class);
		intent.putExtra("data", mPostUser);
		intent.putExtra("detail", mDetails);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MajorDetailsActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MajorDetailsActivity");
		MobclickAgent.onPause(this);
	}

	private void initFocus() {
		System.out.println("mfrom:::::::::::::::" + mFrom + "      " + mInfo);
		if (!StringUtils.isEmpty(mFrom) && mFrom.equals("major")) {
			setRightBtnText(R.string.focus);
			setRightBtnVisibility(View.VISIBLE);
			setRightBtnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = getIntent();
					System.out.println(intent.toString() + "bu::::::::::::::");
					intent.putExtra("addfocus", true);
					intent.setClass(MajorDetailsActivity.this,
							YearsActivity.class);
					startActivity(intent);
				}
			});
		}
	}
	// private void showDialog(){
	// final String []years = getResources().getStringArray(R.array.aim_years);
	// Builder builder = new Builder(this);
	// builder.setTitle(R.string.select_year);
	// builder.setSingleChoiceItems(years, 0, new
	// DialogInterface.OnClickListener(){
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// Intent intent = getIntent();
	// Bundle bundle = intent.getExtras();
	// Intent newIntent = new Intent();
	// newIntent.setAction("com.kygroup.addfocus");
	// newIntent.putExtras(bundle);
	// newIntent.putExtra("year", years[which]);
	// Log.v("AAA", "-------------- "+years[which]);
	// sendBroadcast(newIntent);
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// }
}
