package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;

public class DizcuzList {
	private String url = "";
	private List<Dizcuz> lists = new ArrayList<Dizcuz>();

	public List<Dizcuz> getLists() {
		return lists;
	}

	public void setLists(List<Dizcuz> lists) {
		this.lists = lists;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Dizcuz> refush() {
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			int res = obj.getInt("result");
			if (res != 200) {
				return null;
			}
			JSONArray jsonArr = obj.getJSONArray("dizcuz");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject dizJson = jsonArr.optJSONObject(i);
				Dizcuz dz = new Dizcuz();
				dz.setTime(dizJson.getString("createdtime"));
				dz.setTitle(dizJson.getString("title"));
				dz.setUrl(dizJson.getString("url"));
				dz.setAuthor(dizJson.getString("author"));
				lists.add(dz);
			}

		} catch (Exception e) {
		}
		return lists;
	}
}
