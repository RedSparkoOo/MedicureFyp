package com.example.docportal.Patient;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class BanksAdapter extends FirestoreRecyclerAdapter<BloodBankModel, BanksAdapter .BloodBankViewHolder> {
    private MedicineListAdapter.onItemLongClickListener listener;

    private MedicineListAdapter.onItemClickListener listener1;

    public Integer getI() {
        return i;
    }

    public Integer getPrice() {
        return price;
    }

    private Integer i, price;
    private String Quantity, Price;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BanksAdapter (@NonNull FirestoreRecyclerOptions<BloodBankModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position, @NonNull BloodBankModel model) {

        try {
            holder.name.setText(model.getBloodBankName());
            holder.city.setText("Islamabad");
            holder.time.setText(model.getTiming());
            String imageUri;
            imageUri = model.getLogoUrl();
            Picasso.get().load(imageUri).into(holder.image);




            holder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), allMaps.class ));



                }
            });
            holder.service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), bloodBankOptions.class ));
                }
            });
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }


    @NonNull
    @Override
    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.labs_bank, parent, false);
        return new BloodBankViewHolder(view);


    }

    public class  BloodBankViewHolder extends RecyclerView.ViewHolder {

        TextView name, city, time;
        Button location, service;
        ImageView image;


        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            try{
                image = itemView.findViewById(R.id.display_lab_logo);

                name = itemView.findViewById(R.id.lab_name);
                city = itemView.findViewById(R.id.lab_region);
                time = itemView.findViewById(R.id.lab_time);


                location = itemView.findViewById(R.id.directions);
                service = itemView.findViewById(R.id.services);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener1.onItemClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                });
                service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int position = getAbsoluteAdapterPosition();
                            if (position != RecyclerView.NO_POSITION && listener != null) {
                                listener1.onItemClick(getSnapshots().getSnapshot(position), position);

                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onitemlongClick(getSnapshots().getSnapshot(position), position);

                        }
                        return false;
                    }
                });
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }




    }

    public interface onItemLongClickListener {
        void onitemlongClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemLongClickListener(MedicineListAdapter.onItemLongClickListener listener){
        this.listener = listener;
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();


    }
    public void setOnItemClickListener(MedicineListAdapter.onItemClickListener listener){
        this.listener1 = listener;
    }
    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
