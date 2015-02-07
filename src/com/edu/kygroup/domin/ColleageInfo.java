package com.edu.kygroup.domin;

import java.io.Serializable;

public class ColleageInfo implements Serializable {
	private static final long serialVersionUID = -6930717592375656954L;
	private int ceid;
	private String cename;
	private int mid;
	private String mname;
	private int sid;
	private String sname;
	private String groupid = "";

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	@Override
	public String toString() {
		return "ceid:" + ceid + "  mid:" + mid + "  sid:" + sid;
	}

	public int getCeid() {
		return ceid;
	}

	public void setCeid(int ceid) {
		this.ceid = ceid;
	}

	public String getCename() {
		return cename;
	}

	public void setCename(String cename) {
		this.cename = cename;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}
}
