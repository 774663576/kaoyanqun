package com.edu.kygroup.domin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.UrlUtils;

public class TipsComment {
	private int tip_id;
	private String comment_content = "";
	private String comment_time = "";
	private String comment_img_url = "";
	private String user_name = "";
	private String user_avatar = "";
	private String user_school = "";

	public int getTip_id() {
		return tip_id;
	}

	public void setTip_id(int tip_id) {
		this.tip_id = tip_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	public String getComment_img_url() {
		return comment_img_url;
	}

	public void setComment_img_url(String comment_img_url) {
		this.comment_img_url = comment_img_url;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public String getUser_school() {
		return user_school;
	}

	public void setUser_school(String user_school) {
		this.user_school = user_school;
	}

	public int uploadComment() {
		File file = new File(comment_img_url);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tip_id", tip_id);
		map.put("comment_content", comment_content);
		map.put("user_name", user_name);
		map.put("user_avatar", user_avatar);
		map.put("user_school", user_school);
		String result = FileUtils.postDataWithFile(UrlUtils.BASE_URL
				+ "/exam/servlet/TipCommentServlet", map, file);
		int res = -1;
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
		} catch (JSONException e) {
		}
		return res;
	}
}
