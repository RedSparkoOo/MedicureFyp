package com.example.docportal.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.Pharmacist.MedicalEquipment;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AddToCart extends AppCompatActivity {
    RecyclerView _cartList;
    AddToCartAdapter addToCartAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Integer Price, count;

    TextView TotalPrice;
    Button payment;
    FirebaseAuth firebaseAuth;
    Object currentUserId;
    double totalPrice = 0;
    CollectionReference noteBookref = firestore.collection("Cart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        TotalPrice = findViewById(R.id.tprice);
        payment = (Button) findViewById(R.id.stripe_payment);
        firebaseAuth= FirebaseAuth.getInstance();

        Object currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
        try {
            setUpRecyclerView();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddToCart.this, CheckoutActivityJava.class));
            }
        });


    }
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query, Medicine.class).build();



        addToCartAdapter = new AddToCartAdapter(options);
        count = addToCartAdapter.getItemCount();
        _cartList = findViewById(R.id.addToCartRecycler);
        _cartList.setLayoutManager(new LinearLayoutManager(this));
        String aquery = currentUserId.toString();
        Query filteredQuery = noteBookref.orderBy("id", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
        FirestoreRecyclerOptions<Medicine> optionss = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(filteredQuery, Medicine.class).build();
        filteredQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(AddToCart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                totalPrice = 0;
                for (QueryDocumentSnapshot document : value) {
                    Medicine item = document.toObject(Medicine.class);
                    totalPrice += Integer.parseInt(item.getPrice())  * Integer.parseInt(item.getQuantity());
                }
                TotalPrice.setText(String.valueOf(totalPrice) );


            }

        });
        addToCartAdapter.updateOptions(optionss);
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