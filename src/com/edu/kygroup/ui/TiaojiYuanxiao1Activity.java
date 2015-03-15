package com.edu.kygroup.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.popupwindow.SelectPicPopwindow1;
import com.edu.kygroup.popupwindow.SelectPicPopwindow1.SelectMenuOnclick;

public class TiaojiYuanxiao1Activity extends FragmentActivity implements
		OnClickListener {
	private FragmentTransaction fraTra = null;
	private FragmentManager manager;
	private TiaoJi2014Fragment f2014;
	private Tiaoji2015Fragment f2015;
	private String mid = "";
	private String major = "";
	private TextView txt_2014;
	private TextView txt_2015;
	private View line_2014;
	private View line_2015;
	private Button btn_share;
	private String share_content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KygroupApplication.setAddFoucsActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tiaoji_yuanxiao1);
		mid = getIntent().getStringExtra("major_id");
		major = getIntent().getStringExtra("maror");
		initView();
		initFragment();
	}

	private void initFragment() {
		manager = getSupportFragmentManager();
		fraTra = manager.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("mid", mid);
		bundle.putString("major", major);
		f2014 = new TiaoJi2014Fragment();
		f2014.setArguments(bundle);
		fraTra.add(R.id.main_layout, f2014);
		fraTra.commit();
	}

	private void initView() {
		btn_share = (Button) findViewById(R.id.btn_share);
		line_2014 = (View) findViewById(R.id.line_2014);
		line_2015 = (View) findViewById(R.id.line_2015);
		line_2015.setVisibility(View.GONE);
		txt_2014 = (TextView) findViewById(R.id.txt_2014);
		txt_2015 = (TextView) findViewById(R.id.txt_2015);
		setListener();
	}

	private void setListener() {
		txt_2014.setOnClickListener(this);
		txt_2015.setOnClickListener(this);
		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SelectPicPopwindow1 pop = new SelectPicPopwindow1(
						TiaojiYuanxiao1Activity.this, v, new String[] { "QQ好友",
								"QQ空间", "微信好友", "微信朋友圈" });
				pop.setmSelectOnclick(new SelectMenuOnclick() {

					@Override
					public void onClickPosition(int position) {
						Platform plat = null;
						switch (position) {
						case 0:
							plat = ShareSDK.getPlatform(QQ.NAME);
							share(plat);

							break;
						case 1:
							plat = ShareSDK.getPlatform(QZone.NAME);
							share(plat);

							break;
						case 2:
							plat = ShareSDK.getPlatform(Wechat.NAME);
							shareWe1(plat);

							break;
						case 3:
							plat = ShareSDK.getPlatform(WechatMoments.NAME);
							shareWe(plat);

							break;
						default:
							break;
						}

					}
				});
				pop.show();
			}
		});
	}

	private void share(final Platform plat) {
		new Thread() {
			public void run() {
				ShareParams sp = new ShareParams();
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(share_content.toString());
				sp.setSite("超级考研群");
				sp.setSiteUrl("http://www.yifulou.cn:8180/");
				sp.setImageUrl("http://www.yifulou.cn:8080/picture/logo.png");

				// Platform qzone = ShareSDK.getPlatform(QZone.NAME);
				// qzone.setPlatformActionListener(paListener); // 设置分享事件回调
				// 执行图文分享
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(share_content.toString());
				sp.shareType = Platform.SHARE_TEXT;
				sp.shareType = Platform.SHARE_IMAGE;
				sp.text = share_content.toString();
				sp.imageUrl = "http://www.yifulou.cn:8080/picture/logo.png";
				sp.url = "http://www.yifulou.cn:8180/";
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe1(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(share_content.toString());
				sp.shareType = Platform.SHARE_TEXT;
				sp.text = share_content.toString();
				plat.share(sp);
			}
		}.start();

	}

	@Override
	public void onClick(View v) {
		fraTra = getSupportFragmentManager().beginTransaction();
		switch (v.getId()) {
		case R.id.txt_2014:
			if (f2014 != null) {
				fraTra.hide(f2014);
			}
			fraTra.show(f2014);
			line_2015.setVisibility(View.GONE);
			line_2014.setVisibility(View.VISIBLE);
			share_content = f2014.getShareContent();
			break;
		case R.id.txt_2015:
			if (f2015 == null) {
				f2015 = new Tiaoji2015Fragment();
				Bundle bundle = new Bundle();
				bundle.putString("mid", mid);
				bundle.putString("major", major);
				f2015.setArguments(bundle);
				fraTra.add(R.id.main_layout, f2015);
			} else {
				f2015.onResume();
			}
			if (f2014 != null) {
				fraTra.hide(f2014);
			}
			fraTra.show(f2015);
			line_2014.setVisibility(View.GONE);
			line_2015.setVisibility(View.VISIBLE);
			share_content = f2015.getShareContent();
			break;
		default:
			break;
		}
		fraTra.commit();

	}
}
