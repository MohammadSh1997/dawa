package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.dawa.Adapters.DrugsAdapter;
import com.example.dawa.Adapters.PharmacistRochetaAdapter;
import com.example.dawa.Adapters.RochetaAdapter;
import com.example.dawa.Config.Config;
import com.example.dawa.Models.Rocheta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PharmacistUserRochetas extends AppCompatActivity {
    String userId;
    RecyclerView recycler;
    ArrayList<Rocheta> rochetaArray= new ArrayList<>();
    PharmacistRochetaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_user_rochetas);

        userId = getIntent().getExtras().getString("patientId");
        recycler= findViewById(R.id.pharmacistRochetasRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new PharmacistRochetaAdapter(PharmacistUserRochetas.this, rochetaArray);
        recycler.setAdapter(adapter);
        getRochetas();
    }

    private void getRochetas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"rocheta/user/allRochetas/"+ userId;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray drugs= response.getJSONArray("result");
                                for (int i=0 ;i< drugs.length(); i++) {
                                    JSONObject drugObject = drugs.getJSONObject(i);
                                    Rocheta rocheta = new Rocheta();
                                    rocheta.setId(drugObject.getString("id"));
                                    rocheta.setDate(drugObject.getString("date"));
                                    rocheta.setPatient(drugObject.getString("doctor"));
                                    rochetaArray.add(rocheta);
                                    recycler.setAdapter(adapter);
                                }
                            } else {
                                Toast.makeText(PharmacistUserRochetas.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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
}