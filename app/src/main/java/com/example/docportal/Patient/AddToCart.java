package com.example.docportal.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.docportal.Pharmacist.MedicalEquipmentAdapter;
import com.example.docportal.Pharmacist.MedicalEquipment;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AddToCart extends AppCompatActivity {
    RecyclerView _cartList;
    AddToCartAdapter addToCartAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Integer Price, count;

    TextView TotalPrice;
    CollectionReference noteBookref = firestore.collection("Cart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        TotalPrice = findViewById(R.id.tprice);
        setUpRecyclerView();

    }
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query, Medicine.class).build();
        addToCartAdapter = new AddToCartAdapter(options);
        count = addToCartAdapter.getItemCount();
        _cartList = findViewById(R.id.addToCartRecycler);
        _cartList.setLayoutManager(new LinearLayoutManager(this));
        _cartList.setAdapter(addToCartAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                addToCartAdapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(_cartList);

    }

    @Override
    protected void onStart() {
        super.onStart();
        addToCartAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        addToCartAdapter.stopListening();
    }
}