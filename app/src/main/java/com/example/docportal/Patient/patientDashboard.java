package com.example.docportal.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.docportal.R;
import com.example.docportal.SplashScreen;
import com.google.android.material.navigation.NavigationView;

public class patientDashboard extends AppCompatActivity {
    ImageView patientAppointmentBooking;
    ImageView patientSearchDoctor;
    ImageView patientPharmacyService;
    ImageView patientLabTest;
    ImageView patientMedicalRecord;
    ImageView patientNursingServce;
    ImageView patientPrescription;
    ImageView patientHealthBlog;
    ImageView patientUrgentCare;
    ImageView patientBloodBank;
    ImageView patientHealthTracker;
    ImageView patientOnlineConsultation;
    ImageView patientEWallet;
    ImageView patientSearchDisease;
    ImageView patientCustomerSupport;
    NavigationView navigationView;
    androidx.drawerlayout.widget.DrawerLayout DrawerLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Assigning Ids (Hooks)
        patientAppointmentBooking = findViewById(R.id.patientAppointmentBooking);
        patientSearchDoctor = findViewById(R.id.patientSearchDoctor);
        patientPharmacyService = findViewById(R.id.patientPharmacyService);
        patientLabTest = findViewById(R.id.patientLabTest);
        patientMedicalRecord = findViewById(R.id.patientMedicalRecord);
        patientNursingServce = findViewById(R.id.patientNurseService);
        patientPrescription = findViewById(R.id.patientPrescription);
        patientHealthBlog = findViewById(R.id.patientHealthBlog);
        patientUrgentCare = findViewById(R.id.patientUrgentCare);
        patientBloodBank = findViewById(R.id.patientBloodBank);
        patientHealthTracker = findViewById(R.id.patientHealthTracker);
        patientOnlineConsultation = findViewById(R.id.patientOnlineConsultation);
        patientEWallet = findViewById(R.id.patientEWallet);
        patientSearchDisease = findViewById(R.id.patientSearchDisease);
        patientCustomerSupport = findViewById(R.id.patientCustomerSupport);

        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);






        // For Navigation Bar
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(patientDashboard.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);    // TO SELECT ITEMS IN NAVIGATION DRAWER

        //Accessing different activities in Dashboard
        patientAppointmentBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this,Appointment_Doctor_Check.class);
                startActivity(intent);
            }
        });

        patientSearchDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this,searchDoctor.class);
                startActivity(intent);
            }
        });

        patientUrgentCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this,urgentCare.class);
                startActivity(intent);
            }
        });


           patientPrescription.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(patientDashboard.this,patientPrescription.class);
                   startActivity(intent);
               }
           });




        // Patient Lab Test
        patientLabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, labTestManagement.class);
                startActivity(intent);
            }
        });

        // patient nursing service

        patientNursingServce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, nursingService.class);
                startActivity(intent);
            }
        });

        // view Medical Record

        patientMedicalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, viewMedicalRecord.class);
                startActivity(intent);
            }
        });

        // Online Consultation

        patientOnlineConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, patientOnlineConsultation.class);
                startActivity(intent);
            }
        });


        // Pharmacy Service

        patientPharmacyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, pharmacyEquipmentOptions.class);
                startActivity(intent);
            }
        });


        // Blood bank

        patientBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, bloodBank.class);
                startActivity(intent);
            }
        });

        // health Tracker

        patientHealthTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, healthTracker.class);
                startActivity(intent);
            }
        });


        // E-wallet

        patientEWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, e_wallet.class);
                startActivity(intent);
            }
        });


        // Search Disease

        patientSearchDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, searchDisease.class);
                startActivity(intent);
            }
        });

        patientCustomerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientDashboard.this, customerSupport.class);
                startActivity(intent);
            }
        });











    }

    @Override
    public void onBackPressed() {


        if(DrawerLayout.isDrawerOpen(GravityCompat.START)){
            DrawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.viewProfile:
                Intent intent_view = new Intent(patientDashboard.this,patientViewProfile.class);
                startActivity(intent_view);

                break;

            case R.id.updateProfile:
                Intent intent_update = new Intent(patientDashboard.this,patientProfileUpdate.class);
                startActivity(intent_update);

                break;
            case R.id.logoutNavigation:
                Intent intent_main = new Intent(patientDashboard.this, SplashScreen.class);
                startActivity(intent_main);
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}