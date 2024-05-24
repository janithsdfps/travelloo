package com.s22010503.travelloo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import java.util.List;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
public class home extends AppCompatActivity {
    private PlacesClient placesClient;
    private SearchView searchBar;
    BottomNavigationView bottom_navigation;
    DrawerLayout drawerLayout;
    ImageView person;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference imagesRef = database.getReference("extra_images");
    private SensorManager sensorManager;
    private Sensor lightSensor;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

//    ProgressBar progressBar = findViewById(R.id.progressBar);
    String apiKey="AIzaSyBa9k77lsF38OhYZ2s4TG6mL04iCJJnaQE";
    LinearLayout profile,share,about,logout;
    private static final String TAG = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //sensor
        // Initialize the SensorManager and the ambient light sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor == null) {
                Log.e("SensorError", "Ambient Light Sensor not available!");
            }
        }


        // Initialize PlacesClient
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);

        initializePlaces();

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.RATING,Place.Field.PHOTO_METADATAS));
        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);



        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String placeName = place.getName();
                String placeId = place.getId();
                Double rating = place.getRating();
                Log.i(TAG, "Place: " + placeName+ ", placeId: " + placeId +  "rating:" + rating);

                // Fetch photo metadata
                List<PhotoMetadata> photoMetadataList = place.getPhotoMetadatas();
                if (photoMetadataList != null && !photoMetadataList.isEmpty()) {
                    // Retrieve the first photo metadata

                    PhotoMetadata photoMetadata = photoMetadataList.get(0);
                    Log.i(TAG,"Retrieve the first photo metadata");

                    // Create a FetchPhotoRequest
                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(500) // Optional.
                            .setMaxHeight(300) // Optional.
                            .build();

                    // Fetch the photo asynchronously
                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        byte[] byteArray = stream.toByteArray();
                        Intent intent = new Intent(home.this, Details.class);
//
                        intent.putExtra("placeName", placeName);
                        intent.putExtra("placeId", placeId);
                        intent.putExtra("rating", rating);
                        startActivity(intent);
                        Log.i(TAG, "Place: " + placeName+ ", placeId: " + placeId +  "rating:" + place.getRating()+"passing photodata");
                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + exception.getMessage());
                            int statusCode = apiException.getStatusCode();
                            // Handle error with given status code.
                        }
                    });
                } else {
                    // No photo metadata available, proceed without photo
                    Intent intent = new Intent(home.this, Details.class);
                    intent.putExtra("placeName", placeName);
                    intent.putExtra("placeId", placeId);
                    intent.putExtra("rating", rating);
                    Log.i(TAG, "Place: " + placeName+ ", placeId: " + placeId +  "rating:" + place.getRating()+" No photo metadata available, proceed without photo");
                    startActivity(intent);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle error
            }
        });

        TextView logoutBtn = findViewById(R.id.logoutbtn);
//        //logout
//        logoutBtn.setOnClickListener(view -> {
//            mAuth.signOut();
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            if (currentUser == null) {
//                // Successfully logged out
//                Intent intent = new Intent(home.this, login.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
//                startActivity(intent);
//                finish();
//            } else {
//                // Logout failed - Show an error message
//                Toast.makeText(home.this, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });




        //cardview top 1
        imagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //only added galle and hikduwa in database initilase other after implemnt
                HashMap<String, Map<String, String>> extraImageUrlsMap = new HashMap<>();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String locationName = locationSnapshot.getKey();
                    Map<String, String> imageUrls = new HashMap<>();
                    for (DataSnapshot imageSnapshot : locationSnapshot.getChildren()) {
                        String key = imageSnapshot.getKey();
                        String url = imageSnapshot.getValue(String.class);
                        imageUrls.put(key, url);
                    }
                    extraImageUrlsMap.put(locationName, imageUrls);
                }

                // Inside onDataChange(), set up your card views with the retrieved data
                LinearLayout linearLayout = findViewById(R.id.linear_layout);
                String[] Ttexts = {"gallefort", "Hikkaduwa", "nuwarra Eliya", "mirissa", "Nine arch"};
                int[] TimageResources = {R.drawable.gallefort, R.drawable.hikkaduwa, R.drawable.nuwaraeliyya, R.drawable.mirissa, R.drawable.ninearch};

                for (int i = 0; i < Ttexts.length; i++) {
                    View cardView = getLayoutInflater().inflate(R.layout.card_item, null);

                    ImageView imageView = cardView.findViewById(R.id.imageView);
                    TextView textView = cardView.findViewById(R.id.textView);

                    imageView.setId(View.generateViewId());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(4, 0, 4, 0);
                    cardView.setLayoutParams(layoutParams);

                    textView.setText(Ttexts[i]);
                    imageView.setImageResource(TimageResources[i]);

                    final int imageResource = TimageResources[i];
                    final String locationName = Ttexts[i];
                    cardView.setOnClickListener(v -> {
                        Intent intent = new Intent(home.this, Details.class);
                        intent.putExtra("text", locationName);
                        intent.putExtra("image", imageResource);


                        // Pass text data to the next activity

                        // Get the extra image URLs related to the clicked place
                        Map<String, String> imageUrls = extraImageUrlsMap.get(locationName);
                        if (imageUrls != null) {
                            // Pass extra image URLs to the next activity
                            intent.putExtra("extraImageUrl1", imageUrls.get("image1"));
                            intent.putExtra("extraImageUrl2", imageUrls.get("image2"));
                            intent.putExtra("extraImageUrl3", imageUrls.get("image3"));
                        }

                        startActivity(intent);
                    });

                    linearLayout.addView(cardView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors or cancellation
            }
        });

        //cardview clsoe

        //cardview bottom1

//        progressBar.setVisibility(View.VISIBLE); // Show progress bar before retrieving data

        imagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Map<String, String>> extraImageUrlsMap = new HashMap<>();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String locationName = locationSnapshot.getKey();
                    Map<String, String> imageUrls = new HashMap<>();
                    for (DataSnapshot imageSnapshot : locationSnapshot.getChildren()) {
                        String key = imageSnapshot.getKey();
                        String url = imageSnapshot.getValue(String.class);
                        imageUrls.put(key, url);
                    }
                    extraImageUrlsMap.put(locationName, imageUrls);
                }

                // Inside onDataChange(), set up your card views with the retrieved data
                LinearLayout BlinearLayout = findViewById(R.id.linear_layout2);
                String[] texts = {"gallefort", "Hikkaduwa", "nuwarra Eliya", "mirissa", "Nine arch"};
                int[] imageResources = {R.drawable.gallefort, R.drawable.hikkaduwa, R.drawable.nuwaraeliyya, R.drawable.mirissa, R.drawable.ninearch};

                for (int i = 0; i < texts.length; i++) {
                    View cardView = getLayoutInflater().inflate(R.layout.cardview2, null);

                    ImageView imageView = cardView.findViewById(R.id.BimageV);
                    TextView textView = cardView.findViewById(R.id.Btextview);

                    imageView.setId(View.generateViewId());

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    layoutParams.setMargins(4, 6, 4, 6); // Adjust margin values as needed
                    cardView.setLayoutParams(layoutParams);

                    textView.setText(texts[i]);
                    imageView.setImageResource(imageResources[i]);


                    final int imageResource = imageResources[i];
                    final String locationName = texts[i];
                    cardView.setOnClickListener(v -> {
                        Intent intent = new Intent(home.this, Details.class);
                        intent.putExtra("text", locationName);
                        intent.putExtra("image", imageResource);


                        // Pass text data to the next activity

                        // Get the extra image URLs related to the clicked place
                        Map<String, String> imageUrls = extraImageUrlsMap.get(locationName);
                        if (imageUrls != null) {
                            // Pass extra image URLs to the next activity
                            intent.putExtra("extraImageUrl1", imageUrls.get("image1"));
                            intent.putExtra("extraImageUrl2", imageUrls.get("image2"));
                            intent.putExtra("extraImageUrl3", imageUrls.get("image3"));
                        }

                        startActivity(intent);
                    });

                    BlinearLayout.addView(cardView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors or cancellation
            }
        });

        //cardview clsoe


        // Google map
        ImageView locationIcon = findViewById(R.id.location);
        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });// Google map clsoed


        // side bar call


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
            redirectActivity(home.this, currency.class);
        });

        about.setOnClickListener(view -> {
            redirectActivity(home.this, currency.class);
        });
        logout.setOnClickListener(view -> {
            redirectActivity(home.this, currency.class);
        });
        // sidrawner closed


        //bottom navigation

        bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.calendar) {
                // Handle Calendar item click
                Intent calendarIntent = new Intent(home.this, calender.class);
                startActivity(calendarIntent);
                return true;
            } else if (itemId == R.id.booking) {
                // Handle Booking item click
                Intent bookingIntent = new Intent(home.this, booking.class);
                startActivity(bookingIntent);
                return true;
            } else if (itemId == R.id.home) {
                // Handle Home item click
                Intent homeIntent = new Intent(home.this, home.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.currency) {
                // Handle Currency item click
                Intent currencyIntent = new Intent(home.this, currency.class);
                startActivity(currencyIntent);
                return true;
            } else if (itemId == R.id.emergency) {
                // Handle Emergency item click
                Intent emergencyIntent = new Intent(home.this, emergency.class);
                startActivity(emergencyIntent);
                return true;
            } else {
                return false;
            }
        }); //bottom navigation closed

    } // on create closed


    private void initializePlaces() {

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
    }

//         GOogle map implemnt
    private void openGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
        }
    }  //         GOogle map implemnt closed

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



}



//place api code
//searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//@Override
//public boolean onQueryTextSubmit(String query) {
//        processUserInput(query);
//        return true;
//        }
//
//@Override
//public boolean onQueryTextChange(String query) {
//        processUserInput(query);
//        return false;
//        }
//
//private void processUserInput(String query) {
//
//        try {
//        // When the user submits the query, perform the API call
//        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
//        + URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8.toString())
//        + "&key=" + apikey                                                                                                                                                                                                                                                                                                                                                                                      ;
//
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//@Override public void onFailure(Call call, IOException e) {
//        // Handle network errors
//        }
//
//@Override public void onResponse(Call call, Response response) throws IOException {
//        String jsonData = response.body().string();
//
//        // Gson Parsing
//        Gson gson = new Gson();
//        PlaceResponse placeResponse = gson.fromJson(jsonData, PlaceResponse.class);
//
//        // Access the predictions
//        for (PlacePrediction prediction : placeResponse.predictions) {
//        Log.i("Place Result", "ID: " + prediction.placeId + ", Name: " + prediction.description);
//        }
//        }
//        });
//        } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//        // Handle the exception gracefully
//        }
//        }
//        });




