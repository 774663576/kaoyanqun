package com.edu.kygroup.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Task;
import com.edu.kygroup.popupwindow.TaskZhuTiPopwindow;
import com.edu.kygroup.popupwindow.TaskZhuTiPopwindow.OnlistOnclick;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.AddTask;
import com.edu.kygroup.utils.ToastUtils;

public class PublishTaskActivity extends Activity implements OnClickListener {
	private ImageView img_qiuziliao;
	private ImageView img_zhengxuezhang;
	private ImageView img_qita;
	private EditText edit_zhuti;
	private EditText edit_jiage;
	private EditText edit_content;
	private ImageView img_submit;

	public Dialog mDialog = null;

	private int task_category = 1;

	private TaskZhuTiPopwindow pop;

	private String str_zhengxuezhang_hiht = "1、指导专业课复习\n2、指导复试\n3、讲讲本专业录取情况\n4、帮忙推荐导师";
	private String str_qiuziliao_hiht = "建议详细说明所求资料，如：1、专业课机械原理真题2006-2014年（建议价格5元/年/科）；2、专业课笔记（建议价格30元/科）；3、专业课课件（建议价格30元/科）\n避免：求某专业所有资料（很多东西都是毫无价值的）。";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish_task);
		initView();
	}

	private void initView() {
		img_qita = (ImageView) findViewById(R.id.img_qita);
		img_qiuziliao = (ImageView) findViewById(R.id.img_qiuziliao);
		img_zhengxuezhang = (ImageView) findViewById(R.id.img_zhengxuezhang);
		edit_content = (EditText) findViewById(R.id.edit_content);
		edit_jiage = (EditText) findViewById(R.id.edit_jiage);
		edit_zhuti = (EditText) findViewById(R.id.edit_zhuti);
		img_submit = (ImageView) findViewById(R.id.img_submit);
		img_qiuziliao.setSelected(true);
		edit_content.setHint(str_qiuziliao_hiht);
		setListener();
	}

	private void setListener() {
		img_qita.setOnClickListener(this);
		img_qiuziliao.setOnClickListener(this);
		img_zhengxuezhang.setOnClickListener(this);
		img_submit.setOnClickListener(this);
		edit_zhuti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pop = new TaskZhuTiPopwindow(PublishTaskActivity.this, v);
				pop.setOnlistOnclick(new OnlistOnclick() {

					@Override
					public void onclick(String str) {
						edit_zhuti.setText(str);
					}
				});
				pop.show();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_qita:
			img_qita.setSelected(true);
			img_qiuziliao.setSelected(false);
			img_zhengxuezhang.setSelected(false);
			task_category = 3;
			edit_content.setHint("");
			break;
		case R.id.img_qiuziliao:
			img_qiuziliao.setSelected(true);
			img_qita.setSelected(false);
			img_zhengxuezhang.setSelected(false);
			task_category = 1;
			edit_content.setHint(str_qiuziliao_hiht);

			break;
		case R.id.img_zhengxuezhang:
			img_zhengxuezhang.setSelected(true);
			img_qiuziliao.setSelected(false);
			img_qita.setSelected(false);
			task_category = 2;
			edit_content.setHint(str_zhengxuezhang_hiht);

			break;
		case R.id.img_submit:
			addTask();
			break;
		default:
			break;
		}
	}

	private void addTask() {
		String task_title = edit_zhuti.getText().toString().trim();
		String task_jiage = edit_jiage.getText().toString();
		String task_content = edit_content.getText().toString().trim();
		if ("".equals(task_content)) {
			ToastUtils.showShortToast("请填写内容");
			return;
		}
		if ("".equals(task_jiage)) {
			ToastUtils.showShortToast("请填写主题");
			return;
		}
		if ("".equals(task_jiage)) {
			ToastUtils.showShortToast("请填写酬劳");
			return;
		}
		startWaitingDialog();
		Task task = new Task();
		task.setSid(Integer.valueOf(KygroupApplication.getmApplication().mUser
				.getRSid()));
		task.setTask_category(task_category);
		task.setTask_content(task_content);
		task.setTask_price(task_jiage);
		task.setTask_title(task_title);
		task.setUser_major(KygroupApplication.getmApplication().mUser
				.getRMajor());
		task.setUser_id(KygroupApplication.getmApplication().mUser.getEmail());
		AddTask aTask = new AddTask();
		aTask.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result != 200) {
					ToastUtils.showShortToast("提交失败");
					return;
				}
				ToastUtils.showShortToast("提交成功");

			}
		});
		aTask.executeParallel(task);
	}

	public void startWaitingDialog() {
		try {
			if (mDialog == null) {
				mDialog = new Dialog(this, R.style.waiting);
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
		}
	}
}
