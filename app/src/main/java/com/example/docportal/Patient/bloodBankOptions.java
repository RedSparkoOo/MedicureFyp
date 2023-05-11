package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class bloodBankOptions extends AppCompatActivity {

    RecyclerView bloodList;
    BloodBankAdapter bloodBankAdapter;
   // EditText medicineSearch;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("bloodata");
   Button _pharmacyAddToCart;
    FirebaseAuth firebaseAuth;
    private String id;
    private String name;
    Object currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_options);
        id = getIntent().getStringExtra("bloodId");
        name = getIntent().getStringExtra("name");
        System.out.println(id);


        //     medicineSearch = findViewById(R.id.medicineSearch);
        firebaseAuth = FirebaseAuth.getInstance();
        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }

       _pharmacyAddToCart = findViewById(R.id.bloodToCart);


        setUpRecycler();

        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBankOptions.this, CheckoutActivityJava.class);
                startActivity(intent);
            }
        });
    }










    private void  setUpRecycler(){
        try {
            Query query = noteBookref.orderBy("bloodBankId" , Query.Direction.DESCENDING).startAt(id).endAt(id);
            FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
                    .setQuery(query, BloodBankModel.class).build();
            bloodBankAdapter = new BloodBankAdapter(options);
            bloodList = findViewById(R.id.search_blood_group_recycler);

            bloodList.setLayoutManager(new WrapContentLinearLayoutManager(bloodBankOptions.this,LinearLayoutManager.VERTICAL, false ));
            bloodList.setAdapter(bloodBankAdapter);

//            medicineSearch.addTextChangedListener(new TextWatcher() {
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


            bloodBankAdapter.setOnItemClickListener(new MedicineListAdapter.onItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot snapshot, int position) {

                }
            });
            bloodBankAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
                @Override
                public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {

                    String price = ((TextView) bloodList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.blood_unit_price)).getText().toString();
                    String quantity = ((TextView) bloodList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.blood_unit_quantity)).getText().toString();
                    String totalQuantity = ((TextView) bloodList.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.lab_quantity_group)).getText().toString();
                    if(totalQuantity.equals("Out of stock"));
                    else {
                        String id = documentSnapshot.getId();

                        DocumentReference documentReference = firestore.collection("bloodata").document(id);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", String.valueOf(currentUserId));
                                map.put("Title", value.getString("name"));

                                map.put("Price", price);
                                map.put("Quantity", quantity);
                                map.put("Milligram", value.getString(("donor")));
                                map.put("Description", value.getString("acceptor"));
                                map.put("Identifier", "blood");
                                map.put("Image", value.getString("logoUrl"));
                                map.put("seller", name);
                                CollectionReference documentReference = firestore.collection("Cart");
                                documentReference.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(bloodBankOptions.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                }
            });
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

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