package com.s22010503.travelloo;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class base_sidebar {

    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public base_sidebar(AppCompatActivity activity, DrawerLayout drawerLayout, NavigationView navigationView) {
        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
    }

    public void setupNavigationMenu() {
        navigationView.setNavigationItemSelectedListener(   item -> {
            int id = item.getItemId();
            if (id == R.id.profile) {
                Intent calendarIntent = new Intent(activity, calender.class);
                activity.startActivity(calendarIntent);
                return true;
            } else if (id == R.id.share) {
                Intent bookingIntent = new Intent(activity, booking.class);
                activity.startActivity(bookingIntent);
                return true;
            } else if (id == R.id.logout) {
                Intent homeIntent = new Intent(activity, MainActivity.class);
                activity.startActivity(homeIntent);
                return true;
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    public void setupProfileIconListener(View profileIcon) {
        profileIcon.setOnClickListener(v ->
                drawerLayout.openDrawer(navigationView));
    }
}
