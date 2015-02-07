/**
 * 工程名: KyGroup
 * 文件名: FeedBackActivity.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-12-15下午2:16:23
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.ui;

import java.net.URLEncoder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 类名: FeedBackActivity <br/>
 * 功能: TODO 意见反馈. <br/>
 * 日期: 2013-12-15 下午2:16:23 <br/>
 * 
 * @author lx
 * @version
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener,
		IBindData {
	private View mView;
	private EditText feedbackEdit;
	private RelativeLayout content_layout;
	private TextView txt_content;
	private ImageView img_avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText(R.string.info_response);
		setLeftBtnVisibility(View.GONE);
		mView = LayoutInflater.from(this).inflate(R.layout.feedback_activity,
				null);
		((Button) mView.findViewById(R.id.feedback_commit_btn))
				.setOnClickListener(this);
		feedbackEdit = (EditText) mView.findViewById(R.id.edit_content);

		content_layout = (RelativeLayout) mView
				.findViewById(R.id.content_layout);
		txt_content = (TextView) mView.findViewById(R.id.txt_content);
		img_avatar = (ImageView) mView.findViewById(R.id.img_avatar);
		content_layout.setVisibility(View.GONE);
		addView(mView);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		if (tag == TagUtils.POST_FEEDBACK) {
			closeWaitingDialog();
			if ((Boolean) obj) {
				ToastUtils.showShortToast(FeedBackActivity.this,
						R.string.feedback_success);
				// finish();
			} else {
				ToastUtils.showShortToast(FeedBackActivity.this,
						R.string.feedback_failure);
			}
		}
		return null;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.feedback_commit_btn:
			feedbacking();
			break;
		}

	}

	@Override
	protected View setContentView() {
		return mView;
	}

	private void feedbacking() {
		if (!StringUtils.isEmpty(feedbackEdit.getText().toString())) {
			startWaitingDialog();
			content_layout.setVisibility(View.VISIBLE);
			txt_content.setText(feedbackEdit.getText().toString());
			String url = UrlUtils.POST_FEEDBACK + "user.email="
					+ mUser.getEmail() + "&message="
					+ URLEncoder.encode(feedbackEdit.getText().toString());
			new NetworkTask().execute(this, TagUtils.POST_FEEDBACK, url);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("FeedBackActivity"); // 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FeedBackActivity");
		MobclickAgent.onPause(this);
	}
}
