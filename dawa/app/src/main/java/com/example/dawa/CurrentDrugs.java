package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dawa.Adapters.CurrentDrugsAdapter;
import com.example.dawa.Adapters.DrugsAdapter;
import com.example.dawa.Models.Drug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrentDrugs extends AppCompatActivity {

    SharedPreferences shared;
    String user_id;
    ArrayList<Drug> drugsArray;
    RecyclerView recyclerView;
    CurrentDrugsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_drugs);
        shared = getSharedPreferences(Login.SHARED_PREFS , MODE_PRIVATE);
        user_id = shared.getString("id" , "");
        drugsArray= new ArrayList<>();
        recyclerView= findViewById(R.id.currentDrugsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new CurrentDrugsAdapter(CurrentDrugs.this, drugsArray);
        recyclerView.setAdapter(adapter);
        getDrugs();
    }

    public void getDrugs() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.1.76:8080/api/drugs/currentDrugs/"+ user_id;

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
                                    Drug drug = new Drug();
                                    drug.setId(drugObject.getString("id"));
                                    drug.setName(drugObject.getString("drug"));
                                    drug.setDescription(drugObject.getString("description"));
                                    drug.setTimes(drugObject.getString("times"));
                                    drugsArray.add(drug);
                                    recyclerView.setAdapter(adapter);
                                }
                            } else {
                                Toast.makeText(CurrentDrugs.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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