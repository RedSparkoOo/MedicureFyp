package com.example.docportal.Pharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicineList extends AppCompatActivity {
    RecyclerView medicineList;
    ArrayList<Medicine> dataholder;
    MedicineListAdapter medicineListAdapter;
    FirebaseFirestore firestore;

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

        medicineList = findViewById(R.id.medicine_list);
        medicineList.setHasFixedSize(true);
        medicineList.setLayoutManager(new LinearLayoutManager(this));
        firestore = FirebaseFirestore.getInstance();
        dataholder = new ArrayList<Medicine>();
        medicineListAdapter = new MedicineListAdapter(MedicineList.this,dataholder);
        medicineList.setAdapter(medicineListAdapter);


    }

}
