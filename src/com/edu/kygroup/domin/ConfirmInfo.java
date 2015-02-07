package com.edu.kygroup.domin;

import java.io.Serializable;

public class ConfirmInfo extends BaseBean implements Serializable{
	private static final long serialVersionUID = 1L;
	public int confirm;

	public int getConfirm() {
		return confirm;
	}

	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}
	
}
