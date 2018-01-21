package com.lebelle.azure.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 20-Nov-17.
 */

public class Datum {
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("precipIntensity")
    @Expose
    private Integer precipIntensity;
    @SerializedName("precipProbability")
    @Expose
    private Integer precipProbability;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(Integer precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public Integer getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(Integer precipProbability) {
        this.precipProbability = precipProbability;
    }

}
