package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.ArrayList;

public class ResPoster implements Serializable {

	private static final long serialVersionUID = 1L;

	private int total;
	private int result;
	private int totalpage;
	private ArrayList<Responses> responses;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public ArrayList<Responses> getResponses() {
		return responses;
	}

	public void setResponses(ArrayList<Responses> responses) {
		this.responses = responses;
	}

	public class Responses implements Serializable {
		private static final long serialVersionUID = 1L;

		private String content;
		private Louzhu guest;
		private Louzhu host;
		private int tid;
		private String timestamp;
		private String url = "";

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Louzhu getGuest() {
			return guest;
		}

		public void setGuest(Louzhu guest) {
			this.guest = guest;
		}

		public Louzhu getHost() {
			return host;
		}

		public void setHost(Louzhu host) {
			this.host = host;
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
	}
}
