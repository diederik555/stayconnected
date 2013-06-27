package com.example.android.location;

import java.util.ArrayList;
import java.util.Set;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;




public class BluetoothActivity implements Runnable{
	Context mContext = null;
	public int done = 0;
	public BluetoothActivity(Context context){
      this.mContext = context;
    }
		private BluetoothAdapter btAdapter;
		public int counter = 0;
		public int MAX_DISCOVERY = 1;
		
		private ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();
	    
		
		
        
          
    
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
	        	counter++;
	        	if (foundDevices.isEmpty()) 
	        	{
	        		if(counter<MAX_DISCOVERY){
	        	      Log.d("StayC","Start discovery!.\n");
	        	      btAdapter.cancelDiscovery();
	        		  btAdapter.startDiscovery();
	        		}
	        		else{
	        			
	        			done = 1;
	        			btAdapter.cancelDiscovery();
	        		}
	        		
	        	}
	        	else
	        	{        	
		        	for (BluetoothDevice dev : foundDevices)
		        	{
		        		Log.d("StayC","gets to loop trough devices.\n" + dev);
		        		//btService.connect(dev);
		        	}
		        	foundDevices.clear();
		        	if(counter<MAX_DISCOVERY){
		        	  btAdapter.cancelDiscovery();
		        	  btAdapter.startDiscovery();
		        	  
		        	}
		        	else{
		        		
		        		done = 1;
		        		btAdapter.cancelDiscovery();
		        	}
		        	
	        	}
	        }
	    }
	};
	
	
	@Override
	public void run() {
		//super.onCreate(savedInstanceState);
        //Log.d("Test", "OnCreate");
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
		mContext.startActivity(discoverableIntent);
		//startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mContext.registerReceiver(mReceiver, filter);		
		//btService.start();		
		Log.d("StayC", "Start discovering");
		btAdapter.startDiscovery();
		
		
		while(done==0){}
		mContext.unregisterReceiver(mReceiver);
           
	
		
	}

	
}
