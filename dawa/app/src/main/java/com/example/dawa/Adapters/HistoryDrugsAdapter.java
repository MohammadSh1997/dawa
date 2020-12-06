package com.example.dawa.Adapters;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.dawa.EditDrug;
import com.example.dawa.HistoryDrugs;
import com.example.dawa.Models.Drug;
import com.example.dawa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryDrugsAdapter extends  RecyclerView.Adapter<HistoryDrugsAdapter.viewitem> {

    ArrayList<Drug> drugsArray;
    Context context;

    public HistoryDrugsAdapter(Context context, ArrayList<Drug> drugsArray) {
        this.context = context;
        this.drugsArray = drugsArray;
    }

    public class viewitem extends RecyclerView.ViewHolder {
        TextView drugName , drugDesc , drugTimes;
        ImageView editImage;

        public viewitem(@NonNull View itemView) {
            super(itemView);
             drugName = itemView.findViewById(R.id.historyDrugName);
             drugDesc = itemView.findViewById(R.id.historyDrugDesc);
             drugTimes = itemView.findViewById(R.id.historyDrugTimes);
             editImage = itemView.findViewById(R.id.editImage);
        }
    }

    @NonNull
    @Override
    public HistoryDrugsAdapter.viewitem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_history_drug_item, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return new viewitem(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDrugsAdapter.viewitem holder, int position) {
        holder.drugName.setText("الدواء : "+ drugsArray.get(position).getName());
        holder.drugDesc.setText("الوصف : "+ drugsArray.get(position).getDescription());
        holder.drugTimes.setText("عدد المرات : "+ drugsArray.get(position).getTimes());
        holder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, ""+drugsArray.get(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context , EditDrug.class);
                i.putExtra("drug_id" , drugsArray.get(position).getId());
                i.putExtra("drug_name" , drugsArray.get(position).getName());
                i.putExtra("drug_desc" , drugsArray.get(position).getDescription());
                i.putExtra("drug_times" , drugsArray.get(position).getTimes());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugsArray.size();
    }
}
