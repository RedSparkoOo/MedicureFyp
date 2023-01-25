package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class labTestManagement_options extends AppCompatActivity {
    RecyclerView labTestRecyclerview;
    List<String> testNames;
    labTestManagementAdapter labTestManagementAdapter;
    Button testAddToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management_options);


        labTestRecyclerview = findViewById(R.id.searchLabTestRecycler);
        testAddToCart = findViewById(R.id.testToCart);

        testNames = new ArrayList<>();
        testNames.add("Dengue Test");
        testNames.add("Covid Test");
        testNames.add("Blood Test");
        testNames.add("Typhoid Test");
        testNames.add("Malaria Test");
        testNames.add("H.Pylori Test");
        testNames.add("Renal Failure Test");
        testNames.add("Cardio Test");
        testNames.add("Neuro Test");
        testNames.add("Asthma Test");

        labTestRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        labTestManagementAdapter = new labTestManagementAdapter(testNames);
        labTestRecyclerview.setAdapter(labTestManagementAdapter);


           testAddToCart.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(labTestManagement_options.this, addToCart.class);
                   startActivity(intent);
               }
           });






    }
}