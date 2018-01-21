package com.lebelle.azure.api;

import com.lebelle.azure.classes.WeatherData;
import com.lebelle.azure.url.Url;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by HP on 20-Nov-17.
 */

public interface Service {
//    @GET(Url.API_KEY + Url.LATITUDE + Url.LONGITUDE)
    @GET(Url.API_KEY + "{latitude},{longitude}")
    Call<WeatherData> getCurrentWeatherData(
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );
}
