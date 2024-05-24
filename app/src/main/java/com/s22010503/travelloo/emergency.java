package com.s22010503.travelloo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class emergency extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "BarometerApp";
    private static final String BIOTAG = "TemperatureMonitor";
    private SensorManager sensorManager;
    private Sensor tempSensor,barometerSensor;

    private MediaPlayer mediaPlayer;
    private float lastTemperature = Float.MIN_VALUE; // To store the last temperature value
    private boolean isTemperatureAvailable = false;




    BottomNavigationView bottom_navigation;
    DrawerLayout drawerLayout;
    TextView screenName, barometerReading;
    ImageView person,backicon,locationshare;
    LinearLayout profile,share,about,logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        barometerReading = findViewById(R.id.locationsharetext);
        screenName = findViewById(R.id.screenName);
        screenName.setText("Emergency");

        //side drawner open

        drawerLayout= findViewById(R.id.drwanerlayout);
        person = findViewById(R.id.person);
        profile= findViewById(R.id.profile);
        share= findViewById(R.id.share);
        about= findViewById(R.id.about);
        logout= findViewById(R.id.logout);

        person.setOnClickListener(view -> {
            opendrawner(drawerLayout);
        });

        profile.setOnClickListener(view -> {
            recreate();
        });

        share.setOnClickListener(view -> {
            redirectActivity(emergency.this, currency.class);
        });

        about.setOnClickListener(view -> {
            redirectActivity(emergency.this, currency.class);
        });
        logout.setOnClickListener(view -> {
            redirectActivity(emergency.this, currency.class);
        });
        // sidrawner closed

        //bottom navigation

        bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.calendar) {
                // Handle Calendar item click
                Intent calendarIntent = new Intent(emergency.this, calender.class);
                startActivity(calendarIntent);
                return true;
            } else if (itemId == R.id.booking) {
                // Handle Booking item click
                Intent bookingIntent = new Intent(emergency.this, booking.class);
                startActivity(bookingIntent);
                return true;
            } else if (itemId == R.id.home) {
                // Handle Home item click
                Intent homeIntent = new Intent(emergency.this, home.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.currency) {
                // Handle Currency item click
                Intent currencyIntent = new Intent(emergency.this, currency.class);
                startActivity(currencyIntent);
                return true;
            } else if (itemId == R.id.emergency) {
                // Handle Emergency item click
                Intent emergencyIntent = new Intent(emergency.this, emergency.class);
                startActivity(emergencyIntent);
                return true;
            } else {
                return false;
            }
        }); //bottom navigation closed


        backicon = findViewById(R.id.backarrow);

        backicon.setOnClickListener(view -> {
            Intent back = new Intent(emergency.this,home.class);
            startActivity(back);
        });


        //sensor

// Initialize sensor manager and temperature sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        if (sensorManager != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            if (tempSensor != null) {
                sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
                isTemperatureAvailable = true;
            } else {
                Toast.makeText(this, "Temperature sensor not available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Sensor Manager not available", Toast.LENGTH_SHORT).show();
        }
//
//        if (sensorManager != null) {
//            // Check if the barometer sensor is available
//            barometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//            if (barometerSensor != null) {
//                // Register the sensor listener
//                sensorManager.registerListener(this, barometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            } else {
//                Toast.makeText(this, "Barometer sensor not available", Toast.LENGTH_SHORT).show();
//                Log.e(BIOTAG, "Barometer sensor not available");
//            }
//        } else {
//            Toast.makeText(this, "SensorManager not available", Toast.LENGTH_SHORT).show();
//            Log.e(BIOTAG, "SensorManager not available");
//        }



        // Set up the button click listener
        TextView checkTempButton = findViewById(R.id.tempscheck);
        checkTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button clicked");
                if (isTemperatureAvailable) {
                    // Check temperature and play sound
                    checkTemperatureAndPlaySound();
                    Log.i(TAG, "checktemp caled");
                } else {
                    Toast.makeText(emergency.this, "Temperature sensor not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            lastTemperature = event.values[0];
            Log.i(TAG, "Ambient Temperature: " + lastTemperature + " °C");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle changes in sensor accuracy if needed
    }
    private void checkTemperatureAndPlaySound() {
        if (lastTemperature == Float.MIN_VALUE) {
            Toast.makeText(this, "Temperature data not yet available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastTemperature < 10) {
            playSound(R.raw.cold);
            Log.i(TAG, "cold");
        } else if (lastTemperature >= 10 && lastTemperature < 20) {
            playSound(R.raw.cool);
            Log.i(TAG, "cool");
        } else if (lastTemperature >= 20 && lastTemperature < 25) {
            playSound(R.raw.good);
            Log.i(TAG, "good");
        } else if (lastTemperature >= 25) {
            playSound(R.raw.warm);
            Log.i(TAG, "hot");
        }
    }
    private void playSound(int soundResourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResourceId);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            Toast.makeText(this, "Playing sound for temperature range", Toast.LENGTH_SHORT).show();
        }
    }



    //sidedrwaner methods
    public static void opendrawner(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closedrawner(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void redirectActivity(Activity activity, Class secondActivity){
        Intent intent =new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }



    //sidedrawner methods closed
}