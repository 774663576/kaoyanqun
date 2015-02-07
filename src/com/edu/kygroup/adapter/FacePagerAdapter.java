/**
 * 工程名: KyGroup
 * 文件名: FacePagerAdapter.java
 * 包名: com.edu.kygroup.adapter
 * 日期: 2013-12-15下午5:00:39
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.edu.kygroup.R;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridView;

/**
 * 类名: FacePagerAdapter <br/>
 * 功能: TODO 表情 适配器. <br/>
 * 日期: 2013-12-15 下午5:00:39 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class FacePagerAdapter extends PagerAdapter{
	
private static final String TAG = "FacePagerAdapter";
	
	private Context context;
	private String[] faceNames;
	private int[] faceImages;
	private ArrayList<GridView> faceContainer_gridView;
	private int totalPage;	// 总页数
	private int lastPageNum;	// 最后一页表情数目
	private int columnNum;	// 列数
	private int rowNum;	// 行数
	private EditText editText;	// 输入框引用
	
	public FacePagerAdapter(Context mContext,int faceImages[],String [] faceNames,
			int columnNum , int rowNum,EditText editText){
		this.context = mContext;
		this.faceNames = faceNames;
		this.columnNum = columnNum;
		this.editText = editText;
		this.rowNum = rowNum;
		this.faceImages = faceImages;
		if(faceNames!=null && columnNum!=0 && rowNum!=0){
			totalPage = faceNames.length/columnNum/rowNum;
			lastPageNum = columnNum * rowNum;
			if (faceNames.length%(columnNum*rowNum) > 0){	// 判断有无最后一页
				totalPage++;
				lastPageNum = faceNames.length%(columnNum*rowNum);
			}
		}else{
			//LogUtils.e(TAG,"");
		}
		faceContainer_gridView = new ArrayList<GridView>();
	}
	@Override
	public int getCount() {
		return totalPage;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(faceContainer_gridView.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		
		GridView gridview = new GridView(context);
		gridview.setTag(position);
		gridview.setNumColumns(columnNum);
		gridview.setColumnWidth(
				(int)context.getResources().
				getDimension(R.dimen.face_item_width));
		gridview.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
		gridview.setLayoutParams(param);
		gridview.setVerticalSpacing(
				(int)context.getResources().
				getDimension(R.dimen.face_item_vertial_space));
		gridview.setHorizontalSpacing(
				(int)context.getResources().
				getDimension(R.dimen.face_item_horizontal_space));
		ArrayList<HashMap<String,Object>> src = new ArrayList<HashMap<String,Object>>();
		int total = position*columnNum*rowNum;
		for(int i = total; i < total+columnNum*rowNum && i< faceNames.length; i++){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("image", faceImages[i]);
			map.put("name", faceNames[i]);
			src.add(map);
		}
		FaceGridViewAdapter gridviewAdapter = new FaceGridViewAdapter(context,src,editText);
		gridview.setAdapter(gridviewAdapter);
		gridview.requestFocus();
		faceContainer_gridView.add(gridview);
		((ViewPager)container).addView(gridview);
		return gridview;
	}
}

