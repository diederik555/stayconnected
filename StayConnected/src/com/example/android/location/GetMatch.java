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

import com.example.android.location.SendActivity.DoPOST;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class GetMatch extends Service {
	  
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  AccountManager am = AccountManager.get(this);
	      Account[] accounts = am.getAccountsByType("com.google");
	      
	      Log.d("Sending","Account: " + accounts[0]);
	      
	      DoPOST mDoPOST = new DoPOST(this, accounts[0].toString());
			mDoPOST.execute("");
		  
	      return Service.START_NOT_STICKY;
	  }
	  
	  public class DoPOST extends AsyncTask<String, Void, Boolean>{

			Context mContext = null;
			String account = "";

			
			Exception exception = null;
			
			DoPOST(Context context, String accounts){
				mContext = context;
				account = accounts;
            	String oldaccount = accounts;
            	account = oldaccount.split(",|=")[1];
				System.out.println("verzondendata " + account);
				
			}

			@Override
			protected Boolean doInBackground(String... arg0) {

				try{

					//Setup the parameters
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("Account", account));
					//Add more parameters as necessary

					//Create the HTTP request
					HttpParams httpParameters = new BasicHttpParams();

					//Setup timeouts
					HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
					HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

					HttpClient httpclient = new DefaultHttpClient(httpParameters);
					HttpPost httppost = new HttpPost("http://bekerdesign.nl/stayconnected/match.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					String result = EntityUtils.toString(entity);
					System.out.println("requestresponse " + result);
					if(result != null)
					{
						Intent i = new Intent(getBaseContext(), MatchPopup.class);
						i.putExtra("match", result);
				        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        startActivity(i);
					}
					else
					{
						//popup met helaas nog geen match gevonden
					}

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