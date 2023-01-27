package com.example.docportal.Doctor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;


public class AppointmentNotifications extends AppCompatActivity {
    RecyclerView appointment_recycler_view;
    List<String> _notifiedName;
    SearchView search_patient;
    List<String> _notifiedPhone;
    List<String> _notifiedDate;
    List<String> _notifiedTime;
    AppointmentAdapter appointmentadapter;

    String notifiedName;
    String notifiedPhoneCheck;
    String notifiedPhone;
    String notifiedDate;
    String notifiedTime;

    String search_HINT_color = "#B2B2B2";
    String search_color = "#434242";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_notifications);
        // appointee names

     _notifiedName = new ArrayList<>();
     _notifiedName.add("Imran");
     _notifiedName.add("Abbas");
     _notifiedName.add("Waleed");
     _notifiedName.add("Connor");
     _notifiedName.add("James");
     _notifiedName.add("Ali");
     _notifiedName.add("Asad");
     _notifiedName.add("Khalid");
     _notifiedName.add("Khalil");
     _notifiedName.add("Hamid");


     // appointee phone
     _notifiedPhone = new ArrayList<>();
     _notifiedPhone.add("0318-1290301");
     _notifiedPhone.add("0328-1330302");
     _notifiedPhone.add("0338-1430303");
     _notifiedPhone.add("0348-1639304");
     _notifiedPhone.add("0358-1731305");
     _notifiedPhone.add("0328-1832306");
     _notifiedPhone.add("0338-1933307");
     _notifiedPhone.add("0348-2034308");
     _notifiedPhone.add("0358-2135309");
     _notifiedPhone.add("0378-2236310");

    search_patient = findViewById(R.id.search_patient);
    int id = search_patient.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
    TextView textView = (TextView) search_patient.findViewById(id);
    textView.setTextColor(Color.parseColor(search_color));
    textView.setTextSize(16);
    textView.setHintTextColor(Color.parseColor(search_HINT_color));
    Typeface tf = ResourcesCompat.getFont(this,R.font.inter_light);
    textView.setTypeface(tf);

     appointment_recycler_view = findViewById(R.id.manage_appointment_recycler);
     appointment_recycler_view.setLayoutManager(new LinearLayoutManager(this));
     appointmentadapter = new AppointmentAdapter(_notifiedName, _notifiedPhone);
     appointment_recycler_view.setAdapter(appointmentadapter);

     //, _notifiedPhone, _notifiedDate, _notifiedTime

//     SearchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//         @Override
//         public boolean onQueryTextSubmit(String query) {
//             return false;
//         }
//
//         @Override
//         public boolean onQueryTextChange(String newText) {
//             appointmentadapter.getFilter().filter(newText);
//             return false;
//         }
//     });



    }
}

