package com.s22010503.travelloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class weather extends AppCompatActivity {

    TextView screenName;
    private WeatherApiService weatherApiService;
    private EditText searchBar;
    private BottomNavigationView bottom_navigation;
    private ImageView weatherIconImageView;
    private TextView humidityTextView,cloud;
    private TextView dateTextView;
    private TextView nameTextView;
    private TextView tempCTextView , clodutext;
    private TextView conditionTextView;
    private TextView windMphTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        screenName = findViewById(R.id.screenName);
        screenName.setText("Situation");




        //bottom navigation

        bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.calendar) {
                // Handle Calendar item click
                Intent calendarIntent = new Intent(weather.this, calender.class);
                startActivity(calendarIntent);
                return true;
            } else if (itemId == R.id.booking) {
                // Handle Booking item click
                Intent bookingIntent = new Intent(weather.this, booking.class);
                startActivity(bookingIntent);
                return true;
            } else if (itemId == R.id.home) {
                // Handle Home item click
                Intent homeIntent = new Intent(weather.this, home.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.currency) {
                // Handle Currency item click
                Intent currencyIntent = new Intent(weather.this, currency.class);
                startActivity(currencyIntent);
                return true;
            } else if (itemId == R.id.emergency) {
                // Handle Emergency item click
                Intent emergencyIntent = new Intent(weather.this, emergency.class);
                startActivity(emergencyIntent);
                return true;
            } else {
                return false;
            }
        }); //bottom navigation closed


        //wether api

        dateTextView = findViewById(R.id.time);
        humidityTextView = findViewById(R.id.humadity);
        tempCTextView = findViewById(R.id.temp_c);
        conditionTextView = findViewById(R.id.textcondion);
        windMphTextView = findViewById(R.id.wind_mph);
        nameTextView = findViewById(R.id.wnameplace);
        weatherIconImageView = findViewById(R.id.tdyConditionIcon);
        clodutext = findViewById(R.id.cloud);

        String placeName = getIntent().getStringExtra("placeName");

        fetchWeatherData(placeName);
    }


    private void fetchWeatherData(String placeName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);

        weatherApiService.getCurrentWeather("45b3f9a8c71d434fbc4142604240301", placeName, "no").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    dateTextView.setText(weatherResponse.location.localtime);
                    humidityTextView.setText(String.valueOf(weatherResponse.current.humidity)+"%");
                    nameTextView.setText(weatherResponse.location.name);
                    tempCTextView.setText(String.valueOf(weatherResponse.current.tempC)+"°C");
                    conditionTextView.setText(weatherResponse.current.condition.text);
                    clodutext.setText(String.valueOf(weatherResponse.current.cloud) + "%");
                    String iconUrl = "https:" + weatherResponse.current.condition.icon;
                    Picasso.get().load(iconUrl).into(weatherIconImageView);
                    windMphTextView.setText(String.valueOf(weatherResponse.current.windMph)+"MPH");

                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}