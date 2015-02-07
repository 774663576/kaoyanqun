package com.edu.kygroup.net;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.edu.kygroup.domin.LoginInfo;
/*
 * 登录与注册相关的解析类
 */
import com.edu.kygroup.domin.Register;

public class LoginParse {
	public final static String TAG = "http";
	private static LoginParse mParse;
	private static HttpAgent mHttpAgent;

	public static LoginParse getInstance() {
		if (null == mParse) {
			mParse = new LoginParse();
		}
		if (null == mHttpAgent) {
			mHttpAgent = new HttpAgent();
		}
		return mParse;
	}

	public void parseRegister(String url, Register reg) {
		String result = mHttpAgent.httpPost(url);
		System.out.println("result:::::::::::" + result);
		try {
			JSONObject obj = new JSONObject(result);
			reg.setCurTime(obj.optLong("timestamp"));
			reg.setMegMsg(obj.optString("result"));
			reg.setChatName(obj.getString("chatid"));
			reg.setChatPasswrod(obj.getString("chatpasswd"));
		} catch (Exception e) {
		}
	}

	public boolean submitMsg(String url) {
		String result = mHttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.optString("result").equals("200")) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public Object login(String url) {
		String result = mHttpAgent.httpPost(url);
		LoginInfo info = (LoginInfo) JSON.parseObject(result, LoginInfo.class);
		return info;
	}

	public Object losePassword(String url) {
		String result = mHttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			return obj.optString("result");
		} catch (Exception e) {
		}
		return false;
	}
}
