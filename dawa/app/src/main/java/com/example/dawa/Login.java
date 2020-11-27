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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText email, password;
    public static final String SHARED_PREFS= "SharedPrefis";
    SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        shared = getSharedPreferences(SHARED_PREFS , MODE_PRIVATE);
        if (shared.getBoolean("loggedIn" , false)) {
            goToActivity();
        }
    }


    public void registerClicked(View view) {
        Intent i = new Intent(this , ChooseType.class);
        startActivity(i);
    }

    public void viewForgotPassword(View view) {
        Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show();
    }

    public void goToActivity() {
        Intent intent = null;
        if (shared.getString("type" , "").equals("user")) {
            intent = new Intent(Login.this, User.class);
        } else {
            intent = new Intent(Login.this, Doctor.class);
        }
        startActivity(intent);
        finish();
    }

    public void logIn(View view) {
        //final String url = "http://192.168.1.73:8080/Login/register?userName=" + userName.getText().toString() + "&password=" + password.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.1.76:8080/api/auth/login";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
            new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("id" , response.get("id").toString());
                                editor.putString("email" , response.get("email").toString());
                                editor.putString("firstname" , response.get("firstname").toString());
                                editor.putString("lastname" , response.get("lastname").toString());
                                editor.putString("phone" , response.get("phone").toString());
                                editor.putString("type" , response.get("type").toString());
                                editor.putBoolean("loggedIn" , true);
                                editor.apply();
                                Intent intent = null;
                                if (shared.getString("type" , "").equals("user")) {
                                    intent = new Intent(Login.this, User.class);
                                } else {
                                    intent = new Intent(Login.this, Doctor.class);
                                }
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, response.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("volley error" , volleyError.getMessage());
                        String errorDescription = "";
                        if (volleyError instanceof NetworkError) {
                            errorDescription = "Network Error";
                        } else if (volleyError instanceof ServerError) {
                            errorDescription = "Server Error";
                        } else if (volleyError instanceof AuthFailureError) {
                            errorDescription = "AuthFailureError";
                        } else if (volleyError instanceof ParseError) {
                            errorDescription = "Parse Error";
                        } else if (volleyError instanceof NoConnectionError) {
                            errorDescription = "No Conenction";
                        } else if (volleyError instanceof TimeoutError) {
                            errorDescription = "Time Out";
                        } else {
                            errorDescription = "Connection Error";
                        }
                        Toast.makeText(Login.this, errorDescription, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// add it to the RequestQueue
        queue.add(getRequest);
    }
}