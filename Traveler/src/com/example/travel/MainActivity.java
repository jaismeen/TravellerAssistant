package com.example.travel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TabHost tabHost=getTabHost();
		
		// Tab for Photos
        TabSpec searchspec = tabHost.newTabSpec("Search");
        // setting Title and Icon for the Tab
        searchspec.setIndicator("Search", getResources().getDrawable(R.drawable.search));
        Intent photosIntent = new Intent(this, SearchActivity.class);
        searchspec.setContent(photosIntent);
 
       
        TabSpec dealsspec = tabHost.newTabSpec("Special Deals");
        dealsspec.setIndicator("Special Deals", getResources().getDrawable(R.drawable.specialdeals));
        Intent songsIntent = new Intent(this, SpecialDealsActivity.class);
        dealsspec.setContent(songsIntent);
 
       
        TabSpec weatherspec = tabHost.newTabSpec("Weather");
        weatherspec.setIndicator("Weather", getResources().getDrawable(R.drawable.weather));
        Intent videosIntent = new Intent(this, WeatherActivity.class);
        weatherspec.setContent(videosIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(dealsspec);
        tabHost.addTab(searchspec); 
        tabHost.addTab(weatherspec); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
