package com.example.docportal.Patient;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class bloodBankOptions extends AppCompatActivity {
    RecyclerView search_blood_group_recycler;
    List<String> blood_group_list;
    List<String> blood_group_donors_list;
    List<String> blood_group_acceptors_list;
    List<String> blood_unit_price_list;
    ImageView all_blood_group;
    ImageView blood_group_A;
    ImageView blood_group_B;
    ImageView blood_group_AB;
    ImageView blood_group_O;
    SearchView search_blood_group;
    String search_HINT_color = "#434242";
    String search_color = "#434242";
    bloodBankAdapter bloodBankAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_options);


        search_blood_group_recycler = findViewById(R.id.search_blood_group_recycler);
        search_blood_group = findViewById(R.id.search_blood_group);
        all_blood_group = findViewById(R.id.all_blood_group);
        blood_group_A = findViewById(R.id.blood_group_A);
        blood_group_B = findViewById(R.id.blood_group_B);
        blood_group_AB = findViewById(R.id.blood_group_AB);
        blood_group_O = findViewById(R.id.blood_group_O);

        int id = search_blood_group.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = search_blood_group.findViewById(id);
        textView.setTextColor(Color.parseColor(search_color));
        textView.setTextSize(14);
        textView.setHintTextColor(Color.parseColor(search_HINT_color));
        Typeface tf = ResourcesCompat.getFont(this,R.font.pt_sans_regular);
        textView.setTypeface(tf);

        blood_group_list = new ArrayList<>();
        blood_group_donors_list = new ArrayList<>();
        blood_group_acceptors_list = new ArrayList<>();
        blood_unit_price_list = new ArrayList<>();

        all_blood_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blood_group_list.clear();
                blood_group_donors_list.clear();
                blood_group_acceptors_list.clear();
                blood_unit_price_list.clear();

                blood_group_list.add("A+");
                blood_group_donors_list.add("A+, AB+");
                blood_group_acceptors_list.add("A+, A-, O+, O-");
                blood_unit_price_list.add("150");


                blood_group_list.add("A-");
                blood_group_donors_list.add("A+, A-, AB+, AB-");
                blood_group_acceptors_list.add("A-,O-");
                blood_unit_price_list.add("150");

                blood_group_list.add("B+");
                blood_group_donors_list.add("B+, AB+");
                blood_group_acceptors_list.add("B+,B-, O+, O-");
                blood_unit_price_list.add("130");


                blood_group_list.add("B-");
                blood_group_donors_list.add("B+, B-, AB+, AB-");
                blood_group_acceptors_list.add("B-,O-");
                blood_unit_price_list.add("150");


                blood_group_list.add("AB+");
                blood_group_donors_list.add("AB+");
                blood_group_acceptors_list.add("A+, A-, B+, B-, AB+, AB-, O+, O-");
                blood_unit_price_list.add("170");


                blood_group_list.add("AB-");
                blood_group_donors_list.add("AB+, AB-");
                blood_group_acceptors_list.add("AB-, A-, B-, O-");
                blood_unit_price_list.add("120");


                blood_group_list.add("O+");
                blood_group_donors_list.add("O+, A+, B+, AB+");
                blood_group_acceptors_list.add("O+,O-");
                blood_unit_price_list.add("180");

                blood_group_list.add("O-");
                blood_group_donors_list.add("A+, A-, B+, B-, AB+, AB-, O+, O-");
                blood_group_acceptors_list.add("O-");
                blood_unit_price_list.add("250");

                selectedBloodGroups(blood_group_list,blood_group_donors_list,blood_group_acceptors_list,blood_unit_price_list);
            }
        });

        blood_group_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blood_group_list.clear();
                blood_group_donors_list.clear();
                blood_group_acceptors_list.clear();
                blood_unit_price_list.clear();

                blood_group_list.add("A+");
                blood_group_donors_list.add("A+, AB+");
                blood_group_acceptors_list.add("A+, A-, O+, O-");
                blood_unit_price_list.add("150");


                blood_group_list.add("A-");
                blood_group_donors_list.add("A+, A-, AB+, AB-");
                blood_group_acceptors_list.add("A-,O-");
                blood_unit_price_list.add("150");

                selectedBloodGroups(blood_group_list,blood_group_donors_list,blood_group_acceptors_list,blood_unit_price_list);

            }
        });

        blood_group_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                blood_group_list.clear();
                blood_group_donors_list.clear();
                blood_group_acceptors_list.clear();
                blood_unit_price_list.clear();

                blood_group_list.add("B+");
                blood_group_donors_list.add("B+, AB+");
                blood_group_acceptors_list.add("B+,B-, O+, O-");
                blood_unit_price_list.add("130");


                blood_group_list.add("B-");
                blood_group_donors_list.add("B+, B-, AB+, AB-");
                blood_group_acceptors_list.add("B-,O-");
                blood_unit_price_list.add("150");

                selectedBloodGroups(blood_group_list,blood_group_donors_list,blood_group_acceptors_list,blood_unit_price_list);

            }
        });

        blood_group_AB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blood_group_list.clear();
                blood_group_donors_list.clear();
                blood_group_acceptors_list.clear();
                blood_unit_price_list.clear();

                blood_group_list.add("AB+");
                blood_group_donors_list.add("AB+");
                blood_group_acceptors_list.add("A+, A-, B+, B-, AB+, AB-, O+, O-");
                blood_unit_price_list.add("170");


                blood_group_list.add("AB-");
                blood_group_donors_list.add("AB+, AB-");
                blood_group_acceptors_list.add("AB-, A-, B-, O-");
                blood_unit_price_list.add("120");

                selectedBloodGroups(blood_group_list,blood_group_donors_list,blood_group_acceptors_list,blood_unit_price_list);
            }
        });

        blood_group_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blood_group_list.clear();
                blood_group_donors_list.clear();
                blood_group_acceptors_list.clear();
                blood_unit_price_list.clear();

                blood_group_list.add("O+");
                blood_group_donors_list.add("O+, A+, B+, AB+");
                blood_group_acceptors_list.add("O+,O-");
                blood_unit_price_list.add("180");

                blood_group_list.add("O-");
                blood_group_donors_list.add("A+, A-, B+, B-, AB+, AB-, O+, O-");
                blood_group_acceptors_list.add("O-");
                blood_unit_price_list.add("250");

                selectedBloodGroups(blood_group_list,blood_group_donors_list,blood_group_acceptors_list,blood_unit_price_list);
            }
        });
        search_blood_group.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                bloodBankAdapter.getFilter().filter(newText);
                return true;

            }
        });

    allPresentBloodGroups();
    }

    private void selectedBloodGroups(List<String> blood_name_dataset,List<String> blood_donor_dataset,List<String> blood_acceptor_dataset,List<String> blood_price_dataset){

        search_blood_group_recycler.setLayoutManager(new LinearLayoutManager(this));
        bloodBankAdapter = new bloodBankAdapter(blood_name_dataset,blood_donor_dataset,blood_acceptor_dataset,blood_price_dataset);
        search_blood_group_recycler.setAdapter(bloodBankAdapter);
    }

    private void allPresentBloodGroups(){
        blood_group_list.add("A+");
        blood_group_donors_list.add("A+, AB+");
        blood_group_acceptors_list.add("A+, A-, O+, O-");
        blood_unit_price_list.add("150");


        blood_group_list.add("A-");
        blood_group_donors_list.add("A+, A-, AB+, AB-");
        blood_group_acceptors_list.add("A-,O-");
        blood_unit_price_list.add("150");


        blood_group_list.add("AB+");
        blood_group_donors_list.add("AB+");
        blood_group_acceptors_list.add("A+, A-, B+, B-, AB+, AB-, O+, O-");
        blood_unit_price_list.add("170");


        blood_group_list.add("AB-");
        blood_group_donors_list.add("AB+, AB-");
        blood_group_acceptors_list.add("AB-, A-, B-, O-");
        blood_unit_price_list.add("120");


        blood_group_list.add("B+");
        blood_group_donors_list.add("B+, AB+");
        blood_group_acceptors_list.add("B+,B-, O+, O-");
        blood_unit_price_list.add("130");


        blood_group_list.add("B-");
        blood_group_donors_list.add("B+, B-, AB+, AB-");
        blood_group_acceptors_list.add("B-,O-");
        blood_unit_price_list.add("150");

        blood_group_list.add("O+");
        blood_group_donors_list.add("O+, A+, B+, AB+");
        blood_group_acceptors_list.add("O+,O-");
        blood_unit_price_list.add("180");

        blood_group_list.add("O-");
        blood_group_donors_list.add("A+, A-, B+, B-, AB+, AB-, O+, O-");
        blood_group_acceptors_list.add("O-");
        blood_unit_price_list.add("250");

        selectedBloodGroups(blood_group_list,blood_group_donors_list,blood_group_acceptors_list,blood_unit_price_list);
    }
}