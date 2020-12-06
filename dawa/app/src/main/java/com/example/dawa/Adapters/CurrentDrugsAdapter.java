package com.example.dawa.Adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.dawa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrentDrugsAdapter extends  RecyclerView.Adapter<CurrentDrugsAdapter.viewitem> {

    ArrayList<Drug> drugsArray;
    Context context;

    public CurrentDrugsAdapter(Context context, ArrayList<Drug> drugsArray) {
        this.context = context;
        this.drugsArray = drugsArray;
    }

    public class viewitem extends RecyclerView.ViewHolder {
        TextView drugName , drugDesc , drugTimes;
        Switch drugSwitch;

        public viewitem(@NonNull View itemView) {
            super(itemView);
             drugName = itemView.findViewById(R.id.historyDrugName);
             drugDesc = itemView.findViewById(R.id.historyDrugDesc);
             drugTimes = itemView.findViewById(R.id.historyDrugTimes);
             drugSwitch = itemView.findViewById(R.id.currentDrugSwitch);
        }
    }

    @NonNull
    @Override
    public CurrentDrugsAdapter.viewitem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_current_drug_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return new viewitem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentDrugsAdapter.viewitem holder, int position) {
        holder.drugName.setText("الدواء : "+ drugsArray.get(position).getName());
        holder.drugDesc.setText("الوصف : "+ drugsArray.get(position).getDescription());
        holder.drugTimes.setText("عدد المرات : "+ drugsArray.get(position).getTimes());
        holder.drugSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Switched", Toast.LENGTH_SHORT).show();
                //Jareer code here
            }
        });

    }

    @Override
    public int getItemCount() {
        return drugsArray.size();
    }

    public void purchaseDrug(int position) {
        String id = drugsArray.get(position).getId();
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = Config.URL+ "drugs/purchaseDrug";

        Map<String, String> params = new HashMap<String, String>();
        params.put("drug_id", id);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.PUT, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                drugsArray.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, errorDescription, Toast.LENGTH_SHORT).show();
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
