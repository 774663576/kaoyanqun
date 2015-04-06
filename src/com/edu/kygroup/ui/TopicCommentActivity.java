package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.TipCommentListAdapter;
import com.edu.kygroup.domin.TipCommentList;
import com.edu.kygroup.domin.TipsComment;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.popupwindow.SelectPicPopwindow;
import com.edu.kygroup.popupwindow.SelectPicPopwindow.CameraPath;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.AddTipCommentTask;
import com.edu.kygroup.task.GetTipCommentListTask;
import com.edu.kygroup.utils.Constant;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.Util;

public class TopicCommentActivity extends BaseActivity implements
		OnClickListener, CameraPath {
	private ListView mListView;
	private String title = "";

	private TextView btn_send;
	private EditText edit_content;

	private int tid;

	private ImageView img_carera;
	private ImageView img_del;

	private SelectPicPopwindow pop;

	private List<TipsComment> lists = new ArrayList<TipsComment>();

	private TipCommentListAdapter adapter;

	private TipCommentList list = new TipCommentList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		title = getIntent().getStringExtra("title");
		tid = getIntent().getIntExtra("tid", 0);
		list.setTip_id(tid);
		initView();
		getCommentList();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_topic_comment, null);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText(title);
		mListView = (ListView) findViewById(R.id.listview);
		btn_send = (TextView) findViewById(R.id.send);
		edit_content = (EditText) findViewById(R.id.input_text);
		img_del = (ImageView) findViewById(R.id.img_del);
		img_carera = (ImageView) findViewById(R.id.img_camera);
		btn_send.setOnClickListener(this);
		img_carera.setOnClickListener(this);
		adapter = new TipCommentListAdapter(this, lists);
		mListView.setAdapter(adapter);

	}

	private void getCommentList() {
		startWaitingDialog();
		GetTipCommentListTask task = new GetTipCommentListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("获取失败");
				}
				lists.addAll(list.getLists());
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(list);
	}

	private void setPic(Bitmap bmp) {
		if (bmp == null) {
			// return;
		}
		img_carera.setImageBitmap(bmp);
		img_del.setVisibility(View.VISIBLE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_BYSDCARD
				&& resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					selectPicPath = cursor.getString(column_index);
					bitmap = BitmapFactory.decodeFile(selectPicPath);
					setPic(bitmap);
				}
			}

		}// 拍摄图片
		else if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}
			bitmap = BitmapFactory.decodeFile(selectPicPath);
			setPic(bitmap);
		}

	}

	private String selectPicPath = "";

	@Override
	public void getCameraPath(String path) {
		selectPicPath = path;

	}

	private void upLoadComment() {
		String comment_content = edit_content.getText().toString().trim();
		if (comment_content.length() == 0) {
			return;
		}
		startWaitingDialog();
		final TipsComment comment = new TipsComment();
		comment.setComment_content(comment_content);
		comment.setComment_img_url(selectPicPath);
		comment.setComment_time(Util.getCurrentDate());
		comment.setTip_id(tid);
		comment.setUser_avatar(KygroupApplication.mUser.getPic());
		comment.setUser_name(KygroupApplication.mUser.getNickName());
		comment.setUser_school(KygroupApplication.mUser.getRSchool());
		AddTipCommentTask task = new AddTipCommentTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("回复失败");
					return;
				}
				lists.add(0, comment);
				adapter.notifyDataSetChanged();
				mListView.setSelection(0);
				edit_content.setText("");
				selectPicPath = "";
				img_carera.setImageResource(R.drawable.camera);
				img_del.setVisibility(View.GONE);
			}
		});
		task.executeParallel(comment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			upLoadComment();
			break;
		case R.id.img_camera:
			if ("".equals(selectPicPath)) {
				pop = new SelectPicPopwindow(this, v);
				pop.show();
				pop.setCallBack(this);
			} else {
				selectPicPath = "";
				img_carera.setImageResource(R.drawable.camera);
				img_del.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

}
