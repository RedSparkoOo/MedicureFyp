package com.example.docportal.Pharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MedicineList extends AppCompatActivity {
    RecyclerView medicineList;

    MedicineListAdapter medicineListAdapter;

    EditText search;
    Singleton singleton;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    CollectionReference noteBookref = firestore.collection("Medicine");
    ImageView back_to_pharmacist_dashboard;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 0) {
            startActivity(new Intent(MedicineList.this, EditMedicine.class));
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        search = findViewById(R.id.medicineSearch);

        setUpRecyclerView();

        back_to_pharmacist_dashboard = findViewById(R.id.back_to_pharmacist_dashboard);

        Intent intent = new Intent(MedicineList.this, PharmacistDashboard.class);
        startActivity(intent);



    }

    private void setUpRecyclerView() {
        Query query = noteBookref.whereEqualTo("Id", firestoreHandler.getCurrentUser())
                .orderBy("Title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query, Medicine.class).build();
        medicineListAdapter = new MedicineListAdapter(options);
        medicineList = findViewById(R.id.medicine_list);
        medicineList.setLayoutManager(new WrapContentLinearLayoutManager(MedicineList.this, LinearLayoutManager.VERTICAL, false));

        medicineList.setAdapter(medicineListAdapter);
        search.addTextChangedListener(new TextWatcher() {
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
                            .whereEqualTo("Id", firestoreHandler.getCurrentUser())
                            .orderBy("Title", Query.Direction.DESCENDING);
                } else {
                    newQuery = noteBookref
                            .whereEqualTo("Id", firestoreHandler.getCurrentUser())
                            .orderBy("Title")
                            .startAt(query)
                            .endAt(query + "\uf8ff");
                }
                FirestoreRecyclerOptions<Medicine> newOptions = new FirestoreRecyclerOptions.Builder<Medicine>()
                        .setQuery(newQuery, Medicine.class)
                        .build();
                medicineListAdapter.updateOptions(newOptions);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changed
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                medicineListAdapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(medicineList);
        medicineListAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
            @Override
            public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {
                singleton = new Singleton();
                Medicine medicine = documentSnapshot.toObject(Medicine.class);
                String id = documentSnapshot.getId();
                startActivity(singleton.getIntent(getApplicationContext(), EditMedicine.class).putExtra("Id", id));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        medicineListAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        medicineListAdapter.stopListening();
    }

}
