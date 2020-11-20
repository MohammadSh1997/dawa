package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class User extends AppCompatActivity {

    String id , email , firstname , lastname , phone;
    TextView welcomeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        id= getIntent().getExtras().getString("id");
        email= getIntent().getExtras().getString("email");
        firstname= getIntent().getExtras().getString("firstname");
        lastname= getIntent().getExtras().getString("lastname");
        phone= getIntent().getExtras().getString("phone");
        welcomeMsg = findViewById(R.id.welcomeUserText);
        welcomeMsg.setText("Welome User " +firstname + " " + lastname);
    }

    public void logout(View view) {
        Intent i = new Intent(this , Login.class);
        startActivity(i);
        finish();
    }
}