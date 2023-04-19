package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

public class urgentCare extends AppCompatActivity {

   ImageView patientUrgent_backToDashboard;
   RecyclerView urgent_recycler;
   ImageView empty_phone_book;
   TextView no_contacts_added;
   ImageView add_contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_care);

        patientUrgent_backToDashboard = findViewById(R.id.patientUrgent_backToDashboard);
        urgent_recycler = findViewById(R.id.urgent_recycler);
        empty_phone_book = findViewById(R.id.empty_phone_book);
        no_contacts_added = findViewById(R.id.no_contacts_added);
        add_contacts = findViewById(R.id.add_contacts);

        add_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(urgentCare.this,UrgentCareForm.class);
                startActivity(intent);
            }
        });

    }
}