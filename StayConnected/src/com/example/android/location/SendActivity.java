package com.example.android.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class SendActivity extends Service {
  private CommentsDataSource datasource;
  String location, address;
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
	  AccountManager am = AccountManager.get(this);
      Account[] accounts = am.getAccountsByType("com.google");
      
	  datasource = new CommentsDataSource(this);
      datasource.open();

      List<Comment> values = datasource.getAllComments();

      Log.d("Sending","Account: " + accounts[0]);
      Log.d("Sending","values: " + values);
      
      DoPOST mDoPOST = new DoPOST(this, values.toString(), accounts[0].toString());
		mDoPOST.execute("");
	  
      return Service.START_NOT_STICKY;
  }
  
  public class DoPOST extends AsyncTask<String, Void, Boolean>{

		Context mContext = null;
		String location = "";
		String account = "";
		
		//Result data
		String strFirstName;
		String strLastName;
		int intAge;
		int intPoints;
		
		Exception exception = null;
		
		DoPOST(Context context, String locations, String accounts){
			mContext = context;
			location = locations;
			account = accounts;
			System.out.println("verzondendata " + account + location);
			
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try{

				//Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("Location", location));
				nameValuePairs.add(new BasicNameValuePair("Account", account));
				//Add more parameters as necessary

				//Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				//Setup timeouts
				HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://bekerdesign.nl/stayconnected/login.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);
				System.out.println("requestresponse " + result);

				
				deleteDatabase("locations.db");

			}catch (Exception e){
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}
  }

  @Override
  public IBinder onBind(Intent intent) {
  //TODO for communication return IBinder implementation
    return null;
  }
} 