package com.edu.kygroup.domin;

import java.io.Serializable;

public class Louzhu implements Serializable {
	private static final long serialVersionUID = 1L;

	private String batchelorschool;
	private String email;
	private String gender;
	private String image;
	private String nickname;

	public String getBatchelorschool() {
		return batchelorschool;
	}

	public void setBatchelorschool(String batchelorschool) {
		this.batchelorschool = batchelorschool;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
