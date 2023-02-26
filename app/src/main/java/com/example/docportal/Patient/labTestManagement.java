package com.example.docportal.Patient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;

public class labTestManagement extends AppCompatActivity {

    ArrayList<String> lab_name;
    ArrayList<String> lab_region;
    ArrayList<String> lab_time;
    ArrayList<String> lab_latitude_coordinates;
    ArrayList<String> lab_longitude_coordinates;
    ArrayList<Integer> lab_logos;
    labsAdapter labsAdapter;
    RecyclerView lab_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management);

    lab_recycler = findViewById(R.id.labs_available_recycler);
    lab_name = new ArrayList<>();
    lab_region = new ArrayList<>();
    lab_time = new ArrayList<>();
    lab_logos = new ArrayList<>();
    lab_latitude_coordinates = new ArrayList<>();
    lab_longitude_coordinates = new ArrayList<>();

    lab_name.add("Chughtai Labs");
    lab_logos.add(R.drawable.chughtai_lab);
    lab_name.add("TEMAR Diagnostics");
    lab_logos.add(R.drawable.temar_logo);
    lab_name.add("Excel Labs");
    lab_logos.add(R.drawable.excel_labs);
    lab_name.add("Islamabad Diagnostic Center");
    lab_logos.add(R.drawable.idc_logo_2);
    lab_name.add("CITI Lab");
    lab_logos.add(R.drawable.citi_lab);
    lab_region.add("Islamabad");
    lab_region.add("Rawalpindi");
    lab_region.add("Rawalpindi");
    lab_region.add("Islamabad");
    lab_region.add("Rawalpindi");
    lab_time.add("9:00am - 5:00pm");
    lab_time.add("9:00am - 5:00pm");
    lab_time.add("9:00am - 5:00pm");
    lab_time.add("10:00am - 6:00pm");
    lab_time.add("10:00am - 8:00pm");
    lab_latitude_coordinates.add("33.71020637709538");
    lab_longitude_coordinates.add( "73.05776942666854");
    lab_latitude_coordinates.add("33.63817621122126");
    lab_longitude_coordinates.add( "73.06191458556171");
    lab_latitude_coordinates.add("33.6333495");
    lab_longitude_coordinates.add( "73.078521");
    lab_latitude_coordinates.add("33.633486");
    lab_longitude_coordinates.add( "73.073234");
    lab_latitude_coordinates.add("33.62805581486235");
    lab_longitude_coordinates.add( "73.07006551131991");

    lab_recycler.setLayoutManager(new LinearLayoutManager(this));
    labsAdapter = new labsAdapter(lab_name,lab_region,lab_time,lab_latitude_coordinates,lab_longitude_coordinates,lab_logos);
    lab_recycler.setAdapter(labsAdapter);







    }
}