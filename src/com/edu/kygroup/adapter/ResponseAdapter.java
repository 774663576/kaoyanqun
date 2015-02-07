package com.edu.kygroup.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Louzhu;
import com.edu.kygroup.domin.ResPoster.Responses;
import com.edu.kygroup.ui.ImagePagerActivity;
import com.edu.kygroup.ui.ResponseActivity;
import com.edu.kygroup.utils.Constant;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.CircularImage;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

public class ResponseAdapter extends BaseAdapter implements OnClickListener {
	private ResponseActivity mContext;
	private LayoutInflater mInflater;
	private ArrayList<Responses> mResponse;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	public ResponseAdapter(ResponseActivity context, ArrayList<Responses> res) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		mResponse = res;
		mContext = context;
		initImageOptions();
	}

	private void initImageOptions() {
		if (null == mImageLoader) {
			mImageLoader = ImageLoader.getInstance();
		}
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.pic_boy)
				.showImageForEmptyUri(R.drawable.pic_boy)
				.showImageOnFail(R.drawable.pic_boy).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != mResponse) {
			return mResponse.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (null != mResponse) {
			return mResponse.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null == convertView) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.bbs_comment_item, null);
			holder.nickView = (TextView) convertView
					.findViewById(R.id.nickname);
			holder.picView = (RoundAngleImageView) convertView
					.findViewById(R.id.pic);
			holder.resToView = (TextView) convertView
					.findViewById(R.id.response_to);
			holder.resConView = (TextView) convertView
					.findViewById(R.id.response_content);
			holder.timeView = (TextView) convertView.findViewById(R.id.time);
			holder.resBtnView = (TextView) convertView
					.findViewById(R.id.response_btn);
			holder.resBtnView.setTag(position);
			holder.resBtnView.setOnClickListener(this);
			holder.school = (TextView) convertView.findViewById(R.id.school);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Louzhu guest = mResponse.get(position).getGuest();
		Louzhu host = mResponse.get(position).getHost();
		holder.nickView.setText(host.getNickname());
		holder.resToView.setText(Html.fromHtml("回复   <font color=\"#6abfd4\">"
				+ guest.getNickname() + ":</font>"
				+ mResponse.get(position).getContent()));
		holder.resConView.setText(mResponse.get(position).getContent());
		holder.timeView.setText(mResponse.get(position).getTimestamp());
		setUserPic(holder.picView, host.getImage());
		if (!(StringUtils.isEmpty(mResponse.get(position).getHost()
				.getBatchelorschool()))) {
			holder.school.setText(mResponse.get(position).getHost()
					.getBatchelorschool());
			holder.school.setVisibility(View.VISIBLE);
		} else {
			holder.school.setVisibility(View.GONE);
		}
		if (mResponse.get(position).getUrl().equals("")) {
			holder.img.setVisibility(View.GONE);
		} else {
			holder.img.setVisibility(View.VISIBLE);
			UniversalImageLoadTool.disPlay(mResponse.get(position).getUrl(),
					holder.img, R.drawable.empty_photo);
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intentImagePager(mResponse.get(position).getUrl());
				}
			});
		}
		return convertView;
	}

	private void intentImagePager(String path) {
		List<String> imgUrl = new ArrayList<String>();
		imgUrl.add(path);

		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.EXTRA_IMAGE_URLS, (Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constant.EXTRA_IMAGE_INDEX, 1);
		mContext.startActivity(intent);
	}

	private void setUserPic(ImageView view, String pic) {
		if (StringUtils.isEmpty(pic)
				|| !pic.contains(FileUtils.SAVE_FILE_PATH_DIRECTORY)) {
			mImageLoader.displayImage(pic, view, mOptions);
		} else {
			Bitmap bitmap = BitmapFactory.decodeFile(pic);
			if (null != bitmap) {
				view.setImageBitmap(bitmap);
			}
		}
	}

	class Holder {
		RoundAngleImageView picView;
		TextView nickView;
		TextView timeView;
		TextView resToView;
		TextView resConView;// 内容
		TextView resBtnView;
		TextView school;
		ImageView img;
	}

	@Override
	public void onClick(View v) {
		try {
			int pos = (Integer) v.getTag();
			mContext.resSelected(pos);
		} catch (Exception e) {
		}
	}
}
