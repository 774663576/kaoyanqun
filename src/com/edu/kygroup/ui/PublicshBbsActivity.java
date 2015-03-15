package com.edu.kygroup.ui;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edu.keygroup.chooseimage.RotateImageViewAware;
import com.edu.keygroup.chooseimage.UniversalImageLoadTool;
import com.edu.keygroup.selectshcool.ZhuanYe;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.TopicRetInfo;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.popupwindow.SelectPicPopwindow;
import com.edu.kygroup.popupwindow.SelectPicPopwindow.CameraPath;
import com.edu.kygroup.popupwindow.TaskZhuTiPopwindow;
import com.edu.kygroup.utils.BitmapUtils;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.edu.kygroup.widget.ScrollViewGridView;

public class PublicshBbsActivity extends BaseActivity implements
		OnItemClickListener, CameraPath, OnClickListener {
	private Button btnPublish;
	private EditText content;// 内容输入框
	private ScrollViewGridView mGridView;

	private List<String> photoPathLists = new ArrayList<String>();
	private ArrayList<String> picLists = new ArrayList<String>();

	private MyAdapter adapter;

	private SelectPicPopwindow pop;

	private String cameraPath = "";
	private FocusInfo mFocusInfo;

	private int tid;
	private String time = "";
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				closeWaitingDialog();
				Toast.makeText(PublicshBbsActivity.this, R.string.send_success,
						500).show();
				setResultActivity();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		mFocusInfo = (FocusInfo) getIntent().getSerializableExtra("focusInfo");

	}

	private void initView() {
		btnPublish = (Button) findViewById(R.id.btnUpload);
		content = (EditText) findViewById(R.id.content);
		photoPathLists.add("");
		setLeftBtnVisibility(View.GONE);
		setTitleText("发帖");
		mGridView = (ScrollViewGridView) findViewById(R.id.imgGridview);
		adapter = new MyAdapter();
		mGridView.setAdapter(adapter);
		setListener();
	}

	private void setListener() {
		mGridView.setOnItemClickListener(this);
		btnPublish.setOnClickListener(this);

	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_publicsh_bbs, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_BYSDCARD_MORE) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) bundle
						.getSerializable("imgPath");
				for (String m : list) {
					photoPathLists.add(photoPathLists.size() - 1, m);
				}
			}
		}
		// 拍摄图片
		else if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}
			File file = new File(cameraPath);
			if (!file.exists()) {
				ToastUtils.showShortToast("图片获取失败，请重新获取");
				return;
			}

			photoPathLists.add(photoPathLists.size() - 1, cameraPath);
		}
		adapter.notifyDataSetChanged();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return photoPathLists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(PublicshBbsActivity.this)
						.inflate(R.layout.bbs_publich_grid_item, null);
				holder.img = (RoundAngleImageView) convertView
						.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == photoPathLists.size() - 1) {
				UniversalImageLoadTool.disPlay("file://" + "",
						new RotateImageViewAware(holder.img, ""),
						R.drawable.add_pic);
			} else {
				String path = photoPathLists.get(position);
				UniversalImageLoadTool.disPlay("file://" + path,
						new RotateImageViewAware(holder.img, path),
						R.drawable.empty_photo);
			}
			return convertView;
		}
	}

	class ViewHolder {
		RoundAngleImageView img;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == photoPathLists.size() - 1) {
			if (photoPathLists.size() == 10) {
				ToastUtils.showShortToast("一次最多允许发布9张图片");
				return;
			}
			pop = new SelectPicPopwindow(this, view, photoPathLists.size() - 1);
			pop.show();
			pop.setCallBack(this);
		}
	}

	@Override
	public void getCameraPath(String path) {
		cameraPath = path;
	}

	private void addBBS() {
		String str = content.getText().toString();
		if (!StringUtils.isEmpty(str.trim())) {
			StringBuffer buf = new StringBuffer(UrlUtils.ADD_POSTER);
			buf.append("email=" + mUser.getEmail());
			buf.append("&topic.sid=" + mFocusInfo.getmSid());
			buf.append("&topic.ceid=" + mFocusInfo.getmCid());
			buf.append("&topic.mid=" + mFocusInfo.getmMid());
			buf.append("&topic.title=" + URLEncoder.encode(str.trim()));
			String url = buf.toString();
			startWaitingDialog();
			NetworkTask task = new NetworkTask();
			task.setmFinish(new GetFinish() {

				@Override
				public void finish(Object obj) {
					if (null != obj) {
						final TopicRetInfo info = (TopicRetInfo) obj;
						tid = info.getTopicid();
						time = info.getSendtime();
						// if (id > 0) {
						if (photoPathLists.size() <= 1) {
							closeWaitingDialog();
							Toast.makeText(PublicshBbsActivity.this,
									R.string.send_success, 500).show();
							setResultActivity();
							return;
						}

						new Thread() {
							public void run() {
								String result = addPic(info);
								getPicResult(result);
								mHandler.sendEmptyMessage(0);
							}
						}.start();
						return;
						// }
					}
					Toast.makeText(PublicshBbsActivity.this,
							R.string.tips_send_msg_failed, 500).show();
				}
			});
			task.execute(null, TagUtils.TOPIC_RET_TAG, url);
		} else {
			Toast.makeText(this, R.string.speak_none, 500).show();
		}
	}

	private void setResultActivity() {
		Intent intent = new Intent();
		intent.putExtra("content", content.getText().toString());
		intent.putStringArrayListExtra("picList", picLists);
		intent.putExtra("tid", tid);
		intent.putExtra("time", time);
		setResult(100, intent);
		finish();
	}

	private String addPic(TopicRetInfo info) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tid", info.getTopicid());
		List<File> fileList = new ArrayList<File>();
		for (String path : photoPathLists) {
			if ("".equals(path))
				continue;
			File file = BitmapUtils.getImageFile(path);
			// File file = new File(path);
			if (file != null && file.exists()) {
				fileList.add(file);
			}
		}
		// return FileUtils.upLoadPicArray(UrlUtils.BASE_URL +
		// "/exam/uploadpic",
		// map, fileList, "uploads");
		return FileUtils.upLoadPicArray(UrlUtils.BASE_URL
				+ "/exam/servlet/UploadTopic", map, fileList, "uploads");
	}

	private void getPicResult(String jsonStr) {
		System.out.println("result::::::::::::POic::res--" + jsonStr);

		if (jsonStr == null) {
			return;
		}
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray jsArr = json.getJSONArray("urls");
			for (int i = 0; i < jsArr.length(); i++) {
				picLists.add(jsArr.getString(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpload:
			addBBS();
			break;
		default:
			break;
		}
	}
}
