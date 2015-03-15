package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ScanBbsAdapter;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.fragment.BBSFragment;
import com.edu.kygroup.fragment.FuShiTiaoJiFragment;
import com.edu.kygroup.fragment.TiaoJiFragment;
import com.edu.kygroup.fragment.YuanXiaoXiXunFragment;
import com.edu.kygroup.fragment.ZhaoShengKaoShiFragment;

public class BBSActivity1 extends FragmentActivity implements OnClickListener,
		OnItemClickListener {

	private ViewPager viewPager;
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private BBSFragment bbsFragment;
	private YuanXiaoXiXunFragment yuanxiaoFragment;
	private ZhaoShengKaoShiFragment zhaoshengFragment;
	private FuShiTiaoJiFragment fushiFragment;
	private TiaoJiFragment tjFragment;
	private FocusInfo mFocusInfo;
	private ScanBbsAdapter mAdapter;

	private PopupWindow mPopupWindow;

	private Button btn_left;
	private ListView mScanListView;

	private RelativeLayout title_layout;
	private TextView txt_title;
	private TextView btn_right;
	private TextView txt_tab1, txt_tab2, txt_tab3, txt_tab4, txt_tab5;
	private int mbmpWidth;// 动画图片宽度
	private int mCurrIndex = 0;// 当前页卡编号
	private ImageView imageView;// 动画图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bbsactivity1);
		mFocusInfo = (FocusInfo) getIntent().getSerializableExtra("info");
		initView();
		initViewPager();
	}

	private void initTitle() {
		String title = "";
		if (mPostion == 1) {
			title = mFocusInfo.getmFocusColleage();
		} else if (mPostion == 2) {
			title = mFocusInfo.getmFocusMajor();
		} else {
			title = mFocusInfo.getmFocusSchool();
		}
		txt_title.setText(title);

		title = null;
	}

	private void initView() {
		btn_right = (TextView) findViewById(R.id.right_btn);
		btn_left = (Button) findViewById(R.id.left_btn);
		btn_left.setText(R.string.university);
		title_layout = (RelativeLayout) findViewById(R.id.title_layout);
		txt_title = (TextView) findViewById(R.id.title);
		txt_title.setText(mFocusInfo.getmFocusMajor());
		txt_tab1 = (TextView) findViewById(R.id.tab_1);
		txt_tab2 = (TextView) findViewById(R.id.tab_2);
		txt_tab3 = (TextView) findViewById(R.id.tab_3);
		txt_tab4 = (TextView) findViewById(R.id.tab_4);
		txt_tab5 = (TextView) findViewById(R.id.tab_5);
		initTitle();
		initPop();
		setListener();
		initImageView();
	}

	private void initImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mbmpWidth = dm.widthPixels / 5;
		LayoutParams params = imageView.getLayoutParams();
		params.width = mbmpWidth;
		imageView.setLayoutParams(params);
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		imageView.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void setListener() {
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}

	public FocusInfo getmFocusInfo() {
		return mFocusInfo;
	}

	private void initViewPager() {
		bbsFragment = new BBSFragment();
		yuanxiaoFragment = new YuanXiaoXiXunFragment();
		zhaoshengFragment = new ZhaoShengKaoShiFragment();
		fushiFragment = new FuShiTiaoJiFragment();
		tjFragment = new TiaoJiFragment();
		fragmentList.add(bbsFragment);
		fragmentList.add(yuanxiaoFragment);
		fragmentList.add(zhaoshengFragment);
		fragmentList.add(fushiFragment);
		fragmentList.add(tjFragment);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		// 给ViewPager设置适配器
		viewPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		viewPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public class MyOnPageChangeListener implements
			ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageSelected(int index) {
			imageAni(index);
			// for (TextView txt : tabList) {
			// txt.setBackground(null);
			// }
			// tabList.get(index).setBackgroundResource(R.drawable.tab);
			// if (index == 0) {
			// btn_left.setVisibility(View.VISIBLE);
			// btn_right.setVisibility(View.VISIBLE);
			// } else {
			// btn_left.setVisibility(View.GONE);
			// btn_right.setVisibility(View.GONE);
			// }
		}

	}

	private void imageAni(int arg0) {
		Animation animation = new TranslateAnimation(mbmpWidth * mCurrIndex,
				mbmpWidth * arg0, 0, 0);
		mCurrIndex = arg0;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(100);
		imageView.startAnimation(animation);

	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		List<Fragment> list;
		FragmentManager fm;

		public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
			this.fm = fm;
		}

		@Override
		public Fragment getItem(int i) {
			return list.get(i);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@SuppressLint("Recycle")
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			fm.beginTransaction();

		}

	}

	private Dialog mDialog = null;

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
		}
	}

	private void initPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.mail_listview,
				null);
		view.setBackgroundColor(Color.WHITE);
		mScanListView = (ListView) view.findViewById(R.id.mail_listview);
		mScanListView.setOnItemClickListener(this);
		String[] lists = getResources().getStringArray(R.array.select_bbs);
		mAdapter = new ScanBbsAdapter(this, lists, 2);
		mScanListView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(view, 180, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	private void showPopwindow(View v) {

		mPopupWindow.showAsDropDown(v);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			showPopwindow(title_layout);
			break;
		case R.id.right_btn:
			Intent intent = new Intent();
			intent.putExtra("focusInfo", mFocusInfo);
			intent.setClass(this, PublicshBbsActivity.class);
			startActivityForResult(intent, 100);
			break;
		default:
			break;
		}
	}

	private int mPostion = 2;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mPopupWindow.dismiss();
		Object obj = parent.getAdapter().getItem(position);
		if (obj instanceof String) {
			if (mPostion != position) {
				mPostion = position;
				if (mPostion == 0) {
					btn_left.setText(R.string.university);

				} else if (mPostion == 1) {
					btn_left.setText(R.string.colleage);

				} else if (mPostion == 2) {
					btn_left.setText(R.string.major);

				}
				bbsFragment.setPoster(mPostion);
				mAdapter.setPosition(mPostion);
				mAdapter.notifyDataSetChanged();
				initTitle();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || requestCode != 100) {
			return;
		}

		String content = data.getStringExtra("content");
		ArrayList<String> picList = new ArrayList<String>();
		picList = data.getStringArrayListExtra("picList");
		int tid = data.getIntExtra("tid", 0);
		String time = data.getStringExtra("time");
		bbsFragment.addSucPoster(tid, time, content, picList);
	}

}
