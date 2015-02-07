package com.edu.kygroup.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.kygroup.R;
import com.edu.kygroup.utils.StringUtils;

public class DetailLinearLayout extends LinearLayout implements OnClickListener{
	private Context mContext;
	private ArrayList<String> mLists;
	private TextView mHeader;
	private KyTextaView mTitleView;
	private KyListView mListView;
	private TextView mHeaderLine;
	private TextView mVirtualView;
	private TextView mMoreView;
	private boolean mIsExpansion = false;
	private boolean mIsSub = false;
	private String mSubText;
	private String mAllText;
	private String mFirstText;

	public DetailLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public DetailLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context){
		mContext = context;
		LinearLayout layout = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.detail_layout, null);
		mTitleView = (KyTextaView)layout.findViewById(R.id.title);
		mListView = (KyListView)layout.findViewById(R.id.more_content);
		mHeader = (TextView)layout.findViewById(R.id.tip);
		mHeaderLine = (TextView)layout.findViewById(R.id.header_line);
		mVirtualView = (TextView)layout.findViewById(R.id.virtual);
		mMoreView = (TextView)layout.findViewById(R.id.more);
		addView(layout,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mTitleView.setOnClickListener(this);	
	}
	
	private void getTextViewWidth(final KyTextaView view){
		ViewTreeObserver vto = view.getViewTreeObserver();  
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override  
            public void onGlobalLayout() {
            	try{
            		view.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
            		view.setWidth(view.getWidth());
            		mSubText = mTitleView.getSubText();
            		mTitleView.setText(mSubText);
            		if(!mFirstText.equals(mSubText)){              
            			mIsSub = true;
            			String header = mFirstText.substring(0, mSubText.length());
            			String tailer = mFirstText.substring(mSubText.length(),mFirstText.length());
            			mAllText = header+"\n"+tailer;
            			mIsExpansion = false;
            		}else if(null == mLists || mLists.size()<=0){
            			mMoreView.setVisibility(View.GONE);
            		}
            	}catch(Exception e){         		
            	}
            }  
        });  
	    
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mIsExpansion){
			mListView.setVisibility(View.GONE);
			mHeaderLine.setVisibility(View.GONE);
			mIsExpansion = false;
			if(mIsSub){
				mTitleView.setText(mSubText);
			}
		}else{
			mListView.setVisibility(View.VISIBLE);
			mHeaderLine.setVisibility(View.VISIBLE);
			mIsExpansion = true;		
			if(mIsSub){
				mTitleView.setText(mAllText);
			}
			if(null == mLists || mLists.size()<=0){
				mListView.setVisibility(View.GONE);
				mHeaderLine.setVisibility(View.GONE);
			}
		}
	}
	
	public void setHeader(int id){
		mHeader.setText(id);
	}

	public void setList(ArrayList<String> list){
		mLists = list;
		mHeaderLine.setVisibility(View.GONE);	
		if(list.size() >= 1){
			mFirstText = mLists.get(0);
			if(!StringUtils.isEmpty(mFirstText)){
				mTitleView.setShowText(mFirstText);			
			}
			list.remove(0);
			DetailAdapter adapter = new DetailAdapter();
			mListView.setAdapter(adapter);
			mListView.setVisibility(View.GONE);
			mIsExpansion = false;
		}	
		getTextViewWidth(mTitleView);
	}
	
	class DetailAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(null != mLists){
				return mLists.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if(null != mLists){
				return mLists.get(position);
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
			ViewHolder holder = null;
			if(null == convertView){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_item, null);
				convertView.setTag(holder);
			}{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.view = (TextView)convertView.findViewById(R.id.detail_textview);
			holder.view.setText(mLists.get(position));
			return convertView;
		}
		
		class ViewHolder{
			TextView view;
		}
	}
}
