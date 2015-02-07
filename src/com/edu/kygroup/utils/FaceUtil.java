/**
 * 工程名: KyGroup
 * 文件名: FaceUtil.java
 * 包名: com.example.faceapp
 * 日期: 2013-12-17上午11:09:39
 * Copyright (c) 2013, 北京联龙博通 All Rights Reserved.
 *
*/

package com.edu.kygroup.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.edu.kygroup.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

/**
 * 类名: FaceUtil <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2013-12-17 上午11:09:39 <br/>
 *
 * @author   leixun
 * @version  	 
 */
public class FaceUtil {
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 */
    public static void dealFace(Context context,SpannableString spannableString, Pattern patten, int start) throws Exception {
    	Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            Field field = R.drawable.class.getDeclaredField(key);
			int resId = Integer.parseInt(field.get(null).toString());		
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                ImageSpan imageSpan = new ImageSpan(bitmap);				            
                int end = matcher.start() + key.length();					
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	
                if (end < spannableString.length()) {						
                    dealFace(context,spannableString,  patten, end);
                }
                break;
            }
        }
    }

    public static SpannableString getFaceString(Context context,String str,String zhengze){
    	SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);		//通过传入的正则表达式来生成一个pattern
        try {
            dealFace(context,spannableString, sinaPatten, 0);
        } catch (Exception e) {
        }
        return spannableString;
    }
}

