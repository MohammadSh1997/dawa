package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
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

public class NewDrug extends AppCompatActivity {

    int numberOfDrugs = 0;
    TextView drugsText;
    String rocheta_id;
    EditText drugName;
    EditText drugDesc;
    NumberPicker drugTimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drug);
        drugTimes= findViewById(R.id.numberPicker2);
        drugTimes.setMaxValue(4);
        drugTimes.setMinValue(1);
        drugName = findViewById(R.id.historyDrugName);
        drugDesc = findViewById(R.id.historyDrugDesc);
        rocheta_id = getIntent().getExtras().getString("rocheta_id");
        drugsText = findViewById(R.id.drugsText);
        drugsText.setText("لقد قمت بإضافة  " + numberOfDrugs + " دواء");
    }

    public void addDrugToText() {
        numberOfDrugs++;
        drugsText.setText("لقد قمت بإضافة " + numberOfDrugs + " دواء");
    }

    public void clearEditTexts() {
        drugName.setText("");
        drugDesc.setText("");
    }

    public void saveDrug(View view) {
        String name = drugName.getText().toString();
        String desc = drugDesc.getText().toString();
        String times = drugTimes.getValue()+"";


        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"rocheta/addNewDrugs";

        Map<String, String> params = new HashMap<String, String>();


        params.put("drug", name);
        params.put("drug_desc", desc);
        params.put("times", times);
        params.put("rocheta_id", rocheta_id);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(NewDrug.this, "تم الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                                addDrugToText();
                                clearEditTexts();
                            }
                             else {
                                Toast.makeText(NewDrug.this, response.get("msg").toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NewDrug.this, errorDescription, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(getRequest);
    }

    public void saveRocheta(View view) {
        startActivity(new Intent(NewDrug.this , Doctor.class));
        finish();
    }
}