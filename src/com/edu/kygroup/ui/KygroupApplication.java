package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.edu.keygroup.chooseimage.CheckImageLoaderConfiguration;
import com.edu.kygroup.domin.Announce;
import com.edu.kygroup.domin.KyLocation;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.SharedPreUtils;
import com.funshion.video.mobile.imageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.funshion.video.mobile.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.funshion.video.mobile.imageloader.core.ImageLoader;
import com.funshion.video.mobile.imageloader.core.ImageLoaderConfiguration;
import com.funshion.video.mobile.imageloader.core.download.BaseImageDownloader;
import com.funshion.video.mobile.imageloader.utils.StorageUtils;

public class KygroupApplication extends Application {
	public static KygroupApplication mApplication;
	public static int mFlag;// 0 普通考生报考年份 1 研究生入学年份 2 普通考生本科入学年份 3研究生本科入学年份
	public static User mUser;
	public static Announce mAnnounce;

	private static SharedPreUtils mSharedPre;
	private KyLocation mLocation;

	private static List<Activity> addFoucsList = new ArrayList<Activity>();

	public static KygroupApplication getmApplication() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
		initData();
		initImageLoader(getApplicationContext());
		initHuanxin();
	}

	private void initHuanxin() {
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk
			// 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
			// processName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		EMChat.getInstance().setDebugMode(true);
		// 初始化环信SDK,一定要先调用init()
		EMChat.getInstance().init(mApplication);
		Log.d("EMChat Demo", "initialize EMChat SDK");
		// debugmode设为true后，就能看到sdk打印的log了

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotifyBySoundAndVibrate(true);
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(true);
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(true);
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(true);
		// 设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(mApplication, ChatActivity1.class);
				ChatType chatType = message.getChatType();
				System.out.println("app:::::::::::::::" + chatType);
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("toChatUsername", message.getFrom());
					intent.putExtra("chatType", ChatActivity1.CHATTYPE_SINGLE);
				} else { // 群聊信息
							// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity1.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		// 设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		// options.setNotifyText(new OnMessageNotifyListener() {
		//
		// @Override
		// public String onNewMessageNotify(EMMessage message) {
		// // 可以根据message的类型提示不同文字(可参考微信或qq)，demo简单的覆盖了原来的提示
		// return "你的好基友" + message.getFrom() + "发来了一条消息哦";
		// }
		//
		// @Override
		// public String onLatestMessageNotify(EMMessage message, int
		// fromUsersNum, int messageNum) {
		// return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
		// }
		//
		// @Override
		// public String onSetNotificationTitle(EMMessage message) {
		// //修改标题
		// return "环信notification";
		// }
		//
		//
		// });

	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	class MyConnectionListener implements ConnectionListener {
		@Override
		public void onReConnecting() {
		}

		@Override
		public void onReConnected() {
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// Intent intent = new Intent(applicationContext,
				// MainActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent.putExtra("conflict", true);
				// startActivity(intent);
			}

		}

		@Override
		public void onConnecting(String progress) {

		}

		@Override
		public void onConnected() {
		}
	}

	public static void updateUser() {
		mUser.setRCollege(mSharedPre.getString("regcol", ""));
		mUser.setRCid(mSharedPre.getString("regcol", ""));
		mUser.setRMajor(mSharedPre.getString("regmaj", ""));
		mUser.setRMid(mSharedPre.getString("regmajid", ""));
		mUser.setRYear(mSharedPre.getString("regyea", ""));
		mUser.setRSchool(mSharedPre.getString("reguni", ""));
		mUser.setRSid(mSharedPre.getString("reguniid", ""));

	}

	private void initData() {
		mUser = new User();
		mSharedPre = new SharedPreUtils(this);
		mApplication = this;
		getEmail();
		getRegCol();
		getRegMaj();
		getRegUni();
		getRegYea();// reg 注册信息 即服务端的session
		getNickName();
		getGender();
		getProvince();
		getCity();
		getEnterUni();
		getPic();
		getRegCid();
		getRegMid();
		getRegSid();
		getEcol();
		getEuni();
		getEmaj();
		getEcolid();
		getEuniid();
		getEmajid();
		getAnnounce();
		getFightting();
		getState();
		getProid();
		getCityid();
		getEYear();
		getRole();
		getScore();
		getConfirm();
		getGroupId();
	}

	private String getProid() {
		String proid = mUser.getProid();
		if (TextUtils.isEmpty(proid)) {
			proid = mSharedPre.getString("proid", "");
			mUser.setProid(proid);
		}
		return proid;
	}

	private String getCityid() {
		String cityid = mUser.getCityid();
		if (TextUtils.isEmpty(cityid)) {
			cityid = mSharedPre.getString("cityid", "");
			mUser.setCityid(cityid);
		}
		return cityid;
	}

	private String getAnnounce() {
		String ann = mUser.getAnnounce();
		if (TextUtils.isEmpty(ann)) {
			ann = mSharedPre.getString("announce", "");
			mUser.setAnnounce(ann);
		}
		return ann;
	}

	public static String getGroupId() {
		String groupid = mUser.getChatid();
		if (TextUtils.isEmpty(groupid)) {
			groupid = mSharedPre.getString("group_id", "");
			mUser.setChatid(groupid);
			System.out.println("group::::::::id+" + mUser.getChatid());
		}
		return groupid;
	}

	private String getFightting() {
		String fig = mUser.getHowGoing();
		if (TextUtils.isEmpty(fig)) {
			fig = mSharedPre.getString("howgoing", "");
			mUser.setHowGoing(fig);
		}
		return fig;
	}

	private String getState() {
		String sta = mUser.getState();
		if (TextUtils.isEmpty(sta)) {
			sta = mSharedPre.getString("state", "");
			mUser.setState(sta);
		}
		return sta;
	}

	private String getEcol() {
		String col = mUser.getECollege();
		if (TextUtils.isEmpty(col)) {
			col = mSharedPre.getString("majorcol", "");
			mUser.setECollege(col);
		}
		return col;
	}

	private String getEcolid() {
		String id = mUser.getEColleageid();
		if (TextUtils.isEmpty(id)) {
			id = mSharedPre.getString("majorcolid", "");
			mUser.setEColleageid(id);
		}
		return id;
	}

	private String getEuni() {
		String uni = mUser.getESchool();
		if (TextUtils.isEmpty(uni)) {
			uni = mSharedPre.getString("majoruni", "");
			mUser.setESchool(uni);
		}
		return uni;
	}

	private String getEuniid() {
		String id = mUser.getESchoolid();
		if (TextUtils.isEmpty(id)) {
			id = mSharedPre.getString("majoruniid", "");
			mUser.setESchoolid(id);
		}
		return id;
	}

	private String getEmaj() {
		String maj = mUser.getEMajor();
		if (TextUtils.isEmpty(maj)) {
			maj = mSharedPre.getString("majormaj", "");
			mUser.setEMajor(maj);
		}
		return maj;
	}

	private String getEmajid() {
		String id = mUser.getEMajorid();
		if (TextUtils.isEmpty(id)) {
			id = mSharedPre.getString("majormajid", "");
			mUser.setEMajorid(id);
		}
		return id;
	}

	private String getEYear() {
		String year = mUser.getEYear();
		if (TextUtils.isEmpty(year)) {
			year = mSharedPre.getString("majoryear", "");
			mUser.setEYear(year);
		}
		return year;
	}

	private String getPic() {
		String pic = mUser.getPic();
		if (TextUtils.isEmpty(pic)) {
			pic = mSharedPre.getString("pic", "");
			mUser.setPic(pic);
		}
		return pic;
	}

	private String getEnterUni() {
		String euni = mUser.getESchool();
		if (TextUtils.isEmpty(euni)) {
			euni = mSharedPre.getString("euni", "");
			mUser.setESchool(euni);
		}
		return euni;
	}

	public String getEmail() {
		String email = mUser.getEmail();
		if (TextUtils.isEmpty(email)) {
			email = mSharedPre.getString("email", "");
			mUser.setEmail(email);
		}
		return email;
	}

	private String getRegCol() {
		String rcol = mUser.getRCollege();
		if (TextUtils.isEmpty(rcol)) {
			rcol = mSharedPre.getString("regcol", "");
			mUser.setRCollege(rcol);
		}
		return rcol;
	}

	private String getRegCid() {
		String cid = mUser.getRCid();
		if (TextUtils.isEmpty(cid)) {
			cid = mSharedPre.getString("regcolid", "");
			mUser.setRCid(cid);
		}
		return cid;
	}

	private String getRegMaj() {
		String rmaj = mUser.getRMajor();
		if (TextUtils.isEmpty(rmaj)) {
			rmaj = mSharedPre.getString("regmaj", "");
			mUser.setRMajor(rmaj);
		}
		return rmaj;
	}

	private String getRegMid() {
		String mid = mUser.getRmid();
		if (TextUtils.isEmpty(mid)) {
			mid = mSharedPre.getString("regmajid", "");
			mUser.setRMid(mid);
		}
		return mid;
	}

	private String getRegYea() {
		String ryea = mUser.getRYear();
		if (TextUtils.isEmpty(ryea)) {
			ryea = mSharedPre.getString("regyea", "");
			mUser.setRYear(ryea);
		}
		return ryea;
	}

	private String getRegUni() {
		String runi = mUser.getRSchool();
		if (TextUtils.isEmpty(runi)) {
			runi = mSharedPre.getString("reguni", "");
			mUser.setRSchool(runi);
		}
		return runi;
	}

	private String getRegSid() {
		String sid = mUser.getRSid();
		if (TextUtils.isEmpty(sid)) {
			sid = mSharedPre.getString("reguniid", "");
			mUser.setRSid(sid);
		}
		return sid;
	}

	private String getNickName() {
		String nick = mUser.getNickName();
		if (TextUtils.isEmpty(nick)) {
			nick = mSharedPre.getString("nickname", "");
			mUser.setNickName(nick);
		}
		return nick;
	}

	private int getRole() {
		int role = mUser.getRole();
		if (role != 1) {
			role = mSharedPre.getInt("role", 0);
			mUser.setRole(role);
		}
		return role;
	}

	private String getGender() {
		String gender = mUser.getGender();
		if (TextUtils.isEmpty(gender)) {
			gender = mSharedPre.getString("gender", "");
			mUser.setGender(gender);
		}
		return gender;
	}

	private String getProvince() {
		String pro = mUser.getProvince();
		if (TextUtils.isEmpty(pro)) {
			pro = mSharedPre.getString("province", "");
			mUser.setProvince(pro);
		}
		return pro;
	}

	private String getCity() {
		String city = mUser.getCity();
		if (TextUtils.isEmpty(city)) {
			city = mSharedPre.getString("city", "");
			mUser.setCity(city);
		}
		return city;
	}

	private String getScore() {
		String score = mUser.getScore();
		if (TextUtils.isEmpty(score)) {
			score = mSharedPre.getString("score", "");
			mUser.setScore(score);
		}
		return score;
	}

	private int getConfirm() {
		int confirm = mUser.getConfirm();
		if (confirm == 0) {
			confirm = mSharedPre.getInt("confirm", 0);
			mUser.setConfirm(confirm);
		}
		return confirm;
	}

	public static KygroupApplication getApplication() {
		return mApplication;
	}

	public static void initImageLoader(Context context) {
		try {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context)
					.threadPriority(Thread.NORM_PRIORITY)
					.threadPoolSize(
							Runtime.getRuntime().availableProcessors()
									* FileUtils.POOL_SIZE)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.discCache(
							new TotalSizeLimitedDiscCache(StorageUtils
									.getOwnCacheDirectory(context,
											FileUtils.CACHE_IMAGES_PATH),
									FileUtils.SD_CACHE_SIZE * FileUtils.MB))
					.imageDownloader(
							new BaseImageDownloader(context, 6 * 1000,
									20 * 1000)) // connectTimeout (6 s),
												// readTimeout (20 s)
					.build();
			ImageLoader.getInstance().init(config);
		} catch (Exception e) {
		}
	}

	public KyLocation getLocation() {
		return mLocation;
	}

	public void setLocation(KyLocation location) {
		mLocation = location;
	}

	public static void setAddFoucsActivity(Activity activity) {
		addFoucsList.add(activity);
	}

	public static void exitFoucsActivity() {
		for (Activity activity : addFoucsList) {
			if (activity != null) {
				activity.finish();
			}
		}
		addFoucsList.clear();
	}
}
