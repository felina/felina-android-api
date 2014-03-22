package com.felina.android.api;

import android.content.Context;

import com.felina.android.api.Constants.Params;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class FelinaClient {
	
	private static final String API_URL = "http://ec2-54-194-186-121.eu-west-1.compute.amazonaws.com";
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	
	public FelinaClient(Context context){
		PersistentCookieStore mCookieStore = new PersistentCookieStore(context);
		mClient.setCookieStore(mCookieStore);
	}
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		mClient.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		mClient.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return API_URL + relativeUrl;
	}
	
	public void login(String email, String pass, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put(Params.LOGIN_EMAIL, email);
		params.put(Params.LOGIN_PASS, pass);
		post("/login", params, responseHandler);
	}
	
	public void logincheck(AsyncHttpResponseHandler responseHandler) {
		get("/logincheck", null, responseHandler);
	}

}
