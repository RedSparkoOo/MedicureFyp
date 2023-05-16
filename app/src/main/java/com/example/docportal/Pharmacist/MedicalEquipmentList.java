package com.example.docportal.Pharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.firestore.Query;

public class MedicalEquipmentList extends AppCompatActivity {
    RecyclerView _equipmentList;
    EquipmentListAdapter equipmentListAdapter;
    Singleton singleton;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Medical_Equipment");
    TextView title;
    EditText search;


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == 0) {
            startActivity(new Intent(MedicalEquipmentList.this, EditEquipment.class));
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        title = findViewById(R.id.add_equip);
        search = findViewById(R.id.medicineSearch);
        title.setText("Add Medical Equipment");
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        Query query = noteBookref.whereEqualTo("Id", firestoreHandler.getCurrentUser()).orderBy("Title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MedicalEquipment> options = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                .setQuery(query, MedicalEquipment.class).build();
        equipmentListAdapter = new EquipmentListAdapter(options);
        _equipmentList = findViewById(R.id.medicine_list);
        _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(MedicalEquipmentList.this, LinearLayoutManager.VERTICAL, false));


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
                FirestoreRecyclerOptions<MedicalEquipment> newOptions = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                        .setQuery(newQuery, MedicalEquipment.class)
                        .build();
                equipmentListAdapter.updateOptions(newOptions);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changed
            }
        });

        _equipmentList.setAdapter(equipmentListAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                equipmentListAdapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(_equipmentList);
        equipmentListAdapter.setOnItemLongClickListener(new MedicineListAdapter.onItemLongClickListener() {
            @Override
            public void onitemlongClick(DocumentSnapshot documentSnapshot, int position) {
                singleton = new Singleton();
                MedicalEquipment medicalEquipment = documentSnapshot.toObject(MedicalEquipment.class);
                String id = documentSnapshot.getId();
                startActivity(singleton.getIntent(getApplicationContext(), EditEquipment.class).putExtra("Id", id));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        equipmentListAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        equipmentListAdapter.stopListening();
    }
}