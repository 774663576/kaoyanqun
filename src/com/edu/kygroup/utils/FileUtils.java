package com.edu.kygroup.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.edu.kygroup.ui.KygroupApplication;

public class FileUtils {
	public final static String CACHE_IMAGES_PATH = "/edu" + "/imgfiles/";
	public final static String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public final static String SAVE_FILE_PATH_DIRECTORY = SDCARD_PATH + "/"
			+ "edu";
	public final static int POOL_SIZE = 5;// 单个CPU线程池大小
	public final static int MB = 1024 * 1024;
	public final static int CACHE_SIZE = 3; // 限制apk文件包缓存图片目录大小最大为3M
	public final static int SD_CACHE_SIZE = 10; // 限制SD卡文件图片缓存目录大小最大为10M

	/**
	 * 获取拍照路径
	 */
	public static String getCameraPath() {
		String sdpath = getRootDir();
		File destDir = new File(sdpath + CACHE_IMAGES_PATH + "/camera");
		if (!destDir.exists()) {// 创建文件夹
			destDir.mkdirs();
		}
		createNoMediaFile();
		return destDir.getAbsolutePath();

	}

	private static void createNoMediaFile() {
		File file = new File(getRootDir() + CACHE_IMAGES_PATH + "/.nomedia");
		if (!file.exists()) {// 判断文件是否存在（不存在则创建这个文件）
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取根目录
	 */
	public static String getRootDir() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static String getImgSavePath() {
		String path = getRootDir() + "/kaoyanqunImgSave/";
		File destDir = new File(path);
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		return path;

	}

	/**
	 * 使用当前时间戳拼接一个唯�?��文件�?
	 * 
	 * @param format
	 * @return
	 */
	public static String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}

	/**
	 * 根据文件绝对路径获取文件名称
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isBlank(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	public static boolean isFolderExist() {
		boolean ret = false;
		if (isSDcardExist()) {
			File file = new File(SAVE_FILE_PATH_DIRECTORY);
			if (file.exists()) {
				ret = true;
			} else {
				ret = file.mkdir();
			}
		}
		return ret;
	}

	public static boolean isSDcardExist() {
		boolean isExist = false;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				isExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public static void saveMyBitmap(Bitmap bitmap, String path, int compress) {
		try {
			if (isFolderExist()) {
				File f = new File(path);
				if (f.exists()) {
					f.delete();
				}
				f.createNewFile();
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, compress, fOut);
				fOut.flush();
				fOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	public static int uploadFile(File file, String RequestURL) {
		int res = 0;
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			System.out.println("pic:::::::::::::::::url" + RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null && file.exists()) {
				System.out.println("pic:::::::::::::::::path"
						+ file.getAbsolutePath());
				/**
				 * 当文件不为空时执行上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名
				 */
				sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				res = conn.getResponseCode();
				Log.e(TAG, "response code:" + res);
				if (res == 200) {
					Log.e(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					updatePic(result);
					Log.e(TAG, "result : " + result);
					System.out.println("pic:::::::::::::::::+++++++++++++++++"
							+ res);

				} else {
					System.out.println("pic:::::::::::::::::err_code" + res);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pic:::::::::::::::::err" + e.toString());
		}
		return res;
	}

	public static void updatePic(String result) {
		System.out.println("pic:::::::::::::::::====0000000000000" + result);
		try {
			JSONObject obj = new JSONObject(result);
			String code = obj.getString("result").trim();
			String url = obj.getString("url");
			if (url != null && !url.equals("")) {
				KygroupApplication.mUser.setPic(url);
				SharedPreferences prefs = KygroupApplication
						.getApplication()
						.getSharedPreferences(
								ConstantUtils.SHARED_PREF_FILE_NAME,
								KygroupApplication.getApplication().MODE_PRIVATE);
				Editor editor = prefs.edit();
				editor.putString("pic", url);
				editor.commit();
				System.out.println("pic:::::::::::::::::share" + url);
				KygroupApplication.getApplication().sendBroadcast(
						new Intent(Constant.UPDATE_USER_AVATAR));
			}
		} catch (Exception e) {
		}
	}

	public static String uploadCommentFile(File file, String RequestURL) {
		int res = 0;
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			System.out.println("pic:::::::::::::::::url" + RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null && file.exists()) {
				System.out.println("pic:::::::::::::::::path"
						+ file.getAbsolutePath());
				/**
				 * 当文件不为空时执行上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名
				 */
				sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
			}
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			res = conn.getResponseCode();
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				System.out.println("pic:::::::::::::::::result::" + result);
				return result;
			} else {
				System.out.println("pic:::::::::::::::::err_code" + res);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pic:::::::::::::::::err" + e.toString());
		}
		return result;
	}

	public static String uploadFile(File imageFile) {
		try {
			String requestUrl = UrlUtils.UPLOAD_IMG_URL;
			// 请求普通信息
			Map<String, String> params = new HashMap<String, String>();
			params.put("email", KygroupApplication.mUser.getEmail());
			params.put("fileName", imageFile.getName());
			// 上传文件
			FormFile formfile = new FormFile(imageFile.getName(), imageFile,
					"image", "application/octet-stream");
			SocketHttpRequester.post(requestUrl, params, formfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "OK";
	}

	/**
	 * 获取本地图片
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传图片请求
	 * 
	 * @param url
	 * @param map
	 * @param file
	 * @param pkey
	 * @return
	 */
	public static String postDataWithFile(String url, Map<String, Object> map,
			File file) {
		HttpPost post = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		MultipartEntity mpEntity = new MultipartEntity();
		Iterator<?> iterator = map.entrySet().iterator();
		if (file != null && file.exists()) {
			FileBody fileBody = new FileBody(file);
			mpEntity.addPart("filename", fileBody);
		}
		try {

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry testDemo = (Map.Entry) iterator.next();
				Object key = testDemo.getKey();
				Object value = testDemo.getValue();
				mpEntity.addPart(
						key.toString(),
						new StringBody(value.toString(), Charset
								.forName("UTF-8")));
			}
			post.setEntity(mpEntity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				System.out.println("result::::::::::::pic:::::::"
						+ response.getStatusLine().getStatusCode());

			}
		} catch (Exception e) {
			System.out.println("result::::::::::::pic:::::::" + e.toString());
			return null;

		} finally {
			if (mpEntity != null) {
				try {
					mpEntity.consumeContent();
				} catch (Exception e) {
					System.out.println("result::::::::::::pic:::::::"
							+ e.toString());
				}
			}
			client.getConnectionManager().shutdown();
		}

		return null;
	}

	/**
	 * 上传图片数组
	 * 
	 * @param url
	 * @param map
	 * @param file
	 * @param pkey
	 * @return
	 */
	public static String upLoadPicArray(String url, Map<String, Object> map,
			List<File> files, String pkey) {
		System.out.println("result::::::::::::POic::" + url);

		HttpPost post = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		MultipartEntity mpEntity = new MultipartEntity();
		Iterator<?> iterator = map.entrySet().iterator();
		for (int i = 0; i < files.size(); i++) {
			FileBody fileBody = new FileBody(files.get(i), "image/pjpeg");
			mpEntity.addPart(pkey, fileBody);
			// mpEntity.addPart("image" + i, fileBody);
			System.out.println("size::::::::::::" + fileBody.getFilename());

		}
		try {
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry testDemo = (Map.Entry) iterator.next();
				Object key = testDemo.getKey();
				Object value = testDemo.getValue();
				mpEntity.addPart(
						key.toString(),
						new StringBody(value.toString(), Charset
								.forName("UTF-8")));
				System.out.println("map:::::::::::::::" + value.toString());

			}
			post.setEntity(mpEntity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				System.out.println("err::::::::::::code"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("err::::::::::::" + e.toString());
		} finally {
			if (mpEntity != null) {
				try {
					mpEntity.consumeContent();
				} catch (Exception e) {
					System.out.println("err::::::::::::==" + e.toString());

				}
			}
			client.getConnectionManager().shutdown();
		}

		return null;
	}
}
