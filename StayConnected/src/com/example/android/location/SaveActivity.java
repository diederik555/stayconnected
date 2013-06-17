package com.example.android.location;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.ArrayAdapter;

public class SaveActivity extends Service {
  private CommentsDataSource datasource;
  String location, address;
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    //TODO do something useful
	location = (String) intent.getExtras().get("location");
	address = (String) intent.getExtras().get("address");

    datasource = new CommentsDataSource(this);
    datasource.open();
    
    List<Comment> values = datasource.getAllComments();
    
    ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
            android.R.layout.simple_list_item_1, values);
    
    Comment comment = null;
    comment = datasource.createComment(location, address);
    adapter.add(comment);
    adapter.notifyDataSetChanged();
	  
    return Service.START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
  //TODO for communication return IBinder implementation
    return null;
  }
} 