package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class cartForm extends AppCompatActivity {
    EditText _formPatientName;
    EditText _formPatientPhone;
    EditText _formPatientAddress;

    Button _toPay;

    RecyclerView _formRecycler;

    List<String> _formProductNames;
    List<String> _formProductPrice;
    List<String> _formProductQuantity;

    cartFormAdapter cartFormAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_form);

        _formPatientName = findViewById(R.id.formPatientName);
        _formPatientPhone = findViewById(R.id.formPatientPhone);
        _formPatientAddress = findViewById(R.id.formPatientAddress);

        _toPay = findViewById(R.id.toPayment);

        _formRecycler = findViewById(R.id.formRecyclerView);

        _formProductNames = new ArrayList<>();
        _formProductNames.add("Panadol");
        _formProductNames.add("Paracetamol");
        _formProductNames.add("Brufin");
        _formProductNames.add("Latex golves");
        _formProductNames.add("Syringe");
        _formProductNames.add("Cecon");
        _formProductNames.add("Disprin");

        _formProductPrice = new ArrayList<>();
        _formProductPrice.add("$5");
        _formProductPrice.add("$6");
        _formProductPrice.add("$7");
        _formProductPrice.add("$4");
        _formProductPrice.add("$9");
        _formProductPrice.add("$4");
        _formProductPrice.add("$3");

        _formProductQuantity = new ArrayList<>();
        _formProductQuantity.add("1");
        _formProductQuantity.add("3");
        _formProductQuantity.add("2");
        _formProductQuantity.add("1");
        _formProductQuantity.add("5");
        _formProductQuantity.add("2");
        _formProductQuantity.add("8");

        _formRecycler = findViewById(R.id.formRecyclerView);
        _formRecycler.setLayoutManager(new LinearLayoutManager(this));
        cartFormAdapter = new cartFormAdapter(_formProductNames,_formProductPrice, _formProductQuantity);
        _formRecycler.setAdapter(cartFormAdapter);

        _toPay = findViewById(R.id.toPayment);
        _toPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cartForm.this,paymentConfirmation.class);
                startActivity(intent);
            }
        });




    }
}