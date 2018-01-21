package com.lebelle.azure.classes;

import android.content.Context;
import android.location.Location;

import com.lebelle.azure.R;

import static com.lebelle.azure.data.AppPrefs.getPreferredWeatherLocationFromSharedPrefs;

/**
 * Created by HP on 06-Jan-18.
 */

public class WeatherLocation {
    String location;
    String latitude;
    String longitude;

    public WeatherLocation(Context context) {
        setLocation(getPreferredWeatherLocationFromSharedPrefs(context.getString(R.string.location_1), context));
        setLatitude(getPreferredWeatherLocationFromSharedPrefs(context.getString(R.string.latitude), context));
        setLongitude(getPreferredWeatherLocationFromSharedPrefs(context.getString(R.string.longitude), context));
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Location getLatitudeandLongitude(){
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(getLatitude()));
        location.setLongitude(Double.parseDouble(getLongitude()));

        return location;
    }
}
