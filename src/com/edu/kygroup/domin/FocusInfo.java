package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.kygroup.db.Const;

public class FocusInfo implements Serializable {

	private static final long serialVersionUID = -5340203993756422996L;
	private static final int MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER = 500;
	private String mTime = "";
	private String mFocusSchool = "";
	private String mFocusColleage = "";
	private String mFocusMajor = "";
	private String mFocusYear = "";
	private String mSid = "";
	private String mCid = "";
	private String mMid = "";
	private String group_id = "";

	@Override
	public String toString() {
		return "mFocusSchool: " + mFocusSchool + "  mFocusColleage: "
				+ mFocusColleage + "  mFocusMajor: " + mFocusMajor + " "
				+ mFocusYear + "  mSid:" + mSid + "  mCid:" + mCid + " mid: "
				+ mMid;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getmSid() {
		return mSid;
	}

	public void setmSid(String mSid) {
		this.mSid = mSid;
	}

	public String getmCid() {
		return mCid;
	}

	public void setmCid(String mCid) {
		this.mCid = mCid;
	}

	public String getmMid() {
		return mMid;
	}

	public void setmMid(String mMid) {
		this.mMid = mMid;
	}

	public String getmTime() {
		return mTime;
	}

	public void setmTime(String mTime) {
		this.mTime = mTime;
	}

	public String getmFocusSchool() {
		return mFocusSchool;
	}

	public void setmFocusSchool(String mFocusSchool) {
		this.mFocusSchool = mFocusSchool;
	}

	public String getmFocusColleage() {
		return mFocusColleage;
	}

	public void setmFocusColleage(String mFocusColleage) {
		this.mFocusColleage = mFocusColleage;
	}

	public String getmFocusMajor() {
		return mFocusMajor;
	}

	public void setmFocusMajor(String mFocusMajor) {
		this.mFocusMajor = mFocusMajor;
	}

	public String getmFocusYear() {
		return mFocusYear;
	}

	public void setmFocusYear(String mFocusYear) {
		this.mFocusYear = mFocusYear;
	}

	public String toDbUnionInsertString() {
		return "'" + mFocusSchool + "','" + mFocusColleage + "','"
				+ mFocusMajor + "','" + group_id + "'";
	}

	public static String getDbInsertKeyString() {
		return "(focus_school,focus_colleage,focus_major,group_id)";
	}

	public void write(SQLiteDatabase db, List<FocusInfo> lists) {
		StringBuffer sqlBuffer = new StringBuffer();
		try {
			db.beginTransaction();
			sqlBuffer.append("delete from " + Const.FOCUS_SCHOOL_TABLE_NAME
					+ " where group_id in (");

			int cnt = 0;
			for (FocusInfo info : lists) {
				if (cnt > 0) {
					sqlBuffer.append(",");
				}
				sqlBuffer.append("'" + info.getGroup_id() + "'");
				cnt++;
			}
			if (cnt > 0) {
				sqlBuffer.append(")");
				db.execSQL(sqlBuffer.toString());
			}

			// insert Members
			sqlBuffer = new StringBuffer();
			sqlBuffer.append("insert into " + Const.FOCUS_SCHOOL_TABLE_NAME
					+ getDbInsertKeyString() + " select ");
			cnt = 0;
			for (FocusInfo info : lists) {
				if (cnt > 0) {
					sqlBuffer.append(" union all select ");
				}
				sqlBuffer.append(info.toDbUnionInsertString());

				cnt++;
				if (cnt >= MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER) {
					db.execSQL(sqlBuffer.toString());
					cnt = 0;
					sqlBuffer = new StringBuffer();
					sqlBuffer.append("insert into "
							+ Const.FOCUS_SCHOOL_TABLE_NAME
							+ info.getDbInsertKeyString() + " select ");
				}
			}
			if (cnt > 0) {
				db.execSQL(sqlBuffer.toString());
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("err::::::::::::::::" + e.toString());
		} finally {
			db.endTransaction();
		}
	}

	public void getSchoolAndMajorByGroupID(SQLiteDatabase db) {
		String conditionsKey = "group_id=?";
		String[] conditionsValue = { this.group_id };

		Cursor cursor = db.query(Const.FOCUS_SCHOOL_TABLE_NAME, new String[] {
				"focus_school", "focus_colleage", "focus_major" },
				conditionsKey, conditionsValue, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			String focus_school = cursor.getString(cursor
					.getColumnIndex("focus_school"));
			String focus_colleage = cursor.getString(cursor
					.getColumnIndex("focus_colleage"));
			String focus_major = cursor.getString(cursor
					.getColumnIndex("focus_major"));
			this.mFocusColleage = focus_colleage;
			this.mFocusMajor = focus_major;
			this.mFocusSchool = focus_school;
		}
		cursor.close();
	}
}
