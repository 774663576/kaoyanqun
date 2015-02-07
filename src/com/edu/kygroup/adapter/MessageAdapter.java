/**
 * 工程名: KyGroup
 * 文件名: MessageAdapter.java
 * 包名: com.edu.kygroup.adapter
 * 日期: 2013-11-30上午9:34:22
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.utils.FaceUtil;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

/**
 * 类名: MessageAdapter <br/>
 * 功能: TODO 私信列表 适配器. <br/>
 * 日期: 2013-11-30 上午9:34:22 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class MessageAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private List<MessageBean> list;
	private Context context;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private SimpleDateFormat sdf;
	private SimpleDateFormat sdf2;
	String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断私信内是否有表情
	
	public MessageAdapter(Context context,List<MessageBean> list) {     
		mInflater = LayoutInflater.from(context); 
		this.list = list;
		this.context=context;
		mImageLoader = ImageLoader.getInstance();
		initImageOptions();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf2 = new SimpleDateFormat("MM月dd日");
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

	public void assignment(List<MessageBean> list){
		this.list = list;
	}
	
	public void add(MessageBean bean) {
		list.add(bean);
	}
	
	public void remove(int position){
		list.remove(position);
	}
	
	public int getCount() {  
		return list.size();     
	}   
	
	public MessageBean getItem(int position) {    
		return list.get(position);     
	}    
	
	public long getItemId(int position) {
		return position;   
	} 
	
	public View getView(int position, View convertView, ViewGroup parent) {   
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.message_view_list_item, null, true);
			holder = new ViewHolder();
			holder.pic = (RoundAngleImageView) convertView.findViewById(R.id.qcb);
			holder.name = (TextView) convertView.findViewById(R.id.name);  
			holder.count = (TextView) convertView.findViewById(R.id.count);  
			holder.date = (TextView) convertView.findViewById(R.id.date);  
			holder.content = (TextView) convertView.findViewById(R.id.content);  
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String img = list.get(position).getImg();
		if(!StringUtils.isEmpty(img)){
			if(img.equals("admin")){
				holder.pic.setImageResource(R.drawable.admin);
			}else{
				mImageLoader.displayImage(list.get(position).getImg(), holder.pic,mOptions);
			}
		}else{
			holder.pic.setImageResource(R.drawable.pic_boy);
			String gender = list.get(position).getGender();
			if(!StringUtils.isEmpty(gender) && gender.equals("M")){
				holder.pic.setImageResource(R.drawable.pic_boy);
			}else{
				holder.pic.setImageResource(R.drawable.pic_girl);
			}
		}
		holder.name.setText(list.get(position).getFriendName());
		if (list.get(position).getMsg_count()>0){
			holder.count.setVisibility(View.VISIBLE);
			holder.count.setText("new");
		}else{
			holder.count.setVisibility(View.GONE);
		}		
		try {
			String date = list.get(position).getDate();
			holder.date.setText(sdf2.format(sdf.parse(date)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try{
			SpannableString spannableString = FaceUtil.getFaceString(context, list.get(position).getMsg_content(), zhengze);
			holder.content.setText(spannableString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}   

	public final class ViewHolder {   
		public RoundAngleImageView pic;
		public TextView name;        
		public TextView count;        
		public TextView date;        
		public TextView content;        
	}
}

