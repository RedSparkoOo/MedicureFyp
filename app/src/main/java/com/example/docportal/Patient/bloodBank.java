package com.example.docportal.Patient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;

public class bloodBank extends AppCompatActivity {

    ArrayList<String> blood_bank_name;
    ArrayList<String> blood_bank_region;
    ArrayList<String> blood_bank_time;
    ArrayList<String> bank_latitude_coordinates;
    ArrayList<String> bank_longitude_coordinates;
    ArrayList<Integer> blood_bank_logos;
    BanksAdapter banksAdapter;
    RecyclerView bank_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);


        bank_recycler = findViewById(R.id.banks_available_recycler);
        blood_bank_name = new ArrayList<>();
        blood_bank_region = new ArrayList<>();
        blood_bank_time = new ArrayList<>();
        blood_bank_logos = new ArrayList<>();
        bank_latitude_coordinates = new ArrayList<>();
        bank_longitude_coordinates = new ArrayList<>();

        blood_bank_name.add("Blood Bank, PIMS Hospital");
        blood_bank_region.add("Islamabad");
        blood_bank_time.add("9:00 am - 10: 00 pm");
        bank_latitude_coordinates.add("33.7031527108848");
        bank_longitude_coordinates.add("73.05022568434156");
        blood_bank_logos.add(R.drawable.chughtai_lab);

        blood_bank_name.add("Blood Bank, Al-shifa");
        blood_bank_region.add("Islamabad");
        blood_bank_time.add("9:00 am - 10: 00 pm");
        bank_latitude_coordinates.add("33.67953281713058");
        bank_longitude_coordinates.add("73.06648914506357");
        blood_bank_logos.add(R.drawable.chughtai_lab);

        blood_bank_name.add("Blood Bank, Shifa International Hospital");
        blood_bank_region.add("Islamabad");
        blood_bank_time.add("10:00 am - 12:00 am");
        bank_latitude_coordinates.add("33.67839002232147");
        bank_longitude_coordinates.add("73.06786243615042");
        blood_bank_logos.add(R.drawable.chughtai_lab);

        blood_bank_name.add("Blood Bank, Red Crescent Hospital");
        blood_bank_region.add("Islamabad");
        blood_bank_time.add("10:00 am - 12:00 am");
        bank_latitude_coordinates.add("33.68549795092692");
        bank_longitude_coordinates.add("73.04949463682149");
        blood_bank_logos.add(R.drawable.chughtai_lab);


        bank_recycler.setLayoutManager(new LinearLayoutManager(this));
        banksAdapter = new BanksAdapter(blood_bank_name,blood_bank_region,blood_bank_time,bank_latitude_coordinates,bank_longitude_coordinates,blood_bank_logos);
        bank_recycler.setAdapter(banksAdapter);

    }
}