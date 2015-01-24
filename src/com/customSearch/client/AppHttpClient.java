package com.customSearch.client;

import com.loopj.android.http.AsyncHttpClient;



/**
 * @author nish
 *  singleton for our rest client 
 *  
 *   makes get request
 */
/**
 * @author nish
 *
 */
/**
 * @author nish
 *   singleton client 
 *   as we can expect many request
 *   
 */
public class AppHttpClient {

	private static AppHttpClient appClient;
	private AsyncHttpClient client;

	private AppHttpClient() {
		client = new AsyncHttpClient();
	}

	public static AppHttpClient getClient() {
		if (appClient == null) {
			synchronized (AppHttpClient.class) {
				if (appClient == null) {
					appClient = new AppHttpClient();
				}
			}
		}
		return appClient;
	}

	
	//makes a http get request
	
	public void get(String url, APIResponseHandler<?> handler) {
		client.get(getAbsoluteUrl(url), handler);
	}

	private static String getAbsoluteUrl(String url) {
		return url;
	}

	
}
