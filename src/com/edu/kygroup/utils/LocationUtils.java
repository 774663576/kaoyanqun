package com.edu.kygroup.utils;

import com.edu.kygroup.domin.KyLocation;
import com.edu.kygroup.ui.KygroupApplication;

public class LocationUtils {

	public static double getLongtitude(){
		KyLocation location = KygroupApplication.getApplication().getLocation();
		if(null == location){
			return 0;
		}
		return location.getLongitude();
	}
	
	public static double getLatitude(){
		KyLocation location = KygroupApplication.getApplication().getLocation();
		if(null == location){
			return 0;
		}
		return location.getLatitude();
	}
}
