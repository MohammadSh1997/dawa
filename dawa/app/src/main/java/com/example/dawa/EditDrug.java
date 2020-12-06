package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
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
import com.example.dawa.Models.Drug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditDrug extends AppCompatActivity {

    Drug drug;
    EditText drugName,drugDesc;
    NumberPicker picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_drug);
        drug = new Drug();
        drug.setId(getIntent().getExtras().getString("drug_id"));
        drug.setName(getIntent().getExtras().getString("drug_name"));
        drug.setDescription(getIntent().getExtras().getString("drug_desc"));
        drug.setTimes(getIntent().getExtras().getString("drug_times"));

        drugName = findViewById(R.id.historyDrugName);
        drugDesc = findViewById(R.id.historyDrugDesc);
        picker = findViewById(R.id.numberPicker2);

        drugName.setText(drug.getName());
        drugDesc.setText(drug.getDescription());

        picker.setMinValue(1);
        picker.setMaxValue(4);
        picker.setValue(Integer.parseInt(drug.getTimes()));
    }

    public void editDrug(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+ "drugs/"+drug.getId();

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", drugName.getText().toString());
        params.put("desc", drugDesc.getText().toString());
        params.put("times", picker.getValue()+"");

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.PUT, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(EditDrug.this, "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EditDrug.this, DoctorHistory.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(EditDrug.this, "حدث خطأ في الاتصال", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditDrug.this, errorDescription, Toast.LENGTH_SHORT).show();
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