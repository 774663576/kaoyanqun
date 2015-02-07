package com.edu.kygroup.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.edu.kygroup.R;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends BaseActivity {
	private WebView mWebView;
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	public void initView() {
		setRightBtnVisibility(View.GONE);
		setLeftBtnVisibility(View.GONE);
		setTitleText(getIntent().getStringExtra("title"));
		mWebView = (WebView) findViewById(R.id.line_webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
	}

	public void initData() {
		mUrl = getIntent().getStringExtra("url");
		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				startWaitingDialog();
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				closeWaitingDialog();
				super.onPageFinished(view, url);
			}
		});
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.web_activity, null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("WebActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("WebActivity");
		MobclickAgent.onPause(this);
	}
}
