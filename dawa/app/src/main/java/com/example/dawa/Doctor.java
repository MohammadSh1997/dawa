package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Doctor extends AppCompatActivity {

    String id , email , firstname , lastname , phone;
    TextView welcomeMsg;
    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        shared = getSharedPreferences(Login.SHARED_PREFS , MODE_PRIVATE);
        id=shared.getString("id" , "");
        email= shared.getString("email" , "");
        firstname= shared.getString("firstname" , "");
        lastname= shared.getString("lastname" , "");
        phone= shared.getString("phone" , "");
        welcomeMsg = findViewById(R.id.welcomeDoctorText);
        welcomeMsg.setText("Welome Doctor " +firstname);
    }

    public void logout(View view) {
        Intent i = new Intent(this , Login.class);
        startActivity(i);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("loggedIn", false);
        editor.apply();
        finish();
    }

    public void addNewRocheta(View view) {
        Intent i = new Intent(this , NewRocheta.class);
        startActivity(i);
    }

    public void history(View view) {
        Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
    }
}