package com.example.docportal.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.docportal.R;
import com.example.docportal.R.id;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord extends AppCompatActivity {
    RecyclerView recyclerview;
    SearchView SearchName;
    String arr[] = {"ALI", "IMRAN", "ASIF", "SHAHAB", "WAHAB", "WAQAS"};
    List<String> MoviesList;
    CustomAdapterRecycle customAdapterRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);
        MoviesList = new ArrayList<>();
        MoviesList.add("Ali");
        MoviesList.add("Atif");
        MoviesList.add("Anmol");
        MoviesList.add("Babar");
        MoviesList.add("Bilal");
        MoviesList.add("Barbarosa");
        MoviesList.add("Cameron");
        MoviesList.add("Castillon");
        MoviesList.add("Candy");
        MoviesList.add("Dawood");
        MoviesList.add("David");
        MoviesList.add("Dazzy");
        MoviesList.add("Elijah");
        MoviesList.add("Elias");
        MoviesList.add("Emir");
        MoviesList.add("Farhan");
        MoviesList.add("Farman");
        MoviesList.add("Furkan");
        MoviesList.add("Gujjar");
        MoviesList.add("Ghazali");
        MoviesList.add("Gangze");
        MoviesList.add("Haris");
        MoviesList.add("Haroon");
        MoviesList.add("Imran");
        MoviesList.add("Ithesham");
        MoviesList.add("Jahangeir");
        MoviesList.add("Jameel");
        MoviesList.add("Jamal");

        recyclerview = (RecyclerView) findViewById(id.Recycle);
        SearchName = (SearchView) findViewById(id.Search);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        customAdapterRecycle = new CustomAdapterRecycle(MoviesList);
        recyclerview.setAdapter(customAdapterRecycle);

        SearchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterRecycle.getFilter().filter(newText);
                return false;
            }
        });

    }
}







