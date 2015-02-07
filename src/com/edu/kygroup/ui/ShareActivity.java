//package com.edu.kygroup.ui;
//
//import java.util.HashMap;
//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.framework.Platform.ShareParams;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.weibo.TencentWeibo;
//
//import com.edu.kygroup.R;
//import com.edu.kygroup.domin.Poster.Topic;
//import com.edu.kygroup.utils.StringUtils;
//import com.edu.kygroup.utils.ToastUtils;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class ShareActivity extends BaseActivity implements OnClickListener{
//
//	private EditText mShareView;
//	private Button mShareBtn;
//	private Button mCancelBtn;
//	
//	private Topic mTopic;
//	private String mPlat;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		initData();
//		initView();
//	}
//	
//	@Override
//	protected View setContentView() {
//		// TODO Auto-generated method stub
//		return mInflater.inflate(R.layout.share_activity, null);
//	}
//
//	private void initData(){
//		mTopic = (Topic)getIntent().getSerializableExtra("topic");
//		mPlat = getIntent().getStringExtra("plat");
//	}
//	
//	private void initView(){
//		setTitleText(R.string.share);
//		mShareBtn = (Button)findViewById(R.id.share);
//		mCancelBtn = (Button)findViewById(R.id.cancel);
//		mShareView = (EditText)findViewById(R.id.share_text);
//		setLeftBtnVisibility(View.GONE);
//		mShareBtn.setOnClickListener(this);
//		mCancelBtn.setOnClickListener(this);
//		mShareView.setText(getShareText());
//		mShareView.setSelection(getShareText().length());
//	}
//	public String getShareText(){
//		StringBuffer text = new StringBuffer(getString(R.string.kaoyan_app));
//		text.append("\n");
//		text.append(mTopic.getLouzhu().getNickname());
//		text.append(":");
//		text.append(mTopic.getTitle());
//		text.append("\n");
//		text.append(getString(R.string.kaoyan_share));
//		return text.toString();
//	}
//	
//	public void shareMsg(ShareParams sp,String plat){
//		Platform weibo = ShareSDK.getPlatform(this, plat);
//		weibo.setPlatformActionListener(new PlatformActionListener() {
//			@Override
//			public void onError(Platform arg0, int arg1, Throwable arg2) {
//				// TODO Auto-generated method stub
//				toastHandler.sendEmptyMessage(2);
//				Log.v("AAA", arg2.toString());
//			}
//			
//			@Override
//			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//				// TODO Auto-generated method stub
//				toastHandler.sendEmptyMessage(1);
//			}
//	
//			@Override
//			public void onCancel(Platform arg0, int arg1) {
//				// TODO Auto-generated method stub
//				toastHandler.sendEmptyMessage(3);
//			}
//		}); // 设置分享事件回调
//		// 执行图文分享
//		weibo.share(sp);
//	}
//	
//	Handler toastHandler = new Handler(){
//		public void handleMessage(Message msg) {
//			closeWaitingDialog();
//			finish();
//			switch(msg.what){
//			case 1:
//				ToastUtils.showShortToast(R.string.share_success);
//				break;
//			case 2:
//				ToastUtils.showShortToast(R.string.share_failed);
//				break;
//			case 3:
//				ToastUtils.showShortToast(R.string.share_canceled);
//			default:
//				break;
//			}
//		};
//	};
//	
//	private void shareQQWeibo(){
//		ShareParams sp = new ShareParams();
//		sp.setText(getShareText());		
//		shareMsg(sp,TencentWeibo.NAME);
//	}
//	
//	private void shareSinaWeibo(){
//		ShareParams sp = new ShareParams();
//		sp.setText(getShareText());
//		shareMsg(sp,SinaWeibo.NAME);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch(v.getId()){
//		case R.id.share:
//			startWaitingDialog();
//			share();
//			break;
//		case R.id.cancel:
//			finish();
//			break;
//		default:
//			break;
//		}
//	}
//	
//	public void share(){
//		String shareText = mShareView.getText().toString();
//		if(!StringUtils.isEmpty(shareText)){
//			if(mPlat.equals(TencentWeibo.NAME)){
//				shareQQWeibo();
//			}else if(mPlat.equals(SinaWeibo.NAME)){
//				shareSinaWeibo();
//			}
//		}else{
//			ToastUtils.showShortToast(R.string.share_empty);
//		}
//	}
// }
