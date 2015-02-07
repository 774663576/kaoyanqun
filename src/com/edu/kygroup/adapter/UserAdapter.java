package com.edu.kygroup.adapter;

import java.util.List;

import com.edu.kygroup.R;
import com.edu.kygroup.domin.User;
import com.edu.kygroup.ui.KygroupApplication;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.StringUtils;
import com.funshion.video.mobile.imageloader.core.DisplayImageOptions;
import com.funshion.video.mobile.imageloader.core.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
	private Activity mContext;
	private List<User> mUsers;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	public UserAdapter(Activity context, List<User> users) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mUsers = users;
		mInflater = LayoutInflater.from(mContext);
		initImageOptions();
	}

	private void initImageOptions() {
		if (null == mImageLoader) {
			mImageLoader = ImageLoader.getInstance();
		}

		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.pic_boy)
				.showImageForEmptyUri(R.drawable.pic_boy)
				.showImageOnFail(R.drawable.pic_boy).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public List<User> getList() {
		return mUsers;
	}

	@Override
	public int getCount() {
		if (null != mUsers) {
			return mUsers.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null != mUsers) {
			return mUsers.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.students_item, null);
			holder = new ViewHolder();
			holder.mPic = (ImageView) convertView.findViewById(R.id.pic);
			holder.mNickName = (TextView) convertView
					.findViewById(R.id.nickname);
			holder.mAddress = (TextView) convertView.findViewById(R.id.address);
			holder.mEYear = (TextView) convertView.findViewById(R.id.eyear);
			// holder.mESchool = (TextView)
			// convertView.findViewById(R.id.eschool);
			// holder.mRSchool = (TextView)
			// convertView.findViewById(R.id.fschool);
			holder.mRYear = (TextView) convertView.findViewById(R.id.fyear);
			holder.mAuthPic = (ImageView) convertView.findViewById(R.id.auth);
			holder.mAimPic = (ImageView) convertView.findViewById(R.id.aim);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (null != mUsers && null != mUsers.get(position)) {
			User user = mUsers.get(position);
			holder.mNickName.setText(user.getNickName());
			String mESchool = StringUtils.isEmpty(user.getESchool()) ? getLocalUni()
					: user.getESchool();
			holder.mRYear.setText(user.getRYear() + "  " + user.getRSchool());
			if (StringUtils.isEmpty(user.getEYear())
					|| user.getEYear().equals("0")) {
				holder.mEYear.setText(mESchool);
			} else {
				holder.mEYear.setText(user.getEYear() + "  " + mESchool);

			}
			// setYear(holder.mEYear, user);
			setUserPic(holder.mPic, user);
			setGenderPic(holder.mNickName, user);
			setLocation(holder.mAddress, user);
			setAuthPic(holder.mAuthPic, holder.mAimPic, user);
		}
		return convertView;
	}

	private void setYear(TextView view, User user) {
		if (StringUtils.isEmpty(user.getEYear()) || user.getEYear().equals("0")) {
			view.setVisibility(View.INVISIBLE);
		} else {
			view.setText(user.getEYear());
			view.setVisibility(View.VISIBLE);
		}
	}

	private void setUserPic(ImageView view, User user) {
		if (StringUtils.isEmpty(user.getPic())) {
			if (user.getGender().equals("M")) {
				view.setImageResource(R.drawable.pic_boy);
			} else {
				view.setImageResource(R.drawable.pic_girl);
			}
		} else {
			if (!user.getPic().contains(FileUtils.SAVE_FILE_PATH_DIRECTORY)) {
				mImageLoader.displayImage(user.getPic(), view, mOptions);
			} else {
				Bitmap bitmap = BitmapFactory.decodeFile(user.getPic());
				if (null != bitmap) {
					view.setImageBitmap(bitmap);
				}
			}
		}
	}

	private void setGenderPic(TextView view, User user) {
		if (user.getGender().equals("M")) {
			view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.ic_user_male, 0);
		} else {
			view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.ic_user_famale, 0);
		}
	}

	private void setLocation(TextView view, User user) {
		String location = user.getDistance();
		if (user.getEmail().equals(KygroupApplication.mUser.getEmail())) {
			view.setText("0.00km");
		} else if (!StringUtils.isEmpty(location)) {
			if (!StringUtils.isEmpty(user.getAddress())) {
				location = location + " |" + user.getAddress();
			}
			view.setText(location);
		}
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

	private void setAuthPic(ImageView authPic, ImageView aimPic, User user) {
		System.out.println("user::::::::::::" + user.getRole());
		switch (user.getRole()) {
		case 1:
			authPic.setImageResource(R.drawable.unauth);
			authPic.setVisibility(View.VISIBLE);
			aimPic.setImageResource(R.drawable.post_graduate);
			break;
		case 2:
			authPic.setImageResource(R.drawable.auth);
			authPic.setVisibility(View.VISIBLE);
			aimPic.setImageResource(R.drawable.post_graduate);
			break;
		default:
			aimPic.setImageResource(R.drawable.aim);
			authPic.setVisibility(View.GONE);
			break;
		}
	}

	class ViewHolder {
		ImageView mPic;
		TextView mNickName;
		TextView mEYear;// 入学年份
		// TextView mESchool;// 所在学校
		TextView mRYear;// 考研年份
		// TextView mRSchool;// 考研学校
		ImageView mAuthPic;
		ImageView mAimPic;
		TextView mAddress;// 地理位置
	}
}
