package com.edu.kygroup.ui;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Upgrade;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.DeviceUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.UpgradeHelper;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CheckUpgradeActivity extends BaseActivity implements IBindData {

	private View mView;
	private UpgradeHelper mUpgradeHelper;
	private Upgrade mUpgrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setVersion();
		checkUpgrade();
	}

	@Override
	protected View setContentView() {
		// TODO Auto-generated method stub
		return mView = mInflater.inflate(R.layout.check_upgrade_activity, null);
	}

	private void initView() {
		setTitleText(R.string.check_update);
		setRightBtnVisibility(View.GONE);
		setLeftBtnVisibility(View.GONE);
	}

	private void setVersion() {
		String version = getString(R.string.title_activity_main) + " "
				+ DeviceUtils.getVersion();
		;
		((TextView) mView.findViewById(R.id.tip)).setText(version);
	}

	private void checkUpgrade() {
		((Button) mView.findViewById(R.id.check_upgrade))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						getUpgradeInfo();
					}
				});
	}

	public void getUpgradeInfo() {
		startWaitingDialog();
		mUpgradeHelper = new UpgradeHelper();
		String version = DeviceUtils.getVersion();
		String url = UrlUtils.GET_UPGRADE + "version=" + version;
		new NetworkTask().execute(this, TagUtils.UPGRADE_TAG, url);
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
								Toast.makeText(CheckUpgradeActivity.this,
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

	@Override
	public Object bindData(int tag, Object obj) {
		// TODO Auto-generated method stub
		if (tag == TagUtils.UPGRADE_TAG) {
			closeWaitingDialog();
			if (null != obj) {
				mUpgrade = (Upgrade) obj;
				if (mUpgrade.getNeedupgrade().equals("true")) {
					upgradePrompt();
				} else {
					Toast.makeText(CheckUpgradeActivity.this,
							R.string.upgrade_false, 1000).show();
				}

			}
		}
		return null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("CheckUpgradeActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("CheckUpgradeActivity");
		MobclickAgent.onPause(this);
	}
}
