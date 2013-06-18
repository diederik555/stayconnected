package com.example.android.location;

public class Comment {
  private long id;
  private String location, address;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getComment() {
    return location;
  }

  public void setComment(String location) {
    this.location = location;
  }
  
  public String getAddress() {
	  return address;
  }
  
  public void setAdress(String address) {
	  this.address = address;
  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
	String comment = "*(" + location + ");(Address[" + address + "])*";
    return comment;
  }
} 