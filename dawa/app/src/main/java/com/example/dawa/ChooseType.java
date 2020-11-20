package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseType extends AppCompatActivity {

    ImageView userImage;
    ImageView doctorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);
        userImage = findViewById(R.id.userImage);
        doctorImage = findViewById(R.id.doctorImage);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseType.this, Register.class );
                i.putExtra("type", "user");
                startActivity(i);
                finish();
            }
        });

        doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseType.this, Register.class );
                i.putExtra("type", "doctor");
                startActivity(i);
                finish();
            }
        });
    }
}