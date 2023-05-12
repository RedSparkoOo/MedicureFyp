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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Patient.customerSupport;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
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


public class DoctorNurseDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    private final int max_count = 99;
    ImageView view_appointment;
    ImageView _appointmentManagement;
    ImageView doctor_E_Rx;
    ImageView doctor_profile;
    ImageButton profile_doctor;
    ImageView online_consultation;
    ImageView add_disease_data;
    TextView doctor_name;

    NavigationView navigationView;
    DrawerLayout DrawerLayout;
    Toolbar toolbar;
    StorageReference storageReference;
    Singleton singleton = new Singleton();
    String ID;
    RecyclerView upcoming_appointments;
    ImageView scroll_to_end;
    List<String> approved_patient_phone_no;
    List<String> approved_patient_names;
    List<String> approved_appointment_date;
    List<String> approved_appointment_time;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    List<String> approved_patient_id;
    LinearLayout empty_show;
    int notifications_count = 0;
    UpcomingNotificationsAdapter notificationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_nurse_dashboard);

        doctor_name = findViewById(R.id.doctor_name);
        view_appointment = findViewById(R.id.doctor_view_appointment);
        _appointmentManagement = findViewById(R.id.manage_appointments);
        doctor_E_Rx = findViewById(R.id.doctor_E_Rx);
        doctor_profile = findViewById(R.id.doctor_profile);
        upcoming_appointments = findViewById(R.id.upcoming_appointments);
        add_disease_data = findViewById(R.id.add_disease_data);
        scroll_to_end = findViewById(R.id.scroll);

        empty_show = findViewById(R.id.empty_show);
        online_consultation = findViewById(R.id.doctor_online_consultation);
        upcoming_appointments.setVisibility(View.INVISIBLE);


        upcoming_appointments.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        upcomingAppointments();


        scroll_to_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upcoming_appointments.smoothScrollToPosition(0);

            }

        });

        view_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseDashboard.this, ManageAppointment.class);
            }
        });

        _appointmentManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseDashboard.this, ViewAppointments.class);
            }
        });

        doctor_E_Rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseDashboard.this, Prescription.class);
            }
        });

        online_consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseDashboard.this, OnlineConsultation.class);
            }
        });

        add_disease_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseDashboard.this, AddDiseaseData.class);
            }
        });


        storageReference = FirebaseStorage.getInstance().getReference();
        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.exists())
                    doctor_name.setText(value.getString("Full Name"));
                else
                    singleton.showToast(DoctorNurseDashboard.this, "No Value");
            }
        });


        StorageReference doc_file_ref = storageReference.child("Doctor/" + firestoreHandler.getCurrentUser() + "/doctor_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(doctor_profile);

            }
        });

        navigationView = findViewById(R.id.navigationBar);
        DrawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DoctorNurseDashboard.this, DrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

        if (requestCode == GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri content_uri = data.getData();
                doctor_profile.setImageURI(content_uri);

                uploadProfileToFireBase(content_uri);
            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = storageReference.child("Professions/" + firestoreHandler.getCurrentUser() + "/doctor_profile.jpg");
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
                singleton.showToast(DoctorNurseDashboard.this, "Profile not uploaded");
            }
        });
    }


    @Override
    public void onBackPressed() {


        if (DrawerLayout.isDrawerOpen(GravityCompat.START))
            DrawerLayout.closeDrawer(GravityCompat.START);
        else
            singleton.logout(DoctorNurseDashboard.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.removeProfile:
                firestoreHandler.deleteProfile(DoctorNurseDashboard.this);
                break;
            case R.id.updateProfile:
                singleton.openActivity(DoctorNurseDashboard.this, UpdateDoctorNurseProfile.class);
                break;
            case R.id.customer_support:
                startActivity(singleton.getIntent(DoctorNurseDashboard.this, customerSupport.class).putExtra("identify", "doctor"));
                break;
            case R.id.logoutNavigation:
                singleton.logout(DoctorNurseDashboard.this);
                break;
        }
        DrawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }

    public void upcomingAppointments() {

        FirebaseFirestore FStore = FirebaseFirestore.getInstance();
        FStore.clearPersistence();
        approved_patient_names = new ArrayList<>();
        approved_patient_phone_no = new ArrayList<>();
        approved_appointment_date = new ArrayList<>();
        approved_appointment_time = new ArrayList<>();
        approved_patient_id = new ArrayList<>();

        approved_patient_names.clear();
        approved_patient_phone_no.clear();
        approved_appointment_date.clear();
        approved_appointment_time.clear();

        FStore.collection("Approved Appointments").orderBy("Approved Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    singleton.showToast(DoctorNurseDashboard.this, error.toString());

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.equals(null))
                        singleton.showToast(DoctorNurseDashboard.this, "Nothing to show");
                    else {
                        String patientId = String.valueOf(dc.getDocument().get("Appointed Patient ID"));
                        ID = String.valueOf(dc.getDocument().get("Appointed Doctor Id"));
                        if (firestoreHandler.getCurrentUser().equals(ID)) {

                            if (dc != null) {
                                empty_show.setVisibility(View.INVISIBLE);
                                upcoming_appointments.setVisibility(View.VISIBLE);
                                if (dc.getType() == DocumentChange.Type.ADDED) {

                                    approved_patient_names.add(String.valueOf(dc.getDocument().get("Approved Patient Name")));
                                    approved_patient_phone_no.add(String.valueOf(dc.getDocument().get("Approved Patient Cell")));
                                    approved_appointment_date.add(String.valueOf(dc.getDocument().get("Approved Appointment Date")));
                                    approved_appointment_time.add(String.valueOf(dc.getDocument().get("Approved Appointment Time")));
                                    notificationsAdapter = new UpcomingNotificationsAdapter(approved_patient_names, approved_patient_phone_no, approved_appointment_date, approved_appointment_time);
                                    upcoming_appointments.setAdapter(notificationsAdapter);
                                    upcoming_appointments.scrollToPosition(notificationsAdapter.getItemCount() - 1);
                                }
                                notificationsAdapter.notifyDataSetChanged();
                            } else
                                singleton.showToast(DoctorNurseDashboard.this, "No Appointments to show");
                        }
                    }
                }
            }
        });
    }

//    public void alertBox() {
//        Dialog dialog = new Dialog(DoctorNurseDashboard.this);
//        dialog.setContentView(R.layout.alert_box_layout);
//        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.edges));
//        dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//        Button confirm = dialog.findViewById(R.id.alert_confirm);
//        TextView cancel = dialog.findViewById(R.id.alert_cancel);
//        TextView alert_msg = dialog.findViewById(R.id.alert_msg);
//
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DoctorNurseDashboard.this, "Confirm", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DoctorNurseDashboard.this, "Cancelled", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//
//    }
}