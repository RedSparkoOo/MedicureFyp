package com.example.docportal.Pharmacist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.docportal.Patient.BuyMedicine;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MedicalEquipmentList extends AppCompatActivity {
    RecyclerView _equipmentList;
    EquipmentListAdapter equipmentListAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("Medical_Equipment");
    TextView title;
    EditText search;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(MedicalEquipmentList.this, EditEquipment.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MedicalEquipment> options = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                .setQuery(query, MedicalEquipment.class).build();
        equipmentListAdapter = new  EquipmentListAdapter (options);
        _equipmentList = findViewById(R.id.medicine_list);
        _equipmentList .setLayoutManager(new WrapContentLinearLayoutManager(MedicalEquipmentList.this,LinearLayoutManager.VERTICAL, false ));


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String aquery = charSequence.toString().toLowerCase();
                Query filteredQuery = noteBookref.orderBy("Title", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
                FirestoreRecyclerOptions<MedicalEquipment> options = new FirestoreRecyclerOptions.Builder<MedicalEquipment>()
                        .setQuery(filteredQuery, MedicalEquipment.class).build();
                equipmentListAdapter.updateOptions(options);

            }

            @Override
            public void afterTextChanged(Editable editable) {



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
                MedicalEquipment medicalEquipment = documentSnapshot.toObject(MedicalEquipment.class);
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), EditEquipment.class);
                intent.putExtra("Id", id);
                startActivity(intent);

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