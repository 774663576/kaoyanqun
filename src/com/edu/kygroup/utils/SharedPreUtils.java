/**
 * 工程名: KyGroup
 * 文件名: SharedPreUtils.java
 * 包名: com.edu.kygroup.utils
 * 日期: 2013-10-27上午10:05:37
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.utils;

import java.io.File;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 类名: SharedPreUtils 
 */
public class SharedPreUtils {
	private Context context;

	private static final String TAG = "SharedPreUtils";

	public SharedPreUtils(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 查询数据值
	 */
	public String getString(String key, String defValue) {
		return getString(ConstantUtils.SHARED_PREF_FILE_NAME, key, defValue);
	}
	
	public int getInt(String key,int defValue){
		SharedPreferences prefs = context.getSharedPreferences(ConstantUtils.SHARED_PREF_FILE_NAME,context.MODE_PRIVATE);
		return prefs.getInt(key, defValue);
	}

	/**
	 * 查询数据值
	 */
	public String getString(String fileName, String key, String defValue) {
		SharedPreferences prefs = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
		return prefs.getString(key, defValue);
	}

	public void addOrModify(String key, String value) {
		addOrModify(ConstantUtils.SHARED_PREF_FILE_NAME, key, value);
	}
	
	public void addOrModify(String key, int value) {
		addOrModify(ConstantUtils.SHARED_PREF_FILE_NAME, key, value);
	}
	
	public void addOrModify(final String fileName, final String key, final int value) {
		SharedPreferences prefs = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(key, value);
		editor.commit();			
	}

	/**
	 * 增加或者修改 sharedpreferences
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public void addOrModify(final String fileName, final String key, final String value) {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				SharedPreferences prefs = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.putString(key, value);
				editor.commit();
			}
		}.start();
			
	}

	/**
	 * 删除指定应用中的指定条目数据
	 */
	public boolean deleteItem(String key) {
		return deleteItem(ConstantUtils.SHARED_PREF_FILE_NAME, key);
	}

	/**
	 * 删除指定应用中的指定条目数据
	 */
	public boolean deleteItem(String fileName, String key) {
		SharedPreferences prefs = context.getSharedPreferences(fileName,context.MODE_PRIVATE);
		if (prefs.contains(key)) {
			Editor editor = prefs.edit();
			editor.remove(key);
			return editor.commit();
		} else {
			LogUtils.d(TAG, "deleteItem " + key + " in SharedPreferences: "+ fileName + "is not exist");
			return false;
		}
	}

	/**
	 * 删除指定的配置文件
	 */
	public boolean deleteAll(String fileName) {
		File file = new File("/data/data/" + context.getPackageName()+ "/shared_prefs", fileName + ".xml");
		if (file.exists()) {
			return file.delete();
		} else {
			LogUtils.d(TAG, "SharedPreferences file: " + fileName + "is not exist");
		}
		return false;
	}
	
	/**
	 * 清空应用默认配置文件中的所有数据
	 */
	public void deleteAll(){
		new Thread(){
			public void run(){
				SharedPreferences prefs = context.getSharedPreferences(
						ConstantUtils.SHARED_PREF_FILE_NAME,context.MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.clear();
				editor.commit();
			}
		}.start();
	}
}

