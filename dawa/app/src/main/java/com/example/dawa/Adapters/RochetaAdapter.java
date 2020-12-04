package com.example.dawa.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dawa.Models.Rocheta;
import com.example.dawa.R;
import com.example.dawa.RochetaDrugs;

import java.util.ArrayList;

public class RochetaAdapter extends ArrayAdapter<Rocheta> {

    ArrayList<Rocheta> rochetaArray;
    Context context;
    public RochetaAdapter(@NonNull Context context , ArrayList<Rocheta> rochetaArray) {
        super(context, R.layout.custom_list_item , rochetaArray);
        this.context = context;
        this.rochetaArray = rochetaArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, null, true);
        LinearLayout item = view.findViewById(R.id.drugItem);
        TextView doctorText= view.findViewById(R.id.rochetaHistoryPatient);
        TextView dateText= view.findViewById(R.id.rochetaHistoryDate);
        doctorText.setText(doctorText.getText().toString() + rochetaArray.get(position).getDoctor());
        dateText.setText(dateText.getText().toString() + rochetaArray.get(position).getDate());
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext() , RochetaDrugs.class);
                i.putExtra("rocheta_id" , rochetaArray.get(position).getId());
                getContext().startActivity(i);

            }
        });
        return view;
    }

}
