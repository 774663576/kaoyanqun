package com.edu.kygroup.utils;

import java.io.File;
import java.net.URLDecoder;

import com.edu.kygroup.domin.Upgrade;
import com.edu.kygroup.ui.KygroupApplication;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

public class UpgradeHelper {	
	private FinalHttp mHttp;
	private Upgrade mUpgrade;
	
	public UpgradeHelper(){
		mHttp = new FinalHttp();
	}
	
	public void installApk(String name){
		Intent intent = new Intent(Intent.ACTION_VIEW); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		String path = FileUtils.SAVE_FILE_PATH_DIRECTORY+"/"+name;
		intent.setDataAndType(Uri.parse("file://" +path),"application/vnd.android.package-archive"); 
		KygroupApplication.getApplication().startActivity(intent);
	}
	
	
	@SuppressWarnings("unchecked")
	public void getApk(String url,final String name,String size) {
		String filePath = FileUtils.SAVE_FILE_PATH_DIRECTORY+"/"+name;
		File file = new File(filePath);
		if(file.exists() && file.length()==Long.parseLong(size)){
			installApk(name);
			return;
		}
		HttpHandler handler = mHttp.download(url, filePath,true, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
			// TODO Auto-generated method stub
				super.onLoading(count, current);
			}
			@Override
			public void onSuccess(Object t) {
					// TODO Auto-generated method stub
				//File file = new File(FileUtils.SAVE_FILE_PATH_DIRECTORY+"/"+name);
				installApk(name);		
				super.onSuccess(t);
			}			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	public boolean isPromptDialog(){
		boolean ret = true;
		Context context = KygroupApplication.getApplication();
		SharedPreferences sp = context.getSharedPreferences("installtime",context.MODE_PRIVATE);
		long preTime = sp.getLong("installtime", -1);
		long curTime = System.currentTimeMillis();
		if(preTime != -1){
			long btw = (curTime - preTime)/1000/60/60/24;
			if(btw >= 0 && btw < 3){
				ret = false;
			}else{
				Editor editor = sp.edit();
				editor.putLong("installtime", curTime);
				editor.commit();
			}
		}else{
			Editor editor = sp.edit();
			editor.putLong("installtime", curTime);
			editor.commit();
		}
		return ret;
	}
}
