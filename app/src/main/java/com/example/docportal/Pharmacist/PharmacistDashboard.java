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

import com.example.docportal.Doctor.OptionsActivity;
import com.example.docportal.Doctor.updateDoctorProfile;
import com.example.docportal.Doctor.viewDoctorProfile;
import com.example.docportal.R;
import com.example.docportal.SplashScreenEntrance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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

    TextView notification_count;
    CardView notification_back;




    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String user_id;
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


        doctor_profile = findViewById(R.id.patient_profile);

        scroll_to_end = findViewById(R.id.scroll);
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
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PharmacistDashboard.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue_2));
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);    // TO SELECT ITEMS IN NAVIGATION DRAWER


        doctor_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //-----------------Select Image From Gallery---------\\

                Intent open_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //noinspection deprecation
                startActivityForResult(open_gallery, GALLERY_CODE);


            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri content_uri = data.getData();
                doctor_profile.setImageURI(content_uri);

                uploadProfileToFireBase(content_uri);
            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = storageReference.child("Doctor/"+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()+"/doctor_profile.jpg");
        doc_file_ref.putFile(content_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_doctor);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PharmacistDashboard.this, "Profile not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {


        if(DrawerLayout.isDrawerOpen(GravityCompat.START)){
            DrawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            Intent intent = new Intent(PharmacistDashboard.this, SplashScreenEntrance.class);
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

            case R.id.viewProfile:
                Intent intent_view = new Intent(PharmacistDashboard.this,viewDoctorProfile.class);
                startActivity(intent_view);
                break;
            case R.id.updateProfile:
                Intent intent_update = new Intent(PharmacistDashboard.this,updateDoctorProfile.class);
                startActivity(intent_update);
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