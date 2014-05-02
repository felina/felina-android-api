package com.felina.android.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.felina.android.api.Constants.Params;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class FelinaClient {
	
	private static final String API_URL = "https://api.darwinapp.co:5000";
//	private static String API_URL = "http://ec2-54-194-186-121.eu-west-1.compute.amazonaws.com:5000";
//	private static final String API_URL = "http://nl.ks07.co.uk:5005";
	private static AsyncHttpClient mClient = new AsyncHttpClient();
	private static Context context;
	
	public FelinaClient(Context c){
		context = c;
		PersistentCookieStore mCookieStore = new PersistentCookieStore(context);
		mClient.setCookieStore(mCookieStore);
	}
	
	/**
	 * Performs a get request with the params.
	 * @param url the relative url to get.
	 * @param params the url encoded params.
	 * @param responseHandler the callback to handle response.
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		mClient.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	/**
	 * Performs a post request with the params.
	 * @param url the relative url to post to.
	 * @param params the params to be set as the body of the request.
	 * @param responseHandler the callback to handle response.
	 */
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		mClient.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	/**
	 * Performs a post request with the params and sets the content type to application/json.
	 * @param url the relative url to post to.
	 * @param params the params to be set as the body of the request.
	 * @param responseHandler the callback to handle response.
	 */
	public static void post(String url, StringEntity params, AsyncHttpResponseHandler responseHandler) {
		mClient.post(context, getAbsoluteUrl(url), params, "application/json", responseHandler);
	}

	/**
	 * Appends the relative url to the API Url
	 * @param relativeUrl
	 * @return
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		Log.d(Constants.LOG_TAG, API_URL);
		return API_URL + relativeUrl;
	}
	
	/**
	 * Posts the email and password to the login end-point to login.
	 * @param email the email address as user-name.
	 * @param pass the password for signing in.
	 * @param responseHandler the callback to handle response.
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void login(String email, String pass, AsyncHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException {
		JSONObject params = new JSONObject();
		params.put(Params.LOGIN_EMAIL, email);
		params.put(Params.LOGIN_PASS, pass);
		post("/login", new StringEntity(params.toString()), responseHandler);
	}
	
	/**
	 * Posts the email, name and password to register to new user.
	 * @param email the email address as user-name.
	 * @param pass the password for signing in.
	 * @param name the name of the user.
	 * @param responseHandler the callback to handle response.
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public void register(String email, String pass, String name, AsyncHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException  {
		JSONObject params = new JSONObject();
		params.put(Params.LOGIN_EMAIL, email);
		params.put(Params.LOGIN_PASS, pass);
		params.put(Params.REGISTER_NAME, name);
		post("/user", new StringEntity(params.toString()), responseHandler);
	}
	
	/**
	 * Checks if a user is logged in.
	 * @param responseHandler the callback to handle response.
	 */
	public void logincheck(AsyncHttpResponseHandler responseHandler) {
		get("/logincheck", null, responseHandler);
	}
	
	/**
	 * Gets a new token to be used for authentication.
	 * @param email the email of the sub-user account.
	 * @param responseHandler the callback to handle response.
	 */
	public void token(String email, AsyncHttpResponseHandler responseHandler) throws JSONException, UnsupportedEncodingException {
		JSONObject params = new JSONObject();
		params.put(Params.LOGIN_EMAIL, email);
		post("/token", new StringEntity(params.toString()), responseHandler);
	}
	
	/**
	 * Posts the image file to the server. USE ONLY WITH SUBUSER ACCOUNT.
	 * The project id's are linked to the subuser account.
	 * @param f the file to be posted.
	 * @param contentType the content type of the image.
	 * @param responseHandler the callback to handle response.
	 */
	public void postImg(File f, String contentType, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		try {
			params.put(Params.IMG_FILE, f, contentType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		post("/images", params, responseHandler);
	}
	
	/**
	 * Posts an image with project id to the server.
	 * @param f the file to be posted.
	 * @param contentType the content type of the image.
	 * @param id the project id for the image.
	 * @param responseHandler the callback to handle response.
	 */
	public void postImg(File f, String contentType, String id, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		try {
			params.put(Params.IMG_FILE, f, contentType);
			params.put(Params.IMG_PROJECT, id);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		post("/images", params, responseHandler);
	}
	
	/**
	 * Performs a get requests for the list of image id's that a user owns.
	 * @param responseHandler the callback to handle response.
	 */
	public void getImageList(AsyncHttpResponseHandler responseHandler) {
		get("/images", null, responseHandler);
	}
	
	/**
	 * Performs a get request for the image based on the id.
	 * @param responseHandler the callback to handle response.
	 */
	public void getImage(String id, AsyncHttpResponseHandler responseHandler) {
		get("/images/"+id, null, responseHandler);
	}

}
