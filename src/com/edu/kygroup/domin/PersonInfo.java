package com.edu.kygroup.domin;

import android.util.Log;

public class PersonInfo {
	private ColleageInfo aim;
	private String city;
	private String cname;//city name;
	private String email;
	private String enterTime;
	private String gender;
	private String image;
	private ColleageInfo major;
	private String nickname;
	private String password;
	private String pid;//province id;
	private String pname;//province name;
	private int session;//报考时间
	private String status;//考研状态
	private String declaration;
	private String howgoing;//第几次考
	private String relation;
	private int role;
	private String scores;
	private int confirm;
	
	public int getConfirm() {
		return confirm;
	}

	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}

	public String getScores() {
		return scores;
	}

	public void setScores(String scores) {
		this.scores = scores;
	}
	
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getDeclaration() {
		return declaration;
	}
	
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	
	public String getHowgoing() {
		return howgoing;
	}
	
	public void setHowgoing(String howgoing) {
		this.howgoing = howgoing;
	}
	
	public String getRelation() {
		return relation;
	}
	
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public ColleageInfo getAim() {
		return aim;
	}
	public void setAim(ColleageInfo aim) {
		this.aim = aim;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public ColleageInfo getMajor() {
		return major;
	}
	public void setMajor(ColleageInfo major) {
		this.major = major;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getSession() {
		return session;
	}
	public void setSession(int session) {
		this.session = session;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
