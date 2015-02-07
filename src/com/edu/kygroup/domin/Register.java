package com.edu.kygroup.domin;

import java.io.Serializable;

import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;

/**
 * 获取注册相关的信息
 * 
 * @version
 */
public class Register implements Serializable {
	private long mCurTime;
	private String mMegMsg;
	private String chatName = "";// 环信id
	private String chatPasswrod = "";// 环信密码

	private String register_email = "";
	private String register_passwrod = "";

	private String register_gender = "M";// 注册性别
	private String register_nick_name = "";// 昵称
	private String register_Sid = "";// 注册学校id
	private String register_Cid = "";
	private String register_Mid = "";
	private String register_School_name = "";// 注册学校id
	private String register_Colleage_name = "";
	private String register_Major_name = "";
	private String register_year = "";
	private String register_role = "";
	private int register_province_id = 0;// 省份id
	private int register_city_id = 0;// 市id

	public String getRegister_School_name() {
		return register_School_name;
	}

	public void setRegister_School_name(String register_School_name) {
		this.register_School_name = register_School_name;
	}

	public String getRegister_Colleage_name() {
		return register_Colleage_name;
	}

	public void setRegister_Colleage_name(String register_Colleage_name) {
		this.register_Colleage_name = register_Colleage_name;
	}

	public String getRegister_Major_name() {
		return register_Major_name;
	}

	public void setRegister_Major_name(String register_Major_name) {
		this.register_Major_name = register_Major_name;
	}

	public long getmCurTime() {
		return mCurTime;
	}

	public void setmCurTime(long mCurTime) {
		this.mCurTime = mCurTime;
	}

	public String getmMegMsg() {
		return mMegMsg;
	}

	public void setmMegMsg(String mMegMsg) {
		this.mMegMsg = mMegMsg;
	}

	public String getRegister_email() {
		return register_email;
	}

	public void setRegister_email(String register_email) {
		this.register_email = register_email;
	}

	public String getRegister_passwrod() {
		return register_passwrod;
	}

	public void setRegister_passwrod(String register_passwrod) {
		this.register_passwrod = register_passwrod;
	}

	public String getRegister_gender() {
		return register_gender;
	}

	public void setRegister_gender(String register_gender) {
		this.register_gender = register_gender;
	}

	public String getRegister_nick_name() {
		return register_nick_name;
	}

	public void setRegister_nick_name(String register_nick_name) {
		this.register_nick_name = register_nick_name;
	}

	public String getRegister_Sid() {
		return register_Sid;
	}

	public void setRegister_Sid(String register_Sid) {
		this.register_Sid = register_Sid;
	}

	public String getRegister_Cid() {
		return register_Cid;
	}

	public void setRegister_Cid(String register_Cid) {
		this.register_Cid = register_Cid;
	}

	public String getRegister_Mid() {
		return register_Mid;
	}

	public void setRegister_Mid(String register_Mid) {
		this.register_Mid = register_Mid;
	}

	public String getRegister_year() {
		return register_year;
	}

	public void setRegister_year(String register_year) {
		this.register_year = register_year;
	}

	public String getRegister_role() {
		return register_role;
	}

	public void setRegister_role(String register_role) {
		this.register_role = register_role;
	}

	public int getRegister_province_id() {
		return register_province_id;
	}

	public void setRegister_province_id(int register_province_id) {
		this.register_province_id = register_province_id;
	}

	public int getRegister_city_id() {
		return register_city_id;
	}

	public void setRegister_city_id(int register_city_id) {
		this.register_city_id = register_city_id;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public String getChatPasswrod() {
		return chatPasswrod;
	}

	public void setChatPasswrod(String chatPasswrod) {
		this.chatPasswrod = chatPasswrod;
	}

	public long getCurTime() {
		return mCurTime;
	}

	public void setCurTime(long mCurTime) {
		this.mCurTime = mCurTime;
	}

	public String getMegMsg() {
		return mMegMsg;
	}

	public void setMegMsg(String mMegMsg) {
		this.mMegMsg = mMegMsg;
	}

	// 获取注册验证吗
	public String getVerifyCode(String url) {
		String result = HttpAgent.httpPost(url);
		// try {
		// JSONObject obj = new JSONObject(result);
		// if (obj.optString("result").equals("200")) {
		// }
		// } catch (Exception e) {
		// }
		return result;
	}

	/**
	 * 验证验证码是否正确
	 */
	public boolean checkCode(String url) {
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.optString("result").equals("200")) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
