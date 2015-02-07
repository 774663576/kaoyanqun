/**
 * 工程名: KyGroup
 * 文件名: JSONUtils.java
 * 包名: com.edu.kygroup.utils
 * 日期: 2013-10-27上午10:09:03
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 类名: JSONUtils
 * 功能: TODO JSON数据格式工具类， 用来生成JSON格式的请求字符串，或者解析服务器下发的JSON相应字符串成本地的数据结
 * 日期: 2013-10-27 上午10:09:03
 *
 * @author   lx
 * @version  	 
 */
public class JSONUtils {
	private static String jsonRequest;
	private static Object mObj;

	/**
	 * 把要上传的请求封装成Map（和List, 但最外层必须是Map）的形式 在此方法中解析成为JSON格式的字符串返回
	 */
	@SuppressWarnings("unchecked")
	public static String createJsonRequest(Map<String, Object> data)throws JSONException {
		if (data == null) {
			return null;
		}
		Set<String> keys = data.keySet();
		for (String key : keys) {
			if ((data.get(key)) instanceof List) {
				if (((List) data.get(key)).size() <= 0) {
					data.remove(key);
				}
			}
		}
		mapToJson(data);
		return jsonRequest;
	}

	@SuppressWarnings("unchecked")
	private static void mapToJson(Map<String, Object> data) throws JSONException {
		JSONObject jsonObj = new JSONObject();
		for (String key : data.keySet()) {
			Object value = data.get(key);
			if ((value) instanceof Map<?, ?>) {
				mapToJson((Map<String, Object>) value);
				jsonObj.put(key, mObj);
				mObj = jsonObj;
			} else if (value instanceof List<?>) {
				JSONArray jsonArr = new JSONArray();
				for (int i = 0; i < ((List<?>) value).size(); i++) {
					if (((List<?>) value).get(i) instanceof Map<?, ?>) {
						mapToJson((Map<String, Object>) ((List<?>) value).get(i));
						jsonArr.put(mObj);
						mObj = jsonArr;
					} else {
						jsonArr.put(((List<?>) value).get(i));
						mObj = jsonArr;
					}
				}
				jsonObj.put(key, mObj);
				mObj = jsonObj;
			} else {
				jsonObj.put(key, value);
				mObj = jsonObj;
			}
		}
		jsonRequest = jsonObj.toString();
	}

	/**
	 * 解析单条JSON字符串
	 */
	public static Object parseJsonResponse(String response) throws JSONException {
		JSONTokener jsonTokener = new JSONTokener(response);
		Object object =  jsonTokener.nextValue();
		return parser(object);
	}

	/**
	 * 从json字符串中，取出指定的key对应的value数据 
	 * added by lx. 本方法有风险，谨慎使用
	 */
	public static Object getValueInJSONString(String jsonString, String key) throws JSONException {
		JSONTokener jsonTokener = new JSONTokener(jsonString);
		if (jsonTokener.more()) {
			Object next = jsonTokener.nextValue();
			if (next instanceof JSONObject) {
				Iterator<?> iterator = ((JSONObject) next).keys();
				while (iterator.hasNext()) {
					String nextKey = (String) iterator.next();
					if (key.equals(nextKey)) {
						return ((JSONObject) next).get(key);
					}
				}
			}
		}
		return null;
	}

	private static Object parser(Object next) throws JSONException {
		if (next instanceof JSONObject) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Iterator<?> iterator = ((JSONObject) next).keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				Object value = ((JSONObject) next).get(key);
				if (value instanceof JSONObject) {
					map.put(key, parser(value));
				} else if (value instanceof JSONArray) {
					map.put(key, parser(value));
				} else {
					map.put(key, value);
				}
			}
			return map;
		} else if (next instanceof JSONArray) {
			ArrayList<Object> list = new ArrayList<Object>();
			JSONArray jsonArr = (JSONArray) next;
			for (int i = 0; i < jsonArr.length(); i++) {
				Object value = jsonArr.get(i);
				if (value instanceof JSONObject) {
					list.add(parser(value));
				} else if (value instanceof JSONArray) {
					list.add(parser(value));
				} else {
					list.add(value);
				}
			}
			return list;
		} else {
			return next;
		}
	}

	private static HashMap<String, String> resultGrade1 = new HashMap<String, String>();

	/**
	 * 解析只有结构的JSON类型字符
	 */
	public static HashMap<String, String> parseJsonResponseGrade1(String response) throws JSONException {
		JSONTokener jsonTokener = new JSONTokener(response);
		while (jsonTokener.more()) {
			Object next = jsonTokener.nextValue();
			parseGrade1(next);
		}
		return resultGrade1;
	}

	private static void parseGrade1(Object next) throws JSONException {
		Iterator<?> iterator = ((JSONObject) next).keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = (String) ((JSONObject) next).get(key);
			resultGrade1.put(key, value);
		}
	}
}

