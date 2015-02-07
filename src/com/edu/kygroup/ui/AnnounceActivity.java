package com.edu.kygroup.ui;

import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.AnnounceAdapter;
import com.edu.kygroup.domin.Announce;
import com.edu.kygroup.domin.Announce.Item;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AnnounceActivity extends BaseActivity{
	private ListView mAnnounceListView;
	private AnnounceAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView(){
		setTitleText(R.string.announcement);
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		mAnnounceListView = (ListView)findViewById(R.id.announce_listview);
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.announce_activity, null);
	}
	
	private void initData(){
		Announce announce = (Announce)getIntent().getSerializableExtra("announce");
		ArrayList<Item> items = announce.getAnnounces();
		mAdapter = new AnnounceAdapter(this, items);
		mAnnounceListView.setAdapter(mAdapter);
	}
}
