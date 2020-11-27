package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

public class NewRocheta extends AppCompatActivity {

    String doctorId;
    String patientId;
    EditText userEmail;
    EditText noteText;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rocheta);
        shared= getSharedPreferences(Login.SHARED_PREFS , MODE_PRIVATE);
        doctorId = shared.getString("id" , "");
        userEmail = findViewById(R.id.rochetaUserEmail);
        noteText = findViewById(R.id.rochetaNotes);
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
                                Toast.makeText(NewRocheta.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException jsonException) {
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

    public void saveRocheta(View view) {
        String notes = noteText.getText().toString();
        getUser(userEmail.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"rocheta/addNewRocheta";

        Map<String, String> params = new HashMap<String, String>();
        params.put("patient_id", patientId);
        params.put("doctor_id", doctorId);
        params.put("notes", notes);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                String rocheta_id = response.get("id").toString();
                                Intent intent = new Intent(NewRocheta.this , NewDrug.class);
                                intent.putExtra("rocheta_id" , rocheta_id);
                                startActivity(intent);
                            } else {
                                Toast.makeText(NewRocheta.this, response.get("msg").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("volley error" , volleyError.toString());
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
                        Toast.makeText(NewRocheta.this, errorDescription, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(getRequest);
    }
}