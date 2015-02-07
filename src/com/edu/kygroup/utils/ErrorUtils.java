package com.edu.kygroup.utils;

import com.edu.kygroup.R;
import com.edu.kygroup.ui.KygroupApplication;

public class ErrorUtils {
	public static final int SUBMIT_ERROR = 1;
	public static final int EMAIL_ERROR = -1;//邮箱无效
	
	public static final int EMAIL_REGISTER_ERROR = -2;//邮箱已经被注册
	
	public static final int PWD_ERROR = -3;//密码有误
	
	public static final int USER_NOT_EXIST_ERROR = -4;//用户不存在
	
	public static final int YEAR_ERROR = -5;//报考年份早于本科入学年份
	
	public static String getErrorMsg(int flag){
		switch(flag){
		case EMAIL_ERROR:
			return KygroupApplication.getApplication().getString(R.string.email_error);
		case EMAIL_REGISTER_ERROR:
			return KygroupApplication.getApplication().getString(R.string.email_register_error);
		case PWD_ERROR:
			return KygroupApplication.getApplication().getString(R.string.pwd_error);
		case USER_NOT_EXIST_ERROR:
			return KygroupApplication.getApplication().getString(R.string.user_not_exist);
		case YEAR_ERROR:
			return KygroupApplication.getApplication().getString(R.string.year_error);
		case SUBMIT_ERROR:
			return KygroupApplication.getApplication().getString(R.string.per_submit_failed);
		default:
			return KygroupApplication.getApplication().getString(R.string.get_msg_error);
		}
	}
}
