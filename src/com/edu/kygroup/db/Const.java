package com.edu.kygroup.db;

public class Const {

	public static final String CIRCLE_MEMBER_TABLE_NAME = "members";
	public static final String CIRCLE_MEMBER_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " name varchar, cellphone varchar, avatar varchar";
	public static final String FOCUS_SCHOOL_TABLE_NAME = "focus_school";
	public static final String FOCUS_SCHOOL_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " focus_school varchar, focus_colleage varchar, focus_major varchar, group_id varchar";
}
