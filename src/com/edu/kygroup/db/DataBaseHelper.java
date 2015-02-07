package com.edu.kygroup.db;

import com.edu.kygroup.ui.KygroupApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * sqlist db helper
 */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_PREFIX = "kyq";
	private static final int DATABASE_VERSION_1 = 1;
	private static final int DATABASE_VERSION = DATABASE_VERSION_1;
	private static DataBaseHelper instance;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static DataBaseHelper getInstance() {

		return getInstance(KygroupApplication.getmApplication());
	}

	public static DataBaseHelper getInstance(Context context) {
		return getInstance(context, "");
	}

	public static DataBaseHelper getInstance(Context context, String postfix) {
		if (instance == null) {
			instance = new DataBaseHelper(context, DATABASE_PREFIX + postfix,
					null, DATABASE_VERSION);

		}
		return instance;
	}

	public static void setIinstanceNull() {
		instance = null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDB(db);
	}

	private void createDB(SQLiteDatabase db) {
		// members
		db.execSQL("create table IF NOT EXISTS "
				+ Const.CIRCLE_MEMBER_TABLE_NAME + "("
				+ Const.CIRCLE_MEMBER_TABLE_STRUCTURE + ")");

		db.execSQL("create table IF NOT EXISTS "
				+ Const.FOCUS_SCHOOL_TABLE_NAME + "("
				+ Const.FOCUS_SCHOOL_TABLE_STRUCTURE + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// deleteOldDB(db);
		// createDB(db);
	}

	private void deleteOldDB(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + Const.CIRCLE_MEMBER_TABLE_NAME);
	}

}
