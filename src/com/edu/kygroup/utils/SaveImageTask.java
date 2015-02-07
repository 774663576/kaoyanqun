package com.edu.kygroup.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.edu.keygroup.video.util.Utils;
import com.edu.kygroup.ui.KygroupApplication;

public class SaveImageTask extends AsyncTask<Void, Void, Void> {
	private SaveImge callBack;
	private Bitmap bmp;

	public void setCallBack(SaveImge callBack) {
		this.callBack = callBack;
	}

	public SaveImageTask(Bitmap bmp) {
		this.bmp = bmp;
	}

	@Override
	protected Void doInBackground(Void... params) {
		String name = FileUtils.getFileName() + ".jpg";
		String fileName = FileUtils.getImgSavePath() + name;
		BitmapUtils.createImgToFile(bmp, fileName);
		bmp.recycle();
		Utils.fileScan(KygroupApplication.getApplication(), fileName);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		callBack.saveFinish();
	}

	public interface SaveImge {
		void saveFinish();
	}
}
