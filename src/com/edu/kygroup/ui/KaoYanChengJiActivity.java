package com.edu.kygroup.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.ChengJi;
import com.edu.kygroup.task.AbstractTaskPostCallBack;
import com.edu.kygroup.task.AddChengJiTask;
import com.edu.kygroup.utils.ToastUtils;

public class KaoYanChengJiActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout layout_object2;
	private LinearLayout layout_object3;
	private LinearLayout layout_object4;
	private ImageView img_add;
	private int count = 1;

	private EditText edit_object1, edit_object2, edit_object3, edit_object4;
	private EditText edit_fenshu1, edit_fenshu2, edit_fenshu3, edit_fenshu4;

	private ChengJi chengji;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chengji = (ChengJi) getIntent().getSerializableExtra("chengji");
		initView();
		setValue();
	}

	@Override
	protected View setContentView() {
		return inflate(R.layout.activity_kao_yan_cheng_ji, null);
	}

	private void initView() {
		setLeftBtnVisibility(View.GONE);
		setTitleText("考研成绩");
		img_add = (ImageView) findViewById(R.id.img_add);
		layout_object2 = (LinearLayout) findViewById(R.id.layout_object2);
		layout_object3 = (LinearLayout) findViewById(R.id.layout_object3);
		layout_object4 = (LinearLayout) findViewById(R.id.layout_object4);
		setRightBtnBg(R.drawable.submit);
		setRightBtnVisibility(View.VISIBLE);
		edit_fenshu1 = (EditText) findViewById(R.id.edit_object1_fen);
		edit_fenshu2 = (EditText) findViewById(R.id.edit_object2_fen);
		edit_fenshu3 = (EditText) findViewById(R.id.edit_object3_fen);
		edit_fenshu4 = (EditText) findViewById(R.id.edit_object4_fen);
		edit_object1 = (EditText) findViewById(R.id.edit_object1_title);
		edit_object2 = (EditText) findViewById(R.id.edit_object2_title);
		edit_object3 = (EditText) findViewById(R.id.edit_object3_title);
		edit_object4 = (EditText) findViewById(R.id.edit_object4_title);
		setListener();
	}

	private void setListener() {
		img_add.setOnClickListener(this);
		setRightBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				upload();
			}
		});
	}

	private void setValue() {
		if (chengji == null) {
			return;
		}
		String object1 = chengji.getObject1();
		if (!"".equals(object1)) {
			edit_fenshu1.setText(chengji.getFenshu1());
			edit_object1.setText(object1);
		}
		String object2 = chengji.getObject2();
		if (!"".equals(object2)) {
			edit_fenshu2.setText(chengji.getFenshu2());
			edit_object2.setText(object2);
			layout_object2.setVisibility(View.VISIBLE);
			count = 2;
		}
		String object3 = chengji.getObject3();
		if (!"".equals(object3)) {
			edit_fenshu3.setText(chengji.getFenshu3());
			edit_object3.setText(object3);
			layout_object3.setVisibility(View.VISIBLE);
			count = 3;
		}
		String object4 = chengji.getObject4();
		if (!"".equals(object4)) {
			edit_fenshu4.setText(chengji.getFenshu4());
			edit_object4.setText(object4);
			layout_object4.setVisibility(View.VISIBLE);
			count = 4;
			img_add.setVisibility(View.GONE);
		}
	}

	private void upload() {
		String object1 = edit_object1.getText().toString().trim();
		String fenshu1 = edit_fenshu1.getText().toString().trim();
		if (object1.length() == 0 || fenshu1.length() == 0) {
			return;
		}
		startWaitingDialog();
		AddChengJiTask task = new AddChengJiTask();
		task.setmCallBack(new AbstractTaskPostCallBack<Integer>() {
			@Override
			public void taskFinish(Integer result) {
				closeWaitingDialog();
				if (result < 0) {
					ToastUtils.showShortToast("操作失败");
				} else {
					ToastUtils.showShortToast("提交成功");
					finish();
				}
			}
		});
		ChengJi chengji = new ChengJi();
		chengji.setUser_id(KygroupApplication.getmApplication().mUser
				.getEmail());
		chengji.setFenshu1(fenshu1);
		chengji.setFenshu2(edit_fenshu2.getText().toString());
		chengji.setFenshu3(edit_fenshu3.getText().toString());
		chengji.setFenshu4(edit_fenshu4.getText().toString());
		chengji.setObject1(object1);
		chengji.setObject2(edit_object2.getText().toString().trim());
		chengji.setObject3(edit_object3.getText().toString().trim());
		chengji.setObject4(edit_object4.getText().toString().trim());
		task.executeParallel(chengji);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_add:
			count++;
			if (count == 2) {
				layout_object2.setVisibility(View.VISIBLE);
			} else if (count == 3) {
				layout_object3.setVisibility(View.VISIBLE);
			} else if (count == 4) {
				layout_object4.setVisibility(View.VISIBLE);
				img_add.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}
}
