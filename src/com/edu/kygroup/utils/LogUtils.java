package com.edu.kygroup.utils;


import android.util.Log;


public class LogUtils {

	public static void v(String TAG, String msg, Throwable t) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_VERBOSE) {
				Log.v(TAG, msg, t);
			}
		}
	}

	public static void v(String TAG, String msg) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_VERBOSE) {
				Log.v(TAG, msg);
			}
		}
	}

	public static void d(String TAG, String msg, Throwable t) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_DEBUG) {
				Log.d(TAG, msg, t);
			}
		}
	}

	public static void d(String TAG, String msg) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_DEBUG) {
				Log.d(TAG, msg);
			}
		}
	}

	public static void i(String TAG, String msg) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_INFO) {
				Log.i(TAG, msg);
			}
		}
	}

	public static void i(String TAG, String msg, Throwable t) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_INFO) {
				Log.i(TAG, msg, t);
			}
		}
	}

	public static void w(String TAG, String msg) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_WARN) {
				Log.w(TAG, msg);
			}
		}
	}


	public static void w(String TAG, String msg, Throwable t) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_WARN) {
				Log.w(TAG, msg, t);
			}
		}
	}

	public static void e(String TAG, String msg) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_ERROR) {
				Log.e(TAG, msg);
			}
		}
	}

	public static void e(String TAG, String msg, Throwable t) {
		if (msg != null) {
			if (ConstantUtils.LOG_FLAG_ERROR) {
				Log.e(TAG, msg, t);
			}
		}
	}
}
