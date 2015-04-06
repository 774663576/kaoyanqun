package com.edu.kygroup.ui;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.edu.kygroup.R;
import com.edu.kygroup.adapter.FunctionAdapter;
import com.edu.kygroup.domin.Announce;
import com.edu.kygroup.domin.ChengJi;
import com.edu.kygroup.domin.ConfirmInfo;
import com.edu.kygroup.domin.Upgrade;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.service.KyGroupService;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.GetChengJiTask;
import com.edu.kygroup.utils.ActivityHolder;
import com.edu.kygroup.utils.Constant;
import com.edu.kygroup.utils.ConstantUtils;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.LogUtils;
import com.edu.kygroup.utils.SharedPreUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UpgradeHelper;
import com.edu.kygroup.utils.UrlUtils;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class HomeActivity extends BaseActivity implements OnClickListener,
		IBindData, OnItemClickListener {
	public ImageLoader mImageLoader;
	public DisplayImageOptions mOptions;
	private TextView unreadLabel;

	private boolean mIsForeground = false;
	private TextView mSetting;
	private TextView mBaokao;
	private TextView mPostFriends;
	private TextView mMessage;
	private TextView mTask;
	private TextView mCurView;

	private GraduateView mGraduateView;
	private FriendsView mFriendsView;
	private SettingView1 mSettingView;
	private BaokaoView mBaokaoView;
	private MessageView mMessageView;
	private TaskView mTaskView;

	private int index = 1;
	private int mPos = 0;
	private int mAlterId = R.drawable.baokao_default;

	private KyGroupService mService;
	private UpgradeHelper mUpgradeHelper;
	private Upgrade mUpgrade;

	private PopupWindow mFunctionPopupWindow;
	private GridView mFunctionView;
	private FunctionAdapter mFunctionAdapter;
	private Announce mAnnounce;
	private NewMessageBroadcastReceiver msgReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.setDebugMode(true);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString("index") != null) {
			index = Integer.parseInt(bundle.getString("index"));
		}
		initView();
		initData();
		finishActivity();
		Intent intent = new Intent(this, KyGroupService.class);
		startService(intent);
		bindService(intent, conn, Service.BIND_AUTO_CREATE);
		initImageOptions();
		regisiterRecevier();
		// getUpgradeInfo();
		setLeftBtn();
		initPopupWindow();

		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
		getChengJI();
	}

	private void initImageOptions() {
		if (null == mImageLoader) {
			mImageLoader = ImageLoader.getInstance();
		}
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.pic_boy)
				.showImageForEmptyUri(R.drawable.pic_boy)
				.showImageOnFail(R.drawable.pic_boy).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIsForeground = true;
		MobclickAgent.onPageStart("HomeActivity"); // 统计页面
		MobclickAgent.onResume(this);
		mMessageView.refresh();
		updateUnreadLabel();

	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsForeground = false;
		MobclickAgent.onPageEnd("HomeActivity");
		MobclickAgent.onPause(this);
	}

	private void initView() {
		setRightBtnVisibility(View.GONE);
		setLeftBtnVisibility(View.GONE);
		setTitleText(R.string.kygroup);
		mSetting = (TextView) findViewById(R.id.setting);
		mBaokao = (TextView) findViewById(R.id.baokao);
		mPostFriends = (TextView) findViewById(R.id.post_friends);
		mMessage = (TextView) findViewById(R.id.message);
		mTask = (TextView) findViewById(R.id.txt_task);
		mCurView = mBaokao;
		mSetting.setOnClickListener(this);
		mTask.setOnClickListener(this);
		mBaokao.setOnClickListener(this);
		mPostFriends.setOnClickListener(this);
		mMessage.setOnClickListener(this);
		mBaokao.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.baokao_selected, 0, 0);
		setBottomBarVisibility(View.VISIBLE);
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);

	}

	private void initData() {
		mImageLoader = ImageLoader.getInstance();
		mBaokaoView = new BaokaoView(this);
		mGraduateView = new GraduateView(this);
		mFriendsView = new FriendsView(this);
		mSettingView = new SettingView1(this);
		mMessageView = new MessageView(this, this);
		mTaskView = new TaskView(this, this);
		if (index == 3) {
			addView(mMessageView.getView());
			setBackground(mMessage, R.drawable.message_selected);
			mAlterId = R.drawable.message_default;
			mMessage.performClick();
		} else if (index == 5) {
			addView(mSettingView.getView());
			setBackground(mSetting, R.drawable.setting_selected);
			mAlterId = R.drawable.setting_default;
			mSetting.performClick();
		} else {
			addView(mBaokaoView.getView());
		}
		mBaokaoView.setChangeAim(mGraduateView);
	}

	private void finishActivity() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ActivityHolder.getInstance().finishAllActivity();
				super.run();
			}
		}.start();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// if(mCurView.equals(mBaokao)){
		// if(mBaokaoView.mIsDelete){
		// mBaokaoView.cleanDeleteFocus();
		// }else{
		// moveTaskToBack(true);
		// }
		// }
		// moveTaskToBack(true);
		if (!mCurView.equals(mBaokao)) {
			mBaokao.performClick();
		} else if (mBaokaoView.mIsDelete) {
			mBaokaoView.cleanDeleteFocus();
		} else {
			moveTaskToBack(true);
			super.onBackPressed();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.delete_message, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {

			mMessageView.delMessage(item);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting: {

			setTitleText("我的主页");
			setBottomMsg(mSetting, mSettingView.getView(), 5,
					R.drawable.setting_selected);
			mAlterId = R.drawable.setting_default;

			break;
		}
		case R.id.baokao: {
			setTitleText("报考");
			setBottomMsg(mBaokao, mBaokaoView.getView(), 1,
					R.drawable.baokao_selected);
			mAlterId = R.drawable.baokao_default;
			// setRightBtnVisibility(View.VISIBLE);
			setRightBtnBg(R.drawable.add_focus_tip);
			break;
		}
		case R.id.post_friends: {
			setTitleText("研友");
			setBottomMsg(mPostFriends, mGraduateView.getView(), 2,
					R.drawable.postgraduate_selected);
			mAlterId = R.drawable.postgraduate_default;
			mPos = 0;
			mGraduateView.setPos(mPos);
			break;
		}
		case R.id.txt_task: {
			setTitleText("发现");
			setBottomMsg(mTask, mTaskView.getView(), 4,
					R.drawable.friends_selected);
			mAlterId = R.drawable.friends_default;
			setRightBtnVisibility(View.VISIBLE);
			setRightBg(0);
			setRightBtnText("发布任务");
			mRightBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(HomeActivity.this,
							PublishTaskActivity.class));
				}
			});
			break;
		}
		case R.id.message: {
			setTitleText("我的私信");
			setBottomMsg(mMessage, mMessageView.getView(), 3,
					R.drawable.message_selected);
			mAlterId = R.drawable.message_default;
			break;
		}
		default:
			break;
		}

	}

	private void setBottomMsg(TextView view, View addView, int i, int selectid) {
		setRightBtnVisibility(View.GONE);
		setBackground(view, selectid);
		addView(addView);
		mBaokaoView.cleanDeleteFocus();
		index = i;
	}

	private void setBackground(TextView selectview, int id2) {
		mCurView.setCompoundDrawablesWithIntrinsicBounds(0, mAlterId, 0, 0);
		selectview.setCompoundDrawablesWithIntrinsicBounds(0, id2, 0, 0);
		mCurView.setTextColor(Color.rgb(112, 119, 136));
		selectview.setTextColor(Color.rgb(240, 81, 51));
		mCurView = selectview;
	}

	public void performClickFriends(int pos) {
		switch (pos) {
		case 1:
			mPostFriends.performClick();
			mGraduateView.setPos(1);
			break;
		case 4:
			mSetting.performClick();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(conn);
		unregisterReceiver(receiver);
		mBaokaoView.onDestory();
		unregisterReceiver(msgReceiver);
		mTaskView.onDestory();
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	// if(mCurView.equals(mBaokao)){
	// if(mBaokaoView.mIsDelete){
	// mBaokaoView.cleanDeleteFocus();
	// }else{
	// moveTaskToBack(true);
	// }
	// return true;
	// }
	// moveTaskToBack(true);
	// }
	// return false;
	// }

	public void exitDialog() {
		Builder builder = new Builder(this);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.exit_msg);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		builder.create().show();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null && bundle.getString("index") != null) {
			int index = Integer.parseInt(bundle.getString("index"));
			LogUtils.d("HomeActivity", "default open page is messageview");
			if (index == 3) {
				addView(mMessageView.getView());
				setBackground(mMessage, R.drawable.message_selected);
				mAlterId = R.drawable.message_default;
				mMessage.performClick();
			} else if (index == 5) {
				addView(mSettingView.getView());
				setBackground(mSetting, R.drawable.setting_selected);
				mAlterId = R.drawable.setting_default;
				mSetting.performClick();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		if (requestCode == 100) {
			addToBlack(data);
		} else if (requestCode == 0) {
			// mSettingView.initAuth();
		}
		if (requestCode == 1) {
			updateMessage(data);
			mGraduateView.regetMessage();
		}
		if (requestCode == 2) {// 聊天数据库有变化
		}
		if (requestCode == 300) {
			String uName = data.getStringExtra("uname");
			mSettingView.setUname(uName, data.getStringExtra("sid"),
					data.getStringExtra("cid"), data.getStringExtra("year"));

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateMessage(Intent data) {
		if (null != data) {
			boolean isUpdatePic = data.getBooleanExtra("picupdate", false);
			if (isUpdatePic) {
				mBaokaoView.updataPersonalPic();
			}
		}
		// mSettingView.updateMessage();
	}

	private void addToBlack(Intent data) {
		try {
			User user = (User) data.getSerializableExtra("black");
			mGraduateView.deleteBlackList(user.getEmail());
			mFriendsView.deleteBlackList(user.getEmail());
		} catch (Exception e) {
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		getConfirm();
		super.onStart();
	}

	private void getConfirm() {
		if (mUser.getConfirm() == 1) {
			String url = UrlUtils.GET_CONFIRM + "?user.email="
					+ mUser.getEmail();
			new NetworkTask().execute(this, TagUtils.GET_CONFIRM, url);
		}
	}

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		if (tag == TagUtils.GET_CONFIRM) {
			if (obj != null) {
				ConfirmInfo info = (ConfirmInfo) obj;
				int confirm = info.getConfirm();
				if (confirm == 2) {
					saveConfirm(2);
					setAuthResult();
				} else if (confirm == 3) {
					saveConfirm(0);
					ToastUtils.showLongToast(HomeActivity.this,
							R.string.authorization_failed);
				}
			}
		}
		if (tag == TagUtils.UPGRADE_TAG) {
			if (null != obj) {
				mUpgrade = (Upgrade) obj;
				upgradePrompt();
			}
		}
		return null;
	}

	private void saveConfirm(int flag) {
		SharedPreUtils mSharedPre = new SharedPreUtils(
				KygroupApplication.getApplication());
		mSharedPre.addOrModify("confirm", flag);
		mUser.setConfirm(flag);
	}

	private void setAuthResult() {
		if (mIsForeground) {
			Builder builder = new Builder(this);
			builder.setTitle(R.string.tip);
			builder.setMessage(R.string.exit_relogin);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							KygroupApplication.mUser = new User();
							removePersonMsg();
							Intent intent = new Intent(HomeActivity.this,
									LoginActivity.class);
							startActivity(intent);
							finish();
						}
					});
			Dialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		}
	}

	private void removePersonMsg() {
		SharedPreferences prefs = getSharedPreferences(
				ConstantUtils.SHARED_PREF_FILE_NAME, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			if (null != mService) {
				mService.setHomeMessageListener(null);
			}
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = ((KyGroupService.MyBinder) service).getService();
		}
	};

	private void regisiterRecevier() {
		IntentFilter filter = new IntentFilter("com.kygroup.changed");
		filter.addAction(Constant.REFUSH_FRRIENDS);
		filter.addAction(Constant.UPDATE_USER_AVATAR);

		registerReceiver(receiver, filter);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.REFUSH_FRRIENDS)) {
				mFriendsView.getData();
				return;
			}
			if (action.equals(Constant.UPDATE_USER_AVATAR)) {
				mSettingView.updateAvatar();
				return;
			}
			User user = (User) intent.getSerializableExtra("ruser");
			mGraduateView.notifyAdapterChanged(user);
			mFriendsView.notifyAdapterChanged(user);

		}
	};

	public void stopService() {
		if (null != mService) {
			mService.stopSelf();
		}
	}

	public void getUpgradeInfo() {
		mUpgradeHelper = new UpgradeHelper();
		if (mUpgradeHelper.isPromptDialog()) {
			String version = DeviceUtils.getVersion();
			String url = UrlUtils.GET_UPGRADE + "version=" + version;
			new NetworkTask().execute(this, TagUtils.UPGRADE_TAG, url);
		}
	}

	public void upgradePrompt() {
		if (null != mUpgrade && mUpgrade.getNeedupgrade().equals("true")) {
			Builder builder = new Builder(this);
			if (null != mUpgrade.getNewversion()
					&& !StringUtils.isEmpty(mUpgrade.getNewversion()
							.getDescription())) {
				LinearLayout mUpgradeLayout = (LinearLayout) LayoutInflater
						.from(this).inflate(R.layout.showtextview, null);
				TextView mUpgradeText = (TextView) mUpgradeLayout
						.findViewById(R.id.showtext);
				mUpgradeText.setTextColor(Color.GRAY);
				mUpgradeText.setMovementMethod(ScrollingMovementMethod
						.getInstance());
				String info = mUpgrade.getNewversion().getDescription();
				info = info.replace('|', '\n');
				mUpgradeText.setText(info);
				mUpgradeText.setPadding(27, 0, 27, 0);
				builder.setView(mUpgradeLayout);
			}
			builder.setTitle(R.string.upgrade_prompt);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (null != mUpgradeHelper && null != mUpgrade
									&& null != mUpgrade.getNewversion()) {
								mUpgradeHelper.getApk(mUpgrade.getNewversion()
										.getUrl(), mUpgrade.getNewversion()
										.getVersion(), mUpgrade.getNewversion()
										.getSize());
								Toast.makeText(HomeActivity.this,
										R.string.download_apk, 500).show();
							}
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
			builder.create().show();
		}
	}

	private void setLeftBtn() {
		setLeftBg(R.drawable.add_function);
		setLeftBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mFunctionPopupWindow.showAsDropDown(mLayout, 0, 0);
			}
		});
	}

	private void initPopupWindow() {
		String[] lists = getResources().getStringArray(R.array.add_funshion);
		View view = LayoutInflater.from(this).inflate(R.layout.add_function,
				null);
		view.setBackgroundColor(Color.WHITE);
		mFunctionView = (GridView) view.findViewById(R.id.function_grid);
		mFunctionView.setOnItemClickListener(this);
		mFunctionAdapter = new FunctionAdapter(this, lists);
		mFunctionView.setAdapter(mFunctionAdapter);
		mFunctionPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mFunctionPopupWindow.setFocusable(true);
		mFunctionPopupWindow.setOutsideTouchable(true);
		mFunctionPopupWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// ToastUtils.showShortToast("aaaaaaaa");
	}

	public Announce getAnnounce() {
		return mAnnounce;
	}

	public int getBottomHeight() {
		if (null != mBottomBar) {
			return mBottomBar.getHeight();
		}
		return 0;
	}

	public int getTitleHeight() {
		if (null != mLayout) {
			return mLayout.getHeight();
		}
		return 0;
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			// 消息id
			String msgId = intent.getStringExtra("msgid");
			System.out.println("id::::::::::::::::" + msgId);
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			// 当前页面如果为聊天历史页面，刷新此页面
			mMessageView.refresh();
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	private ChengJi chengji = new ChengJi();

	private void getChengJI() {
		chengji.setUser_id(KygroupApplication.mUser.getEmail());
		GetChengJiTask task = new GetChengJiTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
			}
		});
		task.executeParallel(chengji);
	}

	public ChengJi getChengji() {
		return chengji;
	}
}
