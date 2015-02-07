package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;

public class TiaoJiYuanXiao {
	private String cename = "";// 学院名称
	private String sname = "";// 学校名称
	private String mname = "";// 专业
	private String url = "";// 链接

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getCename() {
		return cename;
	}

	public void setCename(String cename) {
		this.cename = cename;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<TiaoJiYuanXiao> refush(String url) {
		List<TiaoJiYuanXiao> lists = new ArrayList<TiaoJiYuanXiao>();
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray jsonArr = obj.getJSONArray("infos");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject dizJson = jsonArr.optJSONObject(i);
				TiaoJiYuanXiao tj = new TiaoJiYuanXiao();
				tj.setCename(dizJson.getString("cename"));
				tj.setSname(dizJson.getString("sname"));
				tj.setMname(dizJson.getString("mname"));
				tj.setUrl(dizJson.getString("url"));
				lists.add(tj);
			}

		} catch (Exception e) {
			return null;
		}
		return lists;
	}
}
