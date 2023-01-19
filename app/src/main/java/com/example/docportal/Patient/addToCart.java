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

public class addToCart extends AppCompatActivity {
    RecyclerView _addToCartRecyclerview;
    List<String> _productNames;
    List<String> _productPrice;
    List<String> _productQuantity;
    addToCartAdapter addToCartAdapter;

    Button _toForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);




          _productNames = new ArrayList<>();
          _productNames.add("Panadol");
          _productNames.add("Paracetamol");
          _productNames.add("Brufin");
          _productNames.add("Latex golves");
          _productNames.add("Syringe");
          _productNames.add("Cecon");
          _productNames.add("Disprin");

          _productPrice = new ArrayList<>();
          _productPrice.add("$5");
          _productPrice.add("$6");
          _productPrice.add("$7");
          _productPrice.add("$4");
          _productPrice.add("$9");
          _productPrice.add("$4");
          _productPrice.add("$3");

          _productQuantity = new ArrayList<>();
          _productQuantity.add("1");
          _productQuantity.add("3");
          _productQuantity.add("2");
          _productQuantity.add("1");
          _productQuantity.add("5");
          _productQuantity.add("2");
          _productQuantity.add("8");

          _addToCartRecyclerview = findViewById(R.id.addToCartRecycler);
          _addToCartRecyclerview.setLayoutManager(new LinearLayoutManager(this));
          addToCartAdapter = new addToCartAdapter(_productNames,_productPrice, _productQuantity);
          _addToCartRecyclerview.setAdapter(addToCartAdapter);

        _toForm = findViewById(R.id.toForm);
        _toForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addToCart.this,cartForm.class);
                startActivity(intent);
            }
        });





    }
}