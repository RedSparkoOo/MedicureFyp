package com.example.docportal.Patient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Pharmacist.MedicalEquipment;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class BuyMedicalEquipment extends AppCompatActivity {
    RecyclerView _equipmentList;
    Button _pharmacyAddToCart;
    BuyMedicalEquipmentAdapter medicalEquipmentAdapter;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Medical_Equipment");

    EditText editText;
    TextView select, list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_equipment_options);
        select = findViewById(R.id.select);
        list = findViewById(R.id.list_med);
        select.setText("Select Medical Equipment");
        list.setText("List of Medical Equipment");


        _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);
        editText = findViewById(R.id.medicineSearch);

        setUpRecyclerView();


        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(BuyMedicalEquipment.this, CheckoutActivityJava.class);
            }
        });

    }

    private void setUpRecyclerView() {
        Query query = noteBookref.orderBy("Title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MedicalEquipment> options = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                .setQuery(query, MedicalEquipment.class).build();
        medicalEquipmentAdapter = new BuyMedicalEquipmentAdapter(options);
        _equipmentList = findViewById(R.id.medicalProductRecycler);
        _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(BuyMedicalEquipment.this, LinearLayoutManager.VERTICAL, false));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query = charSequence.toString();
                Query newQuery;
                if (query.trim().isEmpty()) {
                    newQuery = noteBookref.orderBy("Title", Query.Direction.DESCENDING);
                } else {
                    // Create a new query for case-insensitive search
                    newQuery = noteBookref.whereGreaterThanOrEqualTo("Title", query)
                            .whereLessThanOrEqualTo("Title", query + "\uf8ff")
                            .orderBy("Title");
                }
                FirestoreRecyclerOptions<MedicalEquipment> newOptions = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                        .setQuery(newQuery, MedicalEquipment.class)
                        .build();
                medicalEquipmentAdapter.updateOptions(newOptions);

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        _equipmentList.setAdapter(medicalEquipmentAdapter);

        medicalEquipmentAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
            @Override
            public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {
                MedicalEquipment medicalEquipment = documentSnapshot.toObject(MedicalEquipment.class);
                String id = documentSnapshot.getId();
                String price = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productPrice)).getText().toString();
                String quantity = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productCount)).getText().toString();

                String totalQuantity = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productQuantity)).getText().toString();
                if (totalQuantity.equals("Out of stock")) ;
                else {
                    DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medical_Equipment").document(id);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", String.valueOf(firestoreHandler.getCurrentUser()));
                            map.put("Title", value.getString("Title"));
                            map.put("Image", value.getString("Image"));
                            map.put("Price", price);
                            map.put("Quantity", quantity);
                            map.put("Description", value.getString("Description"));
                            map.put("seller", "Pharmacist");
                            CollectionReference documentReference = firestoreHandler.getFirestoreInstance().collection("Cart");
                            documentReference.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    singleton.showToast(BuyMedicalEquipment.this, "Item added to cart");
                                }
                            });

                        }
                    });
                }
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