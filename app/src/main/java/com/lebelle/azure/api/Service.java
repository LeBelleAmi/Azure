package com.lebelle.azure.api;

import com.lebelle.azure.classes.RootData;
import com.lebelle.azure.url.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HP on 20-Nov-17.
 */

public interface Service {
   @GET(Url.LOCATION)
    Call<RootData> getCurrentWeatherData(@Query("q") String location,
                                         @Query("units") String unit,
                                         @Query("cnt") int count,
                                         @Query("lang") String language,
                                         @Query("appid") String api);

   /* @GET(Url.LOCATION + "lagos" + Url.UNITS + Url.COUNT + Url.LANG + Url.API_KEY)
    Call<RootData> getCurrentWeatherData();*/
}
