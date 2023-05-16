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
import com.example.docportal.Pharmacist.Medicine;
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

public class BuyMedicine extends AppCompatActivity {
    RecyclerView _equipmentList;
    BuyMedicalAdapter buyMedicalAdapter;
    EditText editText;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();

    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Medicine");
    Button _pharmacyAddToCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_equipment_options);

        editText = findViewById(R.id.medicineSearch);
        _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);
        _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);
        setUpRecycler();


        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(BuyMedicine.this, CheckoutActivityJava.class);
            }
        });

    }


    private void setUpRecycler() {
        try {
            Query query = noteBookref.orderBy("Title", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                    .setQuery(query, Medicine.class).build();
            buyMedicalAdapter = new BuyMedicalAdapter(options);
            _equipmentList = findViewById(R.id.medicalProductRecycler);

            _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(BuyMedicine.this, LinearLayoutManager.VERTICAL, false));


            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // No action needed before text changed
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String query = charSequence.toString();
                    Query newQuery;
                    if (!query.isEmpty()) {
                        query = query.substring(0, 1).toUpperCase() + query.substring(1);
                    }

                    if (query.trim().isEmpty()) {
                        newQuery = noteBookref
                                .orderBy("Title", Query.Direction.DESCENDING);
                    } else {
                        newQuery = noteBookref
                                .orderBy("Title")
                                .startAt(query)
                                .endAt(query + "\uf8ff");
                    }
                    FirestoreRecyclerOptions<Medicine> newOptions = new FirestoreRecyclerOptions.Builder<Medicine>()
                            .setQuery(newQuery, Medicine.class)
                            .build();
                    buyMedicalAdapter.updateOptions(newOptions);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // No action needed after text changed
                }
            });
            _equipmentList.setAdapter(buyMedicalAdapter);

            buyMedicalAdapter.setOnItemClickListener(new MedicineListAdapter.onItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot snapshot, int position) {

                }
            });
            buyMedicalAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
                @Override
                public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {

                    String price = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productPrice)).getText().toString();
                    String quantity = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productCount)).getText().toString();
                    String totalQuantity = ((TextView) _equipmentList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productQuantity)).getText().toString();
                    if (totalQuantity.equals("Out of stock")) ;
                    else {
                        String id = documentSnapshot.getId();

                        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medicine").document(id);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", String.valueOf(firestoreHandler.getCurrentUser()));
                                map.put("Title", value.getString("Title"));
                                map.put("Image", value.getString("Image"));
                                map.put("Price", price);
                                map.put("Quantity", quantity);
                                map.put("Milligram", value.getString(("Milligram")));
                                map.put("Description", value.getString("Description"));
                                map.put("seller", "Pharmacist");

                                CollectionReference documentReference = firestoreHandler.getFirestoreInstance().collection("Cart");
                                documentReference.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        singleton.showToast(BuyMedicine.this, "Item added to cart");
                                    }
                                });


                            }
                        });
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        buyMedicalAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        buyMedicalAdapter.stopListening();
    }


}