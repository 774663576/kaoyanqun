package com.edu.kygroup.domin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailsInfo extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Details detail;

	public Details getDetail() {
		return detail;
	}

	public void setDetail(Details detail) {
		this.detail = detail;
	}

	public class Details implements Serializable {

		private static final long serialVersionUID = 1L;
		private String ceid;
		private String cename;// colleage name
		private ArrayList<String> directions = new ArrayList<String>();// 研究方向****************
		private ArrayList<String> exams = new ArrayList<String>();// 考试科目**************
		private String mid;
		private String mname;// 专业名字
		private ArrayList<String> plan = new ArrayList<String>();// 招生计划*****************
		private String sid;
		private String sname;// 专业学校名字
		private String year;
		private ArrayList<String> retest = new ArrayList<String>();// 复试内容******************
		private ArrayList<String> note = new ArrayList<String>();// 备注************************
		private ArrayList<String> lines = new ArrayList<String>();// 复试线
		private ArrayList<String> teachers = new ArrayList<String>();// 指导老师
		private ArrayList<String> samelevel = new ArrayList<String>();// 同等学历加试*****************
		private ArrayList<String> references = new ArrayList<String>();// 参考书目

		@Override
		public String toString() {
			return "sname:" + sname + " cename:" + cename + " mname:" + mname;
		}

		public ArrayList<String> getSamelevel() {
			return samelevel;
		}

		public void setSamelevel(ArrayList<String> samelevel) {
			this.samelevel = samelevel;
		}

		public ArrayList<String> getReferences() {
			return references;
		}

		public void setReferences(ArrayList<String> references) {
			this.references = references;
		}

		public ArrayList<String> getTeachers() {
			return teachers;
		}

		public void setTeachers(ArrayList<String> teachers) {
			this.teachers = teachers;
		}

		public ArrayList<String> getLines() {
			return lines;
		}

		public void setLines(ArrayList<String> lines) {
			this.lines = lines;
		}

		public ArrayList<String> getRetest() {
			return retest;
		}

		public void setRetest(ArrayList<String> retest) {
			this.retest = retest;
		}

		public ArrayList<String> getNote() {
			return note;
		}

		public void setNote(ArrayList<String> note) {
			this.note = note;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getCeid() {
			return ceid;
		}

		public ArrayList<String> getPlan() {
			return plan;
		}

		public void setPlan(ArrayList<String> plan) {
			this.plan = plan;
		}

		public void setCeid(String ceid) {
			this.ceid = ceid;
		}

		public String getCename() {
			return cename;
		}

		public void setCename(String cename) {
			this.cename = cename;
		}

		public ArrayList<String> getDirections() {
			return directions;
		}

		public void setDirections(ArrayList<String> directions) {
			this.directions = directions;
		}

		public ArrayList<String> getExams() {
			return exams;
		}

		public void setExams(ArrayList<String> exams) {
			this.exams = exams;
		}

		public String getMid() {
			return mid;
		}

		public void setMid(String mid) {
			this.mid = mid;
		}

		public String getMname() {
			return mname;
		}

		public void setMname(String mname) {
			this.mname = mname;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getSname() {
			return sname;
		}

		public void setSname(String sname) {
			this.sname = sname;
		}
	}

}
