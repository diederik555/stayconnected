/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.skeletonapp;
//import java.util.Set;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
//import com.example.projectbat.R;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.view.KeyEvent;
//import android.widget.TextView;
//import android.content.BroadcastReceiver;



/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class SkeletonActivity extends Activity implements BluetoothInterface {
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_DISCOVERABLE = 2;
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;

    private EditText mEditor;
    private EditText blueApparaten;
    public static BluetoothAdapter myAdapter;
    public static ArrayList<String> Adresses = new ArrayList<String>();
    public client clientThread;
    private BluetoothAdapter btAdapter;	
	private Network btService;
	
	private ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
	private final ArrayList<String> pairedData = new ArrayList<String>();
	
	private ArrayAdapter<String> pairedAdapters;	
    public SkeletonActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btService = new Network(this);
        
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.skeleton_activity);
        Log.d("Test", "OnCreate");
     
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        mEditor = (EditText) findViewById(R.id.editor);
        blueApparaten = (EditText) findViewById(R.id.blue);
        // Hook up button presses to the appropriate event handler.
        
        myAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myAdapter == null) {
        	mEditor.append("\nBluetooth wordt niet gesupport. Functionaliteit wordt minder.\n");
        }       
        if (!myAdapter.isEnabled()) {
        	myAdapter.enable();
        }
        //String oldName = myAdapter.getName();
        //na afsluiten herstel naam naar oldName
        String newName = "stayc";
        Log.d("StayC","Adapter naam is " +myAdapter.getName());
    	myAdapter.setName(newName);
    	Log.d("StayC","Adapter naam wordt " +myAdapter.getName());
    	
    	Set<BluetoothDevice> devices = myAdapter.getBondedDevices();
        if(devices.size()>0){
          for (BluetoothDevice device : devices) {
        	  foundDevices.add(device);
        	  Log.d("StayC", "Paired device found " + device);
          }
        }
        	
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
		//startActivity(discoverableIntent);
		startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);		
		btService.start();		
		Log.d("StayC", "Start discovering");
		myAdapter.startDiscovery();		     
    }
    
    public void doDiscovery(View v) 
    {
    	btService.broadcastMessage("Hello");            	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // We are going to create two menus. Note that we assign them
        // unique integer IDs, labels from our string resources, and
        // given them shortcuts.
        menu.add(0, BACK_ID, 0, R.string.back).setShortcut('0', 'b');
        menu.add(0, CLEAR_ID, 0, R.string.clear).setShortcut('1', 'c');

        return true;
    }

    /**
     * Called right before your activity's option menu is displayed.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Before showing the menu, we need to decide whether the clear
        // item is enabled depending on whether there is text to clear.
        menu.findItem(CLEAR_ID).setVisible(mEditor.getText().length() > 0);
        menu.findItem(CLEAR_ID).setVisible(blueApparaten.getText().length() > 0);
        return true;
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case BACK_ID:
            finish();
            return true;
        case CLEAR_ID:
            mEditor.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

  
    public void pairedDevice(final String address)
	{
		this.runOnUiThread(new Runnable()
		{
			public void run()
			{
				pairedData.add(address);
    			pairedAdapters.notifyDataSetChanged();
			}
		});
	}
	public void displayMessage(final String message)
	{
		this.runOnUiThread(new Runnable()
		{
			public void run()
			{
				//TextView textView = (TextView) findViewById(R.id.receivedMsg);
				//textView.setText(message);
				Log.d("StayC","Display message.\n");
			}
		});		
	}	
	public void exit()
	{
		finish();
	}
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        final String action = intent.getAction();
	        btAdapter = BluetoothAdapter.getDefaultAdapter();

	        if (BluetoothDevice.ACTION_FOUND.equals(action))
	        {
	            // Get the BluetoothDevice object from the Intent
	            final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            final String name = device.getName();
	            
	            Log.d("Bluetooth", "Found: " + name);
	            if(foundDevices!=null&&name!=null){
	              if(name.equals("stayc") && !foundDevices.contains(device)){
	                  foundDevices.add(device);
	              	  Log.d("StayC","Slaat naam device op. " +device +" \n");
	              }
	            }
	            		            
	        }	        
	        else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
	        {	        	
	        	Log.i("Bluetooth", "Discovery finished");
	        	
	        	if (foundDevices.isEmpty()) 
	        	{
	        		btAdapter.startDiscovery(); 
	        	}
	        	else
	        	{        	
		        	for (BluetoothDevice dev : foundDevices)
		        	{
		        		Log.d("StayC","gets to loop trough devices.\n" + dev);
		        		btService.connect(dev);
		        	}
		        	foundDevices.clear();
		        	myAdapter.startDiscovery();	
	        	}
	        }
	    }
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_ENABLE_BT && resultCode != RESULT_OK)
		{
			Log.e("Bluetooth", "Request to enable BT has been denied.");
			finish();
		}
		if(REQUEST_DISCOVERABLE == requestCode && resultCode == RESULT_CANCELED)
		{
			Log.e("Bluetooth", "Request to activate discoverability has been denied.");
			finish();
		}
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		unregisterReceiver(mReceiver);
		
		btService.stop();	
	}
}
