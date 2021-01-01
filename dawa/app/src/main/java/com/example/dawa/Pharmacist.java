package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dawa.Config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pharmacist extends AppCompatActivity {
    EditText userEmail;
    String patientId;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist);
        shared = getSharedPreferences(Login.SHARED_PREFS , MODE_PRIVATE);
        userEmail = findViewById(R.id.pharmacistUserEmail);

    }

    public void logout(View view) {
        Intent i = new Intent(this , Login.class);
        startActivity(i);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("loggedIn", false);
        editor.apply();
        finish();
    }

    public void getUser(String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"auth/getUser/"+ email;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                patientId = response.getString("id");
                            } else {
                                Toast.makeText(Pharmacist.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception jsonException) {
                            jsonException.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Error",response.getMessage());
                    }
                }
        );
        queue.add(getRequest);
    }

    public void nextStep(View view) {
        if (userEmail.getText().toString().equals("")) {
            Toast.makeText(this, "يجب ادخال البريد الالكتروني للمريض", Toast.LENGTH_SHORT).show();
            return;
        }
        getUser(userEmail.getText().toString());

        if (patientId == null)
            return;

        Intent intent = new Intent(Pharmacist.this , PharmacistUserRochetas.class);
        intent.putExtra("patientId" , patientId);
        startActivity(intent);
    }
}