package com.edu.kygroup.net;

import org.json.JSONObject;

public class EditParse {
	private static EditParse mParse;
	private static HttpAgent mHttpAgent; 
	
	public static EditParse getInstance(){
		if(null == mParse){
			mParse = new EditParse();
		}
		if(null == mHttpAgent){
			mHttpAgent = new HttpAgent();
		}
		return mParse;
	}
	
	public boolean alterBaokaoAim(String url){
		String result = mHttpAgent.httpPost(url);
		try{
			JSONObject obj = new JSONObject(result);
			if(obj.optString("result").equals("200")){
				return true;
			}
		}catch(Exception e){
		}
		return false;
	}
	
	public String alterPersonalMsg(String url){
		String result = mHttpAgent.httpPost(url);
		try{
			JSONObject obj = new JSONObject(result);
			return obj.optString("result");
		}catch(Exception e){
			e.printStackTrace();
		}
		return "1";
	}
}
