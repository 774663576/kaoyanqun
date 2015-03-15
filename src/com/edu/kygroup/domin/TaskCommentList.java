package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edu.kygroup.net.HttpAgent;
import com.edu.kygroup.utils.UrlUtils;

public class TaskCommentList {
	private List<TaskComment> lists = new ArrayList<TaskComment>();
	private int task_id;

	public List<TaskComment> getLists() {
		return lists;
	}

	public void setLists(List<TaskComment> lists) {
		this.lists = lists;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public int refush() {
		int res = -1;
		StringBuffer buf = new StringBuffer(UrlUtils.GET_TASK_COMMENT_LIST);
		buf.append("&comment.task_id=" + task_id);
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
				TaskComment task = new TaskComment();
				task.setTask_comment_content(json
						.getString("task_comment_content"));
				task.setTask_id(json.getInt("task_id"));
				task.setTask_comment_time(json.getString("task_comment_time"));
				task.setUser_id(json.getString("user_id"));
				task.setUser_avatar(json.getString("user_avatar"));
				task.setUser_name(json.getString("user_name"));
				task.setUser_major(json.getString("user_major"));
				lists.add(task);
			}
		} catch (JSONException e) {
		}
		return res;
	}
}
