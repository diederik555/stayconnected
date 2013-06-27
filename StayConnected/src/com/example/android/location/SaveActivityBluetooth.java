package com.example.android.location;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;

public class SaveActivityBluetooth extends Service {
  private CommentsDataSource datasource;
  String location, address;
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
	  
	  Log.d("Debug","Komt bij save");
    //TODO do something useful
	location = (String) intent.getExtras().get("ownmac");
	address = (String) intent.getExtras().get("founddevices");

    datasource = new CommentsDataSource(this);
    datasource.open();
    
    List<Comment> values = datasource.getAllComments();
    
    ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
            android.R.layout.simple_list_item_1, values);
    
    Comment comment = null;
    comment = datasource.createComment(location, address);
    adapter.add(comment);
    adapter.notifyDataSetChanged();
    
    System.out.println("Got here!! ::: " + location + address);
	  
    return Service.START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
  //TODO for communication return IBinder implementation
    return null;
  }
} 