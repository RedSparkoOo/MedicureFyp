package com.example.docportal.Pharmacist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.docportal.Doctor.updateDoctorProfile;
import com.example.docportal.Doctor.viewDoctorProfile;
import com.example.docportal.Patient.customerSupport;
import com.example.docportal.Patient.patientDashboard;
import com.example.docportal.R;
import com.example.docportal.SplashScreenEntrance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class PharmacistDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView _addMedicine;
    ImageView _addEquipments;
    ImageView _viewPrescription;
    ImageView _manageProducts;
    ImageView _manageEquipment;
    TextView doctor_name;
    NavigationView navigationView;
    DrawerLayout DrawerLayout;

    Toolbar toolbar;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    ImageView view_appointment;
    ImageView _appointmentManagement;
    ImageView doctor_E_Rx;
    ImageView doctor_profile;
    ImageButton profile_doctor;
    ImageView online_consultation;
    Uri content_uri;

    TextView notification_count;
    CardView notification_back;




    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Object user_id;
    String ID;

    ImageView scroll_to_end;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_options);

        _addMedicine =findViewById(R.id.add_medicine);
        _addEquipments = findViewById(R.id.add_medical_equipment);

        _manageProducts = findViewById(R.id.edit_medicine);
        _manageEquipment = findViewById(R.id.edit_medical_equipment);
        doctor_name = findViewById(R.id.patient_name);
        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            user_id   = firebaseAuth.getCurrentUser().getUid();
        }

        doctor_profile = findViewById(R.id.patient_profile);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        notification_count = findViewById(R.id.notify);
        notification_back = findViewById(R.id.notification_icon_count_back);


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
        storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference documentReference = firestore.collection("Professions").document(String.valueOf(user_id));
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists()){
                    doctor_name.setText(value.getString("Full Name"));
                    String Image = value.getString("Image");
                    Picasso.get().load(Uri.parse(Image)).into(doctor_profile);
                }
                else{
                    Toast.makeText(PharmacistDashboard.this, "No Value", Toast.LENGTH_SHORT).show();
                }

            }
        });



        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PharmacistDashboard.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
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

        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                content_uri = data.getData();
                doctor_profile.setImageURI(content_uri);
            }
        }
    }



    @Override
    public void onBackPressed() {


        if(DrawerLayout.isDrawerOpen(GravityCompat.START)){
            DrawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            Intent intent = new Intent(PharmacistDashboard.this,SplashScreenEntrance.class);
            Dialog dialog = new Dialog(PharmacistDashboard.this);
            dialog.setContentView(R.layout.alert_box_layout);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.edges));
            dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            Button confirm = dialog.findViewById(R.id.alert_confirm);
            TextView cancel = dialog.findViewById(R.id.alert_cancel);
            TextView alert_msg = dialog.findViewById(R.id.alert_msg);
            alert_msg.setText("Are you sure you want to logout?");

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent_main = new Intent(PharmacistDashboard.this, SplashScreenEntrance.class);
                    startActivity(intent_main);
                    dialog.dismiss();


                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();


        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.removeProfile:
                Intent intent_view = new Intent(PharmacistDashboard.this,viewDoctorProfile.class);
                startActivity(intent_view);
                break;
            case R.id.updateProfile:
                Intent intent_update = new Intent(PharmacistDashboard.this,UpdatePharmacist.class);
                startActivity(intent_update);
                break;
            case R.id.customer_support:
                Intent intent_support = new Intent(PharmacistDashboard.this, customerSupport.class);

                intent_support.putExtra("identify", "doctor");
                startActivity(intent_support);
                break;
            case R.id.logoutNavigation:
                Dialog dialog = new Dialog(PharmacistDashboard.this);
                dialog.setContentView(R.layout.alert_box_layout);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.edges));
                dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                Button confirm = dialog.findViewById(R.id.alert_confirm);
                TextView cancel = dialog.findViewById(R.id.alert_cancel);
                TextView alert_msg = dialog.findViewById(R.id.alert_msg);
                alert_msg.setText("Are you sure you want to logout?");

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent_main = new Intent(PharmacistDashboard.this, SplashScreenEntrance.class);
                        startActivity(intent_main);
                        dialog.dismiss();


                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }



       // TO SELECT ITEMS IN NAVIGATION DRAWER








}