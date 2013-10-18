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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;


public class GetWeather extends Activity {
 
 TextView weather;
 
 class MyWeather{
  
  String description;
  String city;
  String region;
  String country;

  String sunrise;
  String sunset;

  String conditiontext;
  String conditiondate;
  
  String min_temp;
  String max_temp;
  
  public String toString(){
   
   String s;
   
   s = description + " -\n\n" + "city: " + city + "\n"
     + "Region: " + region + "\n"
     + "Country: " + country + "\n\n"
     + "Temperature\n"
     + "Minimum: " + min_temp + "\n"
     + "Maximum: " + max_temp + "\n\n"
     + "Sunrise: " + sunrise + "\n"
     + "Sunset: " + sunset + "\n\n"
     + "Condition: " + conditiontext + "\n"
     + conditiondate +"\n";
   
   return s;
  }
 }

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onCreate(savedInstanceState);
  setContentView(R.layout.layout_weather);
  weather = (TextView)findViewById(R.id.weather);
  
  Bundle bundle = this.getIntent().getExtras();
        String sel_woeid = (String)bundle.getCharSequence("SEL_WOEID");
        
        new MyQueryYahooWeatherTask(sel_woeid).execute();
        

 }
 
 private class MyQueryYahooWeatherTask extends AsyncTask<Void, Void, Void>{

  String woeid;
  String weatherResult;
  String weatherString;
  
  MyQueryYahooWeatherTask(String w){
   woeid = w;
  }
  
  @Override
  protected Void doInBackground(Void... arg0) {
   weatherString = QueryYahooWeather();
   Document weatherDoc = convertStringToDocument(weatherString);
   
   if(weatherDoc != null){
    weatherResult = parseWeather(weatherDoc).toString();
   }else{
    weatherResult = "Cannot convertStringToDocument!";
   }
   
   return null;
  }

  @Override
  protected void onPostExecute(Void result) {
   weather.setText(weatherResult);
   super.onPostExecute(result);
  }
  
  private String QueryYahooWeather(){
   String qResult = "";
     String queryString = "http://weather.yahooapis.com/forecastrss?w=" + woeid;
     
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
      e.printStackTrace(); 
     } catch (IOException e) {
      e.printStackTrace(); 
     }
     return qResult;   
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
  
  private MyWeather parseWeather(Document srcDoc){
   
   MyWeather myWeather = new MyWeather();
   
   
   NodeList descNodelist = srcDoc.getElementsByTagName("description");
   if(descNodelist != null && descNodelist.getLength() > 0){
    myWeather.description = descNodelist.item(0).getTextContent();
   }else{
    myWeather.description = "EMPTY";
   }

  
   NodeList locationNodeList = srcDoc.getElementsByTagName("yweather:location");
   if(locationNodeList != null && locationNodeList.getLength() > 0){
    Node locationNode = locationNodeList.item(0);
    NamedNodeMap locNamedNodeMap = locationNode.getAttributes();
    
    myWeather.city = locNamedNodeMap.getNamedItem("city").getNodeValue().toString();
    myWeather.region = locNamedNodeMap.getNamedItem("region").getNodeValue().toString();
    myWeather.country = locNamedNodeMap.getNamedItem("country").getNodeValue().toString();
   }else{
    myWeather.city = "EMPTY";
    myWeather.region = "EMPTY";
    myWeather.country = "EMPTY";
   }
   
   NodeList tempNodeList = srcDoc.getElementsByTagName("yweather:forecast");
   if(tempNodeList != null && tempNodeList.getLength() > 0){
    Node conditionNode = tempNodeList.item(0);
    NamedNodeMap conditionNamedNodeMap = conditionNode.getAttributes();
    
    myWeather.min_temp= conditionNamedNodeMap.getNamedItem("low").getNodeValue().toString();
    myWeather.max_temp = conditionNamedNodeMap.getNamedItem("high").getNodeValue().toString();
   }else{
    myWeather.min_temp = "EMPTY";
    myWeather.max_temp = "EMPTY";
   }
  
  
   NodeList astNodeList = srcDoc.getElementsByTagName("yweather:astronomy");
   if(astNodeList != null && astNodeList.getLength() > 0){
    Node astNode = astNodeList.item(0);
    NamedNodeMap astNamedNodeMap = astNode.getAttributes();
    
    myWeather.sunrise = astNamedNodeMap.getNamedItem("sunrise").getNodeValue().toString();
    myWeather.sunset = astNamedNodeMap.getNamedItem("sunset").getNodeValue().toString();
   }else{
    myWeather.sunrise = "EMPTY";
    myWeather.sunset = "EMPTY";
   }
   
  
   NodeList conditionNodeList = srcDoc.getElementsByTagName("yweather:condition");
   if(conditionNodeList != null && conditionNodeList.getLength() > 0){
    Node conditionNode = conditionNodeList.item(0);
    NamedNodeMap conditionNamedNodeMap = conditionNode.getAttributes();
    
    myWeather.conditiontext = conditionNamedNodeMap.getNamedItem("text").getNodeValue().toString();
    myWeather.conditiondate = conditionNamedNodeMap.getNamedItem("date").getNodeValue().toString();
   }else{
    myWeather.conditiontext = "EMPTY";
    myWeather.conditiondate = "EMPTY";
   }
   
   return myWeather; 
  }
  
 }

}