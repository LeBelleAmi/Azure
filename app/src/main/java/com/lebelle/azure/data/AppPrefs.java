package com.lebelle.azure.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.preference.PreferenceManager;

import com.lebelle.azure.R;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by HP on 30-Dec-17.
 */

public class AppPrefs {

    public static final String DEFAULT_WEATHER_LOCATION = "Warri, Nigeria";
    public static final String DEFAULT_WEATHER_COORDS = "{5.5544, 5.7932}";

    /**
     * A helper function that sets a new string to SharedPreferences by using the key and string provided.
     * @param key
     * @param string
     */
    public static void setPreferredWeatherLocationToSharedPrefs(String key, String string, Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        prefs
                .edit()
                .putString(key, string)
                .apply();
    }


    public static void CreateCityInSharedPrefs(Context context, String providedLocation) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            Address location = geocoder.getFromLocationName(providedLocation, 1).get(0);

            // Save the new city value to SharedPreferences.
            setPreferredWeatherLocationToSharedPrefs(context.getString(R.string.location_1), location.getAddressLine(0), context);


            // Set the latitude and longitude in shared preferences.
            setPreferredWeatherLocationToSharedPrefs(context.getString(R.string.latitude), String.valueOf(location.getLatitude()), context);
            setPreferredWeatherLocationToSharedPrefs(context.getString(R.string.longitude), String.valueOf(location.getLongitude()), context);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function that gets a string from SharedPreferences by using the key provided.
     * @return String, key
     */

    public static String getPreferredWeatherLocationFromSharedPrefs(String key, Context context) {
        // Return the user's preferred location from shared prefs
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.location_1);
        String defaultLocation = context.getString(R.string.default_location);
        return prefs.getString(keyForLocation, defaultLocation);
    }

    /**
     * Returns true if the user has selected imperial temperature display.
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
        String defaultUnits = context.getString(R.string.imperial);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String imperial = context.getString(R.string.imperial);
        boolean userPrefersImperial;
        if (imperial.equals(preferredUnits)) {
            userPrefersImperial = true;
        } else {
            userPrefersImperial = false;
        }
        return userPrefersImperial;
    }


}
