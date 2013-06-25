package com.example.android.skeletonapp;

//import java.util.Set;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
/**
import java.io.FileReader;
import java.io.FileWriter;
in plaats van bytes, char buffer gebruiken
*/




public class Network implements Runnable{
/**
 * Zet bluetooth connectie op
 * Bluetooth broadcast
 * 
 * 
 */
	protected SkeletonActivity main;
	private EditText mEditor;
	static EditText blueApparaten;
	static BluetoothAdapter myAdapter;
	private Context context;
	static BluetoothServerSocket serverSocket;
	static BluetoothSocket sock;
	static BluetoothDevice myDevice;
	private InputStream in;
	private OutputStream out;
	private int stop;
	static UUID MY_UUID =
UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	//random string
	
	
	
	public Network(Context context) {
		
		//this.main = main;
		this.context = context;
        

	}
	/**
    in vernietigen
    myAdapter.cancelDiscovery();
    unregisterReceiver(mReceiver);
    */

	public void begin(){
		
		
        
	}

	
	
	
	public void run_server() {
        Log.d("Debug","Gets to run_server.\n");
        byte [] input;
        //File outputData = new File("output.txt");
        //definieer input grootte
        input = new byte[100];
        myAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
			serverSocket = myAdapter.listenUsingInsecureRfcommWithServiceRecord(
					"StayC", MY_UUID);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//luisterd voor 100 seconden
			sock = serverSocket.accept();
			Log.d("Debug","Accepted connection.\n");
			//in = sock.getInputStream();
			//out = sock.getOutputStream();
			//out = new BufferedOutputStream(new FileOutputStream(outputData));
			serverSocket.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
			Log.d("Test", "Write to file\n");
			//slechts 1 connectie per android telefoon
			//nog uit te breiden met algoritme
			
		
		//client is hier geconnect
		//while(stop==0){
			//read buffer
			/**
			try {
				in.read(input);
				//nog te implementeren EOF herkennen
				/**
				 * if(EOF)
				 * in.close
				 * out.close
				 * sock.close
				 * in = NULL;
				 * out = NULL;
				 
				//out.write(input);
				//output definieren
				//out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		//} 
	}
    
	
	public void kill_server(){
	   stop = 1;	
	   try {
		 sock.close();
	   }
	   catch (IOException e) {
	   	 // TODO Auto-generated catch block
		 e.printStackTrace();
	   }
	}


	@Override
	public void run() {
	       Log.d("Debug","Gets to run_server.\n");
	        byte [] input;
	        //File outputData = new File("output.txt");
	        //definieer input grootte
	        input = new byte[100];
	        myAdapter = BluetoothAdapter.getDefaultAdapter();
	        try {
				serverSocket = myAdapter.listenUsingInsecureRfcommWithServiceRecord(
						"StayC", MY_UUID);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				//luisterd voor 100 seconden
				sock = serverSocket.accept();
				Log.d("Debug","Accepted connection.\n");
				//in = sock.getInputStream();
				//out = sock.getOutputStream();
				//out = new BufferedOutputStream(new FileOutputStream(outputData));
				serverSocket.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				
				Log.d("Test", "Write to file\n");
				//slechts 1 connectie per android telefoon
				//nog uit te breiden met algoritme
				
			
			//client is hier geconnect
			//while(stop==0){
				//read buffer
				/**
				try {
					in.read(input);
					//nog te implementeren EOF herkennen
					/**
					 * if(EOF)
					 * in.close
					 * out.close
					 * sock.close
					 * in = NULL;
					 * out = NULL;
					 
					//out.write(input);
					//output definieren
					//out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			//} 
	}
	
}