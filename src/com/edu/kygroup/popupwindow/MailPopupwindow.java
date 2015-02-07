package com.edu.kygroup.popupwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.edu.kygroup.R;
import com.edu.kygroup.adapter.MailAdapter;
import com.edu.kygroup.ui.LoginActivity;
import com.edu.kygroup.ui.RegisterActivity;

public class MailPopupwindow {
	private PopupWindow mMailPopupWindow;
	private Activity mContext;
	private int mHeight;
	private ListView mMaillistView;

	public MailPopupwindow(Activity context) {
		mContext = context;
		initPopupWindow();
	}

	private void initPopupWindow() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.mail_listview, null);
		mMailPopupWindow = new PopupWindow(view);
		mMaillistView = (ListView) view.findViewById(R.id.mail_listview);
		MailAdapter adapter = new MailAdapter(mContext);
		mMaillistView.setAdapter(adapter);
		mMaillistView.setOnItemClickListener(new MailItemClickListener());
	}

	public void setPopupWindowSize(int width, int height) {
		setItemHeight(mHeight);
		mMailPopupWindow.setWidth(width);
		mMailPopupWindow.setHeight(height);
	}

	public void setItemHeight(int height) {
		mHeight = height;
	}

	public PopupWindow getPopWindow() {
		mMailPopupWindow.setFocusable(true);
		mMailPopupWindow.setOutsideTouchable(true);
		mMailPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		return mMailPopupWindow;
	}

	class MailItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String suffix = (String) parent.getAdapter().getItem(position);
			if (mContext instanceof LoginActivity) {
				((LoginActivity) mContext).setLoginName(suffix);
			} else if (mContext instanceof RegisterActivity) {
				((RegisterActivity) mContext).setLoginName(suffix);
			}
			if (mMailPopupWindow.isShowing()) {
				mMailPopupWindow.dismiss();
			}
		}
	}
}
