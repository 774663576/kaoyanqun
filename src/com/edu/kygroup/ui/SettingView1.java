/**
 * 工程名: KyGroup
 * 文件名: SettingView.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-11-24上午9:10:34
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.ui;

import java.net.URLEncoder;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.ChengJi;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.popupwindow.SharePopwindow;
import com.edu.kygroup.popupwindow.SharePopwindow.SelectMenuOnclick;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.SharedUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.utils.UrlUtils;
import com.edu.kygroup.widget.CircularImage;

import fynn.app.PromptDialog;

public class SettingView1 implements OnClickListener, IBindData {

	private HomeActivity mContext;

	private View mView;
	private TextView txt_name;
	private TextView txt_gerenxinxi;
	private LinearLayout layout_biyeyuanxiao;
	private TextView txt_biyeyuanxiao;
	// private TextView txt_gonggao;
	private TextView txt_zhuti;
	// private TextView txt_xiugaimima;
	private TextView txt_heimingdan;
	private TextView txt_fenxiang;
	private TextView txt_jiancegengxin;
	private TextView txt_jijianfankui;
	private TextView txt_bangzhu;
	private TextView txt_tuichu;
	private CircularImage img_avatar;
	private TextView txt_friend;
	private LinearLayout layout_baokao;
	private TextView txt_renwu;
	private TextView txt_kaoyan_chengji;

	private String sname = "";
	private String sid = "";
	private String cid = "";
	private String year = "";

	SettingView1(HomeActivity context) {
		this.mContext = context;
		mView = LayoutInflater.from(mContext).inflate(
				R.layout.seting_veiw_layout, null);
		initView();
		initShareSDk();
	}

	private void initShareSDk() {
		ShareSDK.initSDK(mContext);
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
	}

	public View getView() {
		return mView;
	}

	public void setUname(String uname, String sid, String cid, String year) {
		txt_biyeyuanxiao.setText(uname);
		alterPerMsg(sid, cid, year);
		sname = uname;
		this.sid = sid;
		this.cid = cid;
		this.year = year;
	}

	private void initView() {
		txt_kaoyan_chengji = (TextView) mView.findViewById(R.id.txt_chengji);
		txt_friend = (TextView) mView.findViewById(R.id.txt_friend);
		txt_name = (TextView) mView.findViewById(R.id.txt_name);
		txt_name.setText(KygroupApplication.mUser.getNickName());
		layout_baokao = (LinearLayout) mView
				.findViewById(R.id.layout_baokaoyuanxiao);
		// txt_bangding = (TextView) mView.findViewById(R.id.txt_bangding);
		txt_zhuti = (TextView) mView.findViewById(R.id.txt_zhuti);
		// txt_gonggao = (TextView) mView.findViewById(R.id.txt_gonggao);
		layout_biyeyuanxiao = (LinearLayout) mView
				.findViewById(R.id.layout_biyeyuanxiao);
		txt_gerenxinxi = (TextView) mView.findViewById(R.id.txt_gerenxinxi);
		txt_biyeyuanxiao = (TextView) mView.findViewById(R.id.txt_biye);
		// txt_xiugaimima = (TextView) mView.findViewById(R.id.txt_xiugaimima);
		txt_heimingdan = (TextView) mView.findViewById(R.id.txt_heimingdan);
		txt_fenxiang = (TextView) mView.findViewById(R.id.txt_fenxiang);
		txt_jiancegengxin = (TextView) mView
				.findViewById(R.id.txt_jiancegengxin);
		txt_jijianfankui = (TextView) mView.findViewById(R.id.txt_yijianfank);
		txt_bangzhu = (TextView) mView.findViewById(R.id.txt_bangzhu);
		txt_tuichu = (TextView) mView.findViewById(R.id.txt_tuichu);
		img_avatar = (CircularImage) mView.findViewById(R.id.img_avatar);
		txt_renwu = (TextView) mView.findViewById(R.id.txt_renwu);
		// mContext.mImageLoader.displayImage(KygroupApplication.mUser.getPic(),
		// img_avatar, mContext.mOptions);
		updateAvatar();
		txt_biyeyuanxiao.setText(KygroupApplication.mUser.getESchool());
		initAuth();
		setListener();
	}

	public void updateAvatar() {
		UniversalImageLoadTool.disPlay(KygroupApplication.mUser.getPic(),
				img_avatar, R.drawable.pic_boy);
		System.out.println("img:::::::::::::::::::"
				+ KygroupApplication.mUser.getPic());
	}

	private void setListener() {
		txt_kaoyan_chengji.setOnClickListener(this);
		txt_friend.setOnClickListener(this);
		txt_gerenxinxi.setOnClickListener(this);
		layout_biyeyuanxiao.setOnClickListener(this);
		// txt_gonggao.setOnClickListener(this);
		txt_zhuti.setOnClickListener(this);
		// txt_xiugaimima.setOnClickListener(this);
		txt_heimingdan.setOnClickListener(this);
		txt_fenxiang.setOnClickListener(this);
		txt_jiancegengxin.setOnClickListener(this);
		txt_jijianfankui.setOnClickListener(this);
		txt_bangzhu.setOnClickListener(this);
		txt_tuichu.setOnClickListener(this);
		// txt_bangding.setOnClickListener(this);
		layout_baokao.setOnClickListener(this);
		txt_renwu.setOnClickListener(this);

	}

	public void initAuth() {
		TextView tv = (TextView) mView.findViewById(R.id.txt_baokao);
		int role = KygroupApplication.mUser.getRole();
		int confirm = KygroupApplication.mUser.getConfirm();
		if (confirm == 2) {
			tv.setText(R.string.auth_post3);
			layout_baokao.setEnabled(false);
		} else {
			if (confirm == 1) {
				tv.setText(R.string.authing);
				tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			} else if (role == 0) {
				tv.setText(R.string.auth_post1);
			} else if (role == 1) {
				tv.setText(R.string.auth_post2);
			}
		}
		if (role == 0) {
			txt_kaoyan_chengji.setVisibility(View.GONE);
		} else {
			txt_kaoyan_chengji.setVisibility(View.VISIBLE);

		}

	}

	public void alterPerMsg(String mSchoolId, String mColleageId,
			String mYearName) {
		User user = KygroupApplication.mUser;
		try {
			String nick = URLEncoder.encode(user.getNickName(), "utf-8");
			String state = URLEncoder.encode(user.getState(), "utf-8");
			String fightting = URLEncoder.encode(user.getHowGoing(), "utf-8");
			String province = user.getProid();
			String city = user.getCityid();
			city = URLEncoder.encode(city, "utf-8");
			String url = UrlUtils.UPDATE_MSG_URL + "user.email="
					+ user.getEmail() + "&user.pid=" + province + "&user.city="
					+ city + "&user.nickname=" + nick + "&major.ssid="
					+ mSchoolId + "&major.sceid=" + mColleageId
					+ "&user.senter=" + mYearName + "&user.status=" + state
					+ "&user.howgoing=" + fightting;
			new NetworkTask().execute(this, TagUtils.UPDATE_MSG_TAG, url);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("e:::::::::::::" + e.toString());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_friend:
			mContext.startActivity(new Intent(mContext, MyFriendsActivity.class));
			break;
		case R.id.txt_gerenxinxi:
			mContext.startActivity(new Intent(mContext,
					EditSelfInfoActivity.class));
			break;
		case R.id.layout_biyeyuanxiao:
			KygroupApplication.mFlag = 2;
			mContext.startActivityForResult(new Intent(mContext,
					SelectActivity.class), 300);
			break;
		// case R.id.txt_gonggao:
		// Announce announce = KygroupApplication.mAnnounce;
		// if (announce != null && announce.getAnnounces().size() > 0) {
		// Intent intent = new Intent(mContext, AnnounceActivity.class);
		// intent.putExtra("announce", announce);
		// mContext.startActivity(intent);
		// mContext.overridePendingTransition(R.anim.push_left_in,
		// R.anim.push_left_out);
		// } else {
		// ToastUtils.showShortToast(R.string.no_announce);
		// }
		// break;
		case R.id.txt_zhuti:
			Intent intent = new Intent(mContext, MyPostActivity.class);
			mContext.startActivity(intent);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case R.id.txt_xiugaimima:
			intent = new Intent(mContext, PasswordActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.txt_heimingdan:
			intent = new Intent(mContext, BlacklistActivity.class);
			mContext.startActivity(intent);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case R.id.txt_fenxiang:

			SharePopwindow pop = new SharePopwindow(mContext, v);
			pop.setmSelectOnclick(new SelectMenuOnclick() {

				@Override
				public void onClickPosition(int position) {
					Platform plat = null;
					switch (position) {
					case 0:
						plat = ShareSDK.getPlatform(QQ.NAME);
						share(plat);
						break;
					case 1:
						plat = ShareSDK.getPlatform(QZone.NAME);
						share(plat);
						break;
					case 2:
						plat = ShareSDK.getPlatform(Wechat.NAME);
						shareWe1(plat);

						break;
					case 3:
						plat = ShareSDK.getPlatform(WechatMoments.NAME);
						shareWe(plat);
						break;
					default:
						break;
					}

				}
			});
			pop.show();

			break;
		case R.id.txt_jiancegengxin:
			intent = new Intent(mContext, CheckUpgradeActivity.class);
			mContext.startActivityForResult(intent, 0);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case R.id.txt_yijianfank:
			intent = new Intent(mContext, FeedBackActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.txt_bangzhu:
			intent = new Intent(mContext, HelpActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.txt_tuichu:
			quitDialog();
			break;
		case R.id.txt_bangding:
			intent = new Intent(mContext, BangDingSheJiaoPingTaiActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.layout_baokaoyuanxiao:
			intent = new Intent(mContext, AuthActivity.class);
			mContext.startActivityForResult(intent, 0);
			mContext.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
			break;
		case R.id.txt_renwu:
			intent = new Intent(mContext, MyTaskActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.txt_chengji:
			intent = new Intent(mContext, KaoYanChengJiActivity.class);
			intent.putExtra("chengji", mContext.getChengji());
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void share(final Platform plat) {
		new Thread() {
			public void run() {
				ShareParams sp = new ShareParams();
				sp.setTitle("超级考研群");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText("【超级考研群APP】大视野的考研神器，最实用的考研助手。");
				sp.setSite("超级考研群");
				sp.setSiteUrl("http://www.yifulou.cn:8180/");
				sp.setImageUrl("http://www.yifulou.cn:8080/app_icon/logo.png");
				// Platform qzone = ShareSDK.getPlatform(QZone.NAME);
				// qzone.setPlatformActionListener(paListener); // 设置分享事件回调
				// 执行图文分享
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("超级考研群");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText("【超级考研群APP】大视野的考研神器，最实用的考研助手。");
				sp.shareType = Platform.SHARE_TEXT;
				sp.shareType = Platform.SHARE_IMAGE;
				sp.text = "【超级考研群APP】大视野的考研神器，最实用的考研助手。";
				sp.imageUrl = "http://www.yifulou.cn:8080/picture/logo.png";
				sp.url = "http://www.yifulou.cn:8180/";
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe1(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("超级考研群");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText("【超级考研群APP】大视野的考研神器，最实用的考研助手。");
				sp.shareType = Platform.SHARE_TEXT;
				sp.text = "【超级考研群APP】大视野的考研神器，最实用的考研助手。";
				plat.share(sp);
			}
		}.start();

	}

	private void quitDialog() {
		{
			new PromptDialog.Builder(mContext).setTitle("提示")
					.setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR)
					.setMessage("确定要退出吗?")
					.setButton1("确定", new PromptDialog.OnClickListener() {

						@Override
						public void onClick(Dialog dialog, int which) {
							dialog.dismiss();
							Intent intent = new Intent(mContext,
									LoginActivity.class);
							mContext.startActivity(intent);
							KygroupApplication.mUser.setEmail("");
							SharedPreferences prefs = mContext
									.getSharedPreferences(
											ConstantUtils.SHARED_PREF_FILE_NAME,
											mContext.MODE_PRIVATE);
							Editor editor = prefs.edit();
							editor.putString("email", "");
							editor.commit();
							mContext.finish();
						}
					}).setButton2("取消", new PromptDialog.OnClickListener() {

						@Override
						public void onClick(Dialog dialog, int which) {
							dialog.dismiss();
						}
					}).show();
		}
	}

	@Override
	public Object bindData(int tag, Object obj) {
		KygroupApplication.mUser.setESchool(sname);
		KygroupApplication.mUser.setEColleageid(this.cid);
		KygroupApplication.mUser.setESchoolid(this.sid);
		KygroupApplication.mUser.setEYear(this.year);
		SharedUtils.setString("majoruni", sname);
		return null;
	}
}
