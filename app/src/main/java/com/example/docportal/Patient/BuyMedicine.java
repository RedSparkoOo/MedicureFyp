package com.example.docportal.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.Pharmacist.MedicalEquipment;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class BuyMedicine extends AppCompatActivity {
    RecyclerView _equipmentList;
    BuyMedicalAdapter buyMedicalAdapter;
    EditText editText;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("Medicine");
    Button _pharmacyAddToCart;
    FirebaseAuth firebaseAuth;
    DataSnapshot dataSnapshot;
    CollectionReference databaseReference;
    ArrayList<Medicine> arrayList;
    Object currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_equipment_options);
        try {
            editText = findViewById(R.id.medicineSearch);
            firebaseAuth = FirebaseAuth.getInstance();
            Object currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                currentUserId = firebaseAuth.getCurrentUser().getUid();
            }

            _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);


            _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);
            try {
                setUpRecycler();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyMedicine.this,CheckoutActivityJava.class);
                startActivity(intent);
            }
        });

    }


    private void  setUpRecycler(){
        try {
            Query query = noteBookref.orderBy("Title", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                    .setQuery(query, Medicine.class).build();
            buyMedicalAdapter = new BuyMedicalAdapter(options);
            _equipmentList = findViewById(R.id.medicalProductRecycler);

                _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(BuyMedicine.this,LinearLayoutManager.VERTICAL, false ));


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String aquery = charSequence.toString().toLowerCase();
                Query filteredQuery = noteBookref.orderBy("Title", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
                FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                        .setQuery(filteredQuery, Medicine.class).build();
                buyMedicalAdapter.updateOptions(options);

            }

            @Override
            public void afterTextChanged(Editable editable) {



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
                    if(totalQuantity.equals("Out of stock"));
                    else {
                        String id = documentSnapshot.getId();

                        DocumentReference documentReference = firestore.collection("Medicine").document(id);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", String.valueOf(currentUserId));
                                map.put("Title", value.getString("Title"));
                                map.put("Image", value.getString("Image"));
                                map.put("Price", price);
                                map.put("Quantity", quantity);
                                map.put("Milligram", value.getString(("Milligram")));
                                map.put("Description", value.getString("Description"));
                                map.put("seller", "Pharmacist");

                                DocumentReference documentReference = firestore.collection("Cart").document(currentUserId.toString());
                                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(BuyMedicine.this, "Item added to cart", Toast.LENGTH_SHORT).show();
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
        buyMedicalAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        buyMedicalAdapter.stopListening();
    }




}