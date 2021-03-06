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
import com.example.dawa.Adapters.DrugsAdapter;
import com.example.dawa.Adapters.HistoryRochetaAdapter;
import com.example.dawa.Config.Config;
import com.example.dawa.Models.Drug;
import com.example.dawa.Models.Rocheta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorHistory extends AppCompatActivity {

    ArrayList<Rocheta> rochetaArray;
    HistoryRochetaAdapter adapter;
    RecyclerView recyclerView;
    String doctor_id;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_history);
        shared = getSharedPreferences(Login.SHARED_PREFS , MODE_PRIVATE);
        doctor_id=shared.getString("id" , "");
        rochetaArray= new ArrayList<>();
        recyclerView= findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new HistoryRochetaAdapter(DoctorHistory.this, rochetaArray);
        recyclerView.setAdapter(adapter);
        getRochetas();
    }

    private void getRochetas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"rocheta/doctor/"+ doctor_id;

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
                                    rocheta.setPatient(drugObject.getString("patient"));
                                    rochetaArray.add(rocheta);
                                    recyclerView.setAdapter(adapter);
                                }
                            } else {
                                Toast.makeText(DoctorHistory.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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