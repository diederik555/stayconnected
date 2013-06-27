package com.example.android.location;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MatchPopup extends Activity{
	TextView textView = null;
	Button buttonok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        Intent myIntent= getIntent(); // gets the previously created intent
        String match = myIntent.getStringExtra("match");
        
        textView = (TextView)findViewById(R.id.textView2);
        textView.setText(match);
        
        buttonok = (Button) findViewById(R.id.buttonok);
        buttonok.setOnClickListener(myhandler1);
    }
    View.OnClickListener myhandler1 = new View.OnClickListener()
    {
        public void onClick(View v)
        {
        	Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
        }
	};
}