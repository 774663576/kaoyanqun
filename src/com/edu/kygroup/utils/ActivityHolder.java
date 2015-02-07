package com.edu.kygroup.utils;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public class ActivityHolder {
	private static String TAG = "ActivityHolder";
	private static ArrayList<Activity> mLoginLists = new ArrayList<Activity>();
	
	private static ArrayList<Activity> mFocusLists = new ArrayList<Activity>();
	
	private static ActivityHolder mActivityHolder = new ActivityHolder();

	public static synchronized ActivityHolder getInstance() {
		return mActivityHolder;
	}

	/**
	 * add the activity in to a list
	 */
	public void addActivity(Activity activity) {
		mLoginLists.add(activity);
	}

	/**
	 * finish all the activity in the list.
	 */
	public void finishAllActivity() {
		if (null != mLoginLists && mLoginLists.size()>0) {
			try {
				int size = mLoginLists.size();
				for (int i = size - 1; i >= 0; i--) {
					Activity activity = mLoginLists.get(i);
					if (activity != null) {
						activity.finish();
					}
					mLoginLists.remove(activity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	 /**
     * remove the finished activity in the list.
     */
    public void removeActivity(Activity activity) {
        try {
        	mLoginLists.remove(activity);     
        } catch (Exception e) {
        }
    }
    
    public void addFocusActivity(Activity activity){
    	mFocusLists.add(activity);
    }
    
    public void finishAllFocusActivity() {
		if (null != mFocusLists && mFocusLists.size()>0) {
			try {
				int size = mFocusLists.size();
				for (int i = size - 1; i >= 0; i--) {
					Activity activity = mFocusLists.get(i);
					if (activity != null) {
						activity.finish();
					}
					mFocusLists.remove(activity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
    public void removeFocusActivity(Activity activity) {
        try {
        	mFocusLists.remove(activity);
        } catch (Exception e) {
        }
    }
}
