<?php

	#Setup variables
	$location = $_POST["Location"];
	$account = $_POST["Account"];

	#Avoid SQL injection attacks
	//$firstname = mysql_real_escape_string($firstname);

	#Connect to Database
	$con = mysql_connect("mysql11-int.cp.hostnet.nl","u30984_fhd","stayconnected", "db30984_stayc");
	if (!$con)
	{
		die('Could not connect: ' . mysql_error());
	}

	#Select the test database
	mysql_select_db("db30984_stayc", $con);

	#Get the user details from the database
	$userdetails = mysql_query("INSERT INTO testdatabase (user, location) VALUES ('$account', '$location')");

	#Catch any errors
	if (!$userdetails) {
		echo 'Could not run query: ' . mysql_error();
		exit;
	}


	#Get the first row of the results
	//$row = mysql_fetch_row($userdetails);

	#Build the result array (Assign keys to the values)
	$result_data = "Success";

	#Output the JSON data
	echo $result_data; 

?>
