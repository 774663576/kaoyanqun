package com.edu.kygroup.adapter;

import java.util.ArrayList;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.ui.HomeActivity;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
	private HomeActivity mContext;
	private ArrayList<User> mUsers;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	public GalleryAdapter(HomeActivity context,ArrayList<User> users) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mUsers = users;
		mImageLoader = ImageLoader.getInstance();
		initImageOptions();
	}
	
	private void initImageOptions() {
		if(null == mImageLoader){
			mImageLoader = ImageLoader.getInstance();
		}
		
		this.mOptions = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.pic_default)
		.showImageForEmptyUri(R.drawable.pic_default)
		.showImageOnFail(R.drawable.pic_default)
		.cacheInMemory()
		.cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null != mUsers){
			return mUsers.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(null != mUsers){
			return mUsers.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView image = new ImageView(mContext);  
       // mImageLoader.displayImage(mUsers.get(position).getPic(), image, mOptions);
        
    	if(!mUsers.get(position).getPic().contains("/mnt/sdcard/edu")){
			mImageLoader.displayImage(mUsers.get(position).getPic(),image, mOptions);
		}else{
			Bitmap bitmap = BitmapFactory.decodeFile(mUsers.get(position).getPic());
			if(null != bitmap){
				image.setImageBitmap(bitmap);
			}
		}
    	int size = mContext.getResources().getDimensionPixelSize(R.dimen.gallery_width);
        image.setLayoutParams(new Gallery.LayoutParams(size,size));  
        return image;  
	}

}
