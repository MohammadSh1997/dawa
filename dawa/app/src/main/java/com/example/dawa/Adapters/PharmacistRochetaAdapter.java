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
import com.example.dawa.Models.Rocheta;
import com.example.dawa.R;
import com.example.dawa.RochetaDrugs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PharmacistRochetaAdapter extends  RecyclerView.Adapter<PharmacistRochetaAdapter.viewitem> {

    ArrayList<Rocheta> rochetaArray;
    Context context;

    public PharmacistRochetaAdapter(Context context, ArrayList<Rocheta> rochetaArray) {
        this.rochetaArray = rochetaArray;
        this.context = context;
    }

    @NonNull
    @Override
    public PharmacistRochetaAdapter.viewitem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return new PharmacistRochetaAdapter.viewitem(itemView);
    }

    public class viewitem extends RecyclerView.ViewHolder {

        TextView rochetaDate, rochetaPatient;
        LinearLayout historyRochetaItem;
        String RochetaId;
        public viewitem(@NonNull View itemView) {
            super(itemView);
            rochetaDate =  itemView.findViewById(R.id.rochetaHistoryDate);
            rochetaPatient =  itemView.findViewById(R.id.rochetaHistoryPatient);
            historyRochetaItem = itemView.findViewById(R.id.historyRochetaItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacistRochetaAdapter.viewitem holder, int position) {
        holder.rochetaDate.setText(rochetaArray.get(position).getDate());
        holder.rochetaPatient.setText(rochetaArray.get(position).getPatient());

        // for click on item
        holder.historyRochetaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RochetaDrugs.class);
                i.putExtra("rocheta_id" , rochetaArray.get(position).getId());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rochetaArray.size();
    }
}