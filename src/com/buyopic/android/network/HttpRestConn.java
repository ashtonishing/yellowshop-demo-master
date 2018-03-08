package com.buyopic.android.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.Handler;
import android.os.Message;

public final class HttpRestConn {
	private static final int CONNECTION_TIMEOUT_INTERVAL = 2*60 * 1000;
	private static final int TIMEOUT_INTERVAL = 2*60 * 1000;

	protected static String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		StringBuffer out = new StringBuffer();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			out.append(line);
		}
		return out.toString();
	}

	public static void getRequestData(String url,
			Handler wayHandler) {

		HttpClient httpClient = MySSLSocketFactory.getNewHttpClient();
		HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);
		HttpParams httpParams = httpClient.getParams();

		HttpConnectionParams.setConnectionTimeout(httpParams,
				CONNECTION_TIMEOUT_INTERVAL);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_INTERVAL);
		HttpContext localContext = new BasicHttpContext();
		String text = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("User-Agent", "Mobile");
			HttpResponse response = httpClient.execute(httpGet, localContext);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();

				text = getASCIIContentFromEntity(entity);
				if (text != null) {
					Utils.showLog("Response String ===>>>" + text);
					Message.obtain(wayHandler, Constants.SUCCESS, text)
							.sendToTarget();
				} else {
					Message.obtain(wayHandler, Constants.FAILURE,
							"Unknown Error").sendToTarget();
				}
			} else {
				Message.obtain(wayHandler, Constants.FAILURE,
						"An error has occurred").sendToTarget();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Message.obtain(wayHandler, Constants.FAILURE,
					"Unable to process your request" + e.toString())
					.sendToTarget();
		}
	}
	
	public static void postRequestData(String url,
			List<NameValuePair> nameValuePairs, BuyopicRequestHandler handler) {
		HttpClient httpClient = MySSLSocketFactory.getNewHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				CONNECTION_TIMEOUT_INTERVAL);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_INTERVAL);
		HttpContext localContext = new BasicHttpContext();
		String text = null;
		try {
			HttpPost httppost = new HttpPost(url);
			 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httppost, localContext);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				
				text = getASCIIContentFromEntity(entity);
				if (text != null) {
					Utils.showLog("Response String ===>>>" + text);
					Message.obtain(handler, Constants.SUCCESS, text)
					.sendToTarget();
				} else {
					Message.obtain(handler, Constants.FAILURE, "Unknown Error")
					.sendToTarget();
				}
			} else {
				Message.obtain(handler, Constants.FAILURE,
						"Unable to process your request").sendToTarget();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message.obtain(handler, Constants.FAILURE,
					"Unable to process your request" + e.toString())
					.sendToTarget();
		}
	}

}