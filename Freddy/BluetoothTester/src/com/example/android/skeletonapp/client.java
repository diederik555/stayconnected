package com.example.android.skeletonapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.EditText;

//use a single BluetoothSocket to both initiate an outgoing connection 
//and to manage the connection
public class client implements Runnable{
	protected SkeletonActivity main;
	private EditText mEditor;
	public EditText blueApparaten;
	public BluetoothAdapter myAdapter;
	private Context context;

	private BluetoothSocket sock_client;
	private BluetoothDevice myDevice;
	private InputStream in;
	private OutputStream out;
	private UUID MY_UUID =
			UUID.fromString("4fdabc30-cf4e-11e2-8b8b-0800200c9a66");
	int stop;
	public client(Context context){
		this.context = context;
	}
	public void begin() {
		
	}
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
            }
        }
    };
	
    public void connect_to_server() {
    	Log.d("Debug","Comes to connect_to_server.\n");
		try {
			String addr = myAdapter.getAddress();
			myDevice = myAdapter.getRemoteDevice(addr);
			/*Method m = myDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	        BluetoothSocket tmp = null;
			try {
				tmp = (BluetoothSocket) m.invoke(myDevice, 1);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			BluetoothSocket tmp = myDevice.createRfcommSocketToServiceRecord(MY_UUID);
			sock_client = tmp;
			sock_client.connect();
			Log.d("Debug","Device is connected.\n");
			in = sock_client.getInputStream();
			out = sock_client.getOutputStream();
			Log.d("StayConnected_01","Connectie streams \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		stop = 0;
		Log.d("Debug","Network begin method\n");
		myAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myAdapter == null) {
        	mEditor.append("\nBluetooth wordt niet gesupport. Functionaliteit wordt minder.\n");
        } 
        if (!myAdapter.isEnabled()) {
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        	myAdapter.enable();
        }
        if (myAdapter.isDiscovering()) {
            myAdapter.cancelDiscovery();
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
        
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
        
        //wordt zelf waarneembaar
        Intent discoverableIntent = new
        Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //wordt voor een verlengde tijd waarneembaar
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        context.startActivity(discoverableIntent);
        context.unregisterReceiver(mReceiver);
        myAdapter.cancelDiscovery();
        connect_to_server();
		
	}
	
	


	
	
	
}