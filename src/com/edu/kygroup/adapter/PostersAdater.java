package com.edu.kygroup.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.Louzhu;
import com.edu.kygroup.domin.Poster.Topic;
import com.edu.kygroup.popupwindow.SharePopwindow;
import com.edu.kygroup.popupwindow.SharePopwindow.SelectMenuOnclick;
import com.edu.kygroup.ui.ImagePagerActivity;
import com.edu.kygroup.ui.ResponseActivity;
import com.edu.kygroup.utils.Constant;
import com.edu.kygroup.utils.StringUtils;
import com.edu.kygroup.utils.UniversalImageLoadTool;
import com.edu.kygroup.widget.DrawableCenterTextView;
import com.edu.kygroup.widget.RoundAngleImageView;
import com.edu.kygroup.widget.ScrollViewGridView;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

public class PostersAdater extends BaseAdapter {

	private Activity mContext;
	private ArrayList<Topic> mTopics;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private PopupWindow mPopupWindow;

	public PostersAdater(Activity context, ArrayList<Topic> topics) {
		mContext = context;
		mTopics = topics;
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
		if (null != mTopics) {
			return mTopics.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null != mTopics) {
			return mTopics.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bbs_item_layout_1, null);
			holder = new Holder();
			holder.layout_parent = (LinearLayout) convertView
					.findViewById(R.id.parent);
			holder.mPicImage = (RoundAngleImageView) convertView
					.findViewById(R.id.image);
			holder.mNickView = (TextView) convertView
					.findViewById(R.id.nickname);
			holder.mSchoolView = (TextView) convertView
					.findViewById(R.id.school);
			holder.mContentView = (TextView) convertView
					.findViewById(R.id.content);
			holder.mTimeView = (TextView) convertView
					.findViewById(R.id.time_view);
			holder.btn_comment = (DrawableCenterTextView) convertView
					.findViewById(R.id.btn_comment);
			holder.btn_share = (DrawableCenterTextView) convertView
					.findViewById(R.id.btn_share);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.gridView = (ScrollViewGridView) convertView
					.findViewById(R.id.imgGridview);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Topic topic = mTopics.get(position);
		Louzhu louzhu = topic.getLouzhu();
		if (null != louzhu) {
			mImageLoader.displayImage(louzhu.getImage(), holder.mPicImage,
					mOptions);
			holder.mNickView.setText(louzhu.getNickname());
			holder.mSchoolView.setText((StringUtils.isEmpty(louzhu
					.getBatchelorschool()) ? getLocalUni() : louzhu
					.getBatchelorschool()));
		}
		if (topic.getTotal() > 0) {
			holder.btn_comment.setText("回复 (" + topic.getTotal() + ")");
		}
		holder.mContentView.setText(topic.getTitle());
		holder.mTimeView.setText(topic.getTimestamp());
		holder.btn_comment.setOnClickListener(new OnClick(position));
		holder.btn_share.setOnClickListener(new OnClick(position));
		holder.layout_parent.setOnClickListener(new OnClick(position));
		holder.gridView.setCacheColorHint(0);
		System.out.println("siz::::::::::::::::" + topic.getPicsList());
		if (topic.getPicsList().size() > 1) {
			if (topic.getPicsList().size() > 2) {
				holder.gridView.setNumColumns(3);
			} else {
				holder.gridView.setNumColumns(2);
			}
			holder.gridView.setAdapter(new BBSImgAdapter(mContext, topic
					.getPicsList()));
			holder.img.setVisibility(View.GONE);
			holder.gridView.setVisibility(View.VISIBLE);
			holder.gridView.setOnItemClickListener(new GridViewOnItemClick(
					position));
		} else if (topic.getPicsList().size() == 1) {
			UniversalImageLoadTool.disPlay(topic.getPicsList().get(0),
					holder.img, R.drawable.empty_photo);
			holder.img.setVisibility(View.VISIBLE);
			holder.gridView.setVisibility(View.GONE);
			holder.img.setOnClickListener(new OnClick(position));
		} else {
			holder.img.setVisibility(View.GONE);
			holder.gridView.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Holder {
		RoundAngleImageView mPicImage;
		TextView mNickView;
		TextView mContentView;
		TextView mSchoolView;
		TextView mTimeView;
		DrawableCenterTextView btn_share;
		DrawableCenterTextView btn_comment;
		ImageView img;
		ScrollViewGridView gridView;
		LinearLayout layout_parent;
	}

	private String getLocalUni() {
		String emsg = "";
		try {
			String str[] = mContext.getResources().getStringArray(R.array.unis);
			int pos = (int) (Math.random() * 10) % 3;
			emsg = str[pos];
		} catch (Exception e) {
			emsg = mContext.getString(R.string.local_uni);
		}
		return emsg;
	}

	public interface OnShareResponseListener {
		public void OnResponse(Topic topic);

		public void OnShare(Topic topic);
	}

	class OnClick implements OnClickListener {
		private int position;

		public OnClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_share:
				SharePopwindow pop = new SharePopwindow(mContext, v);
				pop.setmSelectOnclick(new SelectMenuOnclick() {

					@Override
					public void onClickPosition(int index) {
						Platform plat = null;
						switch (index) {
						case 0:
							plat = ShareSDK.getPlatform(QQ.NAME);
							share(plat,
									mTopics.get(position).getTitle(),
									mTopics.get(position).getPicsList().size() > 0 ? mTopics
											.get(position).getPicsList().get(0)
											: "");
							break;
						case 1:
							plat = ShareSDK.getPlatform(QZone.NAME);
							share(plat,
									mTopics.get(position).getTitle(),
									mTopics.get(position).getPicsList().size() > 0 ? mTopics
											.get(position).getPicsList().get(0)
											: "");
							break;
						case 2:
							plat = ShareSDK.getPlatform(Wechat.NAME);
							shareWe1(
									plat,
									mTopics.get(position).getTitle(),
									mTopics.get(position).getPicsList().size() > 0 ? mTopics
											.get(position).getPicsList().get(0)
											: "");
							break;
						case 3:
							plat = ShareSDK.getPlatform(WechatMoments.NAME);
							shareWe(plat,
									mTopics.get(position).getTitle(),
									mTopics.get(position).getPicsList().size() > 0 ? mTopics
											.get(position).getPicsList().get(0)
											: "");
							break;
						default:
							break;
						}

					}
				});
				pop.show();
				break;
			case R.id.btn_comment:
				mContext.startActivity(new Intent(mContext,
						ResponseActivity.class).putExtra("mTopic",
						mTopics.get(position)));
				break;
			case R.id.parent:
				mContext.startActivity(new Intent(mContext,
						ResponseActivity.class).putExtra("mTopic",
						mTopics.get(position)));
				break;
			default:
				intentImagePager(position, 1);
				break;
			}
		}

	}

	class GridViewOnItemClick implements OnItemClickListener {
		int position;

		public GridViewOnItemClick(int position) {
			this.position = position;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int posit,
				long arg3) {
			intentImagePager(position, posit);
		}
	}

	private void intentImagePager(int position, int index) {
		List<String> imgUrl = new ArrayList<String>();
		for (String img : mTopics.get(position).getPicsList()) {
			imgUrl.add(img);
		}
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.EXTRA_IMAGE_URLS, (Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constant.EXTRA_IMAGE_INDEX, index);
		mContext.startActivity(intent);
	}

	private void shareWe(final Platform plat, final String content,
			final String img) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("招生院校");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(content);
				sp.shareType = Platform.SHARE_TEXT;
				sp.shareType = Platform.SHARE_IMAGE;
				sp.text = content;
				sp.imageUrl = img;
				sp.url = "http://www.yifulou.cn:8180/";
				plat.share(sp);
			}
		}.start();

	}

	private void shareWe1(final Platform plat, final String content,
			final String img) {
		new Thread() {
			public void run() {
				WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
				sp.setTitle("招生院校");
				sp.setText(content);
				sp.shareType = Platform.SHARE_TEXT;
				sp.text = content;
				plat.share(sp);
			}
		}.start();

	}

	private void share(final Platform plat, final String content,
			final String img) {
		new Thread() {
			public void run() {
				ShareParams sp = new ShareParams();
				sp.setTitle("招生院校");
				sp.setTitleUrl("http://www.yifulou.cn:8180/"); // 标题的超链接
				sp.setText(content);
				sp.setSite("超级考研群");
				if (!"".equals(img)) {
					sp.setImageUrl(img);
				}
				sp.setSiteUrl("http://www.yifulou.cn:8180/");
				// Platform qzone = ShareSDK.getPlatform(QZone.NAME);
				// qzone.setPlatformActionListener(paListener); // 设置分享事件回调
				// 执行图文分享
				plat.share(sp);
			}
		}.start();

	}
}
