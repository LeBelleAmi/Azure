package com.lebelle.azure.classes;

import android.content.Context;
import android.location.Location;

import com.lebelle.azure.R;


/**
 * Created by HP on 06-Jan-18.
 */

public class WeatherLocation {
    String location;
    String latitude;
    String longitude;

    public WeatherLocation(Context context) {
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

    }

