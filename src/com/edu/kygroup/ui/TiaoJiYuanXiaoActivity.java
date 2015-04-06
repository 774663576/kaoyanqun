package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.TiaoJiaoYuanXiaoAdapter;
import com.edu.kygroup.domin.TiaoJiYuanXiao;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.popupwindow.SharePopwindow;
import com.edu.kygroup.popupwindow.SharePopwindow.SelectMenuOnclick;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class TiaoJiYuanXiaoActivity extends BaseActivity {
	private ListView mListView;
	private TiaoJiaoYuanXiaoAdapter adapter;
	private List<TiaoJiYuanXiao> lists = new ArrayList<TiaoJiYuanXiao>();
	private StringBuffer share_content = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setValue();
		getData(getIntent().getStringExtra("major_id"));
	}

	private void initView() {
		setRightBtnText("分享");
		setRightBtnVisibility(View.VISIBLE);
		setLeftBtnVisibility(View.GONE);
		setTitleText("调剂院校");
		mListView = (ListView) findViewById(R.id.listView1);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(TiaoJiYuanXiaoActivity.this,
						WebActivity.class);
				intent.putExtra("url", lists.get(arg2).getUrl());
				intent.putExtra("title", "调剂内容");
				startActivity(intent);
			}
		});
		setRightBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SharePopwindow pop = new SharePopwindow(
						TiaoJiYuanXiaoActivity.this, v);
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
			}
		});
	}

	private void share(final Platform plat) {
		new Thread() {
			public void run() {
				ShareParams sp = new ShareParams();
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(share_content.toString());
				sp.setSite("超级考研群");
				sp.setSiteUrl("http://www.yifulou.cn:8180/");
				sp.setImageUrl("http://www.yifulou.cn:8080/picture/logo.png");

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
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(share_content.toString());
				sp.shareType = Platform.SHARE_TEXT;
				sp.shareType = Platform.SHARE_IMAGE;
				sp.text = share_content.toString();
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
				sp.setTitle("");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(share_content.toString());
				sp.shareType = Platform.SHARE_TEXT;
				sp.text = share_content.toString();
				plat.share(sp);
			}
		}.start();

	}

	private void setValue() {
		adapter = new TiaoJiaoYuanXiaoAdapter(lists, this);
		mListView.setAdapter(adapter);
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_tiao_ji_yuan_xiao, null);
	}

	private void getData(String mid) {
		startWaitingDialog();
		String url = UrlUtils.TIAOJIYUANXIAO + "mid=" + mid;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {
			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				if (result != null) {
					lists.addAll((List<TiaoJiYuanXiao>) result);
					adapter.notifyDataSetChanged();
					share_content.append("【超级考研群app】2014年考研招生["
							+ getIntent().getStringExtra("major")
							+ "]接收调剂院校名单：");
					for (TiaoJiYuanXiao info : lists) {
						share_content.append(info.getSname() + " | "
								+ info.getCename());
						share_content.append("\n");
					}
					share_content.append("【超级考研群app】大视野的考研神器，最实用的考研助手。");

				} else {
					ToastUtils.showShortToast("获取失败");
				}
			}
		});
		task.execute(null, TagUtils.TIAOJIYUANXIAO, url);
	}
}
