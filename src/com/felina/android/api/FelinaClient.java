package com.felina.android.api;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;

import com.felina.android.api.Constants.Params;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class FelinaClient {
	
	private static final String API_URL = "http://ec2-54-194-186-121.eu-west-1.compute.amazonaws.com";
//	private static final String API_URL = "http://nl.ks07.co.uk:5005";
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	private static Context context;
	
	public FelinaClient(Context c){
		context = c;
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
	
	public void register(String email, String pass, String name, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put(Params.LOGIN_EMAIL, email);
		params.put(Params.LOGIN_PASS, pass);
		params.put(Params.REGISTER_NAME, name);
		post("/register", params, responseHandler);
	}
	
	public void logincheck(AsyncHttpResponseHandler responseHandler) {
		get("/logincheck", null, responseHandler);
	}
	
	public void token(String email, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put(Params.LOGIN_EMAIL, email);
		get("/token", params, responseHandler);
	}
	
	public void postImg(File f, String contentType, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		try {
			params.put(Params.IMG_FILE, f, contentType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		post("/img", params, responseHandler);
	}
	
	public void postImg(File f, String contentType, int id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		try {
			params.put(Params.IMG_FILE, f, contentType);
			params.put(Params.IMG_PROJECT, id);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		post("/img", params, responseHandler);
		
	}
	
	public void getImageList(AsyncHttpResponseHandler responseHandler) {
		get("/images", null, responseHandler);
	}
	
	public void getImage(String id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put(Params.IMG_ID, id);
		get("/img", params, responseHandler);
	}

}
