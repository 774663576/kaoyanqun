package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;

public class ColleageList {
	private List<ColleageInfo> colleageList = new ArrayList<ColleageInfo>();

	public List<ColleageInfo> getColleageList() {
		return colleageList;
	}

	public void setColleageList(List<ColleageInfo> colleageList) {
		this.colleageList = colleageList;
	}

	public List<ColleageInfo> refush(String url) {
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray jsonArr = obj.getJSONArray("infos");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject colleage = jsonArr.optJSONObject(i);
				ColleageInfo info = new ColleageInfo();
				info.setCeid(colleage.getInt("ceid"));
				info.setCename(colleage.getString("cename"));
				info.setMid(colleage.getInt("mid"));
				info.setMname(colleage.getString("mname"));
				info.setSid(colleage.getInt("sid"));
				info.setSname(colleage.getString("sname"));
				colleageList.add(info);
			}

		} catch (Exception e) {
		}
		return colleageList;
	}

}
