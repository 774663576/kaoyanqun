package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.ArrayList;

public class Announce implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long timestamp;
	private ArrayList<Item> announces;
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public ArrayList<Item> getAnnounces() {
		return announces;
	}

	public void setAnnounces(ArrayList<Item> announces) {
		this.announces = announces;
	}

	public class Item implements Serializable{
		public int aid;
		public String msg;
		public String sendTime;
		
		public int getAid() {
			return aid;
		}
		public void setAid(int aid) {
			this.aid = aid;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getSendTime() {
			return sendTime;
		}
		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}
	}
}
