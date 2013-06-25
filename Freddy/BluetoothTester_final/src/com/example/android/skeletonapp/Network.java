package com.example.android.skeletonapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
//http://stackoverflow.com/questions/3031796/disconnect-a-bluetooth-socket-in-android
public class Network 
{
	private final BluetoothAdapter btAdapter;
	private final BluetoothInterface btInterface;
	BluetoothServerSocket btServerSocket = null;
	
	private BluetoothDevice parent;
	private BluetoothDevice child;
	public InputStream tmpIn;
	public OutputStream tmpOut;
	
	private final Map<String, BluetoothSocket> sockets = new HashMap<String, BluetoothSocket>();
	private final Map<String, BluetoothSocket> redundantSockets = new HashMap<String, BluetoothSocket>();
	
	private Map<String, Connection> connections = new HashMap<String, Connection>(); 
	private Map<String, Float> timings = new HashMap<String, Float>();
	
	private Thread acceptThread = null;	
	
	private final UUID uuid = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	public Network(BluetoothInterface ie)
	{
		btInterface = ie;
		btAdapter = BluetoothAdapter.getDefaultAdapter();		
	}
	
	public synchronized void start()
	{
		Log.d("Bluetooth", "Service started");
		          
        acceptThread = new Thread(new AcceptThread());
        acceptThread.start();        	
	}
	
	public synchronized void stop()
	{
		/*
		if (connectThread != null) 
        {
			connectThread.cancel();
            connectThread = null;
        }*/
		
        if (connections.isEmpty()) 
        {
            for (Connection con : connections.values())
            {
            	con.connectedThread.interrupt();
            	con = null;
            }            	
        }
        if (acceptThread != null) 
        {
          acceptThread.interrupt();
          acceptThread = null;
        }		
        
        closeSockets();
	}
	private void resetConnection()
	{
		if(tmpIn != null){
			try{tmpIn.close();}catch (Exception e) {}
			tmpIn = null;
		}
		if(tmpOut != null){
			try{tmpOut.close();}catch(Exception e) {}
			tmpOut = null;
		}
		
		for(BluetoothSocket btSocket : sockets.values())
		{
			try{ btSocket.close(); }
			catch(IOException e){ Log.e("Bluetooth", e.getMessage()); }
		}
		for(BluetoothSocket btSocket : redundantSockets.values())
		{
			try{ btSocket.close(); }
			catch(IOException e){ Log.e("Bluetooth", e.getMessage()); }
		}
		
		
		
	}
	
	private void closeSockets()
	{
		try{ btServerSocket.close(); }
		catch(IOException e){ Log.e("Bluetooth", e.getMessage()); }
		
		for(BluetoothSocket btSocket : sockets.values())
		{
			try{ btSocket.close(); }
			catch(IOException e){ Log.e("Bluetooth", e.getMessage()); }
		}
		for(BluetoothSocket btSocket : redundantSockets.values())
		{
			try{ btSocket.close(); }
			catch(IOException e){ Log.e("Bluetooth", e.getMessage()); }
		}
	}
	
	public void connect(BluetoothDevice device)
	{   
    	btAdapter.cancelDiscovery();
    	resetConnection();
    	BluetoothDevice remDevice = btAdapter.getRemoteDevice(device.getAddress());
    	BluetoothSocket btSocket = null;
    	try
    	{
			btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
		}
    	catch (IOException e)
		{
			Log.e("Bluetooth", e.getMessage());
		}
    	
    	if(btSocket != null)
    	{
    		try
    		{
				btSocket.connect();
			}
    		catch (IOException e)
    		{
    			Log.d("StayC","faalt in connect.\n");
    			e.printStackTrace();
    			try{ btSocket.close(); }
    			catch(IOException e1){ 
    				
    				Log.e("Bluetooth", e1.getMessage()); }
    			
    			Log.e("Bluetooth", e.getMessage());
			}
    		
    		if(btSocket.isConnected())
    		{
    			final String address = device.getAddress();
    			Log.d("StayC", "Connected DJECKO.\n");	        			
				if( !sockets.containsKey(address) )
				{	
					Log.d("StayC","Socket is connected + add connection.\n");
					connections.put(address, new Connection(btSocket));
					sockets.put(address, btSocket);
					
					//btInterface.pairedDevice(address);				
				}
			}		            			
    	}
    	else
    	{
    		Log.e("Bluetooth", "Failed to get socket.");
    	}        	
	}	
	
	public void broadcastMessage(String message)
	{
		Log.d("Bluetooth", "Active connections: " + connections.size());
		for (Connection con : connections.values())
        {			
			con.write(message);
        }
	}
	
	private class AcceptThread implements Runnable
	{	
		public void run()
		{		
			try
			{
				btServerSocket = btAdapter.listenUsingInsecureRfcommWithServiceRecord("THUNDER", uuid);
			}
			catch (IOException e)
			{
				Log.e("Bluetooth", e.getMessage());
				btInterface.exit();
			}
			
			while( !Thread.currentThread().isInterrupted() )
			{
				BluetoothSocket btSocket = null;
				
				Log.i("Bluetooth", "Accepting sheezy my neezy");
				try{ btSocket = btServerSocket.accept(); }
				catch(IOException e){ Log.e("Bluetooth", e.getMessage()); }
				
				if(btSocket != null)
				{
					Log.d("StayC", "CONNECTED GEEZY");
					//komt hier, error hierna misschien door close.
					
					
					final BluetoothDevice remoteDevice = btSocket.getRemoteDevice();
					final String address = remoteDevice.getAddress();
					
					if( !sockets.containsKey(address) )
					{	
						Log.d("StayC","Does not contain key.\n");
						//connections.put(address, new Connection(btSocket));				
						
						sockets.put(address, btSocket);				
						//btInterface.pairedDevice(address);						
					}
					else if( !redundantSockets.containsKey(address))
					{
						Log.d("StayC","Does contain key. Redundant\n");
						redundantSockets.put(address, btSocket);
					}
					try {
						btServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}						
		}
	}

	private class Connection 
	{
		private BluetoothSocket socket;
		
		private final InputStream inStream;
	    private final OutputStream outStream;
	 
	    Thread connectedThread;
	    
		public Connection(BluetoothSocket s)
		{	
			Log.d("StayC","Start nieuwe connection.\n");
			socket = s;	
			
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			
			try 
			{
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	            Log.d("StayC","input en output stream gelukt");
	        }
			catch (IOException e) 
			{ 
				Log.e("Bluetooth", "Failed to create i/o streams.", e);
				btInterface.exit();
			}			
			
			inStream = tmpIn;
			outStream = tmpOut;
			
			connectedThread = new Thread(new ConnectionThread());
			connectedThread.start();
		}
		
		public void write(String s) 
	    {
			byte[] bytes = null;
			
			try { bytes = s.getBytes("UTF-8"); } 
			catch (UnsupportedEncodingException e) { e.printStackTrace(); }
			
	        try { outStream.write(bytes); } catch (IOException e) {}
	    }
		
		private class ConnectionThread implements Runnable
		{
			public void run() 
			{
				Log.d("StayC","Start connectionthread.\n");
				byte[] buffer = new byte[1024];
		        int bytes;
		        
				while( !Thread.currentThread().isInterrupted() )
				{
		            try { bytes = inStream.read(buffer); } catch (IOException e) { break; }
		            
		            if (bytes > 0)
		            {
		            	String text = null;
		            	try { text = new String(buffer, "UTF-8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }
					
		            	btInterface.displayMessage(text);
		            }
				}
				//if(Thread.currentThread().isInterrupted()) handel dit af
			}
		}	    	    
	}
}