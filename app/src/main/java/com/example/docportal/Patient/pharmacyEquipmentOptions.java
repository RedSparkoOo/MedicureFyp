package com.example.docportal.Patient;

import static com.example.docportal.R.layout.spinner_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class pharmacyEquipmentOptions extends AppCompatActivity {
    RecyclerView medicalProductRecyclerview;
    List<String> productNames;
    List<String> productPrice;
    pharmacyAdapter pharmacyAdapter;
    Button _pharmacyAddToCart;
    Spinner pharmacyCategory;

    String[] PharmacyCategory = {"","Medicine","Medical Equipment"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_equipment_options);


        _pharmacyAddToCart = findViewById(R.id.pharmacyToCart);
        pharmacyCategory = findViewById(R.id.pharmacyCategory);



        ArrayAdapter arrayAdapterPharmacy = new ArrayAdapter(this, spinner_item,PharmacyCategory);
        arrayAdapterPharmacy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pharmacyCategory.setAdapter(arrayAdapterPharmacy);


        medicalProductRecyclerview = findViewById(R.id.medicalProductRecycler);
        productNames = new ArrayList<>();
        productNames.add("Panadol");
        productNames.add("Panadol Extra");
        productNames.add("Disprin");
        productNames.add("Libreix");
        productNames.add("Burgin");
        productNames.add("Latex Gloves");
        productNames.add("Syringe");
        productNames.add("Test Tube");
        productNames.add("Thermometer");
        productNames.add("Temperature strip");

        productPrice = new ArrayList<>();
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");
        productPrice.add("30 Pkr");

        medicalProductRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        pharmacyAdapter = new pharmacyAdapter(productNames,productPrice);
        medicalProductRecyclerview.setAdapter(pharmacyAdapter);

        _pharmacyAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pharmacyEquipmentOptions.this,AddToCart.class);
                startActivity(intent);

            }
        });
    }
}