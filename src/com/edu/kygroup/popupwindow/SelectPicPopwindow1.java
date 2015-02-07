package com.edu.kygroup.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.kygroup.R;

/**
 * 选择图片 拍照 选择框
 * 
 * @author teeker_bin
 * 
 */
public class SelectPicPopwindow1 implements OnClickListener,
		OnItemClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;

	private Button btnCancle;
	private View view;
	private String fileName = "";
	private SelectMenuOnclick mSelectOnclick;

	private ListView mListView;
	private RelativeLayout layout_parent;
	private String[] str;

	public void setmSelectOnclick(SelectMenuOnclick mSelectOnclick) {
		this.mSelectOnclick = mSelectOnclick;
	}

	public SelectPicPopwindow1(Context context, View v, String[] str) {
		this.mContext = context;
		this.v = v;
		this.str = str;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.select_pic_popowinow_layout, null);
		initView();
		initPopwindow();

	}

	private void initView() {
		layout_parent = (RelativeLayout) view.findViewById(R.id.layout_parent);
		layout_parent.getBackground().setAlpha(150);
		btnCancle = (Button) view.findViewById(R.id.btn_cancel);
		btnCancle.setOnClickListener(this);
		layout_parent.setOnClickListener(this);
		mListView = (ListView) view.findViewById(R.id.listView1);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(new MyAdapter());
	}

	/**
	 * 初始化popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setAnimationStyle(R.style.AnimBottom);
	}

	/**
	 * popwindow的显示
	 */
	public void show() {
		popupWindow.showAtLocation(v, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏
	public void dismiss() {
		popupWindow.dismiss();
	}

	/**
	 * 返回拍照之后保存路径
	 */
	public String getTakePhotoPath() {
		return fileName;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.layout_parent:
			dismiss();
			break;
		default:
			break;
		}

	}

	public interface SelectMenuOnclick {
		void onClickPosition(int position);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		dismiss();
		if (mSelectOnclick == null)
			return;
		mSelectOnclick.onClickPosition(arg2);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.select_popwindow_layout_item, null);
				holder.btn_menu = (TextView) convertView
						.findViewById(R.id.btn_menu);
				holder.btn_menu.setText(str[arg0]);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView btn_menu;
	}
}
