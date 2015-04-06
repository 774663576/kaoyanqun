package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.edu.kygroup.utils.Util;

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

	private String str_zhengxuezhang_hiht = "1、指导专业课复习\n2、指导复试\n3、讲讲本专业录取情况\n4、帮忙推荐导师\n请大家务必仔细查看是否通过研究生身份认证,责任自负";
	private String str_qiuziliao_hiht = "建议详细说明所求资料，如：\n1、专业课机械原理真题2006-2014年（建议价格5元/年/科）；\n2、专业课笔记（建议价格30元/科）；\n3、专业课课件（建议价格30元/科）\n避免：求某专业所有资料（很多东西都是毫无价值的）。\n请大家务必仔细查看是否通过研究生身份认证，责任自负";

	private List<String> list_zhuti = new ArrayList<String>();

	private Task task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish_task);
		list_zhuti.add("求专业课复习资料 ");
		list_zhuti.add("求公共课（政治/数学/外语）复习资料");
		task = (Task) getIntent().getSerializableExtra("task");
		initView();
		setValue();
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

	private void setValue() {
		if (task != null) {
			if (task.getTask_category() == 1) {
				img_qiuziliao.setSelected(true);
				task_category = 1;
				img_qita.setSelected(false);
				img_zhengxuezhang.setSelected(false);
			} else if (task.getTask_category() == 2) {
				img_zhengxuezhang.setSelected(true);
				task_category = 2;
				img_qiuziliao.setSelected(false);
				img_qita.setSelected(false);
			} else if (task.getTask_category() == 2) {
				img_qita.setSelected(true);
				task_category = 3;
				img_qiuziliao.setSelected(false);
				img_zhengxuezhang.setSelected(false);
			}
			edit_content.setText(task.getTask_content());
			edit_jiage.setText(task.getTask_price());
			edit_zhuti.setText(task.getTask_title());
		}
	}

	private void setListener() {
		img_qita.setOnClickListener(this);
		img_qiuziliao.setOnClickListener(this);
		img_zhengxuezhang.setOnClickListener(this);
		img_submit.setOnClickListener(this);
		edit_zhuti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pop = new TaskZhuTiPopwindow(PublishTaskActivity.this, v,
						list_zhuti);
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
			list_zhuti.clear();
			list_zhuti.add("征研友一起复习");
			list_zhuti.add("征研友参观校园");
			break;
		case R.id.img_qiuziliao:
			img_qiuziliao.setSelected(true);
			img_qita.setSelected(false);
			img_zhengxuezhang.setSelected(false);
			task_category = 1;
			edit_content.setHint(str_qiuziliao_hiht);
			list_zhuti.clear();
			list_zhuti.add("求专业课复习资料 ");
			list_zhuti.add("求公共课（政治/数学/外语）复习资料");

			break;
		case R.id.img_zhengxuezhang:
			img_zhengxuezhang.setSelected(true);
			img_qiuziliao.setSelected(false);
			img_qita.setSelected(false);
			task_category = 2;
			edit_content.setHint(str_zhengxuezhang_hiht);
			list_zhuti.clear();
			list_zhuti.add("指导专业课复习");
			list_zhuti.add("指导复试");
			list_zhuti.add("讲讲本专业录取情况");
			list_zhuti.add("帮忙推荐导师");
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
		if ("".equals(task_title)) {
			ToastUtils.showShortToast("请填写主题");
			return;
		}
		if ("".equals(task_jiage)) {
			ToastUtils.showShortToast("请填写酬劳");
			return;
		}
		startWaitingDialog();
		if (task == null) {
			task = new Task();
		}
		task.setSid(Integer.valueOf(KygroupApplication.getmApplication().mUser
				.getRSid()));
		task.setTask_category(task_category);
		task.setTask_content(task_content);
		task.setTask_price(task_jiage);
		task.setTask_title(task_title);
		task.setTask_time(Util.getCurrentDate());
		task.setUser_avatar(KygroupApplication.getmApplication().mUser.getPic());
		task.setUser_name(KygroupApplication.getmApplication().mUser
				.getNickName());
		task.setUser_major(KygroupApplication.getmApplication().mUser
				.getRMajor()
				+ "  "
				+ KygroupApplication.getmApplication().mUser.getRSchool());
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
				sendBroadcast(new Intent("com.kygroup.add.task").putExtra(
						"task", task));
				finish();

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
