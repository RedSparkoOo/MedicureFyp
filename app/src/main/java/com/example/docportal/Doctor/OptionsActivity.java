package com.example.docportal.Doctor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.example.docportal.SplashScreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OptionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    ImageView view_appointment;
    ImageView _appointmentManagement;
    ImageView doctor_E_Rx;
    ImageView doctor_profile;
    ImageButton profile_doctor;
    TextView doctor_name;
    TextView notification_count;
    CardView notification_back;

    NavigationView navigationView;
    DrawerLayout DrawerLayout;
    Toolbar toolbar;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String user_id;
    RecyclerView upcoming_appointments;
    ImageView scroll_to_end;

    List<String> approved_patient_phone_no;
    List<String> approved_patient_names;
    List<String> approved_appointment_date;
    List<String> approved_appointment_time;

    private final int max_count = 99;
    int notifications_count = 0;
    UpcomingNotificationsAdapter notificationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        doctor_name = findViewById(R.id.doctor_name);
        view_appointment = findViewById(R.id.doctor_view_appointment);
        _appointmentManagement = findViewById(R.id.manage_appointments);
        doctor_E_Rx = findViewById(R.id.doctor_E_Rx);
        doctor_profile = findViewById(R.id.doctor_profile);
        upcoming_appointments = findViewById(R.id.upcoming_appointments);
        scroll_to_end = findViewById(R.id.scroll);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        notification_count = findViewById(R.id.notify);
        notification_back = findViewById(R.id.notification_icon_count_back);
        user_id = firebaseAuth.getCurrentUser().getUid();
        upcoming_appointments.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        upcomingAppointments();


        notification_count.setVisibility(View.INVISIBLE);
        notification_back.setVisibility(View.INVISIBLE);

try {
    Bundle bundle = getIntent().getExtras();
    notifications_count = Integer.parseInt(bundle.getString("note"));

    Toast.makeText(this, Integer.toString(notifications_count), Toast.LENGTH_SHORT).show();

}catch (Exception e){
    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
}

        scroll_to_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    upcoming_appointments.smoothScrollToPosition(0);
                notification_count.setVisibility(View.VISIBLE);
                notification_back.setVisibility(View.VISIBLE);



                    if(notifications_count == max_count){
                        Toast.makeText(OptionsActivity.this, "Limit Reached", Toast.LENGTH_SHORT).show();
                        notification_count.setText(Integer.toString(notifications_count)+"+");

                    }
                    else{
                        ++notifications_count;
                        notification_count.setText(Integer.toString(notifications_count));
                    }


            }

        });

    view_appointment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent view_appointment = new Intent(OptionsActivity.this, checkAppointment.class);
            startActivity(view_appointment);
        }
    });

    _appointmentManagement.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(OptionsActivity.this, AppointmentNotifications.class));
        }
    });

    doctor_E_Rx.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(OptionsActivity.this,Prescription.class));
        }
    });

        storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference documentReference = firestore.collection("Doctor").document(user_id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                doctor_name.setText(value.getString("Full Name"));
            }
        });


        StorageReference doc_file_ref = storageReference.child("Doctor/"+firebaseAuth.getCurrentUser().getUid()+"/doctor_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(doctor_profile);

            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

                navigationView.bringToFront();
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(OptionsActivity.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue_2));
                DrawerLayout.addDrawerListener(toggle);
                toggle.syncState();
                navigationView.setNavigationItemSelectedListener(this);    // TO SELECT ITEMS IN NAVIGATION DRAWER


        doctor_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             //-----------------Select Image From Gallery---------\\

             Intent open_gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
                Toast.makeText(OptionsActivity.this, "Profile not uploaded", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.viewProfile:
                Intent intent_view = new Intent(OptionsActivity.this,viewDoctorProfile.class);
                startActivity(intent_view);
                break;
            case R.id.updateProfile:
                Intent intent_update = new Intent(OptionsActivity.this,updateDoctorProfile.class);
                startActivity(intent_update);
                break;
            case R.id.logoutNavigation:
                Intent intent_main = new Intent(OptionsActivity.this, SplashScreen.class);
                startActivity(intent_main);
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }

    public void upcomingAppointments(){

        FirebaseFirestore FStore = FirebaseFirestore.getInstance();
        approved_patient_names = new ArrayList<>();
        approved_patient_phone_no = new ArrayList<>();
        approved_appointment_date = new ArrayList<>();
        approved_appointment_time = new ArrayList<>();

        FStore.collection("Approved Appointments").orderBy("Approved Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(OptionsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if(dc != null){

                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            approved_patient_names.add(String.valueOf(dc.getDocument().get("Approved Patient Name")));
                            approved_patient_phone_no.add(String.valueOf(dc.getDocument().get("Approved Patient Cell")));
                            approved_appointment_date.add(String.valueOf(dc.getDocument().get("Approved Appointment Date")));
                            approved_appointment_time.add(String.valueOf(dc.getDocument().get("Approved Appointment Time")));
                            notificationsAdapter = new UpcomingNotificationsAdapter(approved_patient_names,approved_patient_phone_no,approved_appointment_date,approved_appointment_time);
                            upcoming_appointments.setAdapter(notificationsAdapter);
                            upcoming_appointments.scrollToPosition(notificationsAdapter.getItemCount()-1);

                        }
                        notificationsAdapter.notifyDataSetChanged();

                    }
                    else
                        Toast.makeText(OptionsActivity.this, "No Appointments to show", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}