package com.edu.kygroup.domin;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.edu.kygroup.db.Const;

public class UserList {
	public final static int MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER = 500;
	private List<User> listUsers = new ArrayList<User>();

	public List<User> getListUsers() {
		return listUsers;
	}

	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}

	public void write(SQLiteDatabase db) {
		StringBuffer sqlBuffer = new StringBuffer();
		try {
			db.beginTransaction();
			// delete Members
			sqlBuffer.append("delete from " + Const.CIRCLE_MEMBER_TABLE_NAME
					+ " where cellphone in (");

			int cnt = 0;
			for (User user : listUsers) {
				if (cnt > 0) {
					sqlBuffer.append(",");
				}
				sqlBuffer.append(user.getEmail());
				cnt++;
			}
			if (cnt > 0) {
				sqlBuffer.append(")");
				// db.execSQL(sqlBuffer.toString());
			}

			// insert Members
			sqlBuffer = new StringBuffer();
			sqlBuffer.append("insert into " + Const.CIRCLE_MEMBER_TABLE_NAME
					+ User.getDbInsertKeyString() + " select ");
			cnt = 0;
			for (User user : listUsers) {
				if (cnt > 0) {
					sqlBuffer.append(" union all select ");
				}
				sqlBuffer.append(user.toDbUnionInsertString());

				cnt++;
				if (cnt >= MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER) {
					db.execSQL(sqlBuffer.toString());
					cnt = 0;
					sqlBuffer = new StringBuffer();
					sqlBuffer.append("insert into "
							+ Const.CIRCLE_MEMBER_TABLE_NAME
							+ user.getDbInsertKeyString() + " select ");
				}
			}
			if (cnt > 0) {
				db.execSQL(sqlBuffer.toString());
				System.out.println("sql::::::::::::::" + sqlBuffer.toString());

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("err:::::::::::::::" + e.toString());
		} finally {
			db.endTransaction();
		}
	}
}
