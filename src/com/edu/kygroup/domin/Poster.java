package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Poster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int total;
	private int totalpage;
	private int result;
	private int flag;// 1 只有学校 2 由学校，学院 3 都有
	private ArrayList<Topic> topics;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public ArrayList<Topic> getTopics() {
		return topics;
	}

	public void setTopics(ArrayList<Topic> topics) {
		this.topics = topics;
	}

	public class Topic implements Serializable {
		private static final long serialVersionUID = 1L;

		private int ceid;
		private String cename = "";
		private Louzhu louzhu;
		private int mid;
		private String mname = "";
		// private String responses;
		private int sid;
		private String sname = "";
		private int tid;// 帖子id
		private String timestamp = "";// 发帖时间
		private String title = "";
		private int total;
		private List<String> picsList;

		public List<String> getPicsList() {
			return picsList;
		}

		public void setPicsList(List<String> picsList) {
			this.picsList = picsList;
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

		public Louzhu getLouzhu() {
			return louzhu;
		}

		public void setLouzhu(Louzhu louzhu) {
			this.louzhu = louzhu;
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

		public int getTid() {
			return tid;
		}

		public void setTid(int tid) {
			this.tid = tid;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}
	}
}
