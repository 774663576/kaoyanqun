package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.edu.kygroup.R;

public class WelcomeActivity extends Activity implements OnClickListener {
	private ViewPager mViewPager;
	private List<ImageView> views = new ArrayList<ImageView>();
	private LinearLayout layout;
	private Button btn_login;
	private List<View> circleViews = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		initView();
	}

	private void initView() {
		View circle = (View) findViewById(R.id.cicle1);
		circleViews.add(circle);
		circle = (View) findViewById(R.id.cicle2);
		circleViews.add(circle);
		circle = (View) findViewById(R.id.cicle3);
		circleViews.add(circle);
		circle = (View) findViewById(R.id.cicle4);
		circleViews.add(circle);
		btn_login = (Button) findViewById(R.id.btn_login);
		layout = (LinearLayout) findViewById(R.id.layout_bottom);
		initViewPage();
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mViewPager.setAdapter(new MyPagerAdapter());
		setListener();
	}

	private void setListener() {
		btn_login.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				if (index == 3) {
					layout.setVisibility(View.VISIBLE);
				} else {
					layout.setVisibility(View.INVISIBLE);

				}
				for (View view : circleViews) {
					view.setBackgroundResource(R.drawable.share_gray);
				}
				circleViews.get(index).setBackgroundResource(
						R.drawable.share_white);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initViewPage() {
		ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.wec1);
		img.setScaleType(ScaleType.FIT_XY);
		views.add(img);
		img = new ImageView(this);
		img.setImageResource(R.drawable.wec2);
		img.setScaleType(ScaleType.FIT_XY);
		views.add(img);
		img = new ImageView(this);
		img.setImageResource(R.drawable.wec3);
		img.setScaleType(ScaleType.FIT_XY);
		views.add(img);
		img = new ImageView(this);
		img.setImageResource(R.drawable.wec4);
		img.setScaleType(ScaleType.FIT_XY);
		views.add(img);
	}

	public class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1));
			return views.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));

		}

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

}
