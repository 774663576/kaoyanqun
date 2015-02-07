package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.ColleageInfo;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.net.NetworkTask.GetFinish;
import com.edu.kygroup.popupwindow.SelectPicPopwindow1;
import com.edu.kygroup.popupwindow.SelectPicPopwindow1.SelectMenuOnclick;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;

public class ZhaoShengYuanXiaoActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, IBindData {
	private ListView mListView;
	private List<ColleageInfo> lists = new ArrayList<ColleageInfo>();
	private Adapter adapter;
	private int major_id;
	private FocusInfo focusInfo;
	private String major = "";
	private StringBuffer share_content = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		major_id = getIntent().getIntExtra("major_id", 0);
		major = getIntent().getStringExtra("major");
		initView();
		getList();
		initShareSDk();
	}

	private void initShareSDk() {
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
	}

	private void initView() {
		setRightBtnText("分享");
		setRightBtnVisibility(View.VISIBLE);
		setLeftBtnVisibility(View.GONE);
		setTitleText("招生院校");
		mListView = (ListView) findViewById(R.id.listView1);
		adapter = new Adapter();
		mListView.setAdapter(adapter);
		setListener();
		// setRightBtnBg();

	}

	private void setListener() {
		setRightBtnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_zhao_sheng_yuan_xiao, null);
	}

	private void getList() {
		startWaitingDialog();
		String url = UrlUtils.GET_SCHOOL_LIST_BY_MAJOR + "majorid=" + major_id;
		NetworkTask task = new NetworkTask();
		task.setmFinish(new GetFinish() {

			@Override
			public void finish(Object result) {
				closeWaitingDialog();
				lists = (List<ColleageInfo>) result;
				adapter.notifyDataSetChanged();
				if (lists.size() == 0) {
					ToastUtils.showShortToast("没有数据");
				}
				share_content.append("【超级考研群app】2015年["
						+ getIntent().getStringExtra("major") + "]考研招生院校名单：");
				for (ColleageInfo info : lists) {
					share_content.append(info.getSname() + " | "
							+ info.getCename());
					share_content.append("\n");
				}
				share_content.append("【超级考研群app】大视野的考研神器，最实用的考研助手。");
			}
		});
		task.execute(null, TagUtils.GET_SCHOOL_LIST_BY_MAJOR, url);
	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
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
				convertView = LayoutInflater.from(
						ZhaoShengYuanXiaoActivity.this).inflate(
						R.layout.zhaoshengyuanxiao_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.focus_msg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String string = lists.get(position).getSname() + " | "
					+ lists.get(position).getCename() + " | "
					+ lists.get(position).getMname();
			holder.text.setText(string);
			return convertView;
		}

	}

	class ViewHolder {
		TextView text;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		startWaitingDialog();
		ColleageInfo info = lists.get(arg2);
		String url = UrlUtils.DETAILS_URL + "major.sid=" + info.getSid()
				+ "&major.ceid=" + info.getCeid() + "&major.mid="
				+ info.getMid();
		new NetworkTask().execute(this, TagUtils.DETAILS_TAG, url);
		focusInfo = new FocusInfo();
		focusInfo.setmCid(info.getCeid() + "");
		focusInfo.setmFocusColleage(info.getCename());
		focusInfo.setmFocusMajor(info.getMname());
		focusInfo.setmMid(info.getMid() + "");
		focusInfo.setmSid(info.getSid() + "");
		focusInfo.setmFocusSchool(info.getSname());
	}

	@Override
	public Object bindData(int tag, Object obj) {
		switch (tag) {

		case TagUtils.DETAILS_TAG:
			if (null != obj) {
				MajorDetail detail = (MajorDetail) obj;
				goToDetailActivity(detail);
			} else {
				ToastUtils.showShortToast(R.string.request_failed);
			}
			closeWaitingDialog();
			break;

		default:
			break;
		}
		return null;
	}

	private void goToDetailActivity(MajorDetail details) {
		Intent intent = new Intent(this, MajorDetailsActivity2.class);
		intent.putExtra("details", details);
		intent.putExtra("focus_info", focusInfo);
		intent.putExtra("from", "major");
		intent.putExtra("major", focusInfo.getmFocusMajor());
		intent.putExtra("majkey", focusInfo.getmMid());
		intent.putExtra("colkey", focusInfo.getmCid());
		intent.putExtra("unikey", focusInfo.getmSid());
		intent.putExtra("uni", focusInfo.getmFocusSchool());
		intent.putExtra("col", focusInfo.getmFocusColleage());
		intent.putExtra("maj", focusInfo.getmFocusMajor());
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public void onClick(View v) {

		SelectPicPopwindow1 pop = new SelectPicPopwindow1(this, v,
				new String[] { "QQ好友", "QQ空间", "微信好友", "微信朋友圈" });
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
					shareWeFriend(plat);
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
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("");
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

	private void shareWeFriend(final Platform plat) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("");
				sp.setText(share_content.toString());
				sp.shareType = Platform.SHARE_TEXT;
				sp.text = share_content.toString();
				plat.share(sp);
			}
		}.start();

	}
}
