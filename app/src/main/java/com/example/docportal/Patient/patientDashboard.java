package com.example.docportal.Patient;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Doctor.UpcomingNotificationsAdapter;
import com.example.docportal.R;
import com.example.docportal.SplashScreenEntrance;
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

    ImageView online_consultation;
    TextView patient_name;
    TextView notification_count;
    CardView notification_back;

    NavigationView navigationView;
    DrawerLayout DrawerLayout;
    Toolbar toolbar;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String user_id;
    String ID;
    RecyclerView patient_upcoming_appointments;
    ImageView scroll_to_end;

    List<String> approved_doctor_phone_no;
    List<String> approved_doctor_names;
    List<String> approved_appointment_date;
    List<String> approved_appointment_time;
    LinearLayout patient_empty_show;


    private final int max_count = 99;
    int notifications_count = 0;
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



        patient_name = findViewById(R.id.patient_name);
        patient_profile = findViewById(R.id.patient_profile);
        patient_upcoming_appointments = findViewById(R.id.patient_upcoming_appointments);
        scroll_to_end = findViewById(R.id.scroll);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        notification_count = findViewById(R.id.notify);
        notification_back = findViewById(R.id.notification_icon_count_back);
        patient_empty_show = findViewById(R.id.patient_empty_show);
        online_consultation = findViewById(R.id.patientOnlineConsultation);
        patient_upcoming_appointments.setVisibility(View.INVISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();



        user_id = firebaseAuth.getCurrentUser().getUid();
        patient_upcoming_appointments.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        upcomingAppointments();
        loadProfile();
        loadUserData();
        notification_count.setVisibility(View.INVISIBLE);
        notification_back.setVisibility(View.INVISIBLE);
//        notifications_check();





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

                    Intent appointment = new Intent(patientDashboard.this, Appointment_Doctor_Check.class);
                    startActivity(appointment);

                    overridePendingTransition(enterAnim, exitAnim);
                }
                catch (Exception e){
                    Toast.makeText(patientDashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        patientPharmacyService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, patientPharmacy.class);
                startActivity(view_appointment);
            }
        });

        patientOnlineConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, patient_online_consultation.class);
                startActivity(view_appointment);
            }
        });

        patientPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, patientPrescription.class);
                startActivity(view_appointment);
            }
        });

        patientSearchDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, searchDisease.class);
                startActivity(view_appointment);
            }
        });

        patientLabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, labTestManagement.class);
                startActivity(view_appointment);
            }
        });

        patientBloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, bloodBank.class);
                startActivity(view_appointment);
            }
        });

        patientHealthTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, healthTracker.class);
                startActivity(view_appointment);
            }
        });

        patientEWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, e_wallet.class);
                startActivity(view_appointment);
            }
        });

        patientUrgentCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view_appointment = new Intent(patientDashboard.this, urgentCare.class);
                startActivity(view_appointment);
            }
        });


        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(patientDashboard.this,DrawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blue_2));
        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);    // TO SELECT ITEMS IN NAVIGATION DRAWER


        patient_profile.setOnClickListener(new View.OnClickListener() {
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
                uploadProfileToFireBase(content_uri);
                patient_profile.setImageURI(content_uri);


            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = storageReference.child("Patient/"+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()+"/patient_profile.jpg");
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


        if(DrawerLayout.isDrawerOpen(GravityCompat.START)){
            DrawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            Intent intent = new Intent(patientDashboard.this,SplashScreenEntrance.class);
            Dialog dialog = new Dialog(patientDashboard.this);
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

                    Intent intent_main = new Intent(patientDashboard.this, SplashScreenEntrance.class);
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
                Intent intent_view = new Intent(patientDashboard.this,patientViewProfile.class);
                startActivity(intent_view);
                break;
            case R.id.updateProfile:
                Intent intent_update = new Intent(patientDashboard.this,patientProfileUpdate.class);
                startActivity(intent_update);
                break;
            case R.id.logoutNavigation:
                Dialog dialog = new Dialog(patientDashboard.this);
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

                        Intent intent_main = new Intent(patientDashboard.this, SplashScreenEntrance.class);
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

    public void upcomingAppointments(){

        FirebaseFirestore FStore = FirebaseFirestore.getInstance();
        FStore.clearPersistence();
        approved_doctor_names = new ArrayList<>();
        approved_doctor_phone_no = new ArrayList<>();
        approved_appointment_date = new ArrayList<>();
        approved_appointment_time = new ArrayList<>();

        approved_doctor_names.clear();
        approved_doctor_phone_no.clear();
        approved_appointment_date.clear();
        approved_appointment_time.clear();

        FStore.collection("Approved Appointments").orderBy("Approved Doctor Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(patientDashboard.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    ID = String.valueOf(dc.getDocument().get("Appointed Patient Id"));
                    if(user_id.equals(ID)){

                        if(dc != null){
                            patient_empty_show.setVisibility(View.INVISIBLE);
                            patient_upcoming_appointments.setVisibility(View.VISIBLE);
                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                approved_doctor_names.add(String.valueOf(dc.getDocument().get("Approved Doctor Name")));
                                approved_doctor_phone_no.add(String.valueOf(dc.getDocument().get("Approved Doctor Cell")));
                                approved_appointment_date.add(String.valueOf(dc.getDocument().get("Approved Appointment Date")));
                                approved_appointment_time.add(String.valueOf(dc.getDocument().get("Approved Appointment Time")));
                                notificationsAdapter = new UpcomingNotificationsAdapter(approved_doctor_names, approved_doctor_phone_no,approved_appointment_date,approved_appointment_time);
                                patient_upcoming_appointments.setAdapter(notificationsAdapter);
                                patient_upcoming_appointments.scrollToPosition(notificationsAdapter.getItemCount()-1);
                            }
                            notificationsAdapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(patientDashboard.this, "No Appointments to show", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void notifications_check(){
        FirebaseFirestore FStore = FirebaseFirestore.getInstance();
        DocumentReference notification_ref = FStore.collection("notification").document(user_id);
        notification_ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                assert value != null;
                int notify_check = Integer.parseInt(value.getString("notifications"));

                if(notify_check == 0){
                    notification_count.setVisibility(View.INVISIBLE);
                    notification_back.setVisibility(View.INVISIBLE);

                }
                else if(notify_check > 0){
                    notification_count.setVisibility(View.VISIBLE);
                    notification_back.setVisibility(View.VISIBLE);
                    notification_count.setText(value.getString("notifications"));
                }


            }
        });
    }
    private void loadProfile(){
        StorageReference doc_file_ref = storageReference.child("Patient/"+firebaseAuth.getCurrentUser().getUid()+"/patient_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(patient_profile);

            }
        });
    }

    private void loadUserData(){
        DocumentReference documentReference = firestore.collection("Patient").document(user_id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                patient_name.setText(value.getString("Patient Name"));
            }
        });
    }
}