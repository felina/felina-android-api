package com.felina.android.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Client;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import android.util.Log;

public class FelinaClient {
	
	private static final String API_URL = "http://ec2-54-194-186-121.eu-west-1.compute.amazonaws.com";
	private Felina felina;
	
	static class LoginCheck {
		Boolean res;
		Err err;
	}
	
	static class Err {
		int code;
		String msg;
	}
	
	class User {
		int id;
		String name;
		String email;
		int privilege;
		String profile_image;
	}
	
	static class Login {
		Boolean res;
		Err err;
		User user;
	}
	
	interface Felina {
		@GET("/logincheck")
		LoginCheck loginCheck();
		
		@FormUrlEncoded
		@POST("/login")
		void login(@Field("email") String email, @Field("pass") String pass, Callback<Login> callback);
	}
	
	public FelinaClient() {
		
		Client client = new ApacheClient() {
		    final CookieStore cookieStore = new BasicCookieStore();
		    @Override
		    protected HttpResponse execute(HttpClient client, HttpUriRequest request) throws IOException {
		        // BasicHttpContext is not thread safe 
		        // CookieStore is thread safe
		        BasicHttpContext httpContext = new BasicHttpContext();
		        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		        return client.execute(request, httpContext);
		    }
		};
		
		RestAdapter restAdapter = new RestAdapter.Builder()
			.setEndpoint(API_URL)
			.setClient(client)
			.build();
		
		felina = restAdapter.create(Felina.class);
	}
	
	public Felina getFelina() {
		return felina;
	}
	
	public Boolean logincheck() {
		LoginCheck logincheck = felina.loginCheck();
		Log.d("FelinaClient", "LoginCheck "+logincheck.res+" "+logincheck.err.msg);
		return logincheck.res;
	}
	
	public Boolean login(String email, String pass) {
		felina.login(email, pass, new Callback<Login>(){

			@Override
			public void failure(RetrofitError error) {
				Log.d("FelinaClient", "Error, body: " + error.getBody().toString());
			}

			@Override
			public void success(Login login, Response response) {
				Log.d("FelinaClientL", login.res+" login");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						logincheck();
					}
				}).start();
			}
		});
		return true;
	}

}
