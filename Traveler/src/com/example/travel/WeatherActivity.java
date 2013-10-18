package com.example.travel;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class WeatherActivity extends Activity {
 
 
 final String yahooPlaceApisBase = "http://query.yahooapis.com/v1/public/yql?q=select*from%20geo.places%20where%20text=";
 final String yahooapisFormat = "&format=xml";
 String yahooPlaceAPIsQuery;

 EditText place;
 Button search;
 ListView listviewWOEID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weathermain);
        
        place = (EditText)findViewById(R.id.place);
        search = (Button)findViewById(R.id.search);
        listviewWOEID = (ListView)findViewById(R.id.woeidlist);

        search.setOnClickListener(searchOnClickListener);
    }
    
    Button.OnClickListener searchOnClickListener
    = new Button.OnClickListener(){

  @Override
  public void onClick(View arg0) {
   if(place.getText().toString().equals("")){
    Toast.makeText(getBaseContext(),
      "Enter place!",
      Toast.LENGTH_LONG).show();
   }else{
    new MyQueryYahooPlaceTask().execute();
   }
  }
    };
    
    private class MyQueryYahooPlaceTask extends AsyncTask<Void, Void, Void>{

     String l;
     
  @Override
  protected Void doInBackground(Void... arg0) {
   l = QueryYahooPlaceAPIs();
   return null;
  }

  @Override
  protected void onPostExecute(Void result) {
  
     
     String selWoeid = l.toString();
    
     
     Intent intent = new Intent();
              intent.setClass(WeatherActivity.this, GetWeather.class);
              Bundle bundle = new Bundle();
              bundle.putCharSequence("SEL_WOEID", selWoeid);
              intent.putExtras(bundle);
              startActivity(intent);
    
   
   super.onPostExecute(result);
  }
     
    }
    
    private String QueryYahooPlaceAPIs(){
     String uriPlace = Uri.encode(place.getText().toString());
     
     yahooPlaceAPIsQuery = yahooPlaceApisBase
       + "%22" + uriPlace + "%22"
       + yahooapisFormat;
     
     String woeidString = QueryYahooWeather(yahooPlaceAPIsQuery);
     Document woeidDoc = convertStringToDocument(woeidString);
     return  parseWOEID(woeidDoc);
    }
    
    private String parseWOEID(Document srcDoc){
     String listWOEID= new String();
     
     NodeList nodeListDescription = srcDoc.getElementsByTagName("woeid");
     listWOEID=(nodeListDescription.item(0).getTextContent());
     
     
     return listWOEID;
    }
    
    private Document convertStringToDocument(String src){
     Document dest = null;
     
     DocumentBuilderFactory dbFactory =
       DocumentBuilderFactory.newInstance();
     DocumentBuilder parser;
     
     try {
      parser = dbFactory.newDocumentBuilder();
      dest = parser.parse(new ByteArrayInputStream(src.getBytes())); 
     } catch (ParserConfigurationException e1) {
      e1.printStackTrace(); 
     } catch (SAXException e) {
      e.printStackTrace(); 
     } catch (IOException e) {
      e.printStackTrace(); 
     }
     
     return dest; 
    }
    
    private String QueryYahooWeather(String queryString){
     String qResult = "";
     
     HttpClient httpClient = new DefaultHttpClient();
     HttpGet httpGet = new HttpGet(queryString);
     
     try {
      HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
      
      if (httpEntity != null){
       InputStream inputStream = httpEntity.getContent();
       Reader in = new InputStreamReader(inputStream);
       BufferedReader bufferedreader = new BufferedReader(in);
       StringBuilder stringBuilder = new StringBuilder();
       
       String stringReadLine = null;
       
       while ((stringReadLine = bufferedreader.readLine()) != null) {
        stringBuilder.append(stringReadLine + "\n"); 
       }
       
       qResult = stringBuilder.toString(); 
      } 
     } catch (ClientProtocolException e) {
      e.printStackTrace();; 
     } catch (IOException e) {
      e.printStackTrace(); 
     }
     
     return qResult; 
    }

}