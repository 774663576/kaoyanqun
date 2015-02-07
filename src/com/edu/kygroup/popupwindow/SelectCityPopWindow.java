package com.edu.kygroup.popupwindow;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.edu.kygroup.R;

public class SelectCityPopWindow implements OnClickListener,
		OnItemClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;

	private View view;
	private String fileName = "";
	private SelectCityOnclick mSelectOnclick;

	private ListView mListView;
	private LinearLayout layout_parent;

	private List<String> citysList = new ArrayList<String>();

	public void setmSelectOnclick(SelectCityOnclick mSelectOnclick) {
		this.mSelectOnclick = mSelectOnclick;
	}

	public SelectCityPopWindow(Context context, View v, List<String> citysList) {
		this.mContext = context;
		this.v = v;
		this.citysList = citysList;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.select_city_pop_layout, null);
		initView();
		initPopwindow();

	}

	private void initView() {
		layout_parent = (LinearLayout) view.findViewById(R.id.layout_parent);
		layout_parent.getBackground().setAlpha(150);

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

	public interface SelectCityOnclick {
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
			return citysList.size();
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
						R.layout.select_city_pop_item, null);
				holder.city = (TextView) convertView
						.findViewById(R.id.txt_city);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.city.setText(citysList.get(arg0));
			return convertView;
		}
	}

	class ViewHolder {
		TextView city;
	}
}
