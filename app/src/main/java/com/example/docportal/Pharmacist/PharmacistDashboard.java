package com.example.docportal.Pharmacist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Patient.customerSupport;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class PharmacistDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView _addMedicine;
    ImageView _addEquipments;

    ImageView _manageProducts;
    ImageView _manageEquipment;
    TextView doctor_name;
    NavigationView navigationView;
    DrawerLayout DrawerLayout;

    Toolbar toolbar;


    ImageView doctor_profile;

    Uri content_uri;
    Singleton singleton = new Singleton();
    FirestoreHandler firestoreHandler = new FirestoreHandler();

    TextView notification_count;
    CardView notification_back;


    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_options);

        _addMedicine = findViewById(R.id.add_medicine);
        _addEquipments = findViewById(R.id.add_medical_equipment);

        _manageProducts = findViewById(R.id.edit_medicine);
        _manageEquipment = findViewById(R.id.edit_medical_equipment);
        doctor_name = findViewById(R.id.patient_name);
        navigationView = findViewById(R.id.navigationBar);
        DrawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        firestoreHandler.getCurrentUser();

        doctor_profile = findViewById(R.id.patient_profile);




        _manageEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.openActivity(PharmacistDashboard.this, MedicalEquipmentList.class);
            }
        });

        _addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PharmacistDashboard.this, AddMedicine.class);
            }
        });
        _manageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PharmacistDashboard.this, MedicineList.class);
            }
        });
        _addEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PharmacistDashboard.this, AddMedicalEquipment.class);
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.exists() ) {
                    doctor_name.setText(value.getString("Full Name"));
                    if(value.getString("Image") != null) {
                        String Image = value.getString("Image");
                        Picasso.get().load(Uri.parse(Image)).into(doctor_profile);
                    }
                } else
                    singleton.showToast(PharmacistDashboard.this, "No Value");
            }
        });


        navigationView = findViewById(R.id.navigationBar);
        DrawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PharmacistDashboard.this, DrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue_2));
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);    // TO SELECT ITEMS IN NAVIGATION DRAWER


        doctor_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1000);

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                content_uri = data.getData();
                doctor_profile.setImageURI(content_uri);
            }
        }
    }


    @Override
    public void onBackPressed() {


        if (DrawerLayout.isDrawerOpen(GravityCompat.START))
            DrawerLayout.closeDrawer(GravityCompat.START);
        else
            singleton.logout(PharmacistDashboard.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.removeProfile:
                firestoreHandler.deleteProfile(PharmacistDashboard.this);
                break;
            case R.id.updateProfile:
                singleton.openActivity(PharmacistDashboard.this, UpdatePharmacist.class);
                break;
            case R.id.customer_support:
                startActivity(singleton.getIntent(PharmacistDashboard.this, customerSupport.class).putExtra("identify", "doctor"));
                break;
            case R.id.logoutNavigation:
                singleton.logout(PharmacistDashboard.this);
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


}