/**
 * 工程名: KyGroup
 * 文件名: KyGroupService.java
 * 包名: com.edu.kygroup.service
 * 日期: 2013-11-26下午4:35:21
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.edu.kygroup.R;
import com.edu.kygroup.domin.Announce;
import com.edu.kygroup.domin.Announce.Item;
import com.edu.kygroup.domin.KyLocation;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.ui.HomeActivity;
import com.edu.kygroup.ui.KygroupApplication;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.SharedPreUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UrlUtils;

/**
 * 类名: KyGroupService <br/>
 * 功能: TODO 应用程序服务类. <br/>
 * 日期: 2013-11-26 下午4:35:21 <br/>
 * 
 * 网络监听、经纬度上送
 * 
 * @version
 */
public class KyGroupService extends Service implements IBindData {
	private static final String TAG = "KyGroupService";
	private static final int MSG_REQ = 30 * 1000;
	private static final int ANN_REQ = 24 * 60 * 60 * 1000;
	private static SharedPreUtils mSharedPre;
	private ArrayList<MessageBean> messageList;
	private NotificationManager mNotifiManager;
	private Notification mNotifi;
	private Intent mIntent;
	private Timer mTimer;
	// private TimerTask mMsgTimerTask;// 消息
	private TimerTask mAnnTimerTask;// 公告
	private OnUpdateMessageListener mHomeMessageLisener;
	private OnUpdateMessageListener mChatMessageListener;
	private String mChatEmail = "";
	private MyBinder mBinder = new MyBinder();

	// --------获取地理位置
	private LocationClient mLocationClient = null;
	private BDLocationListener mListener = new KyLocationListener();

	@Override
	public void onCreate() {

		// TODO Auto-generated method stub
		super.onCreate();
		/**
		 * 注册广播
		 */
		mNotifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifi = new Notification(R.drawable.alter_logo, "研友群提示!",
				System.currentTimeMillis());
		mIntent = new Intent(Intent.ACTION_CALL);
		initLocation();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mLocationClient && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		if (null != mTimer) {
			mTimer.cancel();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mTimer = new Timer();
		// mMsgTimerTask = new TimerTask(){
		// @Override
		// public void run() {
		// if (DeviceUtils.isNetAvailable(getApplicationContext())){
		// // do_GettingMessage();
		// }
		// }
		// };
		// mTimer.scheduleAtFixedRate(mMsgTimerTask, 0, MSG_REQ);
		mAnnTimerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (DeviceUtils.isNetAvailable(getApplicationContext())) {
					getAnnounce();
				}
			}
		};
		mTimer.scheduleAtFixedRate(mAnnTimerTask, 0, ANN_REQ);
		return START_STICKY;
	}

	private String getTimeStamp() {
		if (null == mSharedPre) {
			mSharedPre = new SharedPreUtils(this);
		}
		return mSharedPre.getString(
				"timeStamp"
						+ StringUtils.replaceTrim(KygroupApplication.mUser
								.getEmail()), "0");
	}

	/**
	 * 获取用户私信列表
	 */
	// private void do_GettingMessage(){
	// try{
	// User user = KygroupApplication.mUser;
	// String url =
	// UrlUtils.GET_MESSAGE_LIST+"user.email="+user.getEmail()+"&timestamp="+getTimeStamp();
	// messageList = new ArrayList<MessageBean>();
	// new
	// NetworkTask().execute(this,TagUtils.GET_MESSAGE_LIST,url,messageList);
	// }catch(Exception e){
	// }
	// }

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		public KyGroupService getService() {
			return KyGroupService.this;
		}
	}

	public ArrayList<MessageBean> getList() {
		return messageList;
	}

	public void setHomeMessageListener(OnUpdateMessageListener listener) {
		mHomeMessageLisener = listener;
	}

	public void setChatMessageListener(OnUpdateMessageListener listener,
			String chatEmail) {
		mChatMessageListener = listener;
		mChatEmail = chatEmail;
	}

	@Override
	public Object bindData(int tag, Object obj) {
		switch (tag) {
		case TagUtils.GET_MESSAGE_LIST: {
			if (obj != null && obj instanceof Long && (Long) obj != -1) {
				if (effectiveTotal(obj) > 0) {
					mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mIntent.setClass(KyGroupService.this, HomeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("index", "3");
					mIntent.putExtras(bundle);
					PendingIntent contentIntent = PendingIntent.getActivity(
							KyGroupService.this, R.string.app_name, mIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);

					mNotifi.setLatestEventInfo(KyGroupService.this, "研友群提示：",
							"您有" + Integer.toString(messageList.size())
									+ "个好友发来新私信，点击查看", contentIntent);
					mNotifi.flags = Notification.FLAG_AUTO_CANCEL;
					mNotifi.when = System.currentTimeMillis();
					mNotifiManager.notify(2, mNotifi);
				} else if (messageList.size() > 0) {

				}
			}
			break;
		}
		case TagUtils.GET_ANNOUNCE_TAG: {
			if (obj != null && obj instanceof Announce) {
				Announce announce = (Announce) obj;
				ArrayList<Item> items = announce.getAnnounces();
				if (null != items) {
					int count = items.size();
					if (count > 0) {
						KygroupApplication.mAnnounce = announce;
						String tip = String.format(
								getString(R.string.announcement_tip), count);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mIntent.setClass(KyGroupService.this,
								HomeActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("index", "5");
						mIntent.putExtras(bundle);
						PendingIntent contentIntent = PendingIntent
								.getActivity(KyGroupService.this,
										R.string.app_name, mIntent,
										PendingIntent.FLAG_UPDATE_CURRENT);
						mNotifi.setLatestEventInfo(KyGroupService.this,
								getString(R.string.system_tip), tip,
								contentIntent);
						mNotifi.flags = Notification.FLAG_AUTO_CANCEL;
						mNotifi.when = System.currentTimeMillis();
						mNotifiManager.notify(3, mNotifi);
						SharedPreferences sp = getSharedPreferences(
								ConstantUtils.SHARED_PREF_FILE_NAME,
								MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putLong("timestamp", announce.getTimestamp());
						editor.commit();
					}
				}
			}
		}
		default:
			break;
		}
		return null;
	}

	public void notifyMessageChagned() {
		if (null != mHomeMessageLisener) {
			mHomeMessageLisener.onUpdateMsg();
		}
	}

	private int effectiveTotal(Object obj) {
		if (messageList.size() > 0) {
			writeTimeStamp(obj.toString());
			int total = messageList.size();
			for (MessageBean bean : messageList) {
				if (bean.getFlag() == 0) {
					total--;
				} else if (bean.getFriends_email().equals(mChatEmail)) {

				}
			}
			if (total > 0) {
				if (null != mChatMessageListener) {
					mChatMessageListener.onUpdateMsg();
				}
			}
			return total;
		}
		return 0;
	}

	private void writeTimeStamp(String timeStamp) {
		if (null == mSharedPre) {
			mSharedPre = new SharedPreUtils(this);
		}
		mSharedPre.addOrModify(
				"timeStamp"
						+ StringUtils.replaceTrim(KygroupApplication.mUser
								.getEmail()), timeStamp);
	}

	public interface OnUpdateMessageListener {
		public void onUpdateMsg();
	}

	public void initLocation() {
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(mListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// open gps
		option.setCoorType("bd0911");// 设置坐标类型bd0911
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setScanSpan(50000);// 设置定位时间
		option.setAddrType("all");
		option.disableCache(false);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	public class KyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if (null != arg0) {
				KyLocation location = KygroupApplication.getApplication()
						.getLocation();
				if (null == location) {
					location = new KyLocation();
				}
				if (!StringUtils.isEmpty(arg0.getProvince())) {
					location.setProvince(arg0.getProvince());
				}
				if (!StringUtils.isEmpty(arg0.getCity())) {
					location.setCity(arg0.getCity());
				}
				if (!StringUtils.isEmpty(arg0.getDistrict())) {
					location.setDistrict(arg0.getDistrict());
				}
				location.setLatitude(arg0.getLatitude());
				location.setLongitude(arg0.getLongitude());
				updateLocation(location);
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
		}
	}

	/**
	 * 上送本地地址到服务端
	 */
	private void updateLocation(KyLocation curloc) {
		KyLocation location = KygroupApplication.getApplication().getLocation();
		if (null == location) {
			location = curloc;
			KygroupApplication.getApplication().setLocation(location);
			if (!StringUtils.isEmpty(location.getLocation())) {
				submitLocation(location);
			}
		} else {
			if (location.isSubmit(curloc)) {
				location = curloc;
				submitLocation(location);
			}
		}
	}

	private void submitLocation(KyLocation loc) {
		System.out.println("locaiton:::::::::" + loc);
		try {
			User user = KygroupApplication.mUser;
			String url = UrlUtils.UPDATE_LOCATION + "user.email="
					+ user.getEmail() + "&user.longitude=" + loc.getLongitude()
					+ "&user.latitude=" + loc.getLatitude() + "&user.location="
					+ URLEncoder.encode(loc.getLocation(), "utf-8");
			new NetworkTask().execute(this, TagUtils.UPDATE_LOCATION, url);
		} catch (Exception e) {
		}
	}

	private void getAnnounce() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SharedPreferences sp = getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		long timeStamp = sp.getLong("timestamp", 0);
		// String date = sdf.format(new Date(timeStamp));
		String url = UrlUtils.GET_ANNOUNCES + "timestamp=" + timeStamp;
		new NetworkTask().execute(this, TagUtils.GET_ANNOUNCE_TAG, url);
	}
}
