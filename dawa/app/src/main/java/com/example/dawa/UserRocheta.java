package com.example.dawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dawa.Adapters.RochetaAdapter;
import com.example.dawa.Config.Config;
import com.example.dawa.Models.Rocheta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserRocheta extends AppCompatActivity {

    ListView rochetaList;
    ArrayList<Rocheta> rochetaArray= new ArrayList<>();
    RochetaAdapter adapter;
    SharedPreferences shared;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rocheta);
        shared = getSharedPreferences(Login.SHARED_PREFS , MODE_PRIVATE);
        userId = shared.getString("id" , "");

        rochetaList = findViewById(R.id.rochetaList);
        adapter = new RochetaAdapter(this, rochetaArray);
        rochetaList.setAdapter(adapter);
        getRocheta();

    }

    public void getRocheta() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL+"rocheta/user/"+ userId;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Rocheta rocheta = new Rocheta();
                                rocheta.setId(response.getString("id"));
                                rocheta.setDate(response.getString("date"));
                                rocheta.setDoctor(response.getString("doctor"));
                                rochetaArray.add(rocheta);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(UserRocheta.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
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