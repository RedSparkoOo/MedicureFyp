package com.example.docportal.Pharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Patient.BuyMedicine;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MedicineList extends AppCompatActivity {
    RecyclerView medicineList;

    MedicineListAdapter medicineListAdapter;

    EditText search;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    CollectionReference noteBookref = firestore.collection("Medicine");
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(MedicineList.this, EditMedicine.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        search = findViewById(R.id.medicineSearch);

        setUpRecyclerView();




    }
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query,Medicine.class).build();
        medicineListAdapter = new MedicineListAdapter(options);
        medicineList = findViewById(R.id.medicine_list);
        medicineList.setLayoutManager(new WrapContentLinearLayoutManager(MedicineList.this,LinearLayoutManager.VERTICAL, false ));


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String aquery = charSequence.toString().toLowerCase();
                Query filteredQuery = noteBookref.orderBy("Title", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
                FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                        .setQuery(filteredQuery, Medicine.class).build();
                medicineListAdapter.updateOptions(options);

            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });
        medicineList.setAdapter(medicineListAdapter);
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
                    Medicine medicine = documentSnapshot.toObject(Medicine.class);
                    String id = documentSnapshot.getId();
                    Intent intent = new Intent(getApplicationContext(), EditMedicine.class);
                    intent.putExtra("Id", id);
                    startActivity(intent);
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
