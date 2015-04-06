package com.edu.kygroup.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.TipsComment;
import com.edu.kygroup.ui.ImagePagerActivity;
import com.edu.kygroup.utils.Constant;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

public class TipCommentListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<TipsComment> lists;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private Context mContext;

	public TipCommentListAdapter(Context context, List<TipsComment> lists) {
		mInflater = LayoutInflater.from(context);
		initImageOptions();
		mContext = context;
		this.lists = lists;
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
		if (null != lists) {
			return lists.size();
		}
		return 0;
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
			holder.school = (TextView) convertView.findViewById(R.id.school);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.resToView.setVisibility(View.GONE);
		holder.resConView.setVisibility(View.VISIBLE);
		holder.nickView.setText(lists.get(position).getUser_name());
		holder.resConView.setText(lists.get(position).getComment_content());
		holder.timeView.setText(lists.get(position).getComment_time());
		setUserPic(holder.picView, lists.get(position).getUser_avatar());
		holder.school.setText(lists.get(position).getUser_school());
		if (lists.get(position).getComment_img_url().equals("")) {
			holder.img.setVisibility(View.GONE);
		} else {
			holder.img.setVisibility(View.VISIBLE);
			if (lists.get(position).getComment_img_url().startsWith("http")) {
				UniversalImageLoadTool.disPlay(lists.get(position)
						.getComment_img_url(), holder.img,
						R.drawable.empty_photo);
			} else {
				UniversalImageLoadTool.disPlay("file://"
						+ lists.get(position).getComment_img_url(), holder.img,
						R.drawable.empty_photo);
			}
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intentImagePager(lists.get(position).getComment_img_url());
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
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
