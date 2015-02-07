/**
 * 工程名: KyGroup
 * 文件名: FaceGridViewAdapter.java
 * 包名: com.example.faceapp
 * 日期: 2013-12-17下午2:23:33
 * Copyright (c) 2013, 北京联龙博通 All Rights Reserved.
 *
*/

package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.edu.kygroup.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 类名: FaceGridViewAdapter <br/>
 * 功能: . <br/>
 * 日期: 2013-12-17 下午2:23:33 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class FaceGridViewAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String,Object>> faceNameList;
	private EditText editText;
	
	public FaceGridViewAdapter(Context context,
			ArrayList<HashMap<String,Object>> faceNameList,EditText editText){
		this.context = context;
		this.faceNameList = faceNameList;
		this.editText = editText;
	}

	@Override
	public int getCount() {
		return faceNameList!=null ? faceNameList.size():0;
	}

	@Override
	public Object getItem(int position) {
		return faceNameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null){
			convertView = LayoutInflater.from(context).
					inflate(R.layout.face_gridview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView)convertView.findViewById(R.id.faceImg);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.img.setImageResource(
				(Integer)faceNameList.get(position).get("image"));
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap bitMap = null;
				bitMap = BitmapFactory.decodeResource(context.getResources(),
						(Integer)faceNameList.get(position).get("image"));
				ImageSpan imageSpan = new ImageSpan(context,bitMap);
				String str = (String)faceNameList.get(position).get("name");
				SpannableString spannableString = new SpannableString(
						str.substring(1, str.length()-1));
				spannableString.setSpan(imageSpan, 0, 
						str.length()-2,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				editText.append(spannableString);
			}
		});
		return convertView;
	}
	
	public static class ViewHolder{
		ImageView img;
	}

}

