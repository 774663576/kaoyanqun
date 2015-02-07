package com.edu.kygroup.net;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.edu.kygroup.domin.User;
import com.edu.kygroup.ui.BlacklistActivity;
import com.edu.kygroup.ui.GraduateView;
import com.edu.kygroup.ui.FriendsView;
import com.edu.kygroup.ui.PostGraduateActivity;
import com.edu.kygroup.utils.StringUtils;

public class UserMsgParse {
	private static UserMsgParse mParse;
	private static HttpAgent mHttpAgent;
	private static DecimalFormat df;

	public static UserMsgParse getInstance() {
		if (null == mParse) {
			mParse = new UserMsgParse();
		}
		if (null == mHttpAgent) {
			mHttpAgent = new HttpAgent();
		}
		df = new DecimalFormat("0.00");
		return mParse;
	}

	public ArrayList<User> getFriendsUser(String url, int flag) {
		ArrayList<User> users = new ArrayList<User>();
		String result = mHttpAgent.httpPost(url);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject obj = new JSONObject(result);
				if (flag == 0) {
					GraduateView.BROWSER_PAGE = obj.optInt("totalpage");
				} else if (flag == 1) {
					GraduateView.POST_PAGE = obj.optInt("totalpage");
					GraduateView.POST_TOTAL = obj.optInt("total");
				} else if (flag == 2) {
					GraduateView.FELLOW_PAGE = obj.optInt("totalpage");
				} else if (flag == 3) {
					FriendsView.FRIENDS_PAGE = obj.optInt("totalpage");
					FriendsView.FRIENDS_TOTAL = obj.optInt("total");
				} else if (flag == 4) {
					GraduateView.MATES_PAGE = obj.optInt("totalpage");
				} else if (flag == 5) {
					PostGraduateActivity.POST_PAGE = obj.optInt("totalpage");
				} else if (flag == 6) {
					BlacklistActivity.POST_PAGE = obj.optInt("totalpage");
					BlacklistActivity.POST_TOTAL = obj.optInt("total");
				} else if (flag == 7) {
					GraduateView.SAME_MAJOE_PAGE = obj.optInt("totalpage");
				} else if (flag == 8) {
					GraduateView.SAME_COLLEAGE_PAGE = obj.optInt("totalpage");
				} else if (flag == 9) {
					GraduateView.POST_GRADUATE_PAGE = obj.optInt("totalpage");
				} else if (flag == 10) {
					PostGraduateActivity.MAJOR_PAGE = obj.optInt("totalpage");
				} else if (flag == 11) {
					PostGraduateActivity.COLLEAGE_PAGE = obj
							.optInt("totalpage");
				} else if (flag == 12) {
					PostGraduateActivity.POST_GRADUATE_PAGE = obj
							.optInt("totalpage");
				}
				JSONArray friary = obj.optJSONArray("friends");
				for (int i = 0; friary != null && i < friary.length(); i++) {
					User user = new User();
					JSONObject friObj = friary.optJSONObject(i);
					user.setNickName(friObj.optString("nickname"));
					user.setRole(friObj.optInt("role", 0));
					user.setGender(friObj.optString("gender"));
					user.setEmail(friObj.optString("email"));
					user.setPic(friObj.optString("image"));
					user.setEYear(friObj.optString("enterTime"));
					user.setRYear(friObj.optString("session"));
					user.setAnnounce(friObj.optString("declaration"));
					user.setProvince(friObj.optString("pname"));
					user.setProid(friObj.optString("pid"));
					user.setCity(friObj.optString("cname"));
					user.setChatid(friObj.optString("chatid"));
					user.setCityid(friObj.optString("city"));
					user.setHowGoing(friObj.optString("howgoing"));
					user.setRelation(friObj.optInt("relation"));
					user.setState(friObj.optString("status"));
					user.setConfirm(friObj.optInt("confirm"));
					// if(flag == 0){ // 附近搜索
					// double distance =
					// Double.parseDouble(friObj.optString("distance"));
					double distance = friObj.optDouble("distance");
					if (distance < 0) {
						user.setDistance("未知");
					} else {
						distance = distance / 1000;
						user.setDistance(df.format(distance) + "km");
					}
					user.setAddress(friObj.optString("location"));
					JSONObject aimObj = friObj.optJSONObject("aim");
					if (null != aimObj) {
						user.setRSchool(aimObj.optString("sname"));
						user.setRSid(aimObj.optString("sid"));
						user.setRCollege(aimObj.optString("cename"));
						user.setRCid(aimObj.optString("ceid"));
						user.setRMajor(aimObj.optString("mname"));
						user.setRMid(aimObj.optString("mid"));
					}
					JSONObject majObj = friObj.optJSONObject("major");
					if (null != majObj) {
						user.setESchool(majObj.optString("sname"));
						user.setESchoolid(aimObj.optString("sid"));
						user.setECollege(majObj.optString("cename"));
						user.setEColleageid(majObj.optString("ceid"));
						user.setEMajor(majObj.optString("mname"));
						user.setEMajorid(majObj.optString("mid"));
						user.setScore(friObj.optString("scores"));
					}
					users.add(user);
				}
				return users;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public User getUser(String url) {
		String result = mHttpAgent.httpPost(url);
		if (!StringUtils.isEmpty(result)) {
			try {
				User user = new User();
				JSONObject jo = new JSONObject(result);
				JSONObject obj = jo.optJSONObject("detail");
				user.setNickName(obj.optString("nickname"));
				user.setRole(obj.optInt("role", 0));
				user.setGender(obj.optString("gender"));
				user.setEmail(obj.optString("email"));
				user.setPic(obj.optString("image"));
				user.setEYear(obj.optString("enterTime"));
				user.setRYear(obj.optString("session"));
				user.setAnnounce(obj.optString("declaration"));
				user.setProvince(obj.optString("pname"));
				user.setProid(obj.optString("pid"));
				user.setCity(obj.optString("cname"));
				user.setCityid(obj.optString("city"));
				user.setHowGoing(obj.optString("howgoing"));
				user.setRelation(obj.optInt("relation"));
				user.setState(obj.optString("status"));
				user.setConfirm(obj.optInt("confirm"));
				// if(flag == 0){ // 附近搜索
				// double distance =
				// Double.parseDouble(obj.optString("distance"));
				double distance = obj.optDouble("distance");
				if (distance < 0) {
					user.setDistance("未知");
				} else {
					distance = distance / 1000;
					user.setDistance(df.format(distance) + "km");
				}
				user.setAddress(obj.optString("location"));
				JSONObject aimObj = obj.optJSONObject("aim");
				if (null != aimObj) {
					user.setRSchool(aimObj.optString("sname"));
					user.setRSid(aimObj.optString("sid"));
					user.setRCollege(aimObj.optString("cename"));
					user.setRCid(aimObj.optString("ceid"));
					user.setRMajor(aimObj.optString("mname"));
					user.setRMid(aimObj.optString("mid"));
				}
				JSONObject majObj = obj.optJSONObject("major");
				if (null != majObj) {
					user.setESchool(majObj.optString("sname"));
					user.setESchoolid(aimObj.optString("sid"));
					user.setECollege(majObj.optString("cename"));
					user.setEColleageid(majObj.optString("ceid"));
					user.setEMajor(majObj.optString("mname"));
					user.setEMajorid(majObj.optString("mid"));
					user.setScore(obj.optString("scores"));
				}
				return user;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
