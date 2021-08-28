package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    final String APP_ID = "b53c7a1734c77c50e886aa4bda8e8e0c";
    //final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=London&appid=b53c7a1734c77c50e886aa4bda8e8e0c";
    //final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?appid=b53c7a1734c77c50e886aa4bda8e8e0c&lat=37.421998333333335&lon=-122.084";
    //final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView Location, cur_weather, temperature,  min_max, wind, humidity, pressure, lat_lon;
    ImageView weatherIcon;
    SwitchCompat darkmode;


    LocationManager wLocationManager;
    LocationListener wLocationListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        Location = findViewById(R.id.location);
        cur_weather = findViewById(R.id.cur_weather);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        wind = findViewById(R.id.wind);
        temperature = findViewById(R.id.temp);
        min_max = findViewById(R.id.min_max);
        lat_lon = findViewById(R.id.lat_lon);
        darkmode = findViewById(R.id.darkmode);




        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
            }
        });


        weatherIcon = findViewById(R.id.weather_icon);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherofCurrentLocation();
    }

    private void getWeatherofCurrentLocation() {

        wLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        wLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

               RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);
                Networking(params);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, "Not able to get Location", Toast.LENGTH_SHORT).show();

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        wLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, wLocationListner);

    }
//
    public void Networking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        //Toast.makeText(MainActivity.this," Going to run UI link"+WEATHER_URL+params,Toast.LENGTH_SHORT).show();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                WeatherData WD = WeatherData.fromJson(response);
                updateWeather(WD);
                Toast.makeText(MainActivity.this,"Successfully got DATA",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                //Log.e("DB", String.valueOf(throwable instanceof ConnectTimeoutException));
                Toast.makeText(MainActivity.this,"Did not get DATA",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWeather(WeatherData wd){

        temperature.setText(wd.getTemperature());
        Location.setText(wd.getLocation());
        cur_weather.setText(wd.getWeatherType());
        humidity.setText(wd.getHumidity());
        pressure.setText(wd.getPressure());
        wind.setText(wd.getWind());
        min_max.setText(wd.getTemp_min_max());
        lat_lon.setText(wd.getLat_Long());


        int resourceID=getResources().getIdentifier(wd.getIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Location Granted",Toast.LENGTH_SHORT).show();
                getWeatherofCurrentLocation();
            }
            else
            {
                Toast.makeText(MainActivity.this,"Location Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void onPause() {
        super.onPause();
        if(wLocationManager!=null)
        {
            wLocationManager.removeUpdates(wLocationListner);
        }
    }
}