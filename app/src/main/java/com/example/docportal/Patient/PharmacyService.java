package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.docportal.R;


public class PharmacyService extends AppCompatActivity {
    Button patientPharmacyMedicine;
    Button patientPharmacyEquipment;
    ImageView back_to_patient_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_service);

        patientPharmacyMedicine = findViewById(R.id.pharmacyMedicines);
        patientPharmacyEquipment = findViewById(R.id.pharmacyEquipments);
        back_to_patient_dashboard = findViewById(R.id.back_to_patient_dashboard);

        back_to_patient_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacyService.this,patientDashboard.class);
                startActivity(intent);
            }
        });

        patientPharmacyMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacyService.this,BuyMedicine.class);
                startActivity(intent);
            }
        });

        patientPharmacyEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacyService.this,BuyMedicalEquipment.class);
                startActivity(intent);
            }
        });


    }
}