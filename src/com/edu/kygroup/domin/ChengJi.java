package com.edu.kygroup.domin;

import java.io.Serializable;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;
import com.edu.kygroup.utils.UrlUtils;

public class ChengJi implements Serializable {
	private String user_id = "";
	private String object1 = "";
	private String fenshu1 = "";
	private String object2 = "";
	private String fenshu2 = "";
	private String object3 = "";
	private String fenshu3 = "";
	private String object4 = "";
	private String fenshu4 = "";

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getObject1() {
		return object1;
	}

	public void setObject1(String object1) {
		this.object1 = object1;
	}

	public String getObject2() {
		return object2;
	}

	public void setObject2(String object2) {
		this.object2 = object2;
	}

	public String getObject3() {
		return object3;
	}

	public void setObject3(String object3) {
		this.object3 = object3;
	}

	public String getObject4() {
		return object4;
	}

	public void setObject4(String object4) {
		this.object4 = object4;
	}

	public String getFenshu1() {
		return fenshu1;
	}

	public void setFenshu1(String fenshu1) {
		this.fenshu1 = fenshu1;
	}

	public String getFenshu2() {
		return fenshu2;
	}

	public void setFenshu2(String fenshu2) {
		this.fenshu2 = fenshu2;
	}

	public String getFenshu3() {
		return fenshu3;
	}

	public void setFenshu3(String fenshu3) {
		this.fenshu3 = fenshu3;
	}

	public String getFenshu4() {
		return fenshu4;
	}

	public void setFenshu4(String fenshu4) {
		this.fenshu4 = fenshu4;
	}

	public int addChengji() {
		int res = -1;
		StringBuffer buf = null;
		buf = new StringBuffer(UrlUtils.ADD_CHENGJI);
		buf.append("&chengji.user_id=" + user_id);
		buf.append("&chengji.object1=" + URLEncoder.encode(object1));
		buf.append("&chengji.object2=" + URLEncoder.encode(object2));
		buf.append("&chengji.object3=" + URLEncoder.encode(object3));
		buf.append("&chengji.object4=" + URLEncoder.encode(object4));
		buf.append("&chengji.fenshu1=" + fenshu1);
		buf.append("&chengji.fenshu2=" + fenshu2);
		buf.append("&chengji.fenshu3=" + fenshu3);
		buf.append("&chengji.fenshu4=" + fenshu4);
		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
		} catch (JSONException e) {
		}
		return res;

	}

	public void getChengji() {
		StringBuffer buf = null;
		buf = new StringBuffer(UrlUtils.GET_CHENGJI);
		buf.append("&user_id=" + user_id);
		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			int res = obj.getInt("result");
			if (res == 200) {
				String objRes = obj.getString("chengji");
				JSONObject objChengji = new JSONObject(objRes);
				this.fenshu1 = objChengji.getString("fenshu1");
				this.fenshu2 = objChengji.getString("fenshu2");
				this.fenshu3 = objChengji.getString("fenshu3");
				this.fenshu4 = objChengji.getString("fenshu4");
				this.object1 = objChengji.getString("object1");
				this.object2 = objChengji.getString("object2");
				this.object3 = objChengji.getString("object3");
				this.object4 = objChengji.getString("object4");

			}
		} catch (JSONException e) {
		}
	}
}
