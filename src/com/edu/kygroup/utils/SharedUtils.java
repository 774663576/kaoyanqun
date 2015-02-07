package com.edu.kygroup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.edu.kygroup.ui.KygroupApplication;

/**
 * * SharedPreferences 的公具类
 * 
 * @author teeker_bin
 * 
 */
public class SharedUtils {
	private static final String SP_NAME = ConstantUtils.SHARED_PREF_FILE_NAME;
	private static SharedPreferences sharedPreferences = KygroupApplication
			.getmApplication().getSharedPreferences(SP_NAME,
					Context.MODE_PRIVATE);
	private static Editor editor = sharedPreferences.edit();
	public static final String SP_USER_NAME = "username";

	public static String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();

	}

	public static long getLong(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);

	}

	public static void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public static void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void setUserName(String userName) {
		setString(SP_USER_NAME, userName);
	}

	public static String getUserName() {
		return getString(SP_USER_NAME, "");
	}
}
