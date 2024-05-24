package com.s22010503.travelloo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.TintableCheckedTextView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class calender extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView person;

    LinearLayout profile,share,about,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

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
            redirectActivity(calender.this, currency.class);
        });

        about.setOnClickListener(view -> {
            redirectActivity(calender.this, currency.class);
        });
        logout.setOnClickListener(view -> {
            redirectActivity(calender.this, currency.class);
        });



    }

    public static void opendrawner(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closedrawner(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void redirectActivity(Activity activity,Class secondActivity){
        Intent intent =new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        closedrawner(drawerLayout);
    }





}