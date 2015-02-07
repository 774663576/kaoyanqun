package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.kygroup.db.Const;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7639762085690320319L;

	public final static int MAX_INSERT_COUNT_FOR_CIRCLE_MEMBER = 500;

	private String mEmail = "";
	private String password = "";;
	private String mNickName = "";;
	private String mGender = "";;

	private String mESchool = "";;// 所在学校
	private String mECollege = "";;// 所在学院
	private String mEMajor = "";;// 所在专业
	private String mEYear = "";// 入学年份
	private String mESchoolid = "";
	private String mEColleageid = "";
	private String mEMajorid = "";

	private String mRSchool = "";// 报考学校
	private String mRCollege = "";// 报考学院
	private String mRMajor = "";// 报考专业
	private String mRYear = "";// 报考年份
	private String mRSid = "";// 注册学校id
	private String mRCid = "";
	private String mRMid = "";

	private int mRole = 0; // 0：本科生 1：未认证研究生 2：认证研究生

	private String mState = "";
	private String mResult = "";
	private String mPic = "";
	private String mAnnounce = "";// 宣言

	private String mProvince = "";
	private String mProid = "";
	private String mCity = "";
	private String mCityid = "";

	private String distance = "";
	private String address = "";
	private String mScore;
	private int mConfirm = 0;
	private String chatid = "";

	public void setChatid(String chatid) {
		this.chatid = chatid;
	}

	public String getChatid() {
		return chatid;
	}

	@Override
	public String toString() {
		return "email:" + mEmail + "   password:" + password + "   mNikeName:"
				+ mNickName + "   mGender:" + mGender + "   mEShool:"
				+ mESchool + "  mESchoolid:" + mESchoolid + "   mECollege:"
				+ mEColleageid + "   mEMajor:" + mEMajorid + "   mEYear:"
				+ mEYear + "  mRSchool:" + mRSchool + "   mRCollege+:"
				+ mRCollege + "    mRMajor:" + mRMajor;
	}

	public int getConfirm() {
		return mConfirm;
	}

	public void setConfirm(int confirm) {
		mConfirm = confirm;
	}

	public String getScore() {
		return mScore;
	}

	public void setScore(String score) {
		mScore = score;
	}

	public String getProid() {
		return mProid;
	}

	public void setProid(String mProid) {
		this.mProid = mProid;
	}

	public String getCityid() {
		return mCityid;
	}

	public void setCityid(String mCityid) {
		this.mCityid = mCityid;
	}

	public String getESchoolid() {
		return mESchoolid;
	}

	public void setESchoolid(String mESchoolid) {
		this.mESchoolid = mESchoolid;
	}

	public String getEColleageid() {
		return mEColleageid;
	}

	public void setEColleageid(String mEColleageid) {
		this.mEColleageid = mEColleageid;
	}

	public String getEMajorid() {
		return mEMajorid;
	}

	public void setEMajorid(String mEMajorid) {
		this.mEMajorid = mEMajorid;
	}

	public String getRSid() {
		return mRSid;
	}

	public void setRSid(String mRSid) {
		this.mRSid = mRSid;
	}

	public String getRCid() {
		return mRCid;
	}

	public void setRCid(String mRCid) {
		this.mRCid = mRCid;
	}

	public String getRmid() {
		return mRMid;
	}

	public void setRMid(String mRmid) {
		this.mRMid = mRmid;
	}

	public String mHowGoing;// 第几次考

	public int mRelation;// 和当前人关系 0 陌生人 1 好友

	public int getRelation() {
		return mRelation;
	}

	public void setRelation(int relation) {
		mRelation = relation;
	}

	public String getHowGoing() {
		return mHowGoing;
	}

	public void setHowGoing(String howgoing) {
		mHowGoing = howgoing;
	}

	public String getProvince() {
		return mProvince;
	}

	public void setProvince(String mProvince) {
		this.mProvince = mProvince;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String mCity) {
		this.mCity = mCity;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getESchool() {
		return mESchool;
	}

	public void setESchool(String mESchool) {
		this.mESchool = mESchool;
	}

	public String getECollege() {
		return mECollege;
	}

	public void setECollege(String mECollege) {
		this.mECollege = mECollege;
	}

	public String getEMajor() {
		return mEMajor;
	}

	public void setEMajor(String mEMajor) {
		this.mEMajor = mEMajor;
	}

	public String getEYear() {
		return mEYear;
	}

	public void setEYear(String mEYear) {
		this.mEYear = mEYear;
	}

	public String getRSchool() {
		return mRSchool;
	}

	public void setRSchool(String mRSchool) {
		this.mRSchool = mRSchool;
	}

	public String getRCollege() {
		return mRCollege;
	}

	public void setRCollege(String mRCollege) {
		this.mRCollege = mRCollege;
	}

	public String getRMajor() {
		return mRMajor;
	}

	public void setRMajor(String mRMajor) {
		this.mRMajor = mRMajor;
	}

	public String getRYear() {
		return mRYear;
	}

	public void setRYear(String mRYear) {
		this.mRYear = mRYear;
	}

	public String getNickName() {
		return mNickName;
	}

	public void setNickName(String mName) {
		this.mNickName = mName;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String mGender) {
		this.mGender = mGender;
	}

	public String getState() {
		return mState;
	}

	public String getPic() {
		return mPic;
	}

	public void setPic(String mPic) {
		this.mPic = mPic;
	}

	public String getAnnounce() {
		return mAnnounce;
	}

	public void setAnnounce(String mAnnounce) {
		this.mAnnounce = mAnnounce;
	}

	public void setState(String mState) {
		this.mState = mState;
	}

	public String getResult() {
		return mResult;
	}

	public void setResult(String mResult) {
		this.mResult = mResult;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRole() {
		return mRole;
	}

	public void setRole(int mRole) {
		this.mRole = mRole;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toDbUnionInsertString() {
		return "'" + mNickName + "','" + mEmail + "','" + mPic + "'";
	}

	public static String getDbInsertKeyString() {
		return "(name,cellphone,avatar)";
	}

	public void write(SQLiteDatabase db) {
		String dbName = Const.CIRCLE_MEMBER_TABLE_NAME;

		ContentValues cv = new ContentValues();
		cv.put("name", mNickName);
		cv.put("cellphone", mEmail);
		cv.put("avatar", mPic);
		db.insert(dbName, null, cv);
	}

	public void write(SQLiteDatabase db, List<User> listUsers) {
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
				sqlBuffer.append("'" + user.getEmail() + "'");
				cnt++;
			}
			if (cnt > 0) {
				sqlBuffer.append(")");
				db.execSQL(sqlBuffer.toString());
			}

			// insert Members
			sqlBuffer = new StringBuffer();
			sqlBuffer.append("insert into " + Const.CIRCLE_MEMBER_TABLE_NAME
					+ getDbInsertKeyString() + " select ");
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
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public void getNameAndAvatar(SQLiteDatabase db) {
		String conditionsKey = "cellphone=?";
		String[] conditionsValue = { this.mEmail };

		Cursor cursor = db.query(Const.CIRCLE_MEMBER_TABLE_NAME, new String[] {
				"name", "avatar" }, conditionsKey, conditionsValue, null, null,
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			String name = cursor.getString(cursor.getColumnIndex("name"));
			String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
			this.mNickName = name;
			this.mPic = avatar;
		}
		cursor.close();
	}
}
