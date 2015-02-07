/**
 * 工程名: KyGroup
 * 文件名: ChatListAdapter.java
 * 包名: com.edu.kygroup.adapter
 * 日期: 2013-11-30上午11:54:12
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.adapter;

import java.util.List;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.ChatBean;
import com.edu.kygroup.utils.FaceUtil;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.widget.CustomTextView;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 类名: ChatListAdapter <br/>
 * 功能: TODO 聊天适配器. <br/>
 * 日期: 2013-11-30 上午11:54:12 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class ChatListAdapter extends BaseAdapter{

	private List<ChatBean> list;
	private Context mContext;
	private LayoutInflater inflater;
	private String myImg;
	private String friendImg;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private String gender;
	
	public ChatListAdapter(Context context,List<ChatBean> list,String myImg,String friendImg,String gender){
		this.mContext = context;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.myImg = myImg;
		this.friendImg = friendImg;
		this.gender = gender;
		initImageOptions();
	}
	
	private void initImageOptions() {
		if(null == mImageLoader){
			mImageLoader = ImageLoader.getInstance();
		}
		mOptions = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.pic_boy)
		.showImageForEmptyUri(R.drawable.pic_boy)
		.showImageOnFail(R.drawable.pic_boy)
		.cacheInMemory()
		.cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	@Override
	public int getCount() {
		return list!=null ? list.size():0;
	}

	@Override
	public Object getItem(int position) {
		return list!=null? list.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.list_say_item, null, true);
			holder = new ViewHolder();
			holder.leftcv = (CustomTextView)convertView.findViewById(R.id.messagedetail_row_text_left);
			holder.rightcv = (CustomTextView)convertView.findViewById(R.id.messagedetail_row_text_right);
			holder.lefttv = (TextView)convertView.findViewById(R.id.messagedetail_row_date_left);
			holder.righttv = (TextView)convertView.findViewById(R.id.messagedetail_row_date_right);
			holder.leftlv = (RelativeLayout)convertView.findViewById(R.id.layout_bj_left);
			holder.rightlv = (RelativeLayout)convertView.findViewById(R.id.layout_bj_right);
			holder.leftimg = (ImageView)convertView.findViewById(R.id.messagetail_row_img_left);
			holder.rightimg = (ImageView)convertView.findViewById(R.id.messagetail_row_img_right);
			convertView.setTag(holder);
		}else{	
			holder = (ViewHolder)convertView.getTag();
		}
		ChatBean mb = list.get(position);
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断私信内是否有表情
		if (mb.getLayoutID() == 1){
			try{
				SpannableString spannableString = FaceUtil.getFaceString(mContext, mb.getText(), zhengze);
				holder.leftcv.setText(spannableString);
			}catch(Exception e){
				e.printStackTrace();
			}
			holder.lefttv.setText(mb.getDate());
			holder.rightlv.setVisibility(View.GONE);
			holder.leftlv.setVisibility(View.VISIBLE);
			if(StringUtils.isEmpty(friendImg)){
				if(StringUtils.isEmpty(gender) || gender.equals("M")){
					holder.leftimg.setImageResource(R.drawable.pic_boy);
				}else{
					holder.leftimg.setImageResource(R.drawable.pic_girl);
				}		
			}else{
				if(friendImg.equals("admin")){
					holder.leftimg.setImageResource(R.drawable.admin);
				}else{
					mImageLoader.displayImage(friendImg,holder.leftimg,mOptions);
				}
			}
		} else {
			try{
				SpannableString spannableString = FaceUtil.getFaceString(mContext, mb.getText(), zhengze);
				holder.rightcv.setText(spannableString);
			}catch(Exception e){
				e.printStackTrace();
			}
			holder.leftlv.setVisibility(View.GONE);
			holder.rightlv.setVisibility(View.VISIBLE);
			holder.righttv.setText(mb.getDate());
			if(StringUtils.isEmpty(myImg) || !myImg.contains(FileUtils.SAVE_FILE_PATH_DIRECTORY)){
				mImageLoader.displayImage(myImg,holder.rightimg,mOptions);
			}else{
				Bitmap bitmap = BitmapFactory.decodeFile(myImg);
				if(null != bitmap){
					holder.rightimg.setImageBitmap(bitmap);
				}
			}
		}	
		return convertView;
	}
	
	static class ViewHolder{
		RelativeLayout leftlv;
		RelativeLayout rightlv;
		ImageView leftimg;
		ImageView rightimg;
		CustomTextView leftcv;
		CustomTextView rightcv;
		TextView lefttv;
		TextView righttv;
	}
}

