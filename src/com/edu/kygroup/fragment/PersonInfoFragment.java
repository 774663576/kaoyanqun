package com.edu.kygroup.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.ChengJi;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.GetChengJiTask;
import com.edu.kygroup.ui.MajorDetailsActivity2;
import com.edu.kygroup.ui.PersonDetailActivity;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;

public class PersonInfoFragment extends Fragment implements OnClickListener {
	private User mFriendUser;
	private TextView txt_jiguan;
	private TextView txt_yuanxiao;
	private TextView txt_baokao;
	private TextView txt_guanzhu;
	private TextView txt_beikao;
	private TextView txt_zhankuang;
	private ListView mFocusUniListView;
	private ImageView img_arrow;
	private ArrayList<FocusInfo> focus_infos = new ArrayList<FocusInfo>();
	private ArrayList<String> mFocusUnilist = new ArrayList<String>();
	private ArrayAdapter<String> foucsAdapter;
	public Dialog mDialog = null;
	public NetworkTask task;
	private String url = "";
	private ImageView img_renzheng;

	private TextView txt_renzheng;
	private LinearLayout layout_beikao, layout_zhankuang;
	private LinearLayout layout_chengji;
	private LinearLayout layout_object1, layout_object2, layout_object3,
			layout_object4;
	private TextView txt_object1, txt_object2, txt_object3, txt_object4;
	private TextView txt_fenshu1, txt_fenshu2, txt_fenshu3, txt_fenshu4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.person_info_fragment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFriendUser = ((PersonDetailActivity) getActivity()).getUser();
		initView();
		setValue();
		getFoucsList();
	}

	private void initView() {
		layout_beikao = (LinearLayout) getView().findViewById(
				R.id.layout_beibao);
		layout_zhankuang = (LinearLayout) getView().findViewById(
				R.id.layout_zhankuang);
		layout_chengji = (LinearLayout) getView().findViewById(
				R.id.layout_chengji);
		layout_object1 = (LinearLayout) getView().findViewById(
				R.id.layout_object1);
		layout_object2 = (LinearLayout) getView().findViewById(
				R.id.layout_object2);
		layout_object3 = (LinearLayout) getView().findViewById(
				R.id.layout_object3);
		layout_object4 = (LinearLayout) getView().findViewById(
				R.id.layout_object4);
		txt_fenshu1 = (TextView) getView().findViewById(R.id.txt_fenshu1);
		txt_fenshu2 = (TextView) getView().findViewById(R.id.txt_fenshu2);
		txt_fenshu3 = (TextView) getView().findViewById(R.id.txt_fenshu3);
		txt_fenshu4 = (TextView) getView().findViewById(R.id.txt_fenshu4);
		txt_object1 = (TextView) getView().findViewById(R.id.txt_object1);
		txt_object2 = (TextView) getView().findViewById(R.id.txt_object2);
		txt_object3 = (TextView) getView().findViewById(R.id.txt_object3);
		txt_object4 = (TextView) getView().findViewById(R.id.txt_object4);
		img_renzheng = (ImageView) getView().findViewById(R.id.img_renzheng);
		txt_renzheng = (TextView) getView().findViewById(R.id.txt_renzheng);
		mFocusUniListView = (ListView) getView().findViewById(
				R.id.focus_uni_list);
		txt_baokao = (TextView) getView().findViewById(R.id.txt_baokao);
		txt_baokao.setOnClickListener(this);
		txt_beikao = (TextView) getView().findViewById(R.id.txt_beikao);
		txt_guanzhu = (TextView) getView().findViewById(R.id.txt_guanzhu);
		txt_guanzhu.setVisibility(View.GONE);
		txt_guanzhu.setOnClickListener(this);
		txt_jiguan = (TextView) getView().findViewById(R.id.txt_jiguan);
		txt_yuanxiao = (TextView) getView().findViewById(R.id.txt_yuanxiao);
		txt_zhankuang = (TextView) getView().findViewById(R.id.txt_zhankuang);
		img_arrow = (ImageView) getView().findViewById(R.id.img_arrow);
		img_arrow.setOnClickListener(this);
		mFocusUniListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				startWaitingDialog();
				url = UrlUtils.DETAILS_URL + "major.sid="
						+ focus_infos.get(position + 1).getmSid()
						+ "&major.ceid="
						+ focus_infos.get(position + 1).getmCid()
						+ "&major.mid="
						+ focus_infos.get(position + 1).getmMid();
				task = new NetworkTask();
				task.setmFinish(new GetFinish() {

					@Override
					public void finish(Object result) {
						closeWaitingDialog();
						MajorDetail detail = (MajorDetail) result;
						FocusInfo info = new FocusInfo();
						info.setmSid(focus_infos.get(position + 1).getmSid());
						info.setmCid(focus_infos.get(position + 1).getmCid());
						info.setmMid(focus_infos.get(position + 1).getmMid());
						info.setmFocusSchool(focus_infos.get(position + 1)
								.getmFocusSchool());
						info.setmFocusColleage(focus_infos.get(position + 1)
								.getmFocusColleage());
						info.setmFocusMajor(focus_infos.get(position + 1)
								.getmFocusMajor());
						goToDetailActivity(detail, info,
								focus_infos.get(position + 1).getmFocusMajor());
					}
				});
				task.execute(null, TagUtils.DETAILS_TAG, url);
			}
		});
	}

	private void setValue() {
		layout_chengji.setVisibility(View.GONE);
		String year = "";
		if (mFriendUser.getRole() == 0) {
			year = mFriendUser.getEYear();
		} else {
			year = mFriendUser.getRYear();
		}
		String msg = mFriendUser.getRSchool() + " | "
				+ mFriendUser.getRCollege() + " | " + mFriendUser.getRMajor()
				+ " 专业 " + year + "级研究生";
		if (mFriendUser.getRole() == 2) {
			txt_renzheng.setText(msg);
			img_renzheng.setImageResource(R.drawable.certification);
			layout_beikao.setVisibility(View.GONE);
			layout_zhankuang.setVisibility(View.GONE);

		} else if (mFriendUser.getRole() == 1) {
			img_renzheng.setImageResource(R.drawable.un_certifiaction);
			txt_renzheng.setText(msg);
			layout_beikao.setVisibility(View.GONE);
			layout_zhankuang.setVisibility(View.GONE);

		} else {
			img_renzheng.setImageResource(R.drawable.un_certifiaction);
			txt_renzheng.setText(msg);
			img_renzheng.setVisibility(View.GONE);
			txt_renzheng.setVisibility(View.GONE);
			layout_beikao.setVisibility(View.VISIBLE);
			layout_zhankuang.setVisibility(View.VISIBLE);

		}
		txt_jiguan.setText(hometownString());
		StringBuffer emsg = new StringBuffer();
		if (StringUtils.isEmpty(mFriendUser.getESchool())) {
			try {
				String str[] = getResources().getStringArray(R.array.unis);
				int pos = (int) (Math.random() * 10) % 3;
				emsg.append(str[pos]);
			} catch (Exception e) {
				emsg.append(getString(R.string.local_uni));
			}
		} else {
			emsg.append(mFriendUser.getESchool());
			if (!StringUtils.isEmpty(mFriendUser.getECollege())) {
				appendString(emsg, " | ", mFriendUser.getECollege());
			}
			if (!StringUtils.isEmpty(mFriendUser.getEMajor())) {
				appendString(emsg, " | ", mFriendUser.getEMajor());
			}
		}
		txt_yuanxiao.setText(emsg);
		String postMsg = mFriendUser.getRSchool() + " | "
				+ mFriendUser.getRCollege() + " | " + mFriendUser.getRMajor();
		txt_baokao.setText(postMsg);
		foucsAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.focus_uni_list, R.id.focus_text, mFocusUnilist);
		mFocusUniListView.setAdapter(foucsAdapter);
		getChengJI();
	}

	private void getChengJI() {
		if (mFriendUser.getRole() == 2 || mFriendUser.getRole() == 1) {
			final ChengJi chengji = new ChengJi();
			chengji.setUser_id(mFriendUser.getEmail());
			GetChengJiTask task = new GetChengJiTask();
			task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
				@Override
				public void taskFinish(Integer result) {
					setChengjiValue(chengji);
				}
			});
			task.executeParallel(chengji);
		}

	}

	private void setChengjiValue(ChengJi chengji) {
		if (!"".equals(chengji.getObject1())) {
			layout_chengji.setVisibility(View.VISIBLE);
			txt_fenshu1.setText(chengji.getFenshu1());
			txt_object1.setText(chengji.getObject1());
			layout_object1.setVisibility(View.VISIBLE);

		}
		if (!"".equals(chengji.getObject2())) {
			layout_chengji.setVisibility(View.VISIBLE);
			txt_fenshu2.setText(chengji.getFenshu2());
			txt_object2.setText(chengji.getObject2());
			layout_object2.setVisibility(View.VISIBLE);

		}
		if (!"".equals(chengji.getObject3())) {
			layout_chengji.setVisibility(View.VISIBLE);
			txt_fenshu3.setText(chengji.getFenshu3());
			txt_object3.setText(chengji.getObject3());
			layout_object3.setVisibility(View.VISIBLE);

		}
		if (!"".equals(chengji.getObject4())) {
			layout_chengji.setVisibility(View.VISIBLE);
			txt_fenshu4.setText(chengji.getFenshu4());
			txt_object4.setText(chengji.getObject4());
			layout_object4.setVisibility(View.VISIBLE);

		}
	}

	private void appendString(StringBuffer sb, String... strs) {
		if (null != sb && null != strs) {
			for (String str : strs) {
				sb.append(str);
			}
		}
	}

	private String hometownString() {
		if (StringUtils.isEmpty(mFriendUser.getProvince())) {
			return "";
		} else if (mFriendUser.getProvince().equals(mFriendUser.getCity())) {
			return mFriendUser.getProvince();
		} else {
			return (mFriendUser.getProvince() + " " + mFriendUser.getCity());
		}
	}

	private void roteAnimation(float from, float to) {
		RotateAnimation animation = new RotateAnimation(from, to,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(500);// 设置动画持续时间
		animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		img_arrow.startAnimation(animation);

	}

	/**
	 * 获取关注院校列表
	 */
	private void getFoucsList() {
		if (DeviceUtils.isNetAvailable(getActivity())) {
			String url = UrlUtils.GET_CONCERN + "user.email="
					+ mFriendUser.getEmail();
			NetworkTask task = new NetworkTask();
			task.setmFinish(new GetFinish() {

				@Override
				public void finish(Object result) {
					if (null != result) {
						ArrayList<FocusInfo> infos = (ArrayList<FocusInfo>) result;
						if (null != infos && infos.size() > 0) {
							txt_guanzhu.setVisibility(View.VISIBLE);
							focus_infos = infos;
							for (FocusInfo fi : infos) {
								mFocusUnilist.add(fi.getmFocusSchool() + " | "
										+ fi.getmFocusColleage() + " | "
										+ fi.getmFocusMajor());
							}
							txt_guanzhu.setText(mFocusUnilist.get(0));
							mFocusUnilist.remove(0);
							foucsAdapter.notifyDataSetChanged();

						}
					}
				}
			});
			task.execute(null, TagUtils.GET_CONCERN, url);
		}
	}

	boolean isRote = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_arrow:
			if (!isRote) {
				roteAnimation(0f, 180f);
				isRote = true;
				mFocusUniListView.setVisibility(View.VISIBLE);
			} else {
				roteAnimation(180f, 0f);
				isRote = false;
				mFocusUniListView.setVisibility(View.GONE);

			}
			break;
		case R.id.txt_baokao:
			startWaitingDialog();
			url = UrlUtils.DETAILS_URL + "major.sid=" + mFriendUser.getRSid()
					+ "&major.ceid=" + mFriendUser.getRCid() + "&major.mid="
					+ mFriendUser.getRmid();
			task = new NetworkTask();
			task.setmFinish(new GetFinish() {

				@Override
				public void finish(Object result) {
					closeWaitingDialog();
					MajorDetail detail = (MajorDetail) result;
					FocusInfo info = new FocusInfo();
					info.setmSid(mFriendUser.getRSid());
					info.setmCid(mFriendUser.getRCid());
					info.setmMid(mFriendUser.getRmid());
					info.setmFocusSchool(mFriendUser.getRSchool());
					info.setmFocusColleage(mFriendUser.getRCollege());
					info.setmFocusMajor(mFriendUser.getRMajor());
					goToDetailActivity(detail, info, mFriendUser.getRMajor());
				}
			});
			task.execute(null, TagUtils.DETAILS_TAG, url);

			break;
		case R.id.txt_guanzhu:
			if (focus_infos.size() == 0) {
				return;
			}
			startWaitingDialog();
			url = UrlUtils.DETAILS_URL + "major.sid="
					+ focus_infos.get(0).getmSid() + "&major.ceid="
					+ focus_infos.get(0).getmCid() + "&major.mid="
					+ focus_infos.get(0).getmMid();
			task = new NetworkTask();
			task.setmFinish(new GetFinish() {

				@Override
				public void finish(Object result) {
					closeWaitingDialog();
					MajorDetail detail = (MajorDetail) result;
					FocusInfo info = new FocusInfo();
					info.setmSid(focus_infos.get(0).getmSid());
					info.setmCid(focus_infos.get(0).getmCid());
					info.setmMid(focus_infos.get(0).getmMid());
					info.setmFocusSchool(focus_infos.get(0).getmFocusSchool());
					info.setmFocusColleage(focus_infos.get(0)
							.getmFocusColleage());
					info.setmFocusMajor(focus_infos.get(0).getmFocusMajor());
					goToDetailActivity(detail, info, focus_infos.get(0)
							.getmFocusMajor());
				}
			});
			task.execute(null, TagUtils.DETAILS_TAG, url);
			break;
		default:
			break;
		}
	}

	private void goToDetailActivity(MajorDetail details, FocusInfo info,
			String major) {
		Intent intent = new Intent(getActivity(), MajorDetailsActivity2.class);
		intent.putExtra("details", details);
		intent.putExtra("focus_info", info);
		intent.putExtra("major", major);
		this.startActivity(intent);

	}

	public void startWaitingDialog() {
		try {
			if (mDialog == null) {
				mDialog = new Dialog(getActivity(), R.style.waiting);
			}
			if (!mDialog.isShowing()) {
				mDialog.setContentView(R.layout.waiting);
				mDialog.setCanceledOnTouchOutside(false);
				mDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeWaitingDialog() {
		try {
			if (mDialog != null) {
				mDialog.dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
