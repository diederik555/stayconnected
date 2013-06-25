package com.example.android.skeletonapp;

public interface BluetoothInterface
{
	public void pairedDevice(String address);
	public void displayMessage(String message);
	
	public void exit();
}