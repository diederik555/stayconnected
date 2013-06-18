package com.example.android.location;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
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
	  
      return Service.START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
  //TODO for communication return IBinder implementation
    return null;
  }
} 