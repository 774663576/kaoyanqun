package com.edu.kygroup.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ScanBbsAdapter;
import com.edu.kygroup.popupwindow.SharePopwindow;
import com.edu.kygroup.popupwindow.SharePopwindow.SelectMenuOnclick;
import com.edu.kygroup.utils.DeviceUtils;

public class DizcuzWebViewActivity extends BaseActivity {
	private WebView webView;
	private RelativeLayout parent;

	private String url = "";

	private String title = "";

	private PopupWindow mPopupWindow;
	private ListView mSelectListView;
	private ScanBbsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");
		System.out.println("url:::::::" + url);
		title = getIntent().getStringExtra("title");
		initView();
		initShareSDk();
		initPopupWindow();
	}

	private void initShareSDk() {
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
	}

	private void initView() {
		parent = (RelativeLayout) findViewById(R.id.parent);
		setLeftBtnVisibility(View.GONE);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebChromeClient(new WebViewClient());
		webView.loadUrl(url);
		startWaitingDialog();
		setTitleText(title);
		setRightBtnVisibility(View.VISIBLE);
		// setRightBtnText("分享");
		setRightBg(R.drawable.img_more);
		setRightBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				int width = DeviceUtils
						.getDisplayWidth(DizcuzWebViewActivity.this) - 180;
				mPopupWindow.showAsDropDown(v, width, 0);
			}
		});
	}

	private void initPopupWindow() {
		View view = LayoutInflater.from(this).inflate(R.layout.mail_listview,
				null);
		view.setBackgroundColor(Color.WHITE);
		mSelectListView = (ListView) view.findViewById(R.id.mail_listview);
		mSelectListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				if (position == 0) {
					share(parent);
				} else if (position == 1) {
					String tid = url.substring(url.indexOf("=") + 1,
							url.length());
					startActivity(new Intent(DizcuzWebViewActivity.this,
							TopicCommentActivity.class)
							.putExtra("title", title).putExtra("tid",
									Integer.valueOf(tid)));

				}
				mPopupWindow.dismiss();
			}
		});
		mAdapter = new ScanBbsAdapter(this, new String[] { "分享", "回复" }, -1);
		mSelectListView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(view, 180, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	private void share(final Platform plat) {
		new Thread() {
			public void run() {
				ShareParams sp = new ShareParams();
				sp.setTitle(title);
				sp.setTitleUrl(url); // 标题的超链接
				sp.setSite("超级考研群");
				sp.setSiteUrl(url);
				sp.setText(title);
				// sp.setImageUrl("http://www.yifulou.cn:8080/app_icon/logo.png");
				// Platform qzone = ShareSDK.getPlatform(QZone.NAME);
				// qzone.setPlatformActionListener(paListener); // 设置分享事件回调
				// 执行图文分享
				plat.share(sp);
			}
		}.start();

	}

	private void share(View v) {
		SharePopwindow pop = new SharePopwindow(DizcuzWebViewActivity.this, v);
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

	private void shareWe(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(title);
				sp.shareType = Platform.SHARE_TEXT;
				sp.shareType = Platform.SHARE_IMAGE;
				sp.text = title;
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
				sp.setText(title);
				sp.shareType = Platform.SHARE_TEXT;
				sp.text = title;
				plat.share(sp);
			}
		}.start();

	}

	private class WebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				closeWaitingDialog();
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_dizcuz_web_view, null);
	}

}
