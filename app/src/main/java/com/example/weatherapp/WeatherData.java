package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
    private String Temperature,Icon, Location, WeatherType, Lat, Lon, Temp_min, Temp_max, wind, humidity, pressure;
    private int Condition;

    public static WeatherData fromJson(JSONObject jsonObject)
    {

        try
        {
            WeatherData WD=new WeatherData();
            WD.Location=jsonObject.getString("name");
            WD.Condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            WD.WeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
          /*  WD.Lon = jsonObject.getJSONArray("coord").getJSONObject(0).getString("lon");
            WD.Lat = jsonObject.getJSONArray("coord").getJSONObject(0).getString("lat");*/
            WD.Icon=updateIcon(WD.Condition);
            WD.pressure = Integer.toString(jsonObject.getJSONObject("main").getInt("pressure"));
            WD.humidity = Integer.toString(jsonObject.getJSONObject("main").getInt("humidity"));
           /* WD.Temperature = Double.toString(jsonObject.getJSONObject("main").getDouble("temp")-273.15);
            WD.Temp_min = Double.toString(jsonObject.getJSONObject("main").getDouble("temp_min")-273.15);
            WD.Temp_max = Double.toString(jsonObject.getJSONObject("main").getDouble("temp_max")-273.15);
            WD.pressure = Integer.toString(jsonObject.getJSONObject("main").getInt("pressure"));
            WD.humidity = Integer.toString(jsonObject.getJSONObject("main").getInt("humidity"));
            WD.wind = Double.toString(jsonObject.getJSONObject("wind").getDouble("speed"));
*/

            return WD;
        }


        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    private static String updateIcon(int wCondition) {
        return "suncloud";
    }
/*
    public String getTemperature() {
        return Temperature+" °C";
    }*/

    public String getIcon() {
        return Icon;
    }

    public String getLocation() {
        return Location;
    }

    public String getWeatherType() {
        return WeatherType;
    }
    public String getHumidity() {
        return humidity + " %";
    }

    public String getPressure() {
        return pressure + " hPa";
    }
/*
    public String getLat() {
        return Lat;
    }

    public String getLon() {
        return Lon;
    }

    public String getTemp_min() {
        return Temp_min+ " °C";
    }

    public String getTemp_max() {
        return Temp_max + " °C";
    }

    public String getWind() {
        return wind  + " km/h";
    }

   */

    public int getCondition() {
        return Condition;
    }
}

