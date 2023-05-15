package com.example.docportal.Patient;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class labTestManagementAdapter extends FirestoreRecyclerAdapter<BloodBankModel, labTestManagementAdapter.BloodBankViewHolder> {
    private final Intent mIntent;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference collectionRef = firestoreHandler.getFirestoreInstance().collection("LabTests");
    private MedicineListAdapter.onItemLongClickListener listener;
    private MedicineListAdapter.onItemClickListener listener1;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public labTestManagementAdapter(Intent intent, @NonNull FirestoreRecyclerOptions<BloodBankModel> options) {

        super(options);
        this.mIntent = intent;
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position, @NonNull BloodBankModel model) {


        holder.name.setText(model.getTestName());

        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                String documentId = snapshot.getId();
                String name = mIntent.getStringExtra("name");


                DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("LabTests").document(documentId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Singleton singleton = new Singleton();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(firestoreHandler.getCurrentUser()));
                        map.put("Title", model.getCategory());
                        map.put("Description", model.getDescription());
                        map.put("Price", model.getPrice());
                        map.put("seller", name);

                        map.put("Image", value.getString("logoUrl"));
                        CollectionReference documentReference = firestoreHandler.getFirestoreInstance().collection("Cart");
                        documentReference.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                singleton.showToast(view.getContext(), "Item added to cart");
                            }
                        });

                    }
                });
            }
        });


    }


    @NonNull
    @Override
    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.labtest_text, parent, false);
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

        TextView name, description, price, addToCart;


        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

                name = itemView.findViewById(R.id.lab_test_name);
                description = itemView.findViewById(R.id.lab_test_description);
                addToCart = itemView.findViewById(R.id.add_to_cart);
                price = itemView.findViewById(R.id.lab_test_price);
                addToCart.setOnClickListener(new View.OnClickListener() {
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
