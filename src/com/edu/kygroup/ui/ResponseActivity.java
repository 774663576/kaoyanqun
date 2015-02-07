package com.edu.kygroup.ui;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.edu.kygroup.R;
import com.edu.kygroup.adapter.BBSImgAdapter;
import com.edu.kygroup.adapter.ResponseAdapter;
import com.edu.kygroup.domin.Louzhu;
import com.edu.kygroup.domin.Poster.Topic;
import com.edu.kygroup.domin.ResPoster;
import com.edu.kygroup.domin.ResPoster.Responses;
import com.edu.kygroup.domin.TopicRetInfo;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.popupwindow.SelectPicPopwindow;
import com.edu.kygroup.popupwindow.SelectPicPopwindow.CameraPath;
import com.edu.kygroup.utils.BitmapUtils;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.HttpUrlHelper;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.edu.kygroup.widget.ScrollViewGridView;
import com.umeng.analytics.MobclickAgent;

public class ResponseActivity extends BaseActivity implements IBindData,
		OnClickListener, CameraPath {
	private int mTotalPage = 1;
	private int mCurPage = 1;
	private ArrayList<Responses> mResponse;
	private RoundAngleImageView mImageView;
	private TextView mNickView;
	private TextView mTimeView;
	private TextView mContentview;
	private EditText mInputText;
	private ListView mResListview;
	private TextView mSendView;
	private Topic mTopic;
	private ImageView img;
	private ScrollViewGridView gridView;
	private ResponseAdapter mAdapter;

	private boolean mIsGetData = false;
	private ScrollListener mScrollListener;

	private Louzhu mResUser;// 要回复的用户
	private User mUser;

	private ImageView img_carera;
	private ImageView img_del;
	// private ScrollView mScrollView;
	private int index = 0;

	private SelectPicPopwindow pop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		getResponse();
	}

	@Override
	protected View setContentView() {
		return mInflater.inflate(R.layout.bbs_comment_layout, null);
	}

	private void initView() {
		img_del = (ImageView) findViewById(R.id.img_del);
		img_carera = (ImageView) findViewById(R.id.img_camera);
		setLeftBtnVisibility(View.GONE);
		setRightBtnVisibility(View.GONE);
		setTitleText(R.string.response);
		mImageView = (RoundAngleImageView) findViewById(R.id.image);
		mNickView = (TextView) findViewById(R.id.nickname);
		mTimeView = (TextView) findViewById(R.id.time);
		mContentview = (TextView) findViewById(R.id.content);
		mInputText = (EditText) findViewById(R.id.input_text);
		mResListview = (ListView) findViewById(R.id.listview);
		mResListview.setCacheColorHint(0);
		mSendView = (TextView) findViewById(R.id.send);
		mSendView.setOnClickListener(this);
		mNickView.setOnClickListener(this);
		mImageView.setOnClickListener(this);
		mScrollListener = new ScrollListener();
		mResListview.setOnScrollListener(mScrollListener);
		img = (ImageView) findViewById(R.id.img);
		gridView = (ScrollViewGridView) findViewById(R.id.imgGridview);
		// mScrollView = (ScrollView) findViewById(R.id.scrollview);
		addTouchListener();
		img_carera.setOnClickListener(this);
	}

	private void setListViewHeight() {
		if (null != mResListview) {
			int height = 0;
			Adapter adapter = mResListview.getAdapter();
			if (null != adapter) {
				int count = adapter.getCount();
				for (int i = 0; i < count; i++) {
					View view = adapter.getView(i, null, mResListview);
					view.measure(0, 0);
					height += view.getMeasuredHeight();
				}
				LayoutParams params = mResListview.getLayoutParams();
				params.height = height + (count - 1)
						* mResListview.getDividerHeight() + 5;
				mResListview.setLayoutParams(params);
			}

		}
	}

	private void initData() {
		mUser = KygroupApplication.mUser;
		mTopic = (Topic) getIntent().getSerializableExtra("mTopic");
		mImageLoader.displayImage(mTopic.getLouzhu().getImage(), mImageView);
		mNickView.setText(mTopic.getLouzhu().getNickname());
		mTimeView.setText(mTopic.getTimestamp());
		mContentview.setText(mTopic.getTitle());
		mResUser = mTopic.getLouzhu();
		setDefaultSelected();
		if (mTopic.getPicsList().size() > 1) {
			if (mTopic.getPicsList().size() > 2) {
				gridView.setNumColumns(3);
			} else {
				gridView.setNumColumns(2);
			}
			gridView.setAdapter(new BBSImgAdapter(this, mTopic.getPicsList()));
			img.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);

		} else if (mTopic.getPicsList().size() == 1) {
			UniversalImageLoadTool.disPlay(mTopic.getPicsList().get(0), img,
					R.drawable.empty_photo);
			img.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
		} else {
			img.setVisibility(View.GONE);
			gridView.setVisibility(View.GONE);
		}
	}

	private void getResponse() {
		startWaitingDialog();
		String url = UrlUtils.GET_POSTER_RESPONSE + "res.tid="
				+ mTopic.getTid() + "&page=" + mCurPage + "&rp=10";
		new NetworkTask().execute(this, TagUtils.GET_RESPOSTER_TAG, url);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		switch (tag) {
		case TagUtils.GET_RESPOSTER_TAG:
			showResponse(obj);
			break;
		case TagUtils.RESPOSTER_TAG:
			resPoster(obj);
			break;
		case TagUtils.GET_DETAIL:
			startUserActivity(obj);
			break;
		default:
			break;
		}
		return null;
	}

	private void startUserActivity(Object obj) {
		closeWaitingDialog();
		if (null != obj) {
			User user = (User) obj;
			Intent intent = new Intent(this, PersonDetailActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
		}
	}

	private void showResponse(Object obj) {
		closeWaitingDialog();
		if (null != obj) {
			ResPoster resPoster = (ResPoster) obj;
			mTotalPage = resPoster.getTotalpage();
			if (null == mResponse) {
				mResponse = new ArrayList<ResPoster.Responses>();
			}
			if (null != resPoster.getResponses()
					&& resPoster.getResponses().size() > 0) {
				mResponse.addAll(resPoster.getResponses());
			}
			if (null == mAdapter) {
				mAdapter = new ResponseAdapter(ResponseActivity.this, mResponse);
				mResListview.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
			mIsGetData = false;
			setListViewHeight();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			addPoster();
			mInputText.clearFocus();
			break;
		case R.id.nickname:
			setDefaultSelected();
			break;
		case R.id.pic:
			getDetail();
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

	private void addPoster() {
		final String str = mInputText.getText().toString().trim();
		if (str.length() == 0) {
			ToastUtils.showShortToast("写点什么吧");
			return;
		}
		startWaitingDialog();
		new Thread() {
			public void run() {
				StringBuffer buf = new StringBuffer(UrlUtils.RES_POSTER);
				buf.append("email=" + mUser.getEmail());
				buf.append("&guest=" + mResUser.getEmail());
				buf.append("&res.tid=" + mTopic.getTid());
				buf.append("&res.content=" + str.trim());
				String url = buf.toString();
				File file = BitmapUtils.getImageFile(selectPicPath);
				final String result = FileUtils.uploadCommentFile(file, url);
				System.out.println("result::::::::::::===--" + result);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeWaitingDialog();
						if (result != null && !"".equals(result)) {
							TopicRetInfo info = (TopicRetInfo) JSON
									.parseObject(result, TopicRetInfo.class);
							resPoster(info);

						}
					}
				});

			}
		}.start();

		// if (!StringUtils.isEmpty(str.trim())) {
		// StringBuffer buf = new StringBuffer(UrlUtils.RES_POSTER);
		// buf.append("email=" + mUser.getEmail());
		// buf.append("&guest=" + mResUser.getEmail());
		// buf.append("&res.tid=" + mTopic.getTid());
		// buf.append("&res.content=" + URLEncoder.encode(str.trim()));
		// String url = buf.toString();
		// startWaitingDialog();
		// new NetworkTask().execute(this, TagUtils.RESPOSTER_TAG, url);
		// } else {
		// Toast.makeText(this, R.string.speak_none, 500).show();
		// }
	}

	private void getDetail() {
		String email = mTopic.getLouzhu().getEmail();
		String memail = KygroupApplication.mUser.getEmail();
		if (email != memail) {
			StringBuffer buf = new StringBuffer(UrlUtils.GET_DETAIL);
			buf.append("user.email=" + mTopic.getLouzhu().getEmail());
			startWaitingDialog();
			Log.v("AAA", buf.toString());
			new NetworkTask()
					.execute(this, TagUtils.GET_DETAIL, buf.toString());
		}
	}

	private void resPoster(Object obj) {
		closeWaitingDialog();
		if (null != obj) {
			System.out.println("result::::::::::::===");
			TopicRetInfo info = (TopicRetInfo) obj;
			int id = info.getTopicid();
			String time = info.getSendtime();
			if (id > 0) {
				Toast.makeText(this, R.string.send_success, 500).show();
				addRespose(id, time, info.getUrl());
				mInputText.setText("");
				return;
			}
		}
		Toast.makeText(this, R.string.tips_send_msg_failed, 500).show();
	}

	private void addRespose(int id, String time, String url) {
		ResPoster pos = new ResPoster();
		Responses res = pos.new Responses();
		res.setContent(mInputText.getText().toString().trim());
		res.setTid(id);
		res.setTimestamp(time);
		res.setUrl(url);
		res.setGuest(mResUser);
		Louzhu host = new Louzhu();
		host.setNickname(mUser.getNickName());
		host.setBatchelorschool(mUser.getESchool());
		host.setImage(mUser.getPic());
		res.setHost(host);
		if (null != mResponse) {
			mResponse.add(0, res);
			if (null != mAdapter) {
				mAdapter.notifyDataSetChanged();
				setListViewHeight();
			}
		}
	}

	class ScrollListener implements OnScrollListener {
		private int mLastVisiblePosition = 0;// 记录下拉到最后一个item的位置
		private int mLastVisiblePositionY = 0;// 记录最后一个item的Y坐标
		private int mLastItem = 0;
		private int mCurPos = 0;
		private boolean mIsTip = false;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			Log.v("AAA", mCurPage + " " + mTotalPage);
			if (mCurPage < mTotalPage
					&& scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				int count = mResListview.getCount();
				if (mLastItem >= count && !mIsGetData) {
					mCurPage++;
					mIsGetData = true;
					getResponse();
				}
			}
			if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
					&& mIsGetData) {
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {// 滚动到底部
					View v = (View) view.getChildAt(view.getChildCount() - 1);// 取得最后一个Item的View
					int[] location = new int[2];// 保存最后一个Item的绝对坐标数组
					v.getLocationOnScreen(location);// 获取最后一个Item在整个屏幕内的绝对坐标
					int y = location[1];// 取得最后一个Item的Y坐标
					if (view.getLastVisiblePosition() != mLastVisiblePosition
							&& mLastVisiblePositionY != y) {// 第一次拖至底部
						mLastVisiblePosition = view.getLastVisiblePosition();
						mLastVisiblePositionY = y;
						return;
					} else if (view.getLastVisiblePosition() == mLastVisiblePosition
							&& mLastVisiblePositionY == y) {// 第二次拖至底部
					}
				}
				// 未滚动到底部，第二次拖至底部都初始化
				mLastVisiblePosition = 0;
				mLastVisiblePositionY = 0;
			}
			// 判断可见的数量是否等于总数量如果等于，则提示没有数据了
			if (mLastItem == mTotalPage && !mIsTip) {
				ToastUtils.showShortToast(R.string.havenodata);
				mIsTip = true;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			mLastItem = firstVisibleItem + visibleItemCount;
			mCurPos = firstVisibleItem;
		}

		public int getCurPosition() {
			return mCurPos;
		}
	}

	public void resSelected(int pos) {
		// TODO Auto-generated method stub
		try {
			if (null != mResponse && mResponse.size() > pos) {
				String str = getString(R.string.response)
						+ mResponse.get(pos).getHost().getNickname();
				mResUser = mResponse.get(pos).getHost();
				mInputText.setHint(str);
			}
		} catch (Exception e) {
		}
	}

	public void setDefaultSelected() {
		try {
			mResUser = mTopic.getLouzhu();
			String str = getString(R.string.response) + mResUser.getNickname();
			// mInputText.setHint(str);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ResponseActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ResponseActivity");
		MobclickAgent.onPause(this);
	}

	private void startUserActivity() {
		if (mTopic.getLouzhu().getEmail().equals(mUser.getEmail())) {

		}
	}

	private void addTouchListener() {
		// mScrollView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_MOVE:
		// index++;
		// break;
		// default:
		// break;
		// }
		// if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
		// index = 0;
		// View view = ((ScrollView) v).getChildAt(0);
		// if (view.getMeasuredHeight() <= v.getScrollY()
		// + v.getHeight()) {
		// // 加载数据代码
		// Log.v("AAA", "====================");
		// if (mCurPage < mTotalPage && !mIsGetData) {
		// mCurPage++;
		// mIsGetData = true;
		// getResponse();
		// }
		// }
		// }
		// return false;
		// }
		// });
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
}
