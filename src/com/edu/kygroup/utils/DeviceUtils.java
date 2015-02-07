/**
 * 工程名: KyGroup
 * 文件名: DeviceUtils.java
 * 包名: com.edu.kygroup.utils
 * 日期: 2013-10-27上午9:59:34
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.utils;

import com.edu.kygroup.R;
import com.edu.kygroup.ui.KygroupApplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 类名: DeviceUtils <br/>
 * 功能: TODO 获取手机信息. <br/>
 * 日期: 2013-10-27 上午9:59:34 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class DeviceUtils {
	public static 	int 	displayWidth	= 0;
	public static 	int 	displayHeight	= 0;
	public static 	float	displaydensity	= 0;	
	public static	int		displayDpi		= 0;
	
	/**
	 * 返回屏幕分辨率的宽度  px
	 */
	public static int getDisplayWidth(Context act) {
		if (displayWidth == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) act).getWindowManager().getDefaultDisplay().getMetrics(dm);
			displayWidth = dm.widthPixels;
		}
		return displayWidth;
	}
	
	/**
	 * 返回屏幕分辨率的高度  px
	 */
	public static int getDisplayHeight(Context act) {
		if(displayHeight == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) act).getWindowManager().getDefaultDisplay().getMetrics(dm);
			displayHeight = dm.heightPixels;
    	}
		return displayHeight;
	}
	
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(float dpValue, Context act) {
    	if(displaydensity == 0) {
    		DisplayMetrics dm = new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displaydensity 	= dm.density;
    	}
    	return (int) (dpValue * displaydensity + 0.5f);
    }
    
	/** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(float pxValue, Context act) {  
    	if(displaydensity == 0) {
    		DisplayMetrics dm = new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displaydensity = dm.density;
    	}
        return (int) (pxValue / displaydensity + 0.5f);  
    } 
    
    public static String getIMEI(Context context) {
    	TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telManager.getDeviceId();  //手机上的IMEI号 
		if(StringUtils.isEmpty(deviceId)) {
			return "";
		} else {
			return deviceId;
		}
    }
    
    /**
	 * 方法功能说明 ：判断当前网络状态是否可用
	 */
	public static boolean isNetAvailable(Context context){   
		ConnectivityManager conManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conManager.getActiveNetworkInfo();
		if(info==null || !info.isConnected()){   
			return false;   
		}   
		//漫游状态
		if(info.isRoaming()){   
			return true;   
		}   
		return true;   
	}
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion() {
	    try {
	        PackageManager manager = KygroupApplication.getApplication().getPackageManager();
	        PackageInfo info = manager.getPackageInfo(KygroupApplication.getApplication().getPackageName(), 0);
	        String version = info.versionName;
	        return  version;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
