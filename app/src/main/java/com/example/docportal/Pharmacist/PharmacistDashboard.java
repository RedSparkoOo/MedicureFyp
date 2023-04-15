package com.example.docportal.Pharmacist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.docportal.Doctor.updateDoctorProfile;
import com.example.docportal.Doctor.viewDoctorProfile;
import com.example.docportal.R;
import com.example.docportal.SplashScreen;
import com.google.android.material.navigation.NavigationView;

public class PharmacistDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView _addMedicine;
    ImageView _addEquipments;
    ImageView _viewPrescription;
    ImageView _manageProducts;
    ImageView _manageEquipment;
    NavigationView navigationView;
    DrawerLayout DrawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_options);

        _addMedicine =findViewById(R.id.add_medicine);
        _addEquipments = findViewById(R.id.add_Equipment);
        _viewPrescription = findViewById(R.id.check_prescription);
        _manageProducts = findViewById(R.id.manage_products);
        _manageEquipment = findViewById(R.id.manage_equipment);

        _manageEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PharmacistDashboard.this, MedicalEquipmentList.class));

            }
        });

        _addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PharmacistDashboard.this, AddMedicine.class));

            }
        });
        _manageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(PharmacistDashboard.this, MedicineList.class));
                }
                catch (Exception ex){
                    Toast.makeText(PharmacistDashboard.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        _addEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PharmacistDashboard.this, AddMedicalEquipment.class));

            }
        });
        _viewPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PharmacistDashboard.this, CheckPrescription.class));

            }
        });



        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


                navigationView.bringToFront();
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PharmacistDashboard.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

                DrawerLayout.addDrawerListener(toggle);
                toggle.syncState();


    navigationView.setNavigationItemSelectedListener(this);    // TO SELECT ITEMS IN NAVIGATION DRAWER






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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Home_Navigation:

                break;
            case R.id.viewProfile:
                startActivity(new Intent(PharmacistDashboard.this, viewDoctorProfile.class));
                break;
            case R.id.updateProfile:
                startActivity(new Intent(PharmacistDashboard.this, updateDoctorProfile.class));
                break;
            case R.id.logoutNavigation:
               startActivity(new Intent(PharmacistDashboard.this, SplashScreen.class));
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}