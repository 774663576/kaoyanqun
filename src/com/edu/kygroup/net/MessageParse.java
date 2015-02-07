/**
 * 工程名: KyGroup
 * 文件名: MessageParse.java
 * 包名: com.edu.kygroup.net
 * 日期: 2013-11-30上午10:53:25
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.edu.kygroup.domin.Announce;
import com.edu.kygroup.domin.BaseBean;
import com.edu.kygroup.domin.ConfirmInfo;
import com.edu.kygroup.domin.DetailsInfo;
import com.edu.kygroup.domin.FocusInfo;
import com.edu.kygroup.domin.Louzhu;
import com.edu.kygroup.domin.MajorDetail;
import com.edu.kygroup.domin.MajorDirect;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.domin.Poster;
import com.edu.kygroup.domin.Poster.Topic;
import com.edu.kygroup.domin.ResPoster;
import com.edu.kygroup.domin.TopicRetInfo;
import com.edu.kygroup.domin.Upgrade;
import com.edu.kygroup.ui.MessageView;
import com.edu.kygroup.utils.StringUtils;

/**
 * 类名: MessageParse <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2013-11-30 上午10:53:25 <br/>
 * 
 * @author lx
 * @version
 */
public class MessageParse {
	private static MessageParse mParse;
	private static HttpAgent mHttpAgent;

	private MessageParse() {
	}

	public static MessageParse getInstance() {
		if (mParse == null) {
			mParse = new MessageParse();
		}
		if (mHttpAgent == null) {
			mHttpAgent = new HttpAgent();
		}
		return mParse;
	}

	public long getMessage(String url, ArrayList<MessageBean> msgs, int flag) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				JSONObject obj = new JSONObject(result);
				if (flag == 1) {
					MessageView.MESSAGE_TOTAL = obj.optInt("total");
				}
				JSONArray friary = obj.optJSONArray("records");
				for (int i = 0; i < friary.length(); i++) {
					MessageBean msg = new MessageBean();
					JSONObject friObj = friary.optJSONObject(i);
					msg.setFriends_email(friObj.optString("email"));
					msg.setMsg_content(friObj.optString("message"));
					msg.setImg(friObj.optString("image"));
					msg.setDate(friObj.optString("time"));
					msg.setFriendName(friObj.optString("nickname"));
					msg.setMsg_count(Integer.parseInt(friObj.optString("total")));
					msg.setFlag(friObj.optInt("flag"));
					msg.setGender(friObj.optString("gender"));
					msgs.add(msg);
				}

				return obj.getLong("timestamp");
			} catch (Exception e) {
			}
		}
		return System.currentTimeMillis();
	}

	public Object getDetailsMsg(String url) {
		String result = mHttpAgent.httpPost(url);
		MajorDetail detail = new MajorDetail();
		if (null != result) {
			try {
				JSONObject obj = new JSONObject(result);
				if (obj.getInt("result") != 200) {
					return null;
				}
				JSONObject jsonDetail = obj.getJSONObject("detail");
				int cid = jsonDetail.getInt("ceid");
				int sid = jsonDetail.getInt("sid");
				int mid = jsonDetail.getInt("mid");
				detail.setCid(cid);
				detail.setGroup_id(jsonDetail.getString("groupid"));
				detail.setMid(mid);
				detail.setSid(sid);
				detail.setPlan(jsonDetail.getString("plan"));
				List<MajorDirect> directLists = new ArrayList<MajorDirect>();
				JSONArray directArray = jsonDetail.optJSONArray("directions");
				for (int i = 0; i < directArray.length(); i++) {
					MajorDirect direct = new MajorDirect();
					JSONObject directObj = directArray.optJSONObject(i);
					direct.setDirect_id(directObj.optInt("did"));
					direct.setCid(cid);
					direct.setMid(mid);
					direct.setSid(sid);
					direct.setDirect_name(directObj.optString("dname"));
					directLists.add(direct);
				}
				detail.setDirectLists(directLists);
			} catch (Exception e) {
				System.out.println("e:::::::::::::" + e.toString());
			}
		}
		return detail;
	}

	public ConfirmInfo getConfirmMsg(String url) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				ConfirmInfo info = (ConfirmInfo) JSON.parseObject(result,
						ConfirmInfo.class);
				return info;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Upgrade getUpgrade(String url) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				Upgrade info = (Upgrade) JSON
						.parseObject(result, Upgrade.class);
				return info;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public TopicRetInfo getTopicRet(String url) {
		String result = mHttpAgent.httpPost(url);
		System.out.println("result:::::::::::::" + result);
		if (null != result) {
			try {
				TopicRetInfo info = (TopicRetInfo) JSON.parseObject(result,
						TopicRetInfo.class);
				return info;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Poster getPosters(String url) {
		String result = mHttpAgent.httpPost(url);
		System.out.println("Poster result::::::::" + result);
		if (null != result) {
			try {
				JSONObject obj = new JSONObject(result);
				Poster poster = new Poster();
				poster.setTotal(obj.getInt("total"));
				poster.setTotalpage(obj.optInt("totalpage"));
				poster.setFlag(obj.optInt("flag"));
				ArrayList<Topic> topics = new ArrayList<Poster.Topic>();
				JSONArray ary = obj.optJSONArray("topics");
				for (int i = 0; null != ary && i < ary.length(); i++) {
					Topic topic = poster.new Topic();
					JSONObject aryObj = ary.optJSONObject(i);
					topic.setCeid(aryObj.optInt("ceid"));
					topic.setCename(aryObj.optString("cename"));
					topic.setMid(aryObj.optInt("mid"));
					topic.setMname(aryObj.optString("mname"));
					topic.setSid(aryObj.optInt("sid"));
					topic.setSname(aryObj.optString("sname"));
					topic.setTid(aryObj.optInt("tid"));
					topic.setTimestamp(aryObj.optString("timestamp"));
					topic.setTitle(aryObj.optString("title"));
					topic.setTotal(aryObj.getInt("total"));
					Louzhu louzhu = new Louzhu();
					JSONObject louzhuObj = aryObj.optJSONObject("louzhu");
					louzhu.setBatchelorschool(louzhuObj
							.optString("batchelorschool"));
					louzhu.setEmail(louzhuObj.optString("email"));
					louzhu.setGender(louzhuObj.optString("gender"));
					louzhu.setImage(louzhuObj.optString("image"));
					louzhu.setNickname(louzhuObj.optString("nickname"));
					List<String> picList = new ArrayList<String>();

					String urls = aryObj.getString("urls");
					if (!"null".equals(urls)) {
						JSONArray picArray = aryObj.getJSONArray("urls");
						for (int j = 0; j < picArray.length(); j++) {
							picList.add(picArray.getString(j));
						}
					}
					topic.setPicsList(picList);
					topic.setLouzhu(louzhu);
					topics.add(topic);
				}
				poster.setTopics(topics);
				System.out.println("Poster result::::::::=="
						+ poster.getTopics().size());

				return poster;
			} catch (JSONException e) {
				e.printStackTrace();
				System.out
						.println("Poster result::::::::==----" + e.toString());
			}
		}

		return null;
	}

	public ResPoster getResPoster(String url) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				ResPoster info = (ResPoster) JSON.parseObject(result,
						ResPoster.class);
				return info;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public TopicRetInfo getResRet(String url) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				TopicRetInfo info = (TopicRetInfo) JSON.parseObject(result,
						TopicRetInfo.class);
				return info;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Announce getAnnounce(String url) {
		String result = mHttpAgent.httpPost(url);
		if (null != result) {
			try {
				return (Announce) JSON.parseObject(result, Announce.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<FocusInfo> getConcern(String url) {
		ArrayList<FocusInfo> concerns = new ArrayList<FocusInfo>();
		String result = mHttpAgent.httpPost(url);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject obj = new JSONObject(result);
				JSONArray array = obj.optJSONArray("concerns");
				for (int i = 0; null != array && i < array.length(); i++) {
					JSONObject single = array.optJSONObject(i);
					FocusInfo info = new FocusInfo();
					info.setmCid(single.optInt("ceid") + "");
					info.setmFocusColleage(single.optString("cename"));
					info.setmFocusMajor(single.optString("mname"));
					info.setmFocusSchool(single.optString("sname"));
					info.setmMid(single.optInt("mid") + "");
					info.setmSid(single.optInt("sid") + "");
					info.setmFocusYear(single.optString("year"));
					info.setmTime(single.optString("timestamp"));
					info.setGroup_id(single.optString("groupid"));
					// info.setCeid(single.optInt("ceid"));
					// info.setCename(single.optString("cename"));
					// concern.setMid(single.optInt("mid"));
					// concern.setMname(single.optString("mname"));
					// concern.setSid(single.optInt("sid"));
					// concern.setSname(single.optString("sname"));
					// concern.setYear(single.optString("year"));
					concerns.add(info);
				}
				return concerns;
			} catch (Exception e) {
			}
		}
		return null;
	}

	public boolean addConcern(String url) {
		String result = mHttpAgent.httpPost(url);
		if (!StringUtils.isEmpty(result)) {
			try {
				BaseBean info = (BaseBean) JSON.parseObject(result,
						BaseBean.class);
				if (info.getResult() == 200) {
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delConcern(String url) {
		String result = mHttpAgent.httpPost(url);
		if (!StringUtils.isEmpty(result)) {
			try {
				BaseBean info = (BaseBean) JSON.parseObject(result,
						BaseBean.class);
				if (info.getResult() == 200) {
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
