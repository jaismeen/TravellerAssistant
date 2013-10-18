package com.example.travel;
import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.content.Intent;
 
public class HomePage extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_layout);
   
	
        final Handler handler = new Handler();
	
    handler.postDelayed(new Runnable() {
        public void run() {
            
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
            HomePage.this.finish();
        }
    }, 2500);
	}
    
}
