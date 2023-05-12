package com.example.docportal.Patient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
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

public class bloodBankOptions extends AppCompatActivity {

    RecyclerView bloodList;
    BloodBankAdapter bloodBankAdapter;

    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("bloodata");
    Button _pharmacyAddToCart;
    Singleton singleton = new Singleton();

    private String id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_options);
        id = getIntent().getStringExtra("bloodId");
        name = getIntent().getStringExtra("name");

        _pharmacyAddToCart = findViewById(R.id.bloodToCart);


        setUpRecycler();

        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(bloodBankOptions.this, CheckoutActivityJava.class);
            }
        });
    }


    private void setUpRecycler() {

        Query query = noteBookref.orderBy("bloodBankId", Query.Direction.DESCENDING).startAt(id).endAt(id);
        FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
                .setQuery(query, BloodBankModel.class).build();
        bloodBankAdapter = new BloodBankAdapter(options);
        bloodList = findViewById(R.id.search_blood_group_recycler);

        bloodList.setLayoutManager(new WrapContentLinearLayoutManager(bloodBankOptions.this, LinearLayoutManager.VERTICAL, false));
        bloodList.setAdapter(bloodBankAdapter);

//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    String aquery = charSequence.toString().toLowerCase();
//                    Query filteredQuery = noteBookref.orderBy("category", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
//                    FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
//                            .setQuery(filteredQuery, BloodBankModel.class).build();
//                    bloodBankAdapter.updateOptions(options);
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//
//
//                }
//            });
        bloodBankAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
            @Override
            public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {

                String price = ((TextView) bloodList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.blood_unit_price)).getText().toString();
                String quantity = ((TextView) bloodList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.blood_unit_quantity)).getText().toString();
                String totalQuantity = ((TextView) bloodList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.lab_quantity_group)).getText().toString();
                if (totalQuantity.equals("Out of stock")) ;
                else {
                    String id = documentSnapshot.getId();

                    DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("bloodata").document(id);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", String.valueOf(firestoreHandler.getCurrentUser()));
                            map.put("Title", value.getString("name"));

                            map.put("Price", price);
                            map.put("Quantity", quantity);
                            map.put("Milligram", value.getString(("donor")));
                            map.put("Description", value.getString("acceptor"));
                            map.put("Identifier", "blood");
                            map.put("Image", value.getString("logoUrl"));
                            map.put("seller", name);
                            CollectionReference documentReference = firestoreHandler.getFirestoreInstance().collection("Cart");
                            documentReference.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    singleton.showToast(bloodBankOptions.this, "Item added to cart");
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
        bloodBankAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        bloodBankAdapter.stopListening();
    }


}