package com.edu.kygroup.domin;

import java.io.Serializable;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;
import com.edu.kygroup.utils.UrlUtils;

public class Task implements Serializable {
	private int task_id;
	private int task_category;
	private String user_id = "";
	private int sid;
	private String task_title = "";
	private String task_content = "";
	private String task_time = "";
	private String task_price = "";
	private String user_name = "";
	private String user_school = "";
	private String user_avatar = "";
	private String user_major = "";
	private int task_comment_count;

	public int getTask_comment_count() {
		return task_comment_count;
	}

	public void setTask_comment_count(int task_comment_count) {
		this.task_comment_count = task_comment_count;
	}

	public String getUser_major() {
		return user_major;
	}

	public void setUser_major(String user_major) {
		this.user_major = user_major;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_school() {
		return user_school;
	}

	public void setUser_school(String user_school) {
		this.user_school = user_school;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public int getTask_category() {
		return task_category;
	}

	public void setTask_category(int task_category) {
		this.task_category = task_category;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getTask_title() {
		return task_title;
	}

	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}

	public String getTask_content() {
		return task_content;
	}

	public void setTask_content(String task_content) {
		this.task_content = task_content;
	}

	public String getTask_time() {
		return task_time;
	}

	public void setTask_time(String task_time) {
		this.task_time = task_time;
	}

	public String getTask_price() {
		return task_price;
	}

	public void setTask_price(String task_price) {
		this.task_price = task_price;
	}

	public int addTask() {
		int res = -1;
		StringBuffer buf = null;
		if (task_id > 0) {
			buf = new StringBuffer(UrlUtils.EDIT_TASK);
		} else {
			buf = new StringBuffer(UrlUtils.ADD_TASK);
		}
		buf.append("&task.task_id=" + task_id);
		buf.append("&task.task_category=" + task_category);
		buf.append("&task.user_id=" + user_id);
		buf.append("&task.sid=" + sid);
		buf.append("&task.task_title=" + URLEncoder.encode(task_title));
		buf.append("&task.task_content=" + URLEncoder.encode(task_content));
		buf.append("&task.task_price=" + URLEncoder.encode(task_price));
		buf.append("&task.user_major=" + URLEncoder.encode(user_major));

		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
			if (res == 200) {
				task_id = obj.getInt("task_id");
			}
		} catch (JSONException e) {
		}
		return res;

	}

	public int delTask() {
		int res = -1;
		StringBuffer buf = new StringBuffer(UrlUtils.DEL_TASK);
		buf.append("&task_id=" + task_id);
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
