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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
import android.app.Activity;
import android.os.Bundle;
//import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
//import android.content.BroadcastReceiver;
import android.util.Log;



/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class SkeletonActivity extends Activity {
    
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;
    static final public int REQUEST_ENABLE_BT = 0;
    private EditText mEditor;
    private EditText blueApparaten;
    public static BluetoothAdapter myAdapter;
    public static ArrayList<String> Adresses = new ArrayList<String>();
    public client clientThread;
    public SkeletonActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.skeleton_activity);
        Log.d("Test", "OnCreate");
     
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        mEditor = (EditText) findViewById(R.id.editor);
        blueApparaten = (EditText) findViewById(R.id.blue);
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener);
        ((Button) findViewById(R.id.clear)).setOnClickListener(mClearListener);
        myAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myAdapter == null) {
        	mEditor.append("\nBluetooth wordt niet gesupport. Functionaliteit wordt minder.\n");
        } 
        //StayC wordt toegevoegd aan de bluetooth naam
        //String deviceName = myAdapter.getName();
        //deviceName = deviceName + "StayC";
        //myAdapter.setName(deviceName);
        //als app afsluit haal StayC van de naam af, nog te doen
        if (!myAdapter.isEnabled()) {
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        	myAdapter.enable();
        }

        myAdapter.startDiscovery();
        Set<BluetoothDevice> devices = myAdapter.getBondedDevices();
        if(devices.size()>0){
          for (BluetoothDevice device : devices) {
        	  //sla hier de device namen op, nog te doen
        	  //blueApparaten.append("\n Paired devices found:"+device.getName() + " " + device.getAddress());
        	  Log.d("Test", "\n Paired devices found:"+device.getName() + " " + device.getAddress());
        	  
          }
          
        }
        else{
        	//blueApparaten.append("\nNo paired devices found.");
        	Log.d("Test", "No paired devices found\n");
        }
        //zoek andere apparaten
        Thread networkComponent = new Thread(new Network(this));
    	if(networkComponent != null) {
    		networkComponent.start();
    		Log.d("Debug","Gets to run network\n");
    	}
    	clientThread = new client(this);
    	if(clientThread != null) {
    		//clientThread.start();
    		Log.d("Debug","Gets to run client\n");
    	}
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        
        //wordt zelf waarneembaar
        Intent discoverableIntent = new
        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //wordt voor een verlengde tijd waarneembaar
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);
        //context.unregisterReceiver(mReceiver);
        
        
    	
        mEditor.setText(getText(R.string.main_label));
        
        String my = myAdapter.getAddress();
        Log.d("Debug","My own address is " +my + ".\n");
        //blueApparaten.setText("SOMETHiNG\n");
        //blueApparaten.setText(getText(R.string.main_label));
        
    }
    

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Called when your activity's options menu needs to be created.
     */
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

    /**
     * A call-back for when the user presses the back button.
     */
    OnClickListener mBackListener = new OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * A call-back for when the user presses the clear button.
     */
    OnClickListener mClearListener = new OnClickListener() {
        public void onClick(View v) {
            mEditor.setText("");
        }
    };
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
    	@Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //blueApparaten.append("\n Device found " + device.getName() + " " + device.getAddress());
                Log.d("Debug","\n Server found Device " + device.getName() + " " + device.getAddress());
                Adresses.add(device.getAddress());
                String value = device.getAddress();
                myAdapter.cancelDiscovery();
                Log.d("Debug","Cancel discovery");
                for (String adress : Adresses)
	        	{
	        		clientThread.connect_to_server(adress);
	        	}		 
                
                
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            	myAdapter = BluetoothAdapter.getDefaultAdapter();
            	Log.d("Debug","Start discovery again.\n");
            	myAdapter.startDiscovery();
            }
        }
    }; 
}
