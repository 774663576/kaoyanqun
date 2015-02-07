package com.edu.kygroup.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.edu.keygroup.chooseimage.RotateImageViewAware;
import com.edu.keygroup.chooseimage.UniversalImageLoadTool;
import com.edu.kygroup.R;
import com.edu.kygroup.popupwindow.SelectPicPopwindow;
import com.edu.kygroup.popupwindow.SelectPicPopwindow.CameraPath;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.edu.kygroup.widget.ScrollViewGridView;
import com.umeng.analytics.MobclickAgent;

public class AuthActivity extends BaseActivity implements OnClickListener,
		CameraPath, OnItemClickListener {

	private TextView edit_content;
	private ScrollViewGridView mGridView;
	private Button btn_upLoad;
	private List<String> photoPathLists = new ArrayList<String>();

	private MyAdapter adapter;

	private SelectPicPopwindow pop;

	private String cameraPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		photoPathLists.add("");
		initView();
	}

	@Override
	protected View setContentView() {
		return LayoutInflater.from(this).inflate(R.layout.auth_activity, null);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// try {
	// Uri uri = data.getData();
	// String[] proj = { MediaStore.Images.Media.DATA };
	// Cursor cursor = managedQuery(uri, proj, null, null, null);
	// int index = cursor
	// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	// cursor.moveToFirst();
	// String path = cursor.getString(index);
	// uploadPic(path);
	// } catch (Exception e) {
	// }
	// }

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText(R.string.auth_post2);
		edit_content = (TextView) findViewById(R.id.content);
		edit_content.setText(getString(R.string.upload_msg1) + "\n"
				+ getString(R.string.upload_msg2) + "\n"
				+ getString(R.string.upload_msg3) + "\n\n\n"
				+ getString(R.string.upload_msg4));
		mGridView = (ScrollViewGridView) findViewById(R.id.imgGridview);
		btn_upLoad = (Button) findViewById(R.id.btnUpload);
		adapter = new MyAdapter();
		mGridView.setAdapter(adapter);
		setListener();
	}

	private void setListener() {
		mGridView.setOnItemClickListener(this);
		btn_upLoad.setOnClickListener(this);
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
				convertView = LayoutInflater.from(AuthActivity.this).inflate(
						R.layout.bbs_publich_grid_item, null);
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
			pop = new SelectPicPopwindow(this, view, 0);
			pop.show();
			pop.setCallBack(this);
		}
	}

	@Override
	public void getCameraPath(String path) {
		cameraPath = path;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpload:
			if (photoPathLists.size() == 1) {
				ToastUtils.showShortToast("只选择一张图片上传");
				return;
			}
			startWaitingDialog();
			uploadFile();
			break;

		default:
			break;
		}

	}

	public void setAuthState(Object t) {
		if (null != t && t.toString().contains("200")) {
			SharedPreferences prefs = getSharedPreferences(
					ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("confirm", 1);
			editor.commit();
			mUser.setConfirm(1);
		}
	}

	// public void uploadPic(String path) {
	// File file = new File(path);
	// if (file.exists()) {
	// uploadFile(path);
	// }
	// }

	public String uploadFile() {
		try {
			// 请求普通信息
			// Bitmap bitmap = BitmapFactory.decodeFile(path);
			// String mPhotoPath = FileUtils.SAVE_FILE_PATH_DIRECTORY + "/"
			// + "auth.png";
			// if (null != bitmap) {
			// FileUtils.saveMyBitmap(bitmap, mPhotoPath, 30);
			// bitmap.recycle();
			AjaxParams params = new AjaxParams();
			params.put("email", KygroupApplication.mUser.getEmail());
			// File file = new File(mPhotoPath);
			for (String path : photoPathLists) {
				if (!"".equals(path)) {
					params.put("image", new File(path));
				}

			}
			// if (file.exists()) {
			// params.put("image", file);
			// } else {
			// params.put("image", new File(path));
			// }
			FinalHttp http = new FinalHttp();
			http.post(UrlUtils.AUTH_PIC_URL, params,
					new AjaxCallBack<Object>() {
						@Override
						public void onStart() {
							super.onStart();
						}

						@Override
						public void onSuccess(Object t) {
							setAuthState(t);
							closeWaitingDialog();
							ToastUtils.showShortToast(R.string.upload_success);
							finish();
							super.onSuccess(t);
						}

						@Override
						public void onLoading(long count, long current) {
							// TODO Auto-generated method stub
							try {
								if (count < current) {
									int progress = (int) (count / current * 100);
									// mPathView.setText(KygroupApplication
									// .getApplication().getText(
									// R.string.upload_progress)
									// + " " + progress + "%");
									System.out
											.println("longding::::::::"
													+ KygroupApplication
															.getApplication()
															.getText(
																	R.string.upload_progress)
													+ " " + progress + "%");
								} else {
									// mPathView.setText(KygroupApplication
									// .getApplication().getText(
									// R.string.upload_success));
									System.out
											.println("longding::::::::==="
													+ KygroupApplication
															.getApplication()
															.getText(
																	R.string.upload_success));
								}
							} catch (Exception e) {
							}

							super.onLoading(count, current);
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method stub
							// mPathView.setText(KygroupApplication
							// .getApplication().getText(
							// R.string.upload_failed));
							closeWaitingDialog();
							ToastUtils.showShortToast(strMsg);
							super.onFailure(t, errorNo, strMsg);
						}
					});
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "OK";
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("AuthActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("AuthActivity");
		MobclickAgent.onPause(this);
	}
}
