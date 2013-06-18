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
	public EditText blueApparaten;
	public BluetoothAdapter myAdapter;
	private Context context;
	private BluetoothServerSocket serverSocket;
	private BluetoothSocket sock;
	private BluetoothDevice myDevice;
	private InputStream in;
	private OutputStream out;
	private int stop;
	private UUID MY_UUID =
UUID.fromString("4fdabc30-cf4e-11e2-8b8b-0800200c9a66");
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
	
	public void run_server() {
        Log.d("Debug","Gets to run_server.\n");
        byte [] input;
        //File outputData = new File("output.txt");
        //definieer input grootte
        input = new byte[100];
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
			in = sock.getInputStream();
			out = sock.getOutputStream();
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
		// TODO Auto-generated method stub
		stop = 0;
		Log.d("Debug","Network begin method\n");
		myAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myAdapter == null) {
        	mEditor.append("\nBluetooth wordt niet gesupport. Functionaliteit wordt minder.\n");
        } 
        //StayC wordt toegevoegd aan de bluetooth naam
        String deviceName = myAdapter.getName();
        deviceName = deviceName + "StayC";
        myAdapter.setName(deviceName);
        //als app afsluit haal StayC van de naam af, nog te doen
        if (!myAdapter.isEnabled()) {
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        	myAdapter.enable();
        }
        while(myAdapter.isDiscovering()) {
        }
        myAdapter.cancelDiscovery();
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
        run_server();
	}
	
}