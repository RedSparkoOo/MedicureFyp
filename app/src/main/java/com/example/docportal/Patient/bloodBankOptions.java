package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class bloodBankOptions extends AppCompatActivity {
    RecyclerView bloodBankRecyclerview;
    List<String> bloodList;

    bloodBankAdapter bloodBankAdapter;

    Button bloodAddtoCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_options);




        bloodAddtoCart = findViewById(R.id.bloodAddtoCart);
        bloodAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBankOptions.this,addToCart.class);
                startActivity(intent);
            }
        });
        bloodBankRecyclerview = findViewById(R.id.bloodBankRecycler);
        bloodList = new ArrayList<>();
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("O+");
        bloodList.add("O-");




        bloodBankRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        bloodBankAdapter = new bloodBankAdapter(bloodList);
        bloodBankRecyclerview.setAdapter(bloodBankAdapter);


    }
}