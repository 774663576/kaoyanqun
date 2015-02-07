package com.edu.kygroup.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.edu.keygroup.selectshcool.XueYuan;
import com.edu.keygroup.selectshcool.ZhuanYe;
import com.edu.kygroup.domin.Colleage;

public class UniversityParse {
	private static UniversityParse mParse;
	private static HttpAgent mHttpAgent;

	public static UniversityParse getInstance() {
		if (null == mParse) {
			mParse = new UniversityParse();
		}
		if (null == mHttpAgent) {
			mHttpAgent = new HttpAgent();
		}
		return mParse;
	}

	public ArrayList<XueYuan> parseColleage(String url) {
		ArrayList<XueYuan> lists = new ArrayList<XueYuan>();
		String result = mHttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray ary = obj.optJSONArray("colleges");
			for (int i = 0; null != ary && i < ary.length(); i++) {
				JSONObject item = ary.getJSONObject(i);
				XueYuan col = new XueYuan();
				col.setmId(item.optString("ceid"));
				// col.setName(item.optString("cename"));
				col.setXueYuanName(item.optString("cename"));
				lists.add(col);
			}
		} catch (Exception e) {
		}
		return lists;
	}

	public ArrayList<ZhuanYe> parseMajor(String url) {
		ArrayList<ZhuanYe> lists = new ArrayList<ZhuanYe>();
		String result = mHttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray ary = obj.optJSONArray("major");
			for (int i = 0; null != ary && i < ary.length(); i++) {
				JSONObject item = ary.getJSONObject(i);
				ZhuanYe col = new ZhuanYe();
				col.setZhuanyeID(item.optString("mid"));
				col.setZhuanYeName(item.optString("mname"));
				lists.add(col);
			}
		} catch (Exception e) {
		}
		return lists;
	}
}
