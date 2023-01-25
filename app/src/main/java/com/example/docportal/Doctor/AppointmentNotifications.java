package com.example.docportal.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.docportal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AppointmentNotifications extends AppCompatActivity {
    RecyclerView recyclerview;
    SearchView SearchName;
    List<String> _notifiedName;
    List<String> _notifiedPhone;
    List<String> _notifiedDate;
    List<String> _notifiedTime;
    AppointmentAdapter appointmentadapter;

    FirebaseDatabase RootNode;
    DatabaseReference reference;



    String notifiedName;
    String notifiedPhoneCheck;
    String notifiedPhone;
    String notifiedDate;
    String notifiedTime;

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
     _notifiedPhone = new ArrayList<String>();
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




//        Query checkUser = FirebaseDatabase.getInstance("https://doc-portal-9515b-default-rtdb.firebaseio.com/").getReference("Appointment");
//
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    notifiedName = dataSnapshot.child("appointeeName").getValue(String.class);
//                    notifiedPhone = dataSnapshot.child("appointeePhone").getValue(String.class);
//                    notifiedDate = dataSnapshot.child("time").getValue(String.class);
//                    notifiedTime = dataSnapshot.child("date").getValue(String.class);
//
//                    _notifiedName.add(notifiedName);
//                    _notifiedPhone.add(notifiedPhone);
//                    _notifiedDate.add(notifiedDate);
//                    _notifiedDate.add(notifiedTime);
//                }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(AppointmentNotifications.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });


     recyclerview = findViewById(R.id.AppointmentRecyclerView);
     SearchName = findViewById(R.id.searchAppointment);
     recyclerview.setLayoutManager(new LinearLayoutManager(this));
     appointmentadapter = new AppointmentAdapter(_notifiedName, _notifiedPhone);
     recyclerview.setAdapter(appointmentadapter);

     //, _notifiedPhone, _notifiedDate, _notifiedTime

     SearchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
             return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
             appointmentadapter.getFilter().filter(newText);
             return false;
         }
     });



    }
}

