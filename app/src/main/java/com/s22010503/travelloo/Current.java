package com.s22010503.travelloo;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.locks.Condition;

public class Current {
    @SerializedName("last_updated")
    public String lastUpdated;

    @SerializedName("temp_c")
    public double tempC;

    @SerializedName("condition")
    public WeatherCondition condition;

    @SerializedName("wind_mph")
    public double windMph;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("cloud")
    public int cloud;

}
