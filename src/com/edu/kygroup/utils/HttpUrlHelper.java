package com.edu.kygroup.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUrlHelper {

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
			System.out.println("file:::::::::::::::::999"
					+ file.getAbsolutePath());
			mpEntity.addPart("image", fileBody);
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
				System.out.println("file:::::::::::::::::"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			System.out.println("file:::::::::::::::::==" + e.toString());
			return "";

		} finally {
			if (mpEntity != null) {
				try {
					mpEntity.consumeContent();
				} catch (Exception e) {
				}
			}
			client.getConnectionManager().shutdown();
		}

		return "";
	}

}
