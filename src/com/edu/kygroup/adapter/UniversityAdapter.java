package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;

public class UniversityAdapter extends BaseAdapter {

	private Activity mContext;
	private ArrayList<HashMap<String, String>> mList;
	private LayoutInflater mInflater;
	private String mSearchText;

	public UniversityAdapter(Activity context,
			ArrayList<HashMap<String, String>> list) {
		mContext = context;
		mList = list;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setSearchText(String text) {
		mSearchText = text;
	}

	public void setList(ArrayList<HashMap<String, String>> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.university_item, null);
			holder = new ViewHolder();
			holder.mTextView = (TextView) convertView
					.findViewById(R.id.university_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (null != mList) {
			HashMap<String, String> map = mList.get(pos);
			String uni = map.get("uni");
			if (TextUtils.isEmpty(mSearchText)) {
				holder.mTextView.setText(uni);
			} else {
				setMatchName(uni, holder.mTextView);
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView mTextView;
	}

	private void setMatchName(String name, TextView view) {
		if (TextUtils.isEmpty(name))
			return;
		SpannableString uniName = new SpannableString(name);
		uniName.setSpan(new ForegroundColorSpan(0xff333333), 0, name.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		char[] searchName = name.toCharArray();
		for (int i = 0; i < searchName.length; i++) {
			String strName = searchName[i] + "";
			if (!strName.matches("[0-9a-zA-Z]*")) {
				setMatchColor(uniName, searchName[i], i);
			}
		}
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
		Matcher ma1 = pattern.matcher(mSearchText);
		while (ma1.find()) {
			String str = ma1.group().trim();
			String matchStr = "(?i)" + str;
			Matcher ma2 = Pattern.compile(matchStr).matcher(name);
			while (ma2.find()) {
				if (ma2.end() > ma2.start()) {
					uniName.setSpan(new ForegroundColorSpan(0xffef5a00),
							ma2.start(), ma2.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		view.setText(uniName);
	}

	private void setMatchColor(SpannableString mediaName, char ch, int i) {
		int index = mSearchText.indexOf(ch);
		if (index >= 0) {
			mediaName.setSpan(new ForegroundColorSpan(0xffef5a00), i, i + 1,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}
}
