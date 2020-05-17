package com.lebelle.azure.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.lebelle.azure.R;

/**
 * Created by HP on 30-Dec-17.
 */

public class AppPrefs {

    /*
     * Human readable location string, provided by the API.  Because for styling,
     * "Mountain View" is more recognizable than 94043.
     */
    public static final String PREF_CITY_NAME = "city_name";

    public static String getPreferredWeatherLocation(Context context) {
        // COMPLETED (1) Return the user's preferred location
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return prefs.getString(keyForLocation, defaultLocation);
    }

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences
     *
     * @return true If imperial display should be used
     */

    public static boolean isImperial(Context context) {
        // COMPLETED (2) Return true if the user's preference for units is imperial, false otherwise
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForUnits = context.getString(R.string.units);
        String defaultUnits = context.getString(R.string.metric);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String imperial = context.getString(R.string.imperial);
        boolean userPrefersMetric = false;
        if(imperial.equals(preferredUnits)) {
            userPrefersMetric = true; }
        return userPrefersMetric;
    }


}
