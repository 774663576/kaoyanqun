package com.edu.kygroup.domin;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;
import com.edu.kygroup.utils.UrlUtils;

public class TaskComment {
	private int task_id;
	private String user_id = "";
	private String task_comment_content = "";
	private String task_comment_time = "";
	private String user_name = "";
	private String user_avatar = "";
	private String user_major = "";

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTask_comment_content() {
		return task_comment_content;
	}

	public void setTask_comment_content(String task_comment_content) {
		this.task_comment_content = task_comment_content;
	}

	public String getTask_comment_time() {
		return task_comment_time;
	}

	public void setTask_comment_time(String task_comment_time) {
		this.task_comment_time = task_comment_time;
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

	public String getUser_major() {
		return user_major;
	}

	public void setUser_major(String user_major) {
		this.user_major = user_major;
	}

	public int addTaskComment() {
		int res = -1;
		StringBuffer buf = new StringBuffer(UrlUtils.ADD_TASK_COMMENT);
		buf.append("&comment.task_id=" + task_id);
		buf.append("&comment.user_id=" + user_id);
		buf.append("&comment.task_comment_content="
				+ URLEncoder.encode(task_comment_content));
		buf.append("&comment.user_major=" + URLEncoder.encode(user_major));
		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
		} catch (JSONException e) {
		}
		return res;

	}
}
