package com.example.hzl15104305;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ImageButton imageButton;
	//public static int  ti =100000;
	/*
	private int[] bimageId= new int []{R.drawable.back3,R.drawable.back2,R.drawable.back4};
	private int index=0;
	private LinearLayout backLayout;*/
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageButton)findViewById(R.id.city_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    	/*
        backLayout = (LinearLayout)findViewById(R.layout.activity_main);
		backLayout.setBackgroundResource(bimageId[index]);*/
			
        

    	
    		
    	
        
        //当天
        lowText = (TextView)findViewById(R.id.lowest_text);
        nowText = (TextView)findViewById(R.id.now_text);
        highText = (TextView)findViewById(R.id.highest_text);
        infoLeft = (TextView)findViewById(R.id.info_left_text);
        infoRight = (TextView)findViewById(R.id.info_right);
        nowView = (ImageView)findViewById(R.id.now_img);
        //后四天天气
        firstImg = (ImageView)findViewById(R.id.first_img);
        firstWeather = (TextView)findViewById(R.id.first_weather);
        firstDate = (TextView)findViewById(R.id.first_date);
        secondImg = (ImageView)findViewById(R.id.second_img);
        secondWeather = (TextView)findViewById(R.id.second_weather);
        secondDate = (TextView)findViewById(R.id.second_date);
        thirdImg = (ImageView)findViewById(R.id.third_img);
        thirdWeather = (TextView)findViewById(R.id.third_weather);
        thirdDate = (TextView)findViewById(R.id.third_date);
        fourthImg = (ImageView)findViewById(R.id.fourth_img);
        fourthWeather = (TextView)findViewById(R.id.fourth_weather);
        fourthDate = (TextView)findViewById(R.id.fourth_date);
        
        Getweather getweather = new Getweather();
        getweather.execute(url, defaultName);
        
        IntentFilter inf = new IntentFilter();
        inf.addAction("com.weather.refresh");
        registerReceiver(broadcastReceiver, inf);

        startService(new Intent(this, RefreshService.class));
        

    	
    }
    
    
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Toast.makeText(this, bundle.getString("city"), Toast.LENGTH_LONG).show();
        }
    }
    */
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String cityname = bundle.getString("cityname");
            String[] params = cityname.split(" ");
            defaultName = params[params.length - 1];
        }
    }
    
    private String url = "http://api.jisuapi.com/weather/query?appkey=d78894d8c767ddfa&city=";
	  //需要更新内容的控件
	private TextView lowText, nowText, highText;
	private TextView infoLeft, infoRight;
	private ImageView nowView;
	private ImageView firstImg, secondImg, thirdImg, fourthImg;
	private TextView firstWeather, firstDate, secondWeather, secondDate,
	          thirdWeather, thirdDate, fourthWeather, fourthDate;
	private String defaultName = "天津";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    //访问网络的内部类
    private class Getweather extends AsyncTask<String, String, String> {
    	/*
      @Override
      protected String doInBackground(String... params) {
          return null;
      }*/
      
      private String openConnection(String address, String cityId){
          String result = "";
          try{
      URL url = new URL(address + URLEncoder.encode(cityId, "utf-8"));
              HttpURLConnection connection = (HttpURLConnection)url.openConnection();
              connection.setRequestMethod("GET");
              connection.connect();
              InputStream in = connection.getInputStream();
              BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
              String line = "";
              while ((line = reader.readLine()) != null) {
                  result = result + line;
              }
          }catch(Exception e) {
              e.printStackTrace();
          }
           //Log.i("info", result);
      return result;
      }

      @Override
      protected String doInBackground(String... params) {
          return openConnection(params[0], params[1]);
      }

      
      @Override
      protected void onPostExecute(String result){
          try{
              JSONObject object = new JSONObject(result);
              JSONObject today = (JSONObject) object.get("result");
      //获取当天天气
              highText.setText(today.getString("temphigh"));
              lowText.setText(today.getString("templow"));
              nowText.setText(today.getString("temp")+"\u2103");
             infoLeft.setText(today.getString("city") + "\n\n" + today.getString("week") + "\n\n" + today.getString("weather"));
             infoRight.setText(today.getString("date") + "\n\n" + "湿度"+today.getString("humidity") + "\n\n" +today.getString("pressure"));
             Log.i("infotest",today.getString("date") + "\n\n" +today.getString("winddirect") + "\n\n" + today.getString("windpower"));
             Log.i("infotest",today.getString("city") + "\n\n" + today.getString("week") + "\n\n" + today.getString("weather"));
              if(today.getString("weather").equals("晴"))
              {
            	  nowView.setImageResource(R.drawable.sun) ;          	  
              }
              else if(today.getString("weather").equals("多云")||today.getString("weather").equals("阴"))
              {
            	  nowView.setImageResource(R.drawable.cloud);           	  
              }
              else 
              {
            	  nowView.setImageResource(R.drawable.rain);          	  
              }

            	  
             //获取后四天的预告
              JSONArray forecast = (JSONArray) today.get("daily");
              firstWeather.setText((((JSONObject) forecast.get(0)).getJSONObject("day")).getString("weather") + "\n\n" +
              ((((JSONObject) forecast.get(0)).getJSONObject("day")).getString("temphigh") + "/" + ((((JSONObject) forecast.get(0)).getJSONObject("night")).getString("templow"))));

      secondWeather.setText((((JSONObject) forecast.get(1)).getJSONObject("day")).getString("weather") + "\n\n" +
              ((((JSONObject) forecast.get(1)).getJSONObject("day")).getString("temphigh") + "/" + ((((JSONObject) forecast.get(1)).getJSONObject("night")).getString("templow"))));

      thirdWeather.setText((((JSONObject) forecast.get(2)).getJSONObject("day")).getString("weather") + "\n\n" +
              ((((JSONObject) forecast.get(2)).getJSONObject("day")).getString("temphigh") + "/" + ((((JSONObject) forecast.get(2)).getJSONObject("night")).getString("templow"))));

      fourthWeather.setText((((JSONObject) forecast.get(3)).getJSONObject("day")).getString("weather") + "\n\n" +
              ((((JSONObject) forecast.get(3)).getJSONObject("day")).getString("temphigh") + "/" + ((((JSONObject) forecast.get(3)).getJSONObject("night")).getString("templow"))));

              firstDate.setText(((JSONObject) forecast.get(1)).getString("date"));
      secondDate.setText(((JSONObject) forecast.get(2)).getString("date"));
      thirdDate.setText(((JSONObject) forecast.get(3)).getString("date"));
      fourthDate.setText(((JSONObject) forecast.get(4)).getString("date")); 
      
      
      if((((JSONObject) forecast.get(0)).getJSONObject("day")).getString("weather").equals("晴"))
      {
    	 firstImg.setImageResource(R.drawable.sun) ;          	  
      }
      else if((((JSONObject) forecast.get(0)).getJSONObject("day")).getString("weather").equals("多云")||(((JSONObject) forecast.get(0)).getJSONObject("day")).getString("weather").equals("阴"))
      {
    	  firstImg.setImageResource(R.drawable.cloud);           	  
      }
      else
      {
    	  firstImg.setImageResource(R.drawable.rain);          	  
      }
      
      
      
      if((((JSONObject) forecast.get(1)).getJSONObject("day")).getString("weather").equals("晴"))
      {
    	 secondImg.setImageResource(R.drawable.sun) ;          	  
      }
      else if((((JSONObject) forecast.get(1)).getJSONObject("day")).getString("weather").equals("多云")||(((JSONObject) forecast.get(1)).getJSONObject("day")).getString("weather").equals("阴"))
      {
    	  secondImg.setImageResource(R.drawable.cloud);           	  
      }
      else
      {
    	  secondImg.setImageResource(R.drawable.rain);          	  
      }
      
 
      
      if((((JSONObject) forecast.get(2)).getJSONObject("day")).getString("weather").equals("晴"))
      {
    	 thirdImg.setImageResource(R.drawable.sun) ;          	  
      }
      else if((((JSONObject) forecast.get(2)).getJSONObject("day")).getString("weather").equals("多云")||(((JSONObject) forecast.get(2)).getJSONObject("day")).getString("weather").equals("阴"))
      {
    	  thirdImg.setImageResource(R.drawable.cloud);           	  
      }
      else
      {
    	  thirdImg.setImageResource(R.drawable.rain);          	  
      }
      
      
      if((((JSONObject) forecast.get(3)).getJSONObject("day")).getString("weather").equals("晴"))
      {
    	 fourthImg.setImageResource(R.drawable.sun) ;          	  
      }
      else if((((JSONObject) forecast.get(3)).getJSONObject("day")).getString("weather").equals("多云")||(((JSONObject) forecast.get(3)).getJSONObject("day")).getString("weather").equals("阴"))
      {
    	  fourthImg.setImageResource(R.drawable.cloud);           	  
      }
      else
      {
    	  fourthImg.setImageResource(R.drawable.rain);          	  
      }
      
      
      }catch(Exception e){
              e.printStackTrace();
          }
      
      
    }
   }

/*
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("com.weather.refresh")){
                Toast.makeText(getApplication(),"refresh",Toast.LENGTH_LONG).show();
                Getweather getweather = new Getweather();
                getweather.execute(url, defaultName);
            }
        }
    };
    */
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.weather.refresh")) {
              //  Toast.makeText(getApplication(), "refresh", Toast.LENGTH_LONG).show();
                Getweather getweather = new Getweather();
                getweather.execute(url,defaultName);
            }
        }
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, RefreshService.class));
    	unregisterReceiver(broadcastReceiver);

    }

}


