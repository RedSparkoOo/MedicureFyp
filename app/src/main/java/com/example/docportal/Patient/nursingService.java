package com.example.docportal.Patient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class nursingService extends AppCompatActivity {
    RecyclerView searchNurseRecyclerview;
    List<String> nurseNames;
    List<String> nurseCategory;
    List<String> nursingPrice;
    nursingServiceAdapter nursingServiceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_service);

        searchNurseRecyclerview = findViewById(R.id.searchNurseRecycler);
        nurseNames = new ArrayList<>();
        nurseNames.add("Nurse # 1");
        nurseNames.add("Nurse # 2");
        nurseNames.add("Nurse # 3");
        nurseNames.add("Nurse # 4");
        nurseNames.add("Nurse # 5");
        nurseNames.add("Nurse # 6");
        nurseNames.add("Nurse # 7");

        nursingPrice = new ArrayList<>();
        nursingPrice.add("$5");
        nursingPrice.add("$6");
        nursingPrice.add("$7");
        nursingPrice.add("$4");
        nursingPrice.add("$9");
        nursingPrice.add("$4");
        nursingPrice.add("$3");

        nurseCategory = new ArrayList<>();
        nurseCategory.add("Mental Health Nurse");
        nurseCategory.add("Learning Disability Nurse");
        nurseCategory.add("Adult Nurse");
        nurseCategory.add("Children Nurse");
        nurseCategory.add("Nursing Associate");
        nurseCategory.add("Cardiac Nurse");
        nurseCategory.add("Critical Care Nurse");



        searchNurseRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        nursingServiceAdapter = new nursingServiceAdapter(nurseNames,nursingPrice, nurseCategory);
        searchNurseRecyclerview.setAdapter(nursingServiceAdapter);




    }
}