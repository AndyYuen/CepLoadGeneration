package com.redhat.cep.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalTime;

public class PushOverNofification {
	
	private final String PUSHOVER_URL = "https://api.pushover.net/1/messages.json";
	private final int KEY_LENGTH = 30;
//	private final String DEFAULT_SOUND = "siren";

	
	private CloseableHttpClient httpclient;
	private HttpPost httpPost;
	private List <NameValuePair> paramList = new ArrayList <NameValuePair>();
	private String APP_TOKEN;

	public PushOverNofification(String app_token) {
		httpclient = HttpClients.createDefault();
		httpPost = new HttpPost(PUSHOVER_URL);
		APP_TOKEN = app_token;
	}
	
	public void sendNotification(String userKey, String message) throws ClientProtocolException, IOException {
		if (userKey.length() == KEY_LENGTH) {
			paramList.clear();
			send(userKey, message);
		}
	}
	
	public void sendNotification(String userKey, String title, String message) throws ClientProtocolException, IOException {
		if (userKey.length() == KEY_LENGTH) {
			paramList.clear();
			paramList.add(new BasicNameValuePair("title", title));
			send(userKey, message);
		}
	}
	
	public void sendNotification(String userKey, LocalTime messageTime, String title, String message) throws ClientProtocolException, IOException {
		if (userKey.length() == KEY_LENGTH) {
			paramList.clear();
			paramList.add(new BasicNameValuePair("timestamp", String.format("%d", messageTime.toDateTimeToday().getMillis() / 1000)));
			paramList.add(new BasicNameValuePair("title", title));
			send(userKey, message);
		}
	}
	
	public void send(String userKey, String message) throws ClientProtocolException, IOException {
		paramList.add(new BasicNameValuePair("token", APP_TOKEN));
		paramList.add(new BasicNameValuePair("user", userKey));
		paramList.add(new BasicNameValuePair("message", message));
//		nvps.add(new BasicNameValuePair("sound", DEFAULT_SOUND));
		httpPost.setEntity(new UrlEncodedFormEntity(paramList));
		CloseableHttpResponse response = httpclient.execute(httpPost);
	
		try {
			// only display response msg on error
			String responseMsg = response.getStatusLine().toString();
			if (responseMsg.indexOf("OK") < 0) {
				System.out.println(responseMsg);
			}
		    HttpEntity entity = response.getEntity();
		    // don't care about the response body
		    EntityUtils.consume(entity);
		} finally {
		    response.close();
		}
	}


}
