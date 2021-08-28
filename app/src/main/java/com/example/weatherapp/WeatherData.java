package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
    private String Temperature,Icon, Location, WeatherType, Lati, Longi, Temp_min, Temp_max, wind, humidity, pressure;
    private int Condition;

    public static WeatherData fromJson(JSONObject jsonObject)
    {

        try
        {
            WeatherData WD=new WeatherData();
            WD.Location=jsonObject.getString("name");
            WD.Condition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            WD.WeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            double temp_lat = jsonObject.getJSONObject("coord").getDouble("lat");
            WD.Lati=Integer.toString(round(temp_lat));
            double temp_lon = jsonObject.getJSONObject("coord").getDouble("lon");
            WD.Longi=Integer.toString(round(temp_lon));
            WD.Icon=updateIcon(WD.Condition);
            WD.pressure = Integer.toString(jsonObject.getJSONObject("main").getInt("pressure"));
            WD.humidity = Integer.toString(jsonObject.getJSONObject("main").getInt("humidity"));
            WD.wind = Double.toString(jsonObject.getJSONObject("wind").getDouble("speed"));
            double temp_temp=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            WD.Temperature=Integer.toString(round(temp_temp));
            double temp_minT = jsonObject.getJSONObject("main").getDouble("temp_min")-273.15;
            WD.Temp_min=Integer.toString(round(temp_minT));
            double temp_maxT = jsonObject.getJSONObject("main").getDouble("temp_max")-273.15;
            WD.Temp_max=Integer.toString(round(temp_maxT));


            return WD;
        }


        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    private static int round(double abc) {
        return (int) Math.rint(abc);
    }

    private static String updateIcon(int condition) {

        if(condition==800)
        {
            return "sunny";
        }
        else if(condition==801)
        {
            return "fewclouds";
        }
        else if(condition==802)
        {
            return "scatteredclouds";
        }
        else if(condition==803 || condition==804)
        {
            return "brokenclouds";
        }
        else if(condition>=200 && condition<=232)
        {
            return "thunderstorm";
        }
        else if(condition>=300 && condition<=321)
        {
            return "showerrain";
        }
        else  if(condition>=500 && condition<=531){
            return "rain";
        }
        else  if(condition>=600 && condition<=622){
            return "snow";
        }

        else  if(condition>=700 && condition<=781){
            return "mist";
        }

        return "suncloud";
    }

    public String getTemperature() {
        return Temperature+" °C";
    }

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

    public String getWind() {
        return wind  + " km/h";
    }

    public String getTemp_min_max() {
        return Temp_min+ "°C  / "+  Temp_max + " °C";
    }


    public String getLat_Long() {
        return Lati + " / "+ Longi;
    }







    public int getCondition() {
        return Condition;
    }
}

