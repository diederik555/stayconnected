package com.example.android.skeletonapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.util.Log;
import android.widget.EditText;

//use a single BluetoothSocket to both initiate an outgoing connection 
//and to manage the connection
public class client implements Runnable{
	protected SkeletonActivity main;
	private EditText mEditor;
	public EditText blueApparaten;
	static BluetoothAdapter myAdapter;
	private Context context;

	static BluetoothSocket sock_client;
	private BluetoothDevice myDevice;
	static InputStream in;
	static OutputStream out;
	public static UUID MY_UUID =
			UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	int stop;
	public client(Context context){
		this.context = context;
	}
	public void begin() {
		
	}
	
    public void connect_to_server(String addres) {
    	Log.d("Debug","Comes to connect_to_server.\n");
    	myAdapter = BluetoothAdapter.getDefaultAdapter();
		try {
			
			myAdapter.cancelDiscovery();
			String host = "A0:F4:50:CB:0A:43";
			myDevice = myAdapter.getRemoteDevice(host);
			/*Method m = myDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	        BluetoothSocket tmp = null;
            */
			BluetoothSocket tmp = null; 
			tmp = myDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
			sock_client = tmp;
			//Parcel testinguuid = myDevice.g
			sock_client.connect();
			
			//in = sock_client.getInputStream();
			//out = sock_client.getOutputStream();
			//Log.d("StayConnected_01","Connectie streams \n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Debug",e.getMessage());
			try {
				sock_client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(sock_client.isConnected()){
			Log.d("Debug","Device is connected.\n");
	    }
		
	}
	@Override
	public void run() {
		Log.d("Debug","Comes to connect_to_server.\n");
    	myAdapter = BluetoothAdapter.getDefaultAdapter();
		try {
			String addr = myAdapter.getAddress();
			myAdapter.cancelDiscovery();
			myDevice = myAdapter.getRemoteDevice(addr);
			/*Method m = myDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	        BluetoothSocket tmp = null;
            */
			BluetoothSocket tmp = myDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
			sock_client = tmp;
			//Parcel testinguuid = myDevice.g
			sock_client.connect();
			Log.d("Debug","Device is connected.\n");
			//in = sock_client.getInputStream();
			//out = sock_client.getOutputStream();
			//Log.d("StayConnected_01","Connectie streams \n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Debug",e.getMessage());
			try {
				sock_client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}	
	}
}