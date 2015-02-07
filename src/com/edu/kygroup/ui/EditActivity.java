package com.edu.kygroup.ui;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Colleage;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.ErrorUtils;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class EditActivity extends BaseActivity implements OnClickListener,
		IBindData, TextWatcher {
	public static final int SCHOOL_TAG = 1;
	public static final int COLLEAGE_TAG = 2;
	public static final int MAJOR_TAG = 3;
	public static final int YEAR_TAG = 4;
	public static final int CHOOSE_PHOTO_TAG = 5;
	public static final int TEXT_MAX = 40;
	private ImageView mPicView;
	private TextView mNickView;
	private TextView mHometownView;
	private TextView mSchoolView;
	private TextView mColleageView;
	private TextView mYearView;
	private TextView mStateView;
	private TextView mFightingView;
	private RelativeLayout mNickLayout;
	private RelativeLayout mHomeLayout;
	private RelativeLayout mSchoolLayout;
	private RelativeLayout mColleageLayout;
	private RelativeLayout mYearLayout;
	private RelativeLayout mStateLayout;
	private RelativeLayout mFightingLayout;
	private RelativeLayout mScoreLayout;

	private Bitmap mPicBitmap;
	private Timer mTimer;

	private User mEditUser;

	private String mProvince;
	private String mCity;

	private String mColleageUrl;
	private String mSchoolName = "";

	private String mColleageName = "";

	private String mYearName = "";

	private String mState = "";
	private String mFighting = "";

	private EditText mAnnounceEdit;

	private CharSequence temp;
	private int selectionStart;
	private int selectionEnd;

	private String mPhotoPath = "";
	private ScrollView mScrollView;

	private String mProId;
	private String mCityId;

	private String mSchoolId;
	private String mColleageId;
	private String mAnnounce;
	private String mScore;

	private boolean mIsUpdateBitmap = false;

	private ImageView mSaveMsg;

	private boolean mProviceSelect = false;
	private boolean mCitySelect = false;

	private EditText mSubText1;
	private EditText mSubText2;
	private EditText mSubText3;
	private EditText mSubText4;
	private TextView mTotalScroeView;
	private boolean mIsUpdateSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		addListener();
		getSubjectNum();
	}

	private void initView() {
		setTitleBarVisibility(View.GONE);
		mPicView = (ImageView) findViewById(R.id.pic);
		mNickView = (TextView) findViewById(R.id.edit_nickname);
		mHometownView = (TextView) findViewById(R.id.edit_hometown);
		mSchoolView = (TextView) findViewById(R.id.edit_school);
		mColleageView = (TextView) findViewById(R.id.edit_colleage);
		mYearView = (TextView) findViewById(R.id.edit_year);
		mStateView = (TextView) findViewById(R.id.edit_state);
		mFightingView = (TextView) findViewById(R.id.edit_fighting);
		mNickLayout = (RelativeLayout) findViewById(R.id.nickname_layout);
		mHomeLayout = (RelativeLayout) findViewById(R.id.hometown_layout);
		mSchoolLayout = (RelativeLayout) findViewById(R.id.school_layout);
		mColleageLayout = (RelativeLayout) findViewById(R.id.colleage_layout);
		mYearLayout = (RelativeLayout) findViewById(R.id.year_layout);
		mFightingLayout = (RelativeLayout) findViewById(R.id.fighting_layout);
		mScoreLayout = (RelativeLayout) findViewById(R.id.score_layout);
		mStateLayout = (RelativeLayout) findViewById(R.id.state_layout);
		mAnnounceEdit = (EditText) findViewById(R.id.edit_announce);
		mScrollView = (ScrollView) findViewById(R.id.scroll_view);
		mSaveMsg = (ImageView) findViewById(R.id.save_msg);
		mSubText1 = ((EditText) findViewById(R.id.subject1));
		mSubText2 = ((EditText) findViewById(R.id.subject2));
		mSubText3 = ((EditText) findViewById(R.id.subject3));
		mSubText4 = ((EditText) findViewById(R.id.subject4));
		mTotalScroeView = (TextView) findViewById(R.id.total_score);
		initExamMsg();
		initSubLayout();
	}

	private void initData() {
		initPicView();
		showHometown(mUser.getCity(), mUser.getProvince());
		if (!StringUtils.isEmpty(mUser.getEYear())) {
			mYearView.setText((mUser.getEYear().equals("0")) ? "" : mUser
					.getEYear());
		}
		setString(mStateView, mUser.getState());
		setString(mFightingView, mUser.getHowGoing());
		setString(mAnnounceEdit, mUser.getAnnounce());
		setString(mColleageView, mUser.getECollege());
		setString(mSchoolView, mUser.getESchool());
		setString(mNickView, mUser.getNickName());
		mEditUser = new User();
		mColleageLayout.setEnabled(false);
		if (TextUtils.isEmpty(mUser.getESchool())) {
			setLayoutIsEnable(false, false);
		} else {
			setLayoutIsEnable(true, true);
		}
		mProvince = mUser.getProvince();
		mCity = mUser.getCity();
		mProId = mUser.getProid();
		mCityId = mUser.getCityid();
		mScore = mUser.getScore();
		setScores();
	}

	private void addListener() {
		mPicView.setOnClickListener(this);
		mNickLayout.setOnClickListener(this);
		mHomeLayout.setOnClickListener(this);
		mSchoolLayout.setOnClickListener(this);
		mColleageLayout.setOnClickListener(this);
		mYearLayout.setOnClickListener(this);
		mStateLayout.setOnClickListener(this);
		mFightingLayout.setOnClickListener(this);
		mSaveMsg.setOnClickListener(this);
		addEditListener();
	}

	private void getSubjectNum() {
		if (mUser.getRole() != 0) {
			startWaitingDialog();
			String url = UrlUtils.DETAILS_URL + "major.sid=" + mUser.getRSid()
					+ "&major.ceid=" + mUser.getRCid() + "&major.mid="
					+ mUser.getRmid();
			new NetworkTask().execute(this, TagUtils.DETAILS_TAG, url);
		}
	}

	private void initPicView() {
		if (!StringUtils.isEmpty(mUser.getPic())) {
			if (mUser.getPic().contains("http")) {
				mImageLoader.displayImage(mUser.getPic(), mPicView, mOptions);
			} else {
				if (null == mPicBitmap) {
					mPicBitmap = FileUtils.getLoacalBitmap(mUser.getPic());
				}
				mPicView.setImageBitmap(mPicBitmap);
			}
		}
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.edit_activity, null);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != mPicBitmap) {
			mPicBitmap.recycle();
			mPicBitmap = null;
		}
		if (null != mTimer) {
			mTimer.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pic:
			selectPicture();
			break;
		case R.id.nickname_layout:
			alterNickName();
			break;
		case R.id.hometown_layout:
			showProvince();
			break;
		case R.id.school_layout:
			getSchoolMsg(SCHOOL_TAG, new SelectActivity());
			break;
		case R.id.colleage_layout:
			startWaitingDialog();
			if (StringUtils.isEmpty(mColleageUrl)) {
				Log.v("url", "to get colleges");
				System.out.println("get  sid=" + mUser.getESchoolid());
				mColleageUrl = UrlUtils.GET_COLLEAGE_URL + "sid="
						+ mUser.getESchoolid() + "&kind=1";
				System.out.println("print url=" + mColleageUrl);
			}
			new NetworkTask().execute(EditActivity.this,
					TagUtils.GET_COLLEAGE_TAG, mColleageUrl);
			break;
		case R.id.year_layout:
			KygroupApplication.mFlag = 3;
			getSchoolMsg(YEAR_TAG, new YearsActivity());
			break;
		case R.id.state_layout:
			getRemarkState();
			break;
		case R.id.fighting_layout:
			getFighttingState();
			break;
		case R.id.save_msg:
			alterPerMsg();
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

	private void getFighttingState() {
		final String[] fighting = getResources().getStringArray(
				R.array.fighting);
		Builder builder = new Builder(this);
		builder.setTitle(R.string.fighting);
		builder.setSingleChoiceItems(fighting, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mFighting = fighting[which];
						mFightingView.setText(mFighting);
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	private void getRemarkState() {
		final String[] state = getResources().getStringArray(R.array.state);
		Builder builder = new Builder(this);
		builder.setTitle(R.string.remark_state);
		builder.setSingleChoiceItems(state, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mState = state[which];
						mStateView.setText(mState);
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case SCHOOL_TAG:
			mColleageUrl = data.getStringExtra("url") + "&kind=1";
			if (null == mSchoolName
					|| !mSchoolName.equals(data.getStringExtra("school"))) {
				mColleageView.setText("");
			}
			mSchoolName = data.getStringExtra("school");
			mSchoolId = data.getStringExtra("schoolid");
			mSchoolView.setText(mSchoolName);
			setLayoutIsEnable(true, false);
			break;
		case COLLEAGE_TAG:
			if (StringUtils.isEmpty(mSchoolId)) {
				mSchoolId = mUser.getESchoolid();
			}
			mColleageId = data.getStringExtra("colleageid");
			mColleageName = data.getStringExtra("colleage");
			mColleageView.setText(mColleageName);
			setLayoutIsEnable(true, true);
		case YEAR_TAG:
			mYearName = data.getStringExtra("year");
			mYearView.setText(mYearName);
			break;
		default:
			break;
		}
		switch (requestCode) {
		case CHOOSE_PHOTO_TAG:
			try {
				mPhotoPath = FileUtils.SAVE_FILE_PATH_DIRECTORY + "/"
						+ "photo.png";
				Bitmap bitmap = data.getParcelableExtra("data");
				if (null != bitmap) {
					FileUtils.saveMyBitmap(bitmap, mPhotoPath, 50);
					mPicView.setImageBitmap(bitmap);
					mIsUpdateBitmap = true;
					mIsUpdateSuccess = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	private void getSchoolMsg(int flag, Activity activity) {
		Intent intent = new Intent(this, activity.getClass());
		intent.putExtra("edit", true);
		startActivityForResult(intent, flag);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	private void alterNickName() {
		Builder builder = new Builder(this);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.alter_nick, null);
		final EditText inputView = (EditText) view
				.findViewById(R.id.input_nick);
		inputView.setSelection(inputView.length());
		inputView.setText(inputView.getText().toString());
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String nick = inputView.getText().toString();
						if (!StringUtils.isEmpty(nick)) {
							mEditUser.setNickName(nick);
							mNickView.setText(nick);
							mScrollView.scrollTo(0, 0);
						}
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.setView(view);
		builder.create().show();
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
		if (!StringUtils.isEmpty(mProvince)) {
			for (int i = 0; i < provinces.length; i++) {
				if (mProvince.equals(provinces[i])) {
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
				mEditUser.setProvince(mProvince);
				mEditUser.setCity(mCity);
				showHometown(mCity, mProvince);
				mCityId = String.valueOf(position);
				dialog.dismiss();
			}
		});
	}

	private void showHometown(String city, String province) {
		if (province.equals(city)) {
			mHometownView.setText(city);
		} else {
			mHometownView.setText(province + " " + city);
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

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		if (tag == TagUtils.GET_COLLEAGE_TAG) {
			closeWaitingDialog();
			if (null != obj) {
				ArrayList<Colleage> lists = (ArrayList<Colleage>) obj;
				if (lists.size() > 0) {
					Intent intent = new Intent(EditActivity.this,
							ColleageActivity.class);
					intent.putExtra("colleages", (Serializable) lists);
					intent.putExtra("edit", true);
					startActivityForResult(intent, COLLEAGE_TAG);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
				} else {
					ToastUtils.showShortToast(R.string.request_failed);
				}
			} else {
				ToastUtils.showShortToast(R.string.request_failed);
			}
		} else if (tag == TagUtils.GET_MAJOR_TAG) {
			closeWaitingDialog();
			if (null != obj) {
				ArrayList<Colleage> lists = (ArrayList<Colleage>) obj;
				if (lists.size() > 0) {
					Intent intent = new Intent(EditActivity.this,
							MajorActivity.class);
					intent.putExtra("majors", (Serializable) lists);
					intent.putExtra("edit", true);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
					startActivityForResult(intent, MAJOR_TAG);
				}
			}
		} else if (tag == TagUtils.UPDATE_MSG_TAG) {
			closeWaitingDialog();
			if (((String) obj).equals("200")) {
				upDateMsg();
				ToastUtils.showShortToast(R.string.submit_success);
				Intent intent = new Intent();
				intent.putExtra("picupdate", mIsUpdateSuccess);
				setResult(0, intent);
				finish();
			} else {
				int flag = Integer.parseInt((String) obj);
				ToastUtils.showShortToast(ErrorUtils.getErrorMsg(flag));
			}
		} else if (tag == TagUtils.DETAILS_TAG) {
			closeWaitingDialog();
			if (null != obj) {
				DetailsInfo info = (DetailsInfo) obj;
				showScoreView(info);
			}
		}
		return null;
	}

	private void showScoreView(DetailsInfo info) {
		if (null == info || null == info.getDetail()
				|| null == info.getDetail().getExams())
			return;
		int size = info.getDetail().getExams().size();
		if (size > 0) {
			mScoreLayout.setVisibility(View.VISIBLE);
			if (size >= 1) {
				((RelativeLayout) findViewById(R.id.sub_layout1))
						.setVisibility(View.VISIBLE);
			}
			if (size >= 2) {
				((RelativeLayout) findViewById(R.id.sub_layout2))
						.setVisibility(View.VISIBLE);
			}
			if (size >= 3) {
				((RelativeLayout) findViewById(R.id.sub_layout3))
						.setVisibility(View.VISIBLE);
			}
			if (size >= 4) {
				((RelativeLayout) findViewById(R.id.sub_layout4))
						.setVisibility(View.VISIBLE);
			}
			mSubText1.addTextChangedListener(this);
			mSubText2.addTextChangedListener(this);
			mSubText3.addTextChangedListener(this);
			mSubText4.addTextChangedListener(this);
		}
	}

	private String getScore() {
		mScore = mSubText1.getText().toString() + ","
				+ mSubText2.getText().toString() + ","
				+ mSubText3.getText().toString() + ","
				+ mSubText4.getText().toString() + ", "
				+ getString(R.string.sum_point) + " "
				+ mTotalScroeView.getText().toString();
		try {
			return URLEncoder.encode(mScore, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private void setLayoutIsEnable(boolean flag1, boolean flag2) {
		mColleageLayout.setEnabled(flag1);
		// mMajorLayout.setEnabled(flag2);
		setLayoutBg(mColleageLayout, flag1);
		// setLayoutBg(mMajorLayout, flag2);
	}

	private void setLayoutBg(RelativeLayout layout, boolean flag) {
		if (flag) {
			layout.setBackgroundColor(0xfff4f1f1);
		} else {
			layout.setBackgroundColor(0xffdedede);
		}
	}

	private void addEditListener() {

		mAnnounceEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				temp = s;
				if (null != s) {
					mAnnounceEdit.setSelection(s.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				selectionStart = mAnnounceEdit.getSelectionStart();
				selectionEnd = mAnnounceEdit.getSelectionEnd();
				if (temp != null && temp.length() > TEXT_MAX) {
					ToastUtils.showShortToast(R.string.announce_too_long);
					s.delete(selectionStart - 1, selectionEnd);
					mAnnounceEdit.setText(s);
					mAnnounceEdit.setSelection(selectionEnd);
				}
			}
		});
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

	public void alterPerMsg() {
		try {
			schoolMsg();
			String nick = URLEncoder.encode(mNickView.getText().toString(),
					"utf-8");
			mState = mStateView.getText().toString();
			mFighting = mFightingView.getText().toString();
			mAnnounce = mAnnounceEdit.getText().toString();
			String state = formatString(mState, mUser.getState());
			String fightting = formatString(mFighting, mUser.getHowGoing());
			String announce = formatString(mAnnounce, mUser.getAnnounce());
			String province = StringUtils.isEmpty(mProId) ? mUser.getProid()
					: mProId;
			String city = StringUtils.isEmpty(mCityId) ? mUser.getCityid()
					: mCityId;
			String url = UrlUtils.UPDATE_MSG_URL + "user.email="
					+ mUser.getEmail() + "&user.pid=" + province
					+ "&user.city=" + city + "&user.nickname=" + nick
					+ "&major.ssid=" + mSchoolId + "&major.sceid="
					+ mColleageId + "&user.senter=" + mYearName
					+ "&user.status=" + state + "&user.declaration=" + announce
					+ "&user.howgoing=" + fightting + "&user.scores="
					+ getScore();
			System.out.println("path::::::::::::::" + mPhotoPath + "   "
					+ mIsUpdateBitmap);
			startWaitingDialog();
			new NetworkTask().execute(this, TagUtils.UPDATE_MSG_TAG, url);
			if (mIsUpdateBitmap) {
				Intent sintent = new Intent(this, KyService.class);
				sintent.putExtra("imgPath", mPhotoPath);
				mUser.setPic(mPhotoPath);
				sintent.putExtra("uploadimg", KyService.UPLOAD_IMG);
				startService(sintent);
				mIsUpdateBitmap = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void upDateMsg() {
		mUser.setState(mState);
		mUser.setNickName(mNickView.getText().toString());
		mUser.setHowGoing(mFighting);
		mUser.setAnnounce(mAnnounce);
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
		editor.putString("nickname", mNickView.getText().toString());
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
		editor.putString("announce", mAnnounce);// 个人宣言
		editor.putString("howgoing", mFighting);
		editor.putString("state", mState);
		editor.putString("score", mScore);
		editor.commit();
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

	private void initExamMsg() {
		if (mUser.getRole() == 1) {
			mStateLayout.setVisibility(View.GONE);
			((TextView) findViewById(R.id.show_announce))
					.setText(R.string.sign);
		} else {
			mStateLayout.setVisibility(View.VISIBLE);
		}
	}

	private void initSubLayout() {
		if (mUser.getRole() == 0) {
			mScoreLayout.setVisibility(View.GONE);
		} else {
			mScoreLayout.setVisibility(View.VISIBLE);
		}
	}

	public abstract interface IUpdateMessage {
		public void updateMessage();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		countTotal();
	}

	public void countTotal() {
		int sub1 = getNum(mSubText1);
		int sub2 = getNum(mSubText2);
		int sub3 = getNum(mSubText3);
		int sub4 = getNum(mSubText4);
		int total = sub1 + sub2 + sub3 + sub4;
		mTotalScroeView.setText(total + "");
	}

	public int getNum(EditText editText) {
		String str = editText.getText().toString();
		if (StringUtils.isEmpty(str)) {
			return 0;
		} else {
			return Integer.valueOf(str);
		}
	}

	public void setScores() {
		if (!StringUtils.isEmpty(mScore)) {
			String[] str = mScore.split(",");
			if (null != str) {
				setString(mSubText1, str[0]);
				setString(mSubText2, str[1]);
				setString(mSubText3, str[2]);
				setString(mSubText4, str[3]);
				countTotal();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("EditActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("EditActivity");
		MobclickAgent.onPause(this);
	}
}
