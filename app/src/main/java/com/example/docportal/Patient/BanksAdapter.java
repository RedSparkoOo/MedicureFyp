package com.example.docportal.Patient;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class BanksAdapter extends FirestoreRecyclerAdapter<BloodBankModel, BanksAdapter.BloodBankViewHolder> {

    Singleton singleton = new Singleton();
    private MedicineListAdapter.onItemLongClickListener listener;
    private MedicineListAdapter.onItemClickListener listener1;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BanksAdapter(@NonNull FirestoreRecyclerOptions<BloodBankModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position, @NonNull BloodBankModel model) {


        holder.name.setText(model.getName());
        holder.city.setText("Islamabad");
        holder.time.setText(model.getTiming());
        if(holder.time2.getText().equals("Islamabad")){
            holder.time2.setText("18:00");
        }
        String imageUri;
        imageUri = model.getLogo();
        Picasso.get().load(imageUri).into(holder.image);


        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();


                bundle.putString("latitude", model.getLatitude());

                bundle.putString("longitude", model.getLongitude());
                bundle.putString("lab_name", model.getLabName());
                view.getContext().startActivity(singleton.getIntent(view.getContext(), allMaps.class).putExtras(bundle));


            }
        });
        holder.service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                String documentId = snapshot.getId();
                String name = snapshot.getString("name");
                view.getContext().startActivity(singleton.getIntent(view.getContext(), bloodBankOptions.class).putExtra("bloodId", documentId).putExtra("name", name));
            }
        });
    }


    @NonNull
    @Override
    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.labs_bank, parent, false);
        return new BloodBankViewHolder(view);


    }

    public void setOnItemLongClickListener(MedicineListAdapter.onItemLongClickListener listener) {
        this.listener = listener;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();


    }

    public void setOnItemClickListener(MedicineListAdapter.onItemClickListener listener) {
        this.listener1 = listener;
    }

    public interface onItemLongClickListener {
        void onitemlongClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public class BloodBankViewHolder extends RecyclerView.ViewHolder {

        TextView name, city, time, time2;
        Button location, service;
        ImageView image;


        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                image = itemView.findViewById(R.id.display_lab_logo);

                name = itemView.findViewById(R.id.lab_name);
                city = itemView.findViewById(R.id.lab_region);
                time = itemView.findViewById(R.id.lab_time);
                time2 = itemView.findViewById(R.id.lab_time2);


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
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }


    }
}
