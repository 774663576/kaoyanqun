package com.edu.kygroup.ui;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.LocationUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.utils.Util;
import com.edu.kygroup.widget.CircularImage;

public class MajorDetailsActivity2 extends BaseActivity implements IBindData,
		OnClickListener {
	private MajorDetail detail;

	private ArrayList<User> mPostUser = new ArrayList<User>();
	private LinearLayout mScrollLayout;

	private RelativeLayout layout_zhaoshengxinxi;

	private String mFrom = "";

	private RelativeLayout layout_tiaojiyuanxiao;

	private RelativeLayout layout_zhaoshengyuanxiao;
	private RelativeLayout layout_kaoyanluntan;

	private FocusInfo focusInfo;

	private TextView txt_chat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		getIntentData();
		intView();
		getPostGraduates();
	}

	/**
	 * 设置listview的高度
	 * 
	 * @param listView
	 */

	private void getIntentData() {
		mFrom = getIntent().getStringExtra("from");
		detail = (MajorDetail) getIntent().getSerializableExtra("details");
		focusInfo = (FocusInfo) getIntent().getSerializableExtra("focus_info");
		System.out.println("focus:::::::::::::" + focusInfo);
	}

	private void intView() {
		txt_chat = (TextView) findViewById(R.id.txt_chat);
		setTitleText(getIntent().getStringExtra("major"));
		layout_kaoyanluntan = (RelativeLayout) findViewById(R.id.layout_kaoyanluntan);
		layout_zhaoshengyuanxiao = (RelativeLayout) findViewById(R.id.layout_zhaoshengyaunxiao);
		layout_tiaojiyuanxiao = (RelativeLayout) findViewById(R.id.layout_tiaojiyuanxiao);
		layout_zhaoshengxinxi = (RelativeLayout) findViewById(R.id.layout_zhaoshengxinxi);
		mScrollLayout = (LinearLayout) findViewById(R.id.scroll_layout);
		setLeftBtnVisibility(View.GONE);
		// setTitleText("(" + mDetails.getYear() + ")  " + mDetails.getMname());
		if ("major".equals(mFrom)) {
			setRightBtnVisibility(View.VISIBLE);
			setRightBtnText(R.string.focus);
			setRightBtnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = getIntent();
					System.out.println(intent.toString() + "bu::::::::::::::");
					intent.putExtra("addfocus", true);
					intent.setClass(MajorDetailsActivity2.this,
							YearsActivity.class);
					startActivity(intent);
				}
			});
		}
		setListener();

	}

	private void setListener() {
		mScrollLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPostGraduateActivity();

			}
		});
		layout_zhaoshengxinxi.setOnClickListener(this);
		layout_tiaojiyuanxiao.setOnClickListener(this);
		layout_zhaoshengyuanxiao.setOnClickListener(this);
		layout_kaoyanluntan.setOnClickListener(this);
		txt_chat.setOnClickListener(this);
	}

	private void startPostGraduateActivity() {
		Intent intent = new Intent(this, PostGraduateActivity.class);
		intent.putExtra("data", mPostUser);
		intent.putExtra("detail", detail);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	protected View setContentView() {
		return LayoutInflater.from(this).inflate(
				R.layout.activity_major_details_activity2, null);
	}

	public void getPostGraduates() {
		String url = UrlUtils.GET_POST_GRADUATES_URL + "user.email="
				+ mUser.getEmail() + "&page=" + 1 + "&rp=" + 10
				+ "&user.aim.sid=" + detail.getSid() + "&user.aim.ceid="
				+ detail.getCid() + "&user.aim.mid=" + detail.getMid()
				+ "&user.longitude=" + LocationUtils.getLongtitude()
				+ "&user.latitude=" + LocationUtils.getLatitude();
		new NetworkTask().execute(this, TagUtils.GET_DETAIL_POST, url,
				mPostUser);
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

	class GridViewHolder {
		ImageView img;
		TextView txt_title;
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
			CircularImage image = new CircularImage(this);
			if (i < posterNum) {
				User user = mPostUser.get(i);
				if (user != null && user.getPic() != null) {
					mImageLoader.displayImage(user.getPic(), image, mOptions);
				}
			} else {
				image.setBackgroundResource(R.drawable.non_mate);
			}
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(60, 60);
			lp.setMargins(5, 5, 5, 5);
			image.setLayoutParams(lp);
			mScrollLayout.addView(image);
		}
	}

	private void intentZhaoShengXinXiActivity() {
		Intent intent = new Intent();
		intent.putExtra("plan", detail.getPlan());
		intent.putExtra("directs", (Serializable) detail.getDirectLists());
		intent.putExtra("major", getIntent().getStringExtra("major"));
		intent.setClass(this, ZhaoShengXinXiActivity.class);
		startActivity(intent);
		Util.leftOutRightIn(this);
	}

	private void intentTiaoJiActivity() {
		Intent intent = new Intent();
		intent.putExtra("major", getIntent().getStringExtra("major"));
		intent.putExtra("major_id", detail.getMid() + "");
		intent.setClass(this, TiaoJiYuanXiaoActivity.class);
		startActivity(intent);
		Util.leftOutRightIn(this);
	}

	private void intentZhaoShengYuanxiaoActivity() {
		Intent intent = new Intent();
		intent.putExtra("major_id", detail.getMid());
		intent.putExtra("major", getIntent().getStringExtra("major"));
		intent.setClass(this, ZhaoShengYuanXiaoActivity.class);
		startActivity(intent);
		Util.leftOutRightIn(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_zhaoshengxinxi:
			intentZhaoShengXinXiActivity();
			Util.leftOutRightIn(this);
			break;
		case R.id.layout_tiaojiyuanxiao:
			intentTiaoJiActivity();
			Util.leftOutRightIn(this);
			break;
		case R.id.layout_zhaoshengyaunxiao:
			intentZhaoShengYuanxiaoActivity();
			Util.leftOutRightIn(this);
			break;
		case R.id.layout_kaoyanluntan:
			goToBbsActivity(focusInfo);
			break;
		case R.id.scrollView1:
			startPostGraduateActivity();
			break;
		case R.id.txt_chat:
			intetnChat(detail.getGroup_id());
			break;
		default:
			break;
		}
	}

	private void intetnChat(String groupId) {
		Intent intent = new Intent();
		intent.putExtra("chatType", ChatActivity1.CHATTYPE_GROUP);
		intent.putExtra("groupId", groupId);
		System.out.println("groutp::::::::::" + groupId);
		intent.putExtra("user_name", getIntent().getStringExtra("major"));
		intent.setClass(this, ChatActivity1.class);
		startActivity(intent);
	}

	private void goToBbsActivity(FocusInfo info) {
		Intent intent = new Intent(this, BBSActivity1.class);
		intent.putExtra("info", info);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
