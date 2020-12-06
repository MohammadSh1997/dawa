package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dawa.Adapters.CurrentDrugsAdapter;
import com.example.dawa.Adapters.HistoryDrugsAdapter;
import com.example.dawa.Adapters.HistoryRochetaAdapter;
import com.example.dawa.Config.Config;
import com.example.dawa.Models.Drug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryDrugs extends AppCompatActivity {
    ArrayList<Drug> drugsArray;
    RecyclerView recyclerView;
    HistoryDrugsAdapter adapter;
    String rocheta_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_drugs);
        rocheta_id = getIntent().getExtras().getString("rocheta_id");
        drugsArray= new ArrayList<>();
        recyclerView= findViewById(R.id.historyDrugsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new HistoryDrugsAdapter(HistoryDrugs.this, drugsArray);
        recyclerView.setAdapter(adapter);
        getDrugs();
    }

    public void getDrugs() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"drugs/"+ rocheta_id;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray drugs= response.getJSONArray("result");
                                for (int i=0; i< drugs.length(); i++) {
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
                                Toast.makeText(HistoryDrugs.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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