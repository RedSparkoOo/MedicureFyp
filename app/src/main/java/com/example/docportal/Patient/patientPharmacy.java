package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;


public class patientPharmacy extends AppCompatActivity {
    Button patientPharmacyMedicine;
    Button patientPharmacyEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_service);

        patientPharmacyMedicine = findViewById(R.id.pharmacyMedicines);
        patientPharmacyEquipment = findViewById(R.id.pharmacyEquipments);

        patientPharmacyMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientPharmacy.this, BuyMedicine.class);
                startActivity(intent);
            }
        });

        patientPharmacyEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientPharmacy.this, BuyMedicalEquipment.class);
                startActivity(intent);
            }
        });


    }
}