package com.edu.kygroup.ui;

import java.lang.reflect.Field;
import java.net.URLEncoder;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.edu.keygroup.selectshcool.ZhuanYe;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.ErrorUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.MD5;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class EditSelfInfoActivity extends BaseActivity implements
		OnClickListener, IBindData {
	public static final int CHOOSE_PHOTO_TAG = 5;
	public static final int EDIT_NAME = 6;

	private RelativeLayout layout_avatar;
	private ImageView img_avatar;
	private RelativeLayout layout_name;
	private TextView txt_name;
	private RelativeLayout layotu_beikao;
	private TextView txt_beikao;
	private RelativeLayout layout_zhankuang;
	private TextView txt_zhangkuang;
	private RelativeLayout layout_jiguan;
	private TextView txt_jiguan;

	private String mPhotoPath = "";

	public User mUser;
	private boolean mProviceSelect = false;
	private boolean mCitySelect = false;

	private String mProvince;
	private String mCity;

	private String mColleageUrl;
	private String mSchoolName = "";

	private String mColleageName = "";

	private String mYearName = "";

	private String mState = "";
	private String mFighting = "";
	private String mProId;
	private String mCityId;

	private String mSchoolId;
	private String mColleageId;
	private String mScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = KygroupApplication.mUser;
		initView();
		initData();
	}

	private void initView() {
		setTitleText("信息修改");
		setRightBtnVisibility(View.VISIBLE);
		setRightBtnBg(R.drawable.submit_selector);
		layout_avatar = (RelativeLayout) findViewById(R.id.layout_avatar);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		layout_name = (RelativeLayout) findViewById(R.id.layout_name);
		txt_name = (TextView) findViewById(R.id.txt_name);
		layotu_beikao = (RelativeLayout) findViewById(R.id.layout_beikao);
		txt_beikao = (TextView) findViewById(R.id.txt_beikao);
		layout_zhankuang = (RelativeLayout) findViewById(R.id.layout_zhankuang);
		txt_zhangkuang = (TextView) findViewById(R.id.txt_zhankuang);
		layout_jiguan = (RelativeLayout) findViewById(R.id.layout_jiguan);
		txt_jiguan = (TextView) findViewById(R.id.txt_jiguan);

		setLisener();
	}

	private void setLisener() {
		layout_avatar.setOnClickListener(this);
		layout_name.setOnClickListener(this);
		layotu_beikao.setOnClickListener(this);
		layout_zhankuang.setOnClickListener(this);
		layout_jiguan.setOnClickListener(this);
		setRightBtnClickListener(this);
	}

	private void initData() {
		mProvince = mUser.getProvince();
		mCity = mUser.getCity();
		mProId = mUser.getProid();
		mCityId = mUser.getCityid();
		mScore = mUser.getScore();
		showHometown(mUser.getCity(), mUser.getProvince());
		txt_name.setText(mUser.getNickName());
		txt_beikao.setText(mUser.getState());
		txt_zhangkuang.setText(mUser.getHowGoing());
		initPicView();
	}

	private void initPicView() {
		if (!StringUtils.isEmpty(mUser.getPic())) {
			if (mUser.getPic().contains("http")) {
				mImageLoader.displayImage(mUser.getPic(), img_avatar, mOptions);
			}
		}
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_edit_self_info, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_avatar:
			selectPicture();
			break;
		case R.id.layout_name:
			Intent intent = new Intent(this, EditNamectivity.class);
			intent.putExtra("name", txt_name.getText().toString());
			startActivityForResult(intent, EDIT_NAME);
			break;
		case R.id.layout_beikao:
			getRemarkState();
			break;
		case R.id.layout_zhankuang:
			getFighttingState();
			break;
		case R.id.layout_jiguan:
			showProvince();

			break;
		case R.id.right_btn:
			alterPerMsg();
			break;
		default:
			break;
		}
	}

	private void showProvince() {
		final String[] provinces = getResources().getStringArray(
				R.array.provinces);
		final Builder builder = new Builder(this);
		builder.setTitle(R.string.home_modify);
		View view = LayoutInflater.from(this).inflate(R.layout.adapter_layout,
				null);
		ListView proviceListView = (ListView) view.findViewById(R.id.province);
		final ListView cityListView = (ListView) view.findViewById(R.id.city);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, provinces);
		proviceListView.setAdapter(adapter);
		int pos = 0;
		if (!StringUtils.isEmpty(mUser.getProvince())) {
			for (int i = 0; i < provinces.length; i++) {
				if (mUser.getProvince().equals(provinces[i])) {
					pos = i;
					break;
				}
			}
		}
		proviceListView.setSelection(pos);
		showCity(cityListView, provinces[pos]);
		builder.setView(view);
		final Dialog dialog = builder.create();
		dialog.show();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (mProviceSelect && !mCitySelect) {
					mProvince = mUser.getProvince();
					mProId = mUser.getProid();
				}
				mProviceSelect = false;
				mCitySelect = false;
			}
		});
		proviceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mProvince = (String) parent.getAdapter().getItem(position);
				mProId = String.valueOf(position);
				showCity(cityListView, mProvince);
				mProviceSelect = true;
			}
		});
		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mCitySelect = true;
				mCity = (String) parent.getAdapter().getItem(position);
				showHometown(mCity, mProvince);
				mCityId = String.valueOf(position);
				dialog.dismiss();
			}
		});
	}

	private void showHometown(String city, String province) {
		if (province.equals(city)) {
			txt_jiguan.setText(city);
		} else {
			txt_jiguan.setText(province + " " + city);
		}
	}

	private void showCity(ListView listView, String province) {
		try {
			Field field = R.array.class.getField(province);
			int id = field.getInt(new R.array());
			String[] cities = getResources().getStringArray(id);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, cities);
			listView.setAdapter(adapter);
		} catch (Exception e) {
		}
	}

	private String formatString(String str, String defStr) {
		String ret = str;
		try {
			if (StringUtils.isEmpty(str)) {
				ret = defStr;
			}
			if (!StringUtils.isEmpty(ret)) {
				ret = URLEncoder.encode(ret, "utf-8");
			}
		} catch (Exception e) {
			ret = "";
		}
		return ret;
	}

	private void getFighttingState() {
		final String[] fighting = getResources().getStringArray(
				R.array.fighting);
		Builder builder = new Builder(this);
		builder.setTitle(R.string.fighting);
		builder.setSingleChoiceItems(fighting, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txt_zhangkuang.setText(fighting[which]);
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void schoolMsg() {
		if (StringUtils.isEmpty(mSchoolName)
				|| StringUtils.isEmpty(mColleageName)) {
			mSchoolName = mUser.getESchool();
			mColleageName = mUser.getECollege();
			// mMajorName = mUser.getEMajor();
			if (StringUtils.isEmpty(mUser.getESchoolid())) {
				mSchoolId = "0";
				mColleageId = "0";
				// mMajorId = "0";
				mYearName = "0";
			}
			mSchoolId = mUser.getESchoolid();
			mColleageId = mUser.getEColleageid();
			// mMajorId = mUser.getEMajorid();
			mYearName = mUser.getEYear();
		} else {
			if (StringUtils.isEmpty(mYearName)) {
				mYearName = mUser.getEYear();
			}
		}
	}

	public void alterPerMsg() {
		try {
			schoolMsg();
			String nick = URLEncoder.encode(txt_name.getText().toString(),
					"utf-8");
			mState = txt_beikao.getText().toString();
			mFighting = txt_zhangkuang.getText().toString();
			String state = formatString(mState, mUser.getState());
			String fightting = formatString(mFighting, mUser.getHowGoing());
			String province = StringUtils.isEmpty(mProId) ? mUser.getProid()
					: mProId;
			String city = StringUtils.isEmpty(mCityId) ? mUser.getCityid()
					: mCityId;
			city = URLEncoder.encode(city, "utf-8");
			String url = UrlUtils.UPDATE_MSG_URL + "user.email="
					+ mUser.getEmail() + "&user.pid=" + province
					+ "&user.city=" + city + "&user.nickname=" + nick
					+ "&major.ssid=" + mSchoolId + "&major.sceid="
					+ mColleageId + "&user.senter=" + mYearName
					+ "&user.status=" + state + "&user.howgoing=" + fightting;
			startWaitingDialog();
			new NetworkTask().execute(this, TagUtils.UPDATE_MSG_TAG, url);
			if (!"".equals(mPhotoPath)) {
				Intent sintent = new Intent(this, KyService.class);
				sintent.putExtra("imgPath", mPhotoPath);
				sintent.putExtra("email", KygroupApplication.mUser.getEmail());
				mUser.setPic(mPhotoPath);
				sintent.putExtra("uploadimg", KyService.UPLOAD_IMG);
				startService(sintent);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRemarkState() {
		final String[] state = getResources().getStringArray(R.array.state);
		Builder builder = new Builder(this);
		builder.setTitle(R.string.remark_state);
		builder.setSingleChoiceItems(state, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						txt_beikao.setText(state[which]);
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case CHOOSE_PHOTO_TAG:
			try {
				mPhotoPath = FileUtils.SAVE_FILE_PATH_DIRECTORY + "/"
						+ "photo.png";
				Bitmap bitmap = data.getParcelableExtra("data");
				if (null != bitmap) {
					FileUtils.saveMyBitmap(bitmap, mPhotoPath, 50);
					img_avatar.setImageBitmap(bitmap);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case EDIT_NAME:
			txt_name.setText(data.getStringExtra("name"));
			break;
		default:
			break;
		}
	}

	private void selectPicture() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("outputX", 160);
		intent.putExtra("outputY", 160);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		startActivityForResult(intent, CHOOSE_PHOTO_TAG);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		closeWaitingDialog();
		if (((String) obj).equals("200")) {
			upDateMsg();
			ToastUtils.showShortToast(R.string.submit_success);
			// Intent intent = new Intent();
			// intent.putExtra("picupdate", mIsUpdateSuccess);
			// setResult(0, intent);
			// finish();
		} else {
			int flag = Integer.parseInt((String) obj);
			System.out.println("flag::::::::::::::::::::" + flag);
			ToastUtils.showShortToast(ErrorUtils.getErrorMsg(flag));
		}
		return null;
	}

	private void upDateMsg() {
		mUser.setState(mState);
		mUser.setNickName(txt_name.getText().toString());
		mUser.setHowGoing(mFighting);
		mUser.setProvince(mProvince);
		mUser.setCity(mCity);
		mUser.setESchool(mSchoolName);
		// mUser.setEMajor(mMajorName);
		mUser.setECollege(mColleageName);
		mUser.setEColleageid(mColleageId);
		// mUser.setEMajorid(mMajorId);
		mUser.setESchoolid(mSchoolId);
		mUser.setEYear(mYearName);
		mUser.setProid(mProId);
		mUser.setCityid(mCityId);
		mUser.setScore(mScore);
		SharedPreferences prefs = getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("nickname", txt_name.getText().toString());
		editor.putString("province", mProvince);
		editor.putString("proid", mProId);
		editor.putString("city", mCity);
		editor.putString("cityid", mCityId);
		editor.putString("majoruni", mSchoolName);
		editor.putString("majorcol", mColleageName);
		// editor.putString("majormaj", mMajorName);
		editor.putString("majoryear", mYearName);
		editor.putString("majoruniid", mSchoolId);
		editor.putString("majorcolid", mColleageId);
		// editor.putString("majormajid", mMajorId);
		editor.putString("howgoing", mFighting);
		editor.putString("state", mState);
		editor.putString("score", mScore);
		editor.commit();
	}
}
