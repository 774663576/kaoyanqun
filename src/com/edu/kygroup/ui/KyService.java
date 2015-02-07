package com.edu.kygroup.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.baidu.platform.comapi.map.E;
import com.edu.kygroup.utils.FileUtils;
import com.edu.kygroup.utils.UrlUtils;

public class KyService extends Service {
	public final static int UPLOAD_IMG = 1;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (null != intent) {
			int flag = intent.getIntExtra("uploadimg", 0);
			if (flag == UPLOAD_IMG) {
				upLoadImg(intent);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void upLoadImg(Intent intent) {
		final String path = intent.getStringExtra("imgPath");
		final String emali = intent.getStringExtra("email");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				// String url = UrlUtils.UPLOAD_IMG_URL + "?email=" + emali;//
				// +"&upload="+path;
				File file = new File(path);
				// FileUtils.uploadFile(file, url);
				List<File> fileList = new ArrayList<File>();
				fileList.add(file);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("email", emali);
				String result = FileUtils.upLoadPicArray(UrlUtils.BASE_URL
						+ "/exam/servlet/UploadPic", map, fileList, "uploads");
				FileUtils.updatePic(result);
				return null;
			}

			protected void onPostExecute(Void result) {
				stopSelf();
			};
		}.execute();
	}
}
