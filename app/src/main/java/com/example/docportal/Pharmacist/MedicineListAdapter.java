package com.example.docportal.Pharmacist;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineListViewHolder> {
    ArrayList<Medicine> dataHolder;
    Context context;
    public MedicineListAdapter(Context context, ArrayList<Medicine> dataHolder){
        this.dataHolder = dataHolder;
        this.context = context;

    }

    @NonNull
    @Override
    public MedicineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.medicine_list_layout,parent,false);
        return new MedicineListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position) {
        Medicine medicine = dataHolder.get(position);
        holder.title.setText(medicine.getTitle());
        holder.description.setText(medicine.getDescription());
        holder.price_main.setText(medicine.getPrice());
        holder.milligram.setText(medicine.getMilligram());
        holder.image.setImageBitmap(medicine.getImage());
        holder.quantity.setText(medicine.getQunatity());




    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    public class MedicineListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView title, description, price_main, quantity, milligram;
        ImageView image;
        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.medi_picture);
            title = itemView.findViewById(R.id._title);
            description = itemView.findViewById(R.id._description);
            quantity = itemView.findViewById(R.id._Quantity);
            milligram = itemView.findViewById(R.id._milligram);
            price_main = itemView.findViewById(R.id.Price_main);
            title.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAbsoluteAdapterPosition(),0,0,"Edit item");
            menu.add(this.getAbsoluteAdapterPosition(),1,1,"delete");

        }
    }


}