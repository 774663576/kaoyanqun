package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;
import com.edu.kygroup.utils.UrlUtils;

public class TaskList {
	private List<Task> lists = new ArrayList<Task>();
	private int sid;
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<Task> getLists() {
		return lists;
	}

	public void setLists(List<Task> lists) {
		this.lists = lists;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int refush() {
		int res = -1;
		StringBuffer buf = new StringBuffer(UrlUtils.GET_TASK_LIST);
		buf.append("&task.sid=" + sid);
		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		System.out.println("task:::::::::::::::" + result);
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
			if (res != 200) {
				return res;
			}
			JSONArray jsonArr = obj.getJSONArray("tasks");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject json = jsonArr.optJSONObject(i);
				Task task = new Task();
				task.setSid(json.getInt("sid"));
				task.setTask_category(json.getInt("task_category"));
				task.setTask_content(json.getString("task_content"));
				task.setTask_id(json.getInt("task_id"));
				task.setTask_price(json.getString("task_price"));
				task.setTask_time(json.getString("task_time"));
				task.setTask_title(json.getString("task_title"));
				task.setUser_id(json.getString("user_id"));
				task.setUser_avatar(json.getString("user_avatar"));
				task.setUser_name(json.getString("user_name"));
				task.setUser_major(json.getString("user_major"));
				task.setTask_comment_count(json.getInt("task_comment_count"));
				lists.add(task);
			}
		} catch (JSONException e) {
		}
		return res;
	}

	public int getMyTask() {
		int res = -1;
		StringBuffer buf = new StringBuffer(UrlUtils.GET_MY_TASK);
		buf.append("&user_id=" + user_id);
		String url = buf.toString();
		String result = HttpAgent.httpPost(url);
		System.out.println("task:::::::::::::::" + result);
		try {
			JSONObject obj = new JSONObject(result);
			res = obj.getInt("result");
			if (res != 200) {
				return res;
			}
			JSONArray jsonArr = obj.getJSONArray("tasks");
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject json = jsonArr.optJSONObject(i);
				Task task = new Task();
				task.setSid(json.getInt("sid"));
				task.setTask_category(json.getInt("task_category"));
				task.setTask_content(json.getString("task_content"));
				task.setTask_id(json.getInt("task_id"));
				task.setTask_price(json.getString("task_price"));
				task.setTask_time(json.getString("task_time"));
				task.setTask_title(json.getString("task_title"));
				task.setUser_id(json.getString("user_id"));
				task.setUser_avatar(json.getString("user_avatar"));
				task.setUser_name(json.getString("user_name"));
				task.setUser_major(json.getString("user_major"));
				task.setTask_comment_count(json.getInt("task_comment_count"));
				lists.add(task);
			}
		} catch (JSONException e) {
		}
		return res;
	}
}
