/**
 * 工程名: KyGroup
 * 文件名: ChatBeanParse.java
 * 包名: com.edu.kygroup.net
 * 日期: 2013-11-30下午1:51:34
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.edu.kygroup.domin.ChatBean;
import com.edu.kygroup.ui.KygroupApplication;

/**
 * 类名: ChatBeanParse <br/>
 * 功能: TODO 某好友的聊天记录解析. <br/>
 * 日期: 2013-11-30 下午1:51:34 <br/>
 * 
 * @author lx
 * @version
 */
public class ChatBeanParse {

	private static ChatBeanParse mParse;
	private static HttpAgent mHttpAgent;

	private ChatBeanParse() {
	}

	public static ChatBeanParse getInstance() {
		if (mParse == null) {
			mParse = new ChatBeanParse();
		}
		if (mHttpAgent == null) {
			mHttpAgent = new HttpAgent();
		}
		return mParse;
	}

	public Boolean getChatList(String url, ArrayList<ChatBean> chatList) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				JSONObject obj = new JSONObject(result);
				JSONArray friary = obj.optJSONArray("messages");
				for (int i = 0; i < friary.length(); i++) {
					ChatBean chatBean = new ChatBean();
					JSONObject friObj = friary.optJSONObject(i);
					chatBean.setDate(friObj.optString("sendtime"));
					chatBean.setText(friObj.optString("message"));
					if (KygroupApplication.mUser.getEmail().equals(
							friObj.optString("from"))) {
						chatBean.setLayoutID(2); // me
						chatBean.setName(friObj.optString("from"));
						chatBean.setFrom(friObj.optString("from"));
						chatBean.setTo(friObj.optString("to"));
					} else {
						chatBean.setLayoutID(1); // friends
						chatBean.setName(friObj.optString("to"));
						chatBean.setTo(friObj.optString("to"));
						chatBean.setFrom(friObj.optString("from"));
					}
					chatList.add(chatBean);
				}
				return true;
			} catch (Exception e) {
			}
		}
		return false;
	}

}
