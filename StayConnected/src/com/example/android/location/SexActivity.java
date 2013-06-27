package com.example.android.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SexActivity extends Activity {


	Button btnMan;
	Button btnWoman;
	
	// Session Manager Class
	SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); 
        
        // Session Manager
        session = new SessionManager(getApplicationContext());       
  

        
        btnMan = (Button) findViewById(R.id.btnMan);
        btnWoman = (Button) findViewById(R.id.btnWoman);
        
        
        
        // Login button click event
        btnMan.setOnClickListener(myhandler1);
        btnWoman.setOnClickListener(myhandler2); 
    }
    View.OnClickListener myhandler1 = new View.OnClickListener()
    {
        public void onClick(View v)
        {
        	String sex = "man";
        	session.createLoginSession(sex);
        	Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
        }
	};
    View.OnClickListener myhandler2 = new View.OnClickListener()
	{
    	public void onClick(View v)
    	{
    		String sex = "woman";
    		session.createLoginSession(sex);
    		Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
        }
	};
}