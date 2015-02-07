package com.edu.kygroup.utils;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class ConstantUtils {

	/** 本地配置文件名 */
	public final static String SHARED_PREF_FILE_NAME = "shared_pref_file";

	/** 日志记录等级 */
	public final static String LOG_LEVER = "verbose"; // verbose|debug|info|warn|error

	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;// 选择图片
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;// 拍照
	public static final int REQUEST_CODE_GETIMAGE_DROP = 2;// 裁剪
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD_MORE = 4;// 图片多选
	/**
	 * 日志记录Map
	 */
	public final static Map<String, Integer> LOG_LEVER_MAP = new HashMap<String, Integer>() {
		{
			put("verbose", Integer.valueOf(Log.VERBOSE));
			put("debug", Integer.valueOf(Log.DEBUG));
			put("info", Integer.valueOf(Log.INFO));
			put("warn", Integer.valueOf(Log.WARN));
			put("error", Integer.valueOf(Log.ERROR));
		}
	};

	public final static boolean LOG_FLAG_VERBOSE = Log.VERBOSE >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public final static boolean LOG_FLAG_DEBUG = Log.DEBUG >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public final static boolean LOG_FLAG_INFO = Log.INFO >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public final static boolean LOG_FLAG_WARN = Log.WARN >= LOG_LEVER_MAP
			.get(LOG_LEVER);
	public final static boolean LOG_FLAG_ERROR = Log.ERROR >= LOG_LEVER_MAP
			.get(LOG_LEVER);
}
