package com.edu.kygroup.domin;

import java.io.Serializable;

public class MajorDirect implements Serializable {
	private DirectContent directContent;
	private String direct_name = "";
	private int direct_id;
	private int cid;// 学院id
	private int mid;// 专业id
	private int sid;// 学校id

	@Override
	public String toString() {
		return "direct_name:" + direct_name;
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

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getDirect_name() {
		return direct_name;
	}

	public void setDirect_name(String direct_name) {
		this.direct_name = direct_name;
	}

	public int getDirect_id() {
		return direct_id;
	}

	public void setDirect_id(int direct_id) {
		this.direct_id = direct_id;
	}

	public DirectContent getDirectContent() {
		return directContent;
	}

	public void setDirectContent(DirectContent directContent) {
		this.directContent = directContent;
	}

}
