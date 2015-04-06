package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;
import com.edu.kygroup.utils.UrlUtils;

public class TipCommentList {
	private List<TipsComment> lists = new ArrayList<TipsComment>();
	private int tip_id;

	public List<TipsComment> getLists() {
		return lists;
	}

	public void setLists(List<TipsComment> lists) {
		this.lists = lists;
	}

	public int getTip_id() {
		return tip_id;
	}

	public void setTip_id(int tip_id) {
		this.tip_id = tip_id;
	}

	public int getCommentLists() {
		int res = -1;
		StringBuffer buf = new StringBuffer(UrlUtils.GET_TIP_COMMENT_LIST);
		buf.append("tip_id=" + tip_id);
		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
			if (res != 200) {
				return res;
			}
			JSONArray jsonArr = obj.getJSONArray("comments");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject json = jsonArr.optJSONObject(i);
				TipsComment comment = new TipsComment();
				comment.setComment_content(json.getString("comment_content"));
				comment.setComment_img_url(json.getString("comment_img_url"));
				comment.setComment_time(json.getString("comment_time"));
				comment.setUser_avatar(json.getString("user_avatar"));
				comment.setUser_name(json.getString("user_name"));
				comment.setUser_school(json.getString("user_school"));
				lists.add(comment);
			}
		} catch (JSONException e) {
		}
		return res;
	}
}
