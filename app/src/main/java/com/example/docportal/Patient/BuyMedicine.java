package com.example.docportal.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.Pharmacist.MedicalEquipment;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class BuyMedicine extends AppCompatActivity {
    RecyclerView _equipmentList;
    BuyMedicalAdapter medicalEquipmentAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("Medicine");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        setUpRecyclerView();

    }
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query, Medicine.class).build();
        medicalEquipmentAdapter = new  BuyMedicalAdapter (options);
        _equipmentList = findViewById(R.id.medicine_list);
        _equipmentList.setLayoutManager(new LinearLayoutManager(this));
        _equipmentList.setAdapter(medicalEquipmentAdapter);

        medicalEquipmentAdapter.setOnItemClickListener(new MedicineListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {

            }
        });
        medicalEquipmentAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
            @Override
            public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {
                Medicine medicalEquipment = documentSnapshot.toObject(Medicine.class);
                String id = documentSnapshot.getId();
                String price = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.eprice)).getText().toString();
                String quantity = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.equantity)).getText().toString();


                DocumentReference documentReference = firestore.collection("Medicine").document(id);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("Title", value.getString("Title"));
                        map.put("Image", value.getString("Image"));
                        map.put("Price",  price);
                        map.put("Quantity", quantity);
                        map.put("Milligram", value.getString(("Milligram")));
                        map.put("Description",value.getString("Description"));
                        DocumentReference documentReference = firestore.collection("Cart").document(id);
                        documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(BuyMedicine.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        medicalEquipmentAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        medicalEquipmentAdapter.stopListening();
    }
}