package com.example.loginregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    //create object of DatabaseReference class to access firebase's Realtime database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://natasse-705d1-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullname = findViewById(R.id.fullname);
        final EditText email = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText conPassword = findViewById(R.id.conPassword);
        final EditText location = findViewById(R.id.location);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fullnameTxt = fullname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();
                final String Location = location.getText().toString();

                //check if user has filled all fields before sending to Firebase
                if(fullnameTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty() || Location.isEmpty()){
                    Toast.makeText(Register.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                }

                //check if passwords match else display a toast message
                else if (!passwordTxt.equals(conPasswordTxt)){
                    Toast.makeText(Register.this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
                }

                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if phone is already registered

                            if(snapshot.hasChild(phoneTxt)){
                                Toast.makeText(Register.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                //sending data to firebase Realtime Database
                                // using phone number as unique identity of every user
                                // so all the other details of user come under their phone number
                                databaseReference.child("users").child(phoneTxt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("users").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);
                                databaseReference.child("users").child(phoneTxt).child("Location").setValue(Location);

                                //show a success message then finish the activity
                                Toast.makeText(Register.this, "User Registered successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        }));


        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}