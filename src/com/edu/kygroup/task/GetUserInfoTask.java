package com.edu.kygroup.task;

import com.edu.kygroup.domin.User;

public class GetUserInfoTask extends BaseAsyncTask<User, Void, Integer> {

	private User user;

	@Override
	protected Integer doInBackground(User... params) {
		user = params[0];
		return user.getInfo();
	}

}
