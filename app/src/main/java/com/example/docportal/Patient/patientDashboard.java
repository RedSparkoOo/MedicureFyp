package com.example.docportal.Patient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Doctor.UpcomingNotificationsAdapter;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class patientDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    ImageView patientAppointmentBooking;
    ImageView patientPharmacyService;
    ImageView patientOnlineConsultation;
    ImageView patientPrescription;
    ImageView patientSearchDisease;
    ImageView patientLabTest;
    ImageView patientBloodBank;
    ImageView patientHealthTracker;
    ImageView patientEWallet;
    ImageView patientUrgentCare;
    ImageView patient_profile;
    ImageView cart_icon;

    ImageView online_consultation;
    TextView patient_name;
    NavigationView navigationView;
    DrawerLayout DrawerLayout;
    Toolbar toolbar;
    StorageReference storageReference;


    String ID;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();
    RecyclerView patient_upcoming_appointments;
    ImageView scroll_to_end;

    List<String> approved_doctor_phone_no;
    List<String> approved_doctor_names;
    List<String> approved_appointment_date;
    List<String> approved_appointment_time;
    List<String> approved_doctor_id;
    LinearLayout patient_empty_show;

    UpcomingNotificationsAdapter notificationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);


        patientAppointmentBooking = findViewById(R.id.patientAppointmentBooking);
        patientPharmacyService = findViewById(R.id.patientPharmacyService);
        patientOnlineConsultation = findViewById(R.id.patientOnlineConsultation);
        patientPrescription = findViewById(R.id.patientPrescription);
        patientSearchDisease = findViewById(R.id.patientSearchDisease);
        patientLabTest = findViewById(R.id.patientLabTest);
        patientBloodBank = findViewById(R.id.patientBloodBank);
        patientHealthTracker = findViewById(R.id.patientHealthTracker);
        patientEWallet = findViewById(R.id.patientEWallet);
        patientUrgentCare = findViewById(R.id.patientUrgentCare);
        cart_icon = findViewById(R.id.cart_icon);


        patient_name = findViewById(R.id.patient_name);
        patient_profile = findViewById(R.id.patient_profile);
        patient_upcoming_appointments = findViewById(R.id.patient_upcoming_appointments);
        scroll_to_end = findViewById(R.id.scroll);

        patient_empty_show = findViewById(R.id.patient_empty_show);
        online_consultation = findViewById(R.id.patientOnlineConsultation);
        patient_upcoming_appointments.setVisibility(View.INVISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();


        patient_upcoming_appointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        upcomingAppointments();
        loadProfile();
        loadUserData();

//        notifications_check();


        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, CheckoutActivityJava.class);

            }
        });

        scroll_to_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient_upcoming_appointments.smoothScrollToPosition(0);

            }

        });

        patientAppointmentBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int enterAnim = R.anim.slide_in_right;
                    int exitAnim = R.anim.slide_out_left;
                    singleton.openActivity(patientDashboard.this, AppointmentDoctorNurseSelection.class);


                    overridePendingTransition(enterAnim, exitAnim);
                } catch (Exception e) {
                    Toast.makeText(patientDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        patientPharmacyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, PharmacyService.class);
            }
        });

        patientOnlineConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, PatientOnlineConsultation.class);
            }
        });

        patientPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, PatientPrescription.class);
            }
        });

        patientSearchDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, searchDisease.class);
            }
        });

        patientLabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, labTestManagement.class);

            }
        });

        patientBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, bloodBank.class);
            }
        });

        patientHealthTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, HealthTracker.class);
            }
        });

        patientEWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, TransactionHistory.class);
            }
        });

        patientUrgentCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientDashboard.this, urgentCare.class);
            }
        });


        navigationView = findViewById(R.id.navigationBar);
        DrawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(patientDashboard.this, DrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue_2));
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);    // TO SELECT ITEMS IN NAVIGATION DRAWER


        patient_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GALLERY_CODE);

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri content_uri = data.getData();
                uploadProfileToFireBase(content_uri);
                patient_profile.setImageURI(content_uri);


            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = storageReference.child("Patient/" + firestoreHandler.getCurrentUser() + "/patient_profile.jpg");
        doc_file_ref.putFile(content_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(patient_profile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(patientDashboard.this, "Profile not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {


        if (DrawerLayout.isDrawerOpen(GravityCompat.START))
            DrawerLayout.closeDrawer(GravityCompat.START);

        else
            singleton.logout(patientDashboard.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.removeProfile:
                firestoreHandler.deleteProfile(patientDashboard.this);
                break;
            case R.id.updateProfile:
                singleton.openActivity(patientDashboard.this, UpdatePatientProfile.class);
                break;
            case R.id.customer_support:
                startActivity(singleton.getIntent(patientDashboard.this, customerSupport.class).putExtra("identify", "patient"));
                break;
            case R.id.logoutNavigation:
                singleton.logout(patientDashboard.this);
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }

    public void upcomingAppointments() {

        firestoreHandler.getFirestoreInstance().clearPersistence();
        approved_doctor_names = new ArrayList<>();
        approved_doctor_phone_no = new ArrayList<>();
        approved_appointment_date = new ArrayList<>();
        approved_appointment_time = new ArrayList<>();
        approved_doctor_id = new ArrayList<>();

        approved_doctor_names.clear();
        approved_doctor_phone_no.clear();
        approved_appointment_date.clear();
        approved_appointment_time.clear();

        firestoreHandler.getFirestoreInstance().collection("Approved Appointments").orderBy("ApprovedDoctorName", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    singleton.showToast(patientDashboard.this, error.toString());

                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    ID = String.valueOf(dc.getDocument().get("Appointed Patient Id"));
                    if (firestoreHandler.getCurrentUser().equals(ID)) {

                        if (dc != null) {
                            patient_empty_show.setVisibility(View.INVISIBLE);
                            patient_upcoming_appointments.setVisibility(View.VISIBLE);
                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                approved_doctor_names.add(String.valueOf(dc.getDocument().get("ApprovedDoctorName")));
                                approved_doctor_phone_no.add(String.valueOf(dc.getDocument().get("ApprovedDoctorCell")));
                                approved_appointment_date.add(String.valueOf(dc.getDocument().get("ApprovedAppointmentDate")));
                                approved_appointment_time.add(String.valueOf(dc.getDocument().get("ApprovedAppointmentTime")));
                                approved_doctor_id.add(String.valueOf(dc.getDocument().get("ApprovedAppointmentTime")));
                                notificationsAdapter = new UpcomingNotificationsAdapter(approved_doctor_names, approved_doctor_phone_no, approved_appointment_date, approved_appointment_time, approved_doctor_id);
                                patient_upcoming_appointments.setAdapter(notificationsAdapter);
                                patient_upcoming_appointments.scrollToPosition(notificationsAdapter.getItemCount() - 1);
                            }
                            notificationsAdapter.notifyDataSetChanged();
                        } else
                            singleton.showToast(patientDashboard.this, "No Appointments to show");

                    }
                }
            }
        });
    }

    private void loadProfile() {
        StorageReference doc_file_ref = storageReference.child("Patient/" + firestoreHandler.getCurrentUser() + "/patient_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(patient_profile);

            }
        });
    }

    private void loadUserData() {
        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Patient").document(firestoreHandler.getCurrentUser());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                patient_name.setText(value.getString("Patient Name"));
            }
        });
    }
}