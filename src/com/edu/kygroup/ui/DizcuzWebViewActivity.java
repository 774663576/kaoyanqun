package com.edu.kygroup.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

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

public class DizcuzWebViewActivity extends BaseActivity {
	private WebView webView;

	private String url = "";

	private String title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");
		System.out.println("url:::::::" + url);
		title = getIntent().getStringExtra("title");
		initView();
		initShareSDk();
	}

	private void initShareSDk() {
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
	}

	private void initView() {
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
		setRightBtnText("分享");
		setRightBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelectPicPopwindow1 pop = new SelectPicPopwindow1(
						DizcuzWebViewActivity.this, v, new String[] { "QQ好友",
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
