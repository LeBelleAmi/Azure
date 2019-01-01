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
