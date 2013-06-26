package com.example.android.location;

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
import android.widget.EditText;



public class BluetoothActivity extends Activity{
	private BluetoothAdapter btAdapter;
	private ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        //btService = new Network(this);
        
        // Inflate our UI from its XML layout description.
  
        Log.d("Test", "OnCreate");
     
        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        
        // Hook up button presses to the appropriate event handler.
        
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
        	Log.d("StayC","Geen bluetoothAdapter!\n");
        }       
     
        if (!btAdapter.isEnabled()) {
        	btAdapter.enable();
        }
        //String oldName = myAdapter.getName();
        //na afsluiten herstel naam naar oldName
        String newName = "stayc!";
        Log.d("StayC","Adapter naam is " +btAdapter.getName());
    	btAdapter.setName(newName);
    	Log.d("StayC","Adapter naam wordt " +btAdapter.getName());
    	
    	Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
        if(devices.size()>0){
          for (BluetoothDevice device : devices) {
        	  foundDevices.add(device);
        	  Log.d("StayC", "Paired device found " + device);
          }
        }
        	
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
	    startActivity(discoverableIntent);
		//startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);		
		//btService.start();		
		Log.d("StayC", "Start discovering");
		btAdapter.startDiscovery();		     
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
	              if(!foundDevices.contains(device)){
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
		        		//btService.connect(dev);
		        	}
		        	foundDevices.clear();
		        	btAdapter.startDiscovery();	
	        	}
	        }
	    }
	};
	
	
	
	
	
}