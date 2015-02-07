package com.edu.kygroup.domin;

import android.location.Location;
import android.util.Log;

import com.edu.kygroup.utils.StringUtils;

public class KyLocation {

	private String mProvince;
	private String mCity;
	private String mDistrict;
	private double mLatitude;
	private double mLongitude;

	@Override
	public String toString() {
		return "province:" + mProvince + "  city:" + mCity + "  mLatitude:"
				+ mLatitude + "  mlongitude:" + mLongitude;
	}

	public String getProvince() {
		return mProvince;
	}

	public void setProvince(String mProvince) {
		this.mProvince = mProvince;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String mCity) {
		this.mCity = mCity;
	}

	public String getDistrict() {
		return mDistrict;
	}

	public void setDistrict(String mDistrict) {
		this.mDistrict = mDistrict;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getLocation() {
		StringBuffer sb = new StringBuffer();
		if (!StringUtils.isEmpty(mProvince)) {
			sb.append(mProvince);
		}
		if (!StringUtils.isEmpty(mCity)) {
			if (StringUtils.isEmpty(mProvince) || !mCity.equals(mProvince)) {
				sb.append(mCity);
			}
		}
		if (!StringUtils.isEmpty(mDistrict)) {
			sb.append(mDistrict);
		}
		return sb.toString();
	}

	// 判断是否提交
	public boolean isSubmit(KyLocation loc) {
		boolean ret = false;
		if (null != loc) {
			String locString = loc.getLocation();
			String curString = getLocation();
			if (!StringUtils.isEmpty(locString) && !locString.equals(curString)) {
				ret = true;
			}
		}
		return ret;
	}
}
