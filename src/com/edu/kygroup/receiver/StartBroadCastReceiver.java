/**
 * 工程名: KyGroup
 * 文件名: StartBroadCastReceiver.java
 * 包名: com.edu.kygroup.receiver
 * 日期: 2013-12-8下午6:55:10
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
*/

package com.edu.kygroup.receiver;

import com.edu.kygroup.service.KyGroupService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 类名: StartBroadCastReceiver <br/>
 * 功能: TODO 添加功能描述. <br/>
 * 日期: 2013-12-8 下午6:55:10 <br/>
 *
 * @author   lx
 * @version  	 
 */
public class StartBroadCastReceiver extends BroadcastReceiver{

	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(ACTION.equals(
				intent.getAction())){
//			Intent mIntent = new Intent(context,KyGroupService.class);
//			context.startService(intent);
		}
		
	}

}

