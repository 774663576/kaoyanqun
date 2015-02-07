package com.edu.kygroup.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpAgent {
	private final static int REQUEST_TIMEOUT = 5000;
	public static final int CONNECTION_TIMEOUT = 10 * 1000;
	public static final int SO_TIMEOUT = 10 * 1000;

	/**
	 * get 提交方式 // *
	 * 
	 * @param url
	 *            URL 链接
	 * @return
	 * @throws IOException
	 */
	public static String getUrlData(String url) {
		System.out.println("result::::::::::::" + url);

		String result = "";
		try {
			HttpGet httpRequest = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			// 请求超时
			httpclient.getParams()
					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							CONNECTION_TIMEOUT);
			// 读取超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);

			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 判断是否成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				System.out.println("result::::::::::::==----"
						+ httpResponse.getStatusLine().getStatusCode());

			}
		} catch (Exception e) {
			System.out.println("result::::::::::::==" + e.toString());
		}
		System.out.println("result::::::::::::" + result);
		return result;
	}

	public static String httpPost(String request) {
		return getUrlData(request);
	}
}
