/**
 * 工程名: KyGroup
 * 文件名: PasswordActivity.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-11-24下午1:19:26
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.net.NetworkTask;
import com.edu.kygroup.utils.TagUtils;
import com.edu.kygroup.utils.ToastUtils;
import com.edu.kygroup.utils.UrlUtils;
import com.umeng.analytics.MobclickAgent;


/**
 * 类名: PasswordActivity <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2013-11-24 下午1:19:26 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class PasswordActivity extends BaseActivity implements IBindData,OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleText(R.string.str_modify_password);
		setRightBtnVisibility(View.GONE);
		setLeftBtnVisibility(View.GONE);
		setBottomBarVisibility(View.GONE);
		initView();
	}
	
	private void initView(){
		((Button)findViewById(R.id.back_btn)).setOnClickListener(this);
		((Button)findViewById(R.id.submit_btn)).setOnClickListener(this);
		((TextView)findViewById(R.id.lose_password_tv)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back_btn:
			finish();
			break;
		case R.id.submit_btn:
			String oldPassword = ((EditText)findViewById(R.id.old_edit)).getText().toString();
			String newPassword = ((EditText)findViewById(R.id.new_edit)).getText().toString();
			String surePassword = ((EditText)findViewById(R.id.sure_edit)).getText().toString();
			
			if (oldPassword==null || "".equals(oldPassword)){
				ToastUtils.showShortToast(PasswordActivity.this, 
						R.string.tips_old_password_empty);
				return;
			} else if(newPassword==null || "".equals(newPassword)){
				ToastUtils.showShortToast(PasswordActivity.this, 
						R.string.tips_new_password_empty);
				return;
			} else if(surePassword==null || "".equals(surePassword)){
				ToastUtils.showShortToast(PasswordActivity.this, 
						R.string.tips_new_password_empty);
				return;
			} else if(!newPassword.equals(surePassword)){
				ToastUtils.showShortToast(PasswordActivity.this, 
						R.string.tips_not_same_password_empty);
				return;
			} else {
				postForModifyPassword(oldPassword,newPassword);
			}
			break;
		case R.id.lose_password_tv:{
			postForResetPassword();
			break;
		}
		}
		
	}
	
	private void postForModifyPassword(String oldPassword,String newPassword){
		String url = UrlUtils.MODIFY_PASSWORD_URL+
				"user.email="+mUser.getEmail()+
				"&user.password="+oldPassword+
				"&newpassword="+newPassword;
		startWaitingDialog();
		new NetworkTask().execute(PasswordActivity.this,TagUtils.MODIFY_PASSWORD,url);
	}
	
	private void postForResetPassword(){
		String url = UrlUtils.LOSE_PASSWORD_URL+
				"user.email="+mUser.getEmail();
		startWaitingDialog();
		new NetworkTask().execute(PasswordActivity.this,TagUtils.LOSE_PASSWORD,url);
	}

	@Override
	public Object bindData(int tag, Object obj) {
		
		switch(tag){
		case TagUtils.MODIFY_PASSWORD:{
			closeWaitingDialog();
			if ((Boolean)obj){
				ToastUtils.showShortToast(
						PasswordActivity.this,R.string.tips_password_modify_success);
				finish();
			} else {
				ToastUtils.showShortToast(
						PasswordActivity.this,R.string.tips_password_modify_failure);
			}
			break;
		}
		case TagUtils.LOSE_PASSWORD:{
			closeWaitingDialog();
			if(obj!=null && !(obj instanceof Boolean)){
				if("200".equals(
						obj.toString())){
					ToastUtils.showShortToast(
							PasswordActivity.this,R.string.tips_send_password_email);
					finish();
				} else if("-1".equals(obj.toString())){
					ToastUtils.showShortToast(
							PasswordActivity.this,R.string.email_error);
				} else{
					
				}
			
			}
		}
		break;
		default:
			break;
		}
		return null;
	}

	@Override
	protected View setContentView() {
		
		return mInflater.inflate(R.layout.password_activity, null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onPageStart("PasswordActivity"); //统计页面
		 MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("PasswordActivity"); 
		MobclickAgent.onPause(this);
	}
}

