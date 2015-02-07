package com.edu.kygroup.ui;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.popupwindow.SelectCityPopWindow;
import com.edu.kygroup.popupwindow.SelectCityPopWindow.SelectCityOnclick;
import com.edu.kygroup.popupwindow.SelectPicPopwindow;
import com.edu.kygroup.popupwindow.SelectPicPopwindow.CameraPath;
import com.edu.kygroup.popupwindow.SelectPicPopwindow1;
import com.edu.kygroup.popupwindow.SelectPicPopwindow1.SelectMenuOnclick;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.BitmapUtils;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.ErrorUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.MD5;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.CircularImage;

public class PersonalMsgActivity1 extends BaseActivity implements
		OnClickListener, CameraPath, IBindData {
	private CircularImage img_avatar;
	private EditText edit_user_name;
	private TextView txt_sex;
	private TextView txt_sheng;
	private TextView txt_shi;
	private Button btn_finish;
	private SelectPicPopwindow pop;
	private LinearLayout layout_sex;
	private List<String> mCityList = new ArrayList<String>();
	private String[] mProvices;
	private String[] mCities;
	private List<String> mProvicesList = new ArrayList<String>();
	private String mGender = "M";
	private int mmProvice_id = 0;
	private int mCity_id = 0;
	private String phone = "";
	private String password = "";
	private Register mRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRegister = (Register) getIntent().getSerializableExtra("register");
		initView();
		initData();
		phone = getIntent().getStringExtra("phone");
		password = getIntent().getStringExtra("password");
		ActivityHolder.getInstance().addActivity(this);

	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_personal_msg_activity1, null);
	}

	private void initView() {
		setTitleText("注册");
		layout_sex = (LinearLayout) findViewById(R.id.layout_sex);
		img_avatar = (CircularImage) findViewById(R.id.img_avatar);
		edit_user_name = (EditText) findViewById(R.id.edit_user_name);
		txt_sex = (TextView) findViewById(R.id.txt_user_sex);
		txt_sheng = (TextView) findViewById(R.id.sheng);
		txt_shi = (TextView) findViewById(R.id.shi);
		btn_finish = (Button) findViewById(R.id.btn_finish);
		setListener();
	}

	private void setListener() {
		img_avatar.setOnClickListener(this);
		layout_sex.setOnClickListener(this);
		txt_shi.setOnClickListener(this);
		txt_sheng.setOnClickListener(this);
		btn_finish.setOnClickListener(this);
	}

	private void initData() {
		mProvices = getResources().getStringArray(R.array.provinces);
		mProvicesList.addAll(Arrays.asList(mProvices));
		System.out.println("provices:::::::::::::::::"
				+ mProvicesList.toString());
		getCities(mProvicesList.get(0));
	}

	private void getCities(final String province) {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					Field field = R.array.class.getField(province);
					int id = field.getInt(new R.array());
					mCities = getResources().getStringArray(id);
					mCityList.clear();
					mCityList.addAll(Arrays.asList(mCities));
					return true;
				} catch (Exception e) {
				}
				return false;
			}

			protected void onPostExecute(Boolean result) {
				txt_shi.setText(mCityList.get(0));

			};
		}.execute();
	}

	private String selectPicPath = "";

	private void selectAvatar(View v) {
		pop = new SelectPicPopwindow(this, v);
		pop.show();
		pop.setCallBack(this);

	}

	@Override
	public void getCameraPath(String path) {
		selectPicPath = path;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_BYSDCARD
				&& resultCode == RESULT_OK && data != null) {
			selectPicPath = BitmapUtils.startPhotoZoom(this, data.getData());
		}// 拍摄图片
		else if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}
			selectPicPath = BitmapUtils.startPhotoZoom(this,
					Uri.fromFile(new File(selectPicPath)));
		} else if (requestCode == ConstantUtils.REQUEST_CODE_GETIMAGE_DROP
				&& data != null) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				bitmap = photo;
			}
			if (bitmap != null) {
				img_avatar.setImageBitmap(bitmap);
				File file = new File(selectPicPath);
				if (file.exists()) {
					return;
				}
				String name = FileUtils.getFileName() + ".jpg";
				String fileName = FileUtils.getCameraPath() + File.separator
						+ name;
				BitmapUtils.saveFile(bitmap, fileName);
				selectPicPath = fileName;

			}
		}

	}

	private void selectSex(View v) {
		SelectPicPopwindow1 pop = new SelectPicPopwindow1(this, v,
				new String[] { "男", "女" });
		pop.setmSelectOnclick(new SelectMenuOnclick() {

			@Override
			public void onClickPosition(int position) {
				switch (position) {
				case 0:
					txt_sex.setText("男");
					mGender = "M";
					break;
				case 1:
					mGender = "F";
					txt_sex.setText("女");
					break;

				default:
					break;
				}

			}
		});
		pop.show();

	}

	private void selectSheng(View v) {
		SelectCityPopWindow pop = new SelectCityPopWindow(this, v,
				mProvicesList);
		pop.setmSelectOnclick(new SelectCityOnclick() {
			@Override
			public void onClickPosition(int position) {
				mmProvice_id = position;
				txt_sheng.setText(mProvicesList.get(position));
				getCities(mProvicesList.get(position));
			}
		});
		pop.show();
	}

	private void selectShi(View v) {
		SelectCityPopWindow pop = new SelectCityPopWindow(this, v, mCityList);
		pop.setmSelectOnclick(new SelectCityOnclick() {

			@Override
			public void onClickPosition(int position) {
				txt_shi.setText(mCityList.get(position));
				mCity_id = position;
			}
		});
		pop.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_avatar:
			selectAvatar(v);
			break;
		case R.id.layout_sex:
			selectSex(v);
			break;
		case R.id.sheng:
			selectSheng(v);
			break;
		case R.id.shi:
			selectShi(v);
			break;
		case R.id.btn_finish:
			save();
			break;
		default:
			break;
		}
	}

	private String mNickName;

	private void saveRegisterMsg() {
		KygroupApplication.mUser.setNickName(mNickName);
		KygroupApplication.mUser.setGender(mGender);
		KygroupApplication.mUser.setProvince(txt_sheng.getText().toString());
		KygroupApplication.mUser.setCity(txt_shi.getText().toString());
		KygroupApplication.mUser.setProid(mmProvice_id + "");
		KygroupApplication.mUser.setCityid(mCity_id + "");
		SharedPreferences prefs = getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("nickname", KygroupApplication.mUser.getNickName());
		editor.putString("gender", KygroupApplication.mUser.getGender());
		editor.putString("province", KygroupApplication.mUser.getProvince());
		editor.putString("city", KygroupApplication.mUser.getCity());
		editor.putString("pic", selectPicPath);
		editor.putString("proid", KygroupApplication.mUser.getProid());
		editor.putString("cityid", KygroupApplication.mUser.getCityid());
		editor.commit();
	}

	private void save() {
		mNickName = edit_user_name.getText().toString();
		if (!TextUtils.isEmpty(mNickName)) {
			try {
				saveRegisterMsg();
				System.out.println("pic:::::::::::::::::" + selectPicPath);
				if (!"".equals(selectPicPath)) {
					Intent sintent = new Intent(this, KyService.class);
					sintent.putExtra("imgPath", selectPicPath);
					sintent.putExtra("email", MD5.Md5_16(phone));
					mUser.setPic(selectPicPath);
					sintent.putExtra("uploadimg", KyService.UPLOAD_IMG);
					startService(sintent);
					request(mUser.getEmail(), mUser.getPassword());
				} else {
					ToastUtils.showShortToast(PersonalMsgActivity1.this,
							R.string.login_no_picture);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			ToastUtils.showShortToast(PersonalMsgActivity1.this,
					R.string.complete_msg);
		}
	}

	public void request(String email, String pwd) {
		startWaitingDialog();
		try {
			String nick = URLEncoder.encode(mNickName, "utf-8");
			String url = UrlUtils.REGISTER_URL + "user.email="
					+ mRegister.getRegister_email() + "&user.password="
					+ mRegister.getRegister_passwrod() + "&aim.sid="
					+ mRegister.getRegister_Sid() + "&aim.ceid="
					+ mRegister.getRegister_Cid() + "&aim.mid="
					+ mRegister.getRegister_Mid() + "&user.session="
					+ mRegister.getRegister_year() + "&user.role="
					+ mRegister.getRegister_role() + "&user.pid="
					+ mmProvice_id + "&user.city=" + mCity_id + "&user.gender="
					+ mGender + "&user.nickname=" + nick;
			new NetworkTask().execute(this, TagUtils.REGISTER_TAG, url,
					mRegister);
		} catch (Exception e) {
		}
	}

	private void saveRegisterMsg(final long timestamp) {
		new Thread() {
			public void run() {
				SharedPreferences prefs = getSharedPreferences(
						ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.putString("email", phone);
				editor.putString("regcol",
						mRegister.getRegister_Colleage_name());
				editor.putString("regmaj", mRegister.getRegister_Major_name());
				editor.putString("regyea", mRegister.getRegister_year());
				editor.putString("reguni", mRegister.getRegister_School_name());
				editor.putString("reguniid", mRegister.getRegister_Sid());
				editor.putString("regmajid", mRegister.getRegister_Mid());
				editor.putString("regcolid", mRegister.getRegister_Cid());
				editor.putInt("role",
						Integer.valueOf(mRegister.getRegister_role()));
				editor.putLong("timestamp", timestamp);
				editor.commit();
				// ActivityHolder.getInstance().finishAllActivity();
				addAdminUser();// 添加
			};
		}.start();
	}

	public void addAdminUser() {
		MessageBean bean = new MessageBean();
		Context context = KygroupApplication.getApplication();
		bean.setFriendName(context.getString(R.string.admin_user));
		bean.setFriends_email(context.getString(R.string.admin_mail));
		bean.setMsg_content(context.getString(R.string.admin_support));
		bean.setMsg_count(0);
		bean.setImg("admin");
		bean.setRead("0");
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		closeWaitingDialog();
		if (tag == TagUtils.REGISTER_TAG) {
			if (null != mRegister && null != mRegister.getMegMsg()) {
				if (mRegister.getMegMsg().equals("200")) {
					mUser.setEmail(mUser.getEmail());
					saveRegisterMsg(mRegister.getCurTime());
					saveRegisterMsg();
					finish();
					ToastUtils.showShortToast(this, "注册成功");

					// loginHuanxin(phone, password);
				} else if (mRegister.getMegMsg().equals("-2")) {
					ToastUtils.showShortToast(this, "手机号已注册，请登录");
				} else {
					ToastUtils.showShortToast(this,
							ErrorUtils.getErrorMsg(Integer.valueOf(mRegister
									.getMegMsg())));
				}
			} else {
				ToastUtils.showShortToast(this, R.string.register_failed);
			}
		}
		return null;
	}

	private void loginHuanxin(final String mRegister, final String password) {
		System.out.println("passsros::::::::::::::::" + mRegister + "      "
				+ password);
		new Thread() {
			public void run() {
				// 调用sdk登陆方法登陆聊天服务器
				EMChatManager.getInstance().login(mRegister, password,
						new EMCallBack() {

							@Override
							public void onSuccess() {
								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(getApplicationContext(),
												"登录成功", 0).show();
										Intent intent = new Intent(
												PersonalMsgActivity1.this,
												HomeActivity.class);
										startActivity(intent);
										finish();
									}
								});
							}

							@Override
							public void onProgress(int progress, String status) {

							}

							@Override
							public void onError(int code, final String message) {
								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(getApplicationContext(),
												"登录失败: " + message, 0).show();

									}
								});

							}
						});
			}
		}.start();
	}
}
