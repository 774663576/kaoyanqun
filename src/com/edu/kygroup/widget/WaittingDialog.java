/**
 * 工程名: KyGroup
 * 文件名: WaittingDialog.java
 * 包名: com.edu.kygroup.widget
 * 日期: 2013-10-27上午10:14:17
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * 类名: WaittingDialog <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2013-10-27 上午10:14:17 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class WaittingDialog extends Dialog{
	private Context context;
	private String msg;//提醒私信
	private ImageView spaceshipImage;//旋转图片
	private Animation hyperspaceJumpAnimation;//旋转动画

	public WaittingDialog(Context context,String msg) {
		
		super(context);
		this.context=context;
		this.msg=msg;
		init();
		
	}
	
	public WaittingDialog(Context context,int theme,String msg){
		super(context);
		this.context=context;
		this.msg=msg;
		init();
	}
	
	
//	public WaitingDialog(Context context, int theme,String msg) {
//		
//	}
	/**
	 * 
	 * init:初始化布局 <br/>
	 *
	 * @author fmh
	 * @since 1.0
	 */
	public void init(){
		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout., root)
//		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
//		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//		// main.xml中的ImageView
//		 spaceshipImage = (ImageView) v.findViewById(R.id.img);
//		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//		// 加载动画
//		 hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//				context, R.anim.loading_rotate);
//		// 使用ImageView显示动画
//		tipTextView.setText(msg);// 设置加载信息
//		this.setCancelable(false);// 不可以用“返回键”取消
//		this.setContentView(layout, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	}
	public Context getCurrentContent() {
		return context;
	}

	@Override
	public void show() {
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		super.show();
	}

}

