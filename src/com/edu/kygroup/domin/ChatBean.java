/**
 * 工程名: KyGroup
 * 文件名: ChatBean.java
 * 包名: com.edu.kygroup.domin
 * 日期: 2013-11-30上午11:53:44
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.domin;
/**
 * 类名: ChatBean <br/>
 * 功能: TODO 聊天bean. <br/>
 * 日期: 2013-11-30 上午11:53:44 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class ChatBean {

	private String name;	// 信息显示的用户名
	private String date;	// 时间
	private String text;	// 内容
	private int layoutID;	// 需要的布局		1: 左面	2：右面
	private String from;	// 发送者邮箱
	private String to;	// 接受者邮箱
	
	public ChatBean(String name, String date, String text, int layoutID){
		super();
		this.name = name;
		this.date = date;
		this.text = text;
		this.layoutID = layoutID;
	}
	
	public ChatBean(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLayoutID() {
		return layoutID;
	}
	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
	
}

