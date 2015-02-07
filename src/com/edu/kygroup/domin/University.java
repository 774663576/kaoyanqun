package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import com.edu.keygroup.selectshcool.XueYuan;

public class University {
	private String uName = "";
	private String uCode = "0";
	private List<XueYuan> cList = new ArrayList<XueYuan>();

	public List<XueYuan> getcList() {
		return cList;
	}

	public void setcList(List<XueYuan> cList) {
		this.cList = cList;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuCode() {
		return uCode;
	}

	public void setuCode(String uCode) {
		this.uCode = uCode;
	}

}
