package com.example.dawa.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.dawa.Doctor;
import com.example.dawa.Login;
import com.example.dawa.Models.Drug;
import com.example.dawa.Models.Rocheta;
import com.example.dawa.R;
import com.example.dawa.RochetaDrugs;
import com.example.dawa.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrugsAdapter extends  RecyclerView.Adapter<DrugsAdapter.viewitem> {

    ArrayList<Drug> drugsArray;
    Context context;

    public DrugsAdapter(Context context, ArrayList<Drug> drugsArray) {
        this.context = context;
        this.drugsArray = drugsArray;
    }

    public class viewitem extends RecyclerView.ViewHolder {
        TextView drug;
        CheckBox drugCheck;

        public viewitem(@NonNull View itemView) {
            super(itemView);
             drug = itemView.findViewById(R.id.drugName);
             drugCheck = itemView.findViewById(R.id.drugCheck);
            Log.d("drugs" ,""+ drugsArray);
        }
    }

    @NonNull
    @Override
    public DrugsAdapter.viewitem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_drug_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return new viewitem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugsAdapter.viewitem holder, int position) {
        holder.drug.setText("drug :"+ drugsArray.get(position).getName());
        holder.drugCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseDialog(position).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugsArray.size();
    }

    private AlertDialog purchaseDialog(int position) {
        AlertDialog alertDialog =new AlertDialog.Builder(context)
                .setTitle("Purchase")
                .setMessage("are you purchase this drug ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        purchaseDrug(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return alertDialog;
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
