package com.example.docportal.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.Pharmacist.MedicalEquipment;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.sql.SQLOutput;
import java.util.HashMap;

public class BuyMedicalEquipment extends AppCompatActivity {
    RecyclerView _equipmentList;
    Button _pharmacyAddToCart;
    BuyMedicalEquipmentAdapter medicalEquipmentAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("Medical_Equipment");
    FirebaseAuth firebaseAuth;
    EditText editText;
    TextView select, list;
    Object currentUserId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_equipment_options);
        select = findViewById(R.id.select);
        list = findViewById(R.id.list_med);
        select.setText("Select Medical Equipment");
        list.setText("List of Medical Equipment");
        firebaseAuth= FirebaseAuth.getInstance();
        Object currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null) {
             currentUserId = firebaseAuth.getCurrentUser().getUid();
        }

        _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);
        editText = findViewById(R.id.medicineSearch);

        try {
            setUpRecyclerView();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyMedicalEquipment.this,CheckoutActivityJava.class);
                startActivity(intent);
            }
        });

    }
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MedicalEquipment> options = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                .setQuery(query, MedicalEquipment.class).build();
        medicalEquipmentAdapter = new BuyMedicalEquipmentAdapter(options);
        _equipmentList = findViewById(R.id.medicalProductRecycler);
        _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(BuyMedicalEquipment.this,LinearLayoutManager.VERTICAL, false ));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String aquery = charSequence.toString().toLowerCase();
                Query filteredQuery = noteBookref.orderBy("Title", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
                FirestoreRecyclerOptions<MedicalEquipment> options = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                        .setQuery(filteredQuery, MedicalEquipment.class).build();
                medicalEquipmentAdapter.updateOptions(options);

            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });
        _equipmentList.setAdapter(medicalEquipmentAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                medicalEquipmentAdapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(_equipmentList);
        medicalEquipmentAdapter.setOnItemClickListener(new MedicineListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {

            }
        });
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


                    DocumentReference documentReference = firestore.collection("Medical_Equipment").document(id);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", String.valueOf(currentUserId));
                            map.put("Title", value.getString("Title"));
                            map.put("Image", value.getString("Image"));
                            map.put("Price", price);
                            map.put("Quantity", quantity);
                            map.put("Description", value.getString("Description"));
                            map.put("seller", "Pharmacist");
                            DocumentReference documentReference = firestore.collection("Cart").document(id);
                            documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(BuyMedicalEquipment.this, "Item added to cart", Toast.LENGTH_SHORT).show();
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