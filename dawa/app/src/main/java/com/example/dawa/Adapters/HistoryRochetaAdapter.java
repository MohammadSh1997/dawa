package com.example.dawa.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.dawa.HistoryDrugs;
import com.example.dawa.Models.Drug;
import com.example.dawa.Models.Rocheta;
import com.example.dawa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryRochetaAdapter extends  RecyclerView.Adapter<HistoryRochetaAdapter.viewitem> {

    ArrayList<Rocheta> rochetaArray;
    Context context;

    public HistoryRochetaAdapter(Context context, ArrayList<Rocheta> rochetaArray) {
        this.rochetaArray = rochetaArray;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryRochetaAdapter.viewitem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_doctor_history_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return new HistoryRochetaAdapter.viewitem(itemView);
    }

    public class viewitem extends RecyclerView.ViewHolder {

        TextView rochetaDate, rochetaPatient;
        ImageView deleteImage;
        LinearLayout historyRochetaItem;
        String RochetaId;
        public viewitem(@NonNull View itemView) {
            super(itemView);
            rochetaDate =  itemView.findViewById(R.id.rochetaHistoryDate);
            rochetaPatient =  itemView.findViewById(R.id.rochetaHistoryPatient);
            deleteImage = itemView.findViewById(R.id.deleteImage);
            historyRochetaItem = itemView.findViewById(R.id.historyRochetaItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRochetaAdapter.viewitem holder, int position) {
    holder.rochetaDate.setText(rochetaArray.get(position).getDate());
    holder.rochetaPatient.setText(rochetaArray.get(position).getPatient());

    // for delete rocheta in history
    holder.deleteImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteDialog(position).show();
        }
    });
    // for click on item
    holder.historyRochetaItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, HistoryDrugs.class);
            i.putExtra("rocheta_id" , rochetaArray.get(position).getId());
            v.getContext().startActivity(i);
        }
    });
    }

    @Override
    public int getItemCount() {
        return rochetaArray.size();
    }

    private AlertDialog deleteDialog(int position) {
        AlertDialog alertDialog =new AlertDialog.Builder(context)
                .setTitle("حذف")
                .setMessage("هل انت متأكد من حذف هذه الروشيتا ؟")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteRocheta(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return alertDialog;
    }

    public void deleteRocheta(int position) {
        String rocheta_id = rochetaArray.get(position).getId();
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = Config.URL+ "rocheta/"+ rocheta_id;

        Map<String, String> params = new HashMap<String, String>();
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                rochetaArray.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "حدث خطا في الاتصال", Toast.LENGTH_SHORT).show();
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
