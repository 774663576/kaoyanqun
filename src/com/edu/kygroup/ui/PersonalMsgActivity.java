package com.edu.kygroup.ui;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.ErrorUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

public class PersonalMsgActivity extends BaseActivity implements
		OnClickListener, OnItemSelectedListener, IBindData {
	private final static int CHOOSE_PHOTO_TAG = 1;
	private Register mRegister;
	private boolean mIsUpdateBitmap = false; // 判断是否上传头像
	private boolean mIsUpdateSuccess = false;
	private String mPhotoPath = "";
	private Spinner mProvinceSpinner;
	private Spinner mCitiesSpinner;
	private ImageView mPhoto;
	private ArrayAdapter<String> mProAdatper;
	private ArrayAdapter<String> mCitAdapter;
	private String[] mProvices;
	private String[] mCities;
	private ArrayList<String> mCityList;
	private EditText mNickEdit;
	private String mNickName;
	private TextView mMaleView;
	private TextView mFemaleView;
	private String mGender = "M";
	private int mPid = 0;
	private int mCid = 0;
	private String phone = "";
	private String password = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phone = getIntent().getStringExtra("phone");
		password = getIntent().getStringExtra("password");
		initView();
		initData1();
		initData();
		addListener();
		ActivityHolder.getInstance().addActivity(this);
	}

	private void initData1() {
		mRegister = new Register();
	}

	@Override
	protected View setContentView() {
		return mInflater.inflate(R.layout.personal_activity, null);
	}

	private void initView() {
		setTitleText(R.string.personal_msgs);
		setLeftBtnVisibility(View.GONE);
		setRightBg(R.drawable.save_msg);
		mProvinceSpinner = (Spinner) findViewById(R.id.province);
		mCitiesSpinner = (Spinner) findViewById(R.id.cities);
		mPhoto = (ImageView) findViewById(R.id.photo);
		mNickEdit = (EditText) findViewById(R.id.nick_input);
		mMaleView = (TextView) findViewById(R.id.male);
		mFemaleView = (TextView) findViewById(R.id.female);
	}

	private void initData() {
		mProvices = getResources().getStringArray(R.array.provinces);
		mProAdatper = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mProvices);
		// 设置下拉列表的风格
		mProAdatper
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mProvinceSpinner.setAdapter(mProAdatper);
		String province = mProvinceSpinner.getSelectedItem().toString();
		mCityList = new ArrayList<String>();
		getCities(province);
	}

	private void addListener() {
		mPhoto.setOnClickListener(this);
		mProvinceSpinner.setOnItemSelectedListener(this);
		mCitiesSpinner.setOnItemSelectedListener(this);
		setRightBtnClickListener(this);
		mMaleView.setOnClickListener(this);
		mFemaleView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.photo: {
			Intent intent = new Intent();
			/* 开启Pictures画面Type设定为image */
			intent.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent.setAction(Intent.ACTION_GET_CONTENT);
			/* 出现截取界面 */
			intent.putExtra("crop", "true");
			/* 保存到SD */
			intent.putExtra("outputFormat",
					Bitmap.CompressFormat.JPEG.toString());
			/* 设置图片像素 */
			intent.putExtra("outputX", 160);
			intent.putExtra("outputY", 160);
			/* 设置比例 1:1 */
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			startActivityForResult(intent, CHOOSE_PHOTO_TAG);
			break;
		}

		case R.id.right_btn: {
			mNickName = mNickEdit.getText().toString();
			if (!TextUtils.isEmpty(mNickName)) {
				try {
					// startWaitingDialog();
					// String nick = URLEncoder.encode(mNickName, "utf-8");
					// String url =
					// UrlUtils.PERSONAL_MSG_URL+"user.pid="+mPid+"&user.city="+mCid+"&user.gender="+mGender
					// +"&user.nickname="+nick+"&user.email="+mUser.getEmail();
					// startWaitingDialog();
					// String url =
					// UrlUtils.REGISTER_URL+"user.email="+mUser.getEmail()+"&user.password="+pwd+"&aim.sid="
					// +mUser.getRSid()+"&aim.ceid="+mUser.getRCid()+"&aim.mid="+mUser.getRmid()+"&user.session="
					// +mUser.getRYear()+"&user.role="+mUser.getRole()+"user.pid="+mPid+"&user.city="+mCid+"&user.gender="+mGender
					// +"&user.nickname="+nick;
					// new
					// NetworkTask().execute(this,TagUtils.REGISTER_TAG,url,mRegister);
					//
					// new
					// NetworkTask().execute(PersonalMsgActivity.this,TagUtils.REPORT_PERSONAL_MSG,url);
					saveRegisterMsg();
					if (mIsUpdateBitmap) {
						Intent sintent = new Intent(this, KyService.class);
						sintent.putExtra("imgPath", mPhotoPath);
						mUser.setPic(mPhotoPath);
						sintent.putExtra("uploadimg", KyService.UPLOAD_IMG);
						startService(sintent);

					}
					if (mIsUpdateBitmap) {
						request(mUser.getEmail(), mUser.getPassword());
					} else {
						ToastUtils.showShortToast(PersonalMsgActivity.this,
								R.string.login_no_picture);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				ToastUtils.showShortToast(PersonalMsgActivity.this,
						R.string.complete_msg);
			}
			break;
		}
		case R.id.male:
			mGender = "M";
			setGenderBg();
			break;
		case R.id.female:
			mGender = "F";
			setGenderBg();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CHOOSE_PHOTO_TAG) {
			try {
				mPhotoPath = FileUtils.SAVE_FILE_PATH_DIRECTORY + "/"
						+ "photo.png";
				Bitmap bitmap = data.getParcelableExtra("data");
				if (null != bitmap) {
					FileUtils.saveMyBitmap(bitmap, mPhotoPath, 50);
					mPhoto.setImageBitmap(bitmap);
					mUser.setPic(mPhotoPath);
					mIsUpdateBitmap = true;
					mIsUpdateSuccess = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent.getAdapter().equals(mProAdatper)) {
			mPid = position;
			String province = (String) parent.getAdapter().getItem(position);
			getCities(province);
		} else {
			mCid = position;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	private void setGenderBg() {
		if (mGender.equals("M")) {
			mMaleView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.male_select, 0);
			mFemaleView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.female_default, 0);
			mMaleView.setTextColor(Color.BLUE);
			mFemaleView.setTextColor(R.color.title_text_color);
		} else {
			mMaleView.setTextColor(R.color.title_text_color);
			mMaleView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.male_default, 0);
			mFemaleView.setTextColor(Color.RED);
			mFemaleView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.female_select, 0);
		}
	}

	// private void startActivity(){
	// Intent intent = new Intent(this,RegisterActivity.class);
	// startActivity(intent);
	// }

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
				if (result) {
					if (null == mCitAdapter) {
						mCitAdapter = new ArrayAdapter<String>(
								PersonalMsgActivity.this,
								android.R.layout.simple_spinner_item, mCityList);
						mCitAdapter
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						mCitiesSpinner.setAdapter(mCitAdapter);
					} else {
						mCitAdapter.notifyDataSetChanged();
					}
					mCitiesSpinner.setVisibility(View.VISIBLE);
				} else {
					mCitiesSpinner.setVisibility(View.GONE);
				}
			};
		}.execute();
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
					mIsUpdateBitmap = false;
					loginHuanxin(phone, password);
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
												PersonalMsgActivity.this,
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

	private void saveRegisterMsg() {
		KygroupApplication.mUser.setNickName(mNickName);
		KygroupApplication.mUser.setGender(mGender);
		KygroupApplication.mUser.setProvince(mProvinceSpinner.getSelectedItem()
				.toString());
		KygroupApplication.mUser.setCity(mCitiesSpinner.getSelectedItem()
				.toString());
		KygroupApplication.mUser.setProid(mPid + "");
		KygroupApplication.mUser.setCityid(mCid + "");
		SharedPreferences prefs = getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("nickname", KygroupApplication.mUser.getNickName());
		editor.putString("gender", KygroupApplication.mUser.getGender());
		editor.putString("province", KygroupApplication.mUser.getProvince());
		editor.putString("city", KygroupApplication.mUser.getCity());
		editor.putString("pic", mPhotoPath);
		editor.putString("proid", KygroupApplication.mUser.getProid());
		editor.putString("cityid", KygroupApplication.mUser.getCityid());
		editor.commit();
	}

	private void saveRegisterMsg(final long timestamp) {
		new Thread() {
			public void run() {
				SharedPreferences prefs = getSharedPreferences(
						ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.putString("email", phone);
				editor.putString("regcol", mUser.getRCollege());
				editor.putString("regmaj", mUser.getRMajor());
				editor.putString("regyea", mUser.getRYear());
				editor.putString("reguni", mUser.getRSchool());
				editor.putString("reguniid", mUser.getRSid());
				editor.putString("regmajid", mUser.getRmid());
				editor.putString("regcolid", mUser.getRCid());
				editor.putInt("role", mUser.getRole());
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ActivityHolder.getInstance().removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("PersonalMsgActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("PersonalMsgActivity");
		MobclickAgent.onPause(this);
	}

	public void request(String email, String pwd) {
		startWaitingDialog();
		try {
			String nick = URLEncoder.encode(mUser.getNickName(), "utf-8");
			String url = UrlUtils.REGISTER_URL + "user.email=" + phone
					+ "&user.password=" + password + "&aim.sid="
					+ mUser.getRSid() + "&aim.ceid=" + mUser.getRCid()
					+ "&aim.mid=" + mUser.getRmid() + "&user.session="
					+ mUser.getRYear() + "&user.role=" + mUser.getRole()
					+ "&user.pid=" + mUser.getProid() + "&user.city="
					+ mUser.getCityid() + "&user.gender=" + mUser.getGender()
					+ "&user.nickname=" + nick;
			new NetworkTask().execute(this, TagUtils.REGISTER_TAG, url,
					mRegister);
		} catch (Exception e) {
		}
	}

}
