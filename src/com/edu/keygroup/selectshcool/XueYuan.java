package com.edu.keygroup.selectshcool;

import java.util.ArrayList;
import java.util.List;

public class XueYuan {
	private String mId;
	String xueYuanName = "";

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	List<ZhuanYe> zhuanYeList = new ArrayList<ZhuanYe>();

	public String getXueYuanName() {
		return xueYuanName;
	}

	public void setXueYuanName(String xueYuanName) {
		this.xueYuanName = xueYuanName;
	}

	public List<ZhuanYe> getZhuanYeList() {
		return zhuanYeList;
	}

	public void setZhuanYeList(List<ZhuanYe> zhuanYeList) {
		this.zhuanYeList = zhuanYeList;
	}

}
