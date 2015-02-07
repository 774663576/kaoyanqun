/**
 * 工程名: KyGroup
 * 文件名: MessageBean.java
 * 包名: com.edu.kygroup.domin
 * 日期: 2013-11-30上午9:54:06
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.domin;
/**
 * 类名: MessageBean <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2013-11-30 上午9:54:06 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class MessageBean {

	private String friends_email;
	private int msg_count = 0;
	private String msg_content;
	private String img = "";
	private String date = "11.30";
	private String read;
	private String friendName;
	private String gender;
	private int flag;
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public MessageBean(String friends_email, int msgCount,String msg_content,  String img,
			String date ,String read) {
		this.friends_email = friends_email;
		this.msg_content = msg_content;
		this.img = img;
		this.date = date;
		this.read = read;
	}
	
	public MessageBean(String friends_email,String msg_content) {
		this.friends_email = friends_email;
		this.msg_content = msg_content;
	}
	
	public MessageBean(){
		
	}

	public String getFriends_email() {
		return friends_email;
	}

	public void setFriends_email(String friends_email) {
		this.friends_email = friends_email;
	}

	public int getMsg_count() {
		return msg_count;
	}

	public void setMsg_count(int msg_count) {
		this.msg_count = msg_count;
	}

	public String getMsg_content() {
		return msg_content;
	}

	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	
	
	
	
}

