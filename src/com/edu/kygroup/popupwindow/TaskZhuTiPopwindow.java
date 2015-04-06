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
import com.edu.kygroup.utils.ToastUtils;

public class TaskZhuTiPopwindow implements OnItemClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private View view;
	private ListView listview;
	private MyAdapter adapter;
	private OnlistOnclick callback;
	private List<String> lists = new ArrayList<String>();

	public TaskZhuTiPopwindow(Context context, View v, List<String> lists) {
		this.mContext = context;
		this.v = v;
		this.lists = lists;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.task_zhuti_popwindow, null);
		initView();
		initPopwindow();
	}

	private void initView() {
		listview = (ListView) view.findViewById(R.id.listView1);
		listview.setOnItemClickListener(this);
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
	}

	/**
	 * 初始化popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, false);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * popwindow的显示
	 */
	public void show() {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
				location[0] + v.getWidth(), location[1] + v.getHeight()); // 使其聚集
		// popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏
	public void dismiss() {
		popupWindow.dismiss();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.task_zhuti_popwindow_item, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(lists.get(position));
			holder.text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					callback.onclick(lists.get(position));
					dismiss();
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		TextView text;
		LinearLayout laybg;
	}

	public void setOnlistOnclick(OnlistOnclick callback) {
		this.callback = callback;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		// callback.onclick(lists.get(position));
		// dismiss();
	}

	public interface OnlistOnclick {
		void onclick(String str);
	}
}
