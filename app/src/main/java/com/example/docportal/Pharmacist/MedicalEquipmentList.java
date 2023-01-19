package com.example.docportal.Pharmacist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.docportal.R;

import java.util.ArrayList;

public class MedicalEquipmentList extends AppCompatActivity {
    RecyclerView _equipmentList;
    ArrayList<MedicalEquipment> dataholder = new ArrayList<>();

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
        setContentView(R.layout.activity_medical_equipment_list);
        _equipmentList = findViewById(R.id.medical_equipment_list);
        _equipmentList.setLayoutManager(new LinearLayoutManager(this));






        }
}