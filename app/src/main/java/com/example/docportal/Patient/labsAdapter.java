package com.example.docportal.Patient;

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

public class labsAdapter extends FirestoreRecyclerAdapter<BloodBankModel, labsAdapter.BloodBankViewHolder> {


    private MedicineListAdapter.onItemClickListener listener1;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public labsAdapter(@NonNull FirestoreRecyclerOptions<BloodBankModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position, @NonNull BloodBankModel model) {
        Singleton singleton = new Singleton();
        holder.name.setText(model.getLabName());
        holder.city.setText("Islamabad");
        holder.time.setText(model.getTiming());
        String imageUri;
        imageUri = model.getLogoUrl();
        Picasso.get().load(imageUri).into(holder.image);


        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.openActivity(view.getContext(), allMaps.class);
            }
        });
        holder.service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                String name = snapshot.getString("labName");
                view.getContext().startActivity(singleton.getIntent(view.getContext(), labTestManagement_options.class).putExtra("name", name));
            }
        });
    }

    @NonNull
    @Override
    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.labs_bank, parent, false);
        return new BloodBankViewHolder(view);


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

        TextView name, city, time;
        Button location, service;
        ImageView image;


        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);

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
                    if (position != RecyclerView.NO_POSITION && listener1 != null) {
                        listener1.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
            service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener1 != null) {
                            listener1.onItemClick(getSnapshots().getSnapshot(position), position);

                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });


        }


    }
}
