package com.edu.kygroup.domin;

import java.io.Serializable;

public class BaseBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long timestamp;
	private int result;
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
}
