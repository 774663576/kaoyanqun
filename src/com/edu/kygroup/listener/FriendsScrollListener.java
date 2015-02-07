package com.edu.kygroup.listener;

import com.edu.kygroup.R;
import com.edu.kygroup.ui.GraduateView;
import com.edu.kygroup.ui.FriendsView;
import com.edu.kygroup.utils.ToastUtils;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class FriendsScrollListener implements OnScrollListener {
	
	public FriendsView mItem;
	private int mCurPage;
	private int mLastVisiblePosition = 0;//记录下拉到最后一个item的位置
	private int mLastVisiblePositionY = 0;//记录最后一个item的Y坐标
	private int mTotalSize;
	private int mTotal;
	private int mLastItem = 0;
	private int mCurPos = 0;
	private int mFlag;
	private ListView mCurListView;
	private boolean mIsTip = false;
	
	public FriendsScrollListener(FriendsView item,ListView listView,int flag,int curPage){
		mItem = item;
		mCurPage = curPage;
		mFlag = flag;
		setTotalSize(flag);
		mCurListView = listView;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		setTotalSize(mFlag);
		if (mCurPage < mTotalSize && scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			int count = mCurListView.getCount();
			if (mLastItem >= count && !mItem.mIsGetData) {
				mCurPage++;
				setCurPage(mCurPage);
				mItem.mIsGetData = true;
				getMoreData(mCurPage);
			}
		}	
		if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL&& mItem.mIsGetData) {
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {// 滚动到底部
				View v = (View) view.getChildAt(view.getChildCount() - 1);// 取得最后一个Item的View
				int[] location = new int[2];// 保存最后一个Item的绝对坐标数组
				v.getLocationOnScreen(location);// 获取最后一个Item在整个屏幕内的绝对坐标
				int y = location[1];// 取得最后一个Item的Y坐标
				if (view.getLastVisiblePosition() != mLastVisiblePosition&& mLastVisiblePositionY != y) {// 第一次拖至底部
					mLastVisiblePosition = view.getLastVisiblePosition();
					mLastVisiblePositionY = y;
					//mItem.setLoadMore(View.VISIBLE);
					return;
				} else if (view.getLastVisiblePosition() == mLastVisiblePosition&& mLastVisiblePositionY == y) {// 第二次拖至底部
					//mItem.setLoadMore(View.VISIBLE);
				}
			}
			// 未滚动到底部，第二次拖至底部都初始化
			mLastVisiblePosition = 0;
			mLastVisiblePositionY = 0;
			}
		// 判断可见的数量是否等于总数量如果等于，则提示没有数据了
		if(mLastItem == mTotal && !mIsTip){
			ToastUtils.showShortToast(R.string.havenodata);
			mIsTip = true;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mLastItem = firstVisibleItem + visibleItemCount;
		mCurPos = firstVisibleItem;
	}

	public int getCurPosition(){
		return mCurPos;
	}
	
	private void setTotalSize(int flag){
		switch(flag){
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}
	}
	
	private void setCurPage(int page){
		//mItem.setFriendsPage(page);
	}
	
	private void getMoreData(int page){
		//mItem.getData(page);
	}
}
