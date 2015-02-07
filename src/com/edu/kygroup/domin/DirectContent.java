package com.edu.kygroup.domin;

import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;

public class DirectContent {
	private String daoshi = "";
	private String kemu = "";
	private String beizhu = "";
	private String zhengzhi = "";
	private String waiyu = "";
	private String major1 = "";
	private String major2 = "";

	public String getZhengzhi() {
		return zhengzhi;
	}

	public void setZhengzhi(String zhengzhi) {
		this.zhengzhi = zhengzhi;
	}

	public String getWaiyu() {
		return waiyu;
	}

	public void setWaiyu(String waiyu) {
		this.waiyu = waiyu;
	}

	public String getMajor1() {
		return major1;
	}

	public void setMajor1(String major1) {
		this.major1 = major1;
	}

	public String getMajor2() {
		return major2;
	}

	public void setMajor2(String major2) {
		this.major2 = major2;
	}

	public String getDaoshi() {
		return daoshi;
	}

	public void setDaoshi(String daoshi) {
		this.daoshi = daoshi;
	}

	public String getKemu() {
		return kemu;
	}

	public void setKemu(String kemu) {
		this.kemu = kemu;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public void refush(String url) {
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			int res = obj.getInt("result");
			if (res != 200) {
			}
			JSONObject jsonDetail = obj.getJSONObject("detail");
			this.beizhu = jsonDetail.getString("note");
			this.daoshi = jsonDetail.getString("teacher");
			this.major1 = jsonDetail.getString("major1");
			this.major2 = jsonDetail.getString("major2");
			this.waiyu = jsonDetail.getString("foreignLanguage");
			this.zhengzhi = jsonDetail.getString("politics");
		} catch (Exception e) {
		}
	}
}
