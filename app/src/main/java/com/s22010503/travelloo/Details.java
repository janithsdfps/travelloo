package com.s22010503.travelloo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class Details extends AppCompatActivity {

    private String placeName;
    BottomNavigationView bottom_navigation;
    DrawerLayout drawerLayout;
    TextView screenName;
    Button locationBtn ,situaton;
    ImageView person,backicon;
    LinearLayout profile,share,about,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        locationBtn = findViewById(R.id.Location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        screenName = findViewById(R.id.screenName);
        screenName.setText("Details");

        TextView name = findViewById(R.id.DtextView);
        ImageView imageView = findViewById(R.id.DmageView);
        ImageView extraImageView1 = findViewById(R.id.DimageView1);
        ImageView extraImageView2 = findViewById(R.id.DimageView2);
        ImageView extraImageView3 = findViewById(R.id.DimageView3);



        placeName = getIntent().getStringExtra("placeName");
        String placeId = getIntent().getStringExtra("placeId");
        double rating = getIntent().getDoubleExtra("rating", 0.0);

        if (placeId != null){
            PlacesClient placesClient = Places.createClient(this);
            List<Place.Field> placeFields = Arrays.asList(Place.Field.PHOTO_METADATAS);
            FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields);

// Fetch the place details
            placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
                if (photoMetadataList != null && !photoMetadataList.isEmpty()) {
                    // Iterate over the photo metadata list
                    for (int i = 0; i < photoMetadataList.size(); i++) {
                        PhotoMetadata photoMetadata = photoMetadataList.get(i);
                        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                .setMaxWidth(1000)
                                .setMaxHeight(1000)
                                .build();

                        // Fetch the photo
                        int finalI = i;
                        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                            Bitmap bitmap = fetchPhotoResponse.getBitmap();
                            // Display the bitmap in the appropriate ImageView
                            switch (finalI) {
                                case 0:
                                    imageView.setImageBitmap(bitmap);
                                    break;
                                case 1:
                                    extraImageView1.setImageBitmap(bitmap);
                                    break;
                                case 2:
                                    extraImageView2.setImageBitmap(bitmap);

                                case 3:
                                    extraImageView3.setImageBitmap(bitmap);
                                    break;
                                // Add more cases if you have additional ImageViews
                            }
                            name.setText(placeName + "("+rating+")");
                        }).addOnFailureListener((exception) -> {
                            Log.e("image","Image not available");
                        });
                    }

                }
            }).addOnFailureListener((exception) -> {
                Log.e("image","place details not available");
            });

        }
        else{

            //capturing card resorcrs

            String text = getIntent().getStringExtra("text");
            int imageResource = getIntent().getIntExtra("image", 0);
            String extraImageUrl1 = getIntent().getStringExtra("extraImageUrl1");
            String extraImageUrl2 = getIntent().getStringExtra("extraImageUrl2");
            String extraImageUrl3 = getIntent().getStringExtra("extraImageUrl3");

            // Set text and main image
            imageView.setImageResource(imageResource);
            name.setText(text);

            if (extraImageUrl1 != null) {
                Picasso.get().load(extraImageUrl1).into(extraImageView1);
            }
            if (extraImageUrl2 != null) {
                Picasso.get().load(extraImageUrl2).into(extraImageView2);
            }
            if (extraImageUrl3 != null) {
                Picasso.get().load(extraImageUrl3).into(extraImageView3);
            }

            // card resorces closed
        }

        //onclick stiations
        situaton= findViewById(R.id.Situation);

        situaton.setOnClickListener(v -> {
            Intent intent = new Intent(Details.this, weather.class);
            intent.putExtra("placeName", placeName);
            startActivity(intent);
        });
        //closed




        // review handler with domy data

        LinearLayout linearLayout = findViewById(R.id.review_linerlayout);

        String[] Btexts = {"gallefort", "Hikkaduwa", "nuwarra Eliya", "mirissa", "Nine arch"};
        int[] BimageResources = {R.drawable.gallefort, R.drawable.hikkaduwa, R.drawable.nuwaraeliyya, R.drawable.mirissa, R.drawable.ninearch};

        for (int i = 0; i < Btexts.length; i++) {
            View review = getLayoutInflater().inflate(R.layout.review, null);

            ImageView profile_img = review.findViewById(R.id.profile_image);
            TextView profile_name = review.findViewById(R.id.profile_name);

            profile_img.setId(View.generateViewId());

            profile_name.setText(Btexts[i]);
            profile_img.setImageResource(BimageResources[i]);

            // Set margins programmatically
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(4, 4, 4, 4);
            review.setLayoutParams(layoutParams);

            linearLayout.addView(review);
        }


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
            redirectActivity(Details.this, currency.class);
        });

        about.setOnClickListener(view -> {
            redirectActivity(Details.this, currency.class);
        });
        logout.setOnClickListener(view -> {
            redirectActivity(Details.this, currency.class);
        });
        // sidrawner closed

        //bottom navigation

        bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.calendar) {
                // Handle Calendar item click
                Intent calendarIntent = new Intent(Details.this, calender.class);
                startActivity(calendarIntent);
                return true;
            } else if (itemId == R.id.booking) {
                // Handle Booking item click
                Intent bookingIntent = new Intent(Details.this, booking.class);
                startActivity(bookingIntent);
                return true;
            } else if (itemId == R.id.home) {
                // Handle Home item click
                Intent homeIntent = new Intent(Details.this, home.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.currency) {
                // Handle Currency item click
                Intent currencyIntent = new Intent(Details.this, currency.class);
                startActivity(currencyIntent);
                return true;
            } else if (itemId == R.id.emergency) {
                // Handle Emergency item click
                Intent emergencyIntent = new Intent(Details.this, emergency.class);
                startActivity(emergencyIntent);
                return true;
            } else {
                return false;
            }
        }); //bottom navigation closed


        backicon = findViewById(R.id.backarrow);

        backicon.setOnClickListener(view -> {
            Intent back = new Intent(Details.this,home.class);
            startActivity(back);
        });


        locationBtn.setOnClickListener(v -> {
            // Check if GPS is enabled
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                checkLocationPermission();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please enable GPS to use this feature.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Open location settings to allow the user to enable GPS
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            // User cancelled, do nothing
                        })
                        .show();
            }
        });



    } // oncreate closed
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            Log.d("LocationPermission", "Permission already granted. Fetching current location.");
            getCurrentLocation();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation();
            } else {
                // Permission denied

                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getpath(String startPoint,String endpoint) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/dir/"+startPoint+"/"+endpoint);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        catch (ActivityNotFoundException exception){
            Uri  uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=en&gl=US&pli=1");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            Log.i("locationError", "Place: " + placeName);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private FusedLocationProviderClient fusedLocationClient;

    private void getCurrentLocation() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, proceed to get the location
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String startPoint = latitude + "," + longitude;
                            Log.i("LocationInfo", "Start point: " + startPoint);
                            Log.i("locationError", "start poit latitiude fetched");
                            getpath(startPoint, placeName);
                        }
                    });
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
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

    @Override
    protected  void onPause(){
        super.onPause();
        closedrawner(drawerLayout);
    }

    //sidedrawner methods closed

//    location sirection pass




}