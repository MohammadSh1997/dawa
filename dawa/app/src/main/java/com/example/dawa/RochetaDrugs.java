package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dawa.Adapters.DrugsAdapter;
import com.example.dawa.Models.Drug;
import com.example.dawa.Models.Rocheta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RochetaDrugs extends AppCompatActivity {

    String rocheta_id;
    ArrayList<Drug> drugsArray;
    DrugsAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocheta_drugs);
        rocheta_id= getIntent().getExtras().getString("rocheta_id");
        drugsArray= new ArrayList<>();
        recyclerView= findViewById(R.id.drugsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new DrugsAdapter(RochetaDrugs.this, drugsArray);
        recyclerView.setAdapter(adapter);
        getDrugs();
    }

    public void getDrugs() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.1.76:8080/api/rocheta/drugs/"+ rocheta_id;

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
                                Toast.makeText(RochetaDrugs.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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