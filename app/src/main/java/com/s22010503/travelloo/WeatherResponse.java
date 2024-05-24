package com.s22010503.travelloo;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("location")
    public Location location;

    @SerializedName("current")
    public Current current;


}
