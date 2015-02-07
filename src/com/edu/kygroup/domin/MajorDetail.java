package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MajorDetail implements Serializable {
	private int cid;// 学院id
	private int mid;// 专业id
	private String group_id = "";// 环信聊天id
	private int sid;// 学校id
	private String plan = "";// 招生计划
	private List<MajorDirect> directLists = new ArrayList<MajorDirect>();

	@Override
	public String toString() {
		return "cid:" + cid + "  mid:" + mid;
	}

	public List<MajorDirect> getDirectLists() {
		return directLists;
	}

	public void setDirectLists(List<MajorDirect> directLists) {
		this.directLists = directLists;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

}
