package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.List;

public class Bbs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total;
	private int pagesize;
	private Topic topic;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	class Topic implements Serializable {
		private static final long serialVersionUID = 1L;
		private int tid;
		private String title;
		private PersonInfo louzhu;// 发帖人信息
		private String timestamp;
		private List<Response> responses;
		private int sid;
		private int ceid;
		private int mid;
		private String sname;// 学校名称
		private String cename;
		private String mname;

		public int getTid() {
			return tid;
		}

		public void setTid(int tid) {
			this.tid = tid;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public PersonInfo getLouzhu() {
			return louzhu;
		}

		public void setLouzhu(PersonInfo louzhu) {
			this.louzhu = louzhu;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public List<Response> getResponses() {
			return responses;
		}

		public void setResponses(List<Response> responses) {
			this.responses = responses;
		}

		public int getSid() {
			return sid;
		}

		public void setSid(int sid) {
			this.sid = sid;
		}

		public int getCeid() {
			return ceid;
		}

		public void setCeid(int ceid) {
			this.ceid = ceid;
		}

		public int getMid() {
			return mid;
		}

		public void setMid(int mid) {
			this.mid = mid;
		}

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}

		public String getCename() {
			return cename;
		}

		public void setCename(String cename) {
			this.cename = cename;
		}

		public String getMname() {
			return mname;
		}

		public void setMname(String mname) {
			this.mname = mname;
		}
	}

	class Response implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int tid;
		private String content;
		private String timestamp;
		private PersonInfo host;
		private PersonInfo guest;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public PersonInfo getHost() {
			return host;
		}

		public void setHost(PersonInfo host) {
			this.host = host;
		}

		public PersonInfo getGuest() {
			return guest;
		}

		public void setGuest(PersonInfo guest) {
			this.guest = guest;
		}
	}

	class PersonInfo implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String email;
		private String gender;
		private String image;
		private String nickname;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

	}

}
