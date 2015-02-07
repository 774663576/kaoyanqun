package com.edu.kygroup.ui;
import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ColleageAdapter;
import com.edu.kygroup.domin.Colleage;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MajorActivity extends SelectBaseActivity implements IBindData{
	private boolean mIsEdit = false;
	private boolean mIsAddFocus = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityHolder.getInstance().addActivity(this);
		initData();
		addItemClickListener();
	}
	
	@SuppressWarnings("unchecked")
	private void initData(){
		setTitleText(R.string.s_select_major);
		mLists = (ArrayList<Colleage>)getIntent().getSerializableExtra("majors");
		mAdapter = new ColleageAdapter(this, mLists);
		mListView.setAdapter(mAdapter);
		mIsEdit = getIntent().getBooleanExtra("edit", false);
		mIsAddFocus = getIntent().getBooleanExtra("addfocus", false);
		if(!mIsEdit || !mIsAddFocus){
    		ActivityHolder.getInstance().addActivity(this);
    	}
		if(mIsAddFocus){
			Log.v("AAA", "3-------add");
    		ActivityHolder.getInstance().addFocusActivity(this);
    	}
	}
	
	private void addItemClickListener(){
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Colleage col = mLists.get(position);
				if(mIsEdit){							
					Intent intent = new Intent();
					intent.putExtra("major", col.getName());
					intent.putExtra("majorid", col.getId());
					setResult(EditActivity.MAJOR_TAG, intent);
					finish();
				}else{
					if(mIsAddFocus){
						Intent intent = getIntent();
					//	intent.setClass(MajorActivity.this, YearsActivity.class);
						intent.putExtra("maj", col.getName());
						intent.putExtra("majkey", col.getId());
						startMajorDetailsActivity(intent);
//						startActivity(intent);
//						finish();
					}
					else{
						KygroupApplication.mUser.setRMajor(col.getName());
						KygroupApplication.mUser.setRMid(col.getId());
						Intent intent = new Intent(MajorActivity.this, YearsActivity.class);
						startActivity(intent);
					}
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ActivityHolder.getInstance().removeActivity(this);
		if(mIsAddFocus){
			Log.v("AAA", "3-------destroy");
    		ActivityHolder.getInstance().removeFocusActivity(this);
    	}
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onPageStart("MajorActivity"); //统计页面
		 MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MajorActivity"); 
		MobclickAgent.onPause(this);
	}
	
	public void startMajorDetailsActivity(Intent intent){
		startWaitingDialog();
		String sid = intent.getStringExtra("unikey");
		String cid = intent.getStringExtra("colkey");
		String mid = intent.getStringExtra("majkey");
		String url = UrlUtils.DETAILS_URL+"major.sid="+sid+"&major.ceid="+cid+"&major.mid="+mid;
		new NetworkTask().execute(this,TagUtils.DETAILS_TAG,url);
		Log.v("AAA","focus - "+url);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch(tag){
			case TagUtils.DETAILS_TAG:{
				if(null != obj){
					DetailsInfo info = (DetailsInfo)obj;
					startDetailActivity(info);
				}else{
					ToastUtils.showShortToast(R.string.request_failed);
				}
				closeWaitingDialog();
			}
		}
		return null;
	}
	
	private void startDetailActivity(DetailsInfo info){
		Intent intent = getIntent();
		intent.setClass(this,MajorDetailsActivity.class);
		intent.putExtra("from", "major");
		intent.putExtra("details", info);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}
