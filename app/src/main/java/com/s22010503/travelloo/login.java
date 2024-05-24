package com.s22010503.travelloo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(login.this, home.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        EditText EmailUserInput = findViewById(R.id.email);
        EditText passwordUserInput = findViewById(R.id.password);
        Button loginbutton = findViewById(R.id.Login);

        loginbutton.setOnClickListener(view -> {

            String email, password;

            email =EmailUserInput.getText().toString();
            password = passwordUserInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Login successful - Start the next activity
                                    Intent intent = new Intent(login.this, home.class);
                                    startActivity(intent);

                                } else {
                                    // Login failed - Show an error message
                                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            });




        });

//        dont have account?sign up button
        TextView signup = findViewById(R.id.sighnflog);
        signup.setOnClickListener(view -> {

                Intent intent = new Intent(login.this, sighnup.class);
                startActivity(intent);
                finish();

        });
        //       forgot password
        TextView forgotPs = findViewById(R.id.forgotPs);
        forgotPs.setOnClickListener(view -> {

            Intent intent = new Intent(login.this, resetpassword.class);
            startActivity(intent);
            finish();

        });

    }

}