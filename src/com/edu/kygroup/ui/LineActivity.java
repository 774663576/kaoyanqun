package com.edu.kygroup.ui;

import com.edu.kygroup.R;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;

public class LineActivity extends BaseActivity{
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initWebView();
	}
	
	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return LayoutInflater.from(this).inflate(R.layout.line_activity, null);
	}

	private void initWebView(){
		mWebView = (WebView)findViewById(R.id.line_webview);
		WebSettings ws = mWebView.getSettings();
		mWebView.setScrollBarStyle(0);
		ws.setJavaScriptEnabled(true);
		ws.setRenderPriority(RenderPriority.HIGH);
		ws.setBlockNetworkImage(false);
		ws.setUseWideViewPort(true);
		ws.setBuiltInZoomControls(true);
		ws.setSupportZoom(true);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
		ws.setDefaultTextEncodingName("utf-8");
		mWebView.loadUrl("http://gradinfo.cau.edu.cn/admission/infoSingleArticle.do?articleId=1000228&columnId=10423");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onPageStart("LineActivity"); //统计页面
		 MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("LineActivity"); 
		MobclickAgent.onPause(this);
	}
}
