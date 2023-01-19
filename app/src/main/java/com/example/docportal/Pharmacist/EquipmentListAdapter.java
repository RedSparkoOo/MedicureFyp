package com.example.docportal.Pharmacist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docportal.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EquipmentListAdapter extends RecyclerView.Adapter<EquipmentListAdapter.EquipmentListViewHolder> {
    ArrayList<MedicalEquipment> dataHolder;
    FirebaseFirestore firestore;

    public EquipmentListAdapter(ArrayList<MedicalEquipment> dataHolder) {
        this.dataHolder = dataHolder;

    }

    @NonNull
    @Override
    public EquipmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list_layout, parent, false);
        return new EquipmentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentListViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    public class EquipmentListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView title, description, price;

        public EquipmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titles);
            description = itemView.findViewById(R.id.descriptions);
            price = itemView.findViewById(R.id.prices);
            title.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAbsoluteAdapterPosition(), 0, 0, "Edit item");
            menu.add(this.getAbsoluteAdapterPosition(), 1, 1, "delete");

        }
    }
}


