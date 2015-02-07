package com.edu.kygroup.domin;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

//学院和专业结构体
public class Colleage implements Serializable{
	private String mId;
	private String mName;
	
	public String getId() {
		return mId;
	}
	public void setId(String mId) {
		this.mId = mId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
}
