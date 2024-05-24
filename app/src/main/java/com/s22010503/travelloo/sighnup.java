package com.s22010503.travelloo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sighnup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(sighnup.this, home.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighnup);

        TextView myTextView = findViewById(R.id.alsign);
        mAuth = FirebaseAuth.getInstance();

        EditText UserName = findViewById(R.id.UserName);
        EditText EmailUserInput = findViewById(R.id.Email);
        EditText passwordUserInput = findViewById(R.id.Password);
        EditText psRetype = findViewById(R.id.Ps_Retype);
        Button signup = findViewById(R.id.SignUpButton);





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email, password,username,psretype;

                username =UserName.getText().toString();
                psretype =psRetype.getText().toString();
                email =EmailUserInput.getText().toString();
                password = passwordUserInput.getText().toString();


                if (email.isEmpty() || password.isEmpty()|| username.isEmpty()|| psretype.isEmpty()) {
                    Toast.makeText(sighnup.this, "Please fill in all user inputs", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(psretype)){

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(sighnup.this, "sign up successfull", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(sighnup.this, login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(sighnup.this, "sign up failed! try again latter", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else{
                    Toast.makeText(sighnup.this, "RE-type password doesn't match with previous one", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });




//        onclick listner for loging
        myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sighnup.this, login.class);
                startActivity(intent);

            }
        });
    }


}
