package com.example.docportal.Patient;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Appointment_Doctor_Check extends AppCompatActivity {

    SearchView search_doctor;
    String search_HINT_color = "#434242";
    String search_color = "#434242";

    RecyclerView doctor_profile_recycler;

    ArrayList<String> doctor_names;
    ArrayList<String> doctor_specializations;
    ArrayList<String> doctor_UID;
    ArrayList<String> doctor_phone_no;
    ArrayList<String> imageList;


    ImageView all_category;
    ImageView cardio_category;
    ImageView neuro_category;
    ImageView nephro_category;
    ImageView oncol_category;
    ImageView pedri_category;
    ImageView physio_category;
    ImageView physco_category;
    ImageView no_doctor_image;

    ImageView all_category_nurse;
    ImageView mhn_nurse;
    ImageView ldn_nurse;
    ImageView cn_nurse;
    ImageView an_nurse;
    ImageView ccn_nurse;
    ImageView back_to_patient_dashboard;
    Singleton singleton = new Singleton();
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    AppointmentBookingAdapter book_appointment_helper_class;

    View snack_bar_layout;
    TextView no_doctors_available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_doctor_check);

        all_category = findViewById(R.id.all_category);
        cardio_category = findViewById(R.id.cardio_category);
        nephro_category = findViewById(R.id.nephro_category);
        neuro_category = findViewById(R.id.neuro_category);
        oncol_category = findViewById(R.id.oncol_category);
        pedri_category = findViewById(R.id.pedri_category);
        physio_category = findViewById(R.id.physio_category);
        physco_category = findViewById(R.id.physco_category);
        no_doctor_image = findViewById(R.id.no_doctor_image);
        no_doctors_available = findViewById(R.id.no_doctors_available);
        all_category_nurse = findViewById(R.id.all_category_nurse);
        mhn_nurse = findViewById(R.id.mhn_nurse);
        ldn_nurse = findViewById(R.id.ldn_nurse);
        cn_nurse = findViewById(R.id.cn_nurse);
        an_nurse = findViewById(R.id.an_nurse);
        ccn_nurse = findViewById(R.id.ccn_nurse);
        back_to_patient_dashboard = findViewById(R.id.back_to_patient_dashboard);

        back_to_patient_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(Appointment_Doctor_Check.this, patientDashboard.class);
            }
        });

        no_doctor_image.setVisibility(View.INVISIBLE);
        no_doctors_available.setVisibility(View.INVISIBLE);


        snack_bar_layout = findViewById(android.R.id.content);
        search_doctor = findViewById(R.id.search_doctor);
        int id = search_doctor.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = search_doctor.findViewById(id);
        textView.setTextColor(Color.parseColor(search_color));
        textView.setTextSize(14);
        textView.setHintTextColor(Color.parseColor(search_HINT_color));
        Typeface tf = ResourcesCompat.getFont(this, R.font.pt_sans_regular);
        textView.setTypeface(tf);

        doctor_profile_recycler = findViewById(R.id.doctor_list_recycler);


        doctor_names = new ArrayList<>();
        doctor_specializations = new ArrayList<>();
        doctor_UID = new ArrayList<>();
        doctor_phone_no = new ArrayList<>();
        imageList = new ArrayList<>();

        doctor_profile_recycler.setLayoutManager(new LinearLayoutManager(Appointment_Doctor_Check.this));
        book_appointment_helper_class = new AppointmentBookingAdapter(doctor_names, doctor_specializations, doctor_UID, doctor_phone_no, new AppointmentBookingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String details) {
                Log.d(details, "Works");
            }
        });
        doctor_profile_recycler.setAdapter(book_appointment_helper_class);


        all_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_doctor_image.setVisibility(View.INVISIBLE);
                no_doctors_available.setVisibility(View.INVISIBLE);
                doctor_profile_recycler.setVisibility(View.VISIBLE);


                doctor_names.clear();
                doctor_specializations.clear();
                doctor_UID.clear();
                doctor_phone_no.clear();
                FireStoreUsers();
                snackBarShow(snack_bar_layout, "All Doctors Selected");


            }
        });

        cardio_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Cardiologist", "Cardiologist Selected");
            }
        });

        neuro_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Neurologist", "Neurologist Selected");

            }
        });

        nephro_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Nephrologist", "Nephrologist Selected");
            }
        });

        oncol_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Oncologist", "Oncologist Selected");
            }
        });

        pedri_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Pedriatican", "Pedriatican Selected");
            }
        });

        physio_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Physiologist", "Physiologist Selected");
            }
        });

        physco_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctor("Psychologist", "Psychologist Selected");
            }
        });

        //-------------------------------------NURSE----------------------------------

        all_category_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_doctor_image.setVisibility(View.INVISIBLE);
                no_doctors_available.setVisibility(View.INVISIBLE);
                doctor_profile_recycler.setVisibility(View.VISIBLE);


                doctor_names.clear();
                doctor_specializations.clear();
                doctor_UID.clear();
                doctor_phone_no.clear();
                NurseFireStoreUsers();
                snackBarShow(snack_bar_layout, "All Nurses Selected");


            }
        });

        mhn_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNurse("Mental Health Nurse (MHN)", "Mental Health Nurse Selected");
            }
        });


        ldn_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNurse("Learning Disability Nurse (LDN)", "Learning Disability Nurse Selected");
            }
        });

        cn_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNurse("Adult Nurse (AN)", "Adult Nurse Selected");
            }
        });

        an_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNurse("Children Nurse (CN)", "Children Nurse Selected");
            }
        });

        ccn_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNurse("Critical Care Nurse (CCN)", "Critical Care Nurse Selected");

            }
        });

        FireStoreUsers();
    }

    private void NurseFireStoreUsersSpecific(String Category) {

        firestoreHandler.getFirestoreInstance().collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String category = String.valueOf(documentChange.getDocument().get("Profession"));
                        String Specialization = String.valueOf(documentChange.getDocument().get("Doctor_profession"));

                        if (category.equals("Nurse")) {

                            if (Specialization.equals(Category)) {

                                doctor_names.add(String.valueOf(documentChange.getDocument().get("Full Name")));
                                doctor_specializations.add(String.valueOf(documentChange.getDocument().get("Doctor_profession")));
                                doctor_UID.add(documentChange.getDocument().getId());
                                doctor_phone_no.add(String.valueOf(documentChange.getDocument().get("Phone #")));


                            }
                            if (doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0) {
                                no_doctor_image.setVisibility(View.VISIBLE);
                                no_doctors_available.setVisibility(View.VISIBLE);
                                doctor_profile_recycler.setVisibility(View.INVISIBLE);
                            } else {
                                no_doctor_image.setVisibility(View.INVISIBLE);
                                no_doctors_available.setVisibility(View.INVISIBLE);
                                doctor_profile_recycler.setVisibility(View.VISIBLE);
                            }
                        }

                        book_appointment_helper_class.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void handleDoctor(String specific, String snackBarMessage) {
        doctor_names.clear();
        doctor_specializations.clear();
        doctor_UID.clear();
        doctor_phone_no.clear();
        FireStoreUsersSpecific(specific);
        snackBarShow(snack_bar_layout, snackBarMessage);
    }

    private void handleNurse(String specific, String snackBarMessage) {
        doctor_names.clear();
        doctor_specializations.clear();
        doctor_UID.clear();
        doctor_phone_no.clear();
        NurseFireStoreUsersSpecific(specific);
        snackBarShow(snack_bar_layout, snackBarMessage);
    }

    private void NurseFireStoreUsers() {
        firestoreHandler.getFirestoreInstance().collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    singleton.showToast(Appointment_Doctor_Check.this, error.toString());
                }


                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {

                        String Category = String.valueOf(dc.getDocument().get("Profession"));

                        if (Category.equals("Nurse")) {
                            doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                            doctor_specializations.add(String.valueOf(dc.getDocument().get("Doctor_profession")));
                            doctor_UID.add(dc.getDocument().getId());
                            doctor_phone_no.add(String.valueOf(dc.getDocument().get("Phone #")));

                            if (doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0) {
                                no_doctor_image.setVisibility(View.VISIBLE);
                                no_doctors_available.setVisibility(View.VISIBLE);
                                doctor_profile_recycler.setVisibility(View.INVISIBLE);
                            }
                        }


                    }
                    book_appointment_helper_class.notifyDataSetChanged();
                }
            }
        });


    }

    private void FireStoreUsers() {
        firestoreHandler.getFirestoreInstance().collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    singleton.showToast(Appointment_Doctor_Check.this, error.toString());
                }


                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {

                        String Category = String.valueOf(dc.getDocument().get("Profession"));

                        if (Category.equals("Doctor")) {
                            doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                            doctor_specializations.add(String.valueOf(dc.getDocument().get("Doctor_profession")));
                            doctor_UID.add(dc.getDocument().getId());
                            doctor_phone_no.add(String.valueOf(dc.getDocument().get("Phone #")));

                            if (doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0) {
                                no_doctor_image.setVisibility(View.VISIBLE);
                                no_doctors_available.setVisibility(View.VISIBLE);
                                doctor_profile_recycler.setVisibility(View.INVISIBLE);
                            }
                        }


                    }
                    book_appointment_helper_class.notifyDataSetChanged();
                }
            }
        });

    }

    public void FireStoreUsersSpecific(String Category) {

        firestoreHandler.getFirestoreInstance().collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String category = String.valueOf(documentChange.getDocument().get("Profession"));
                        String Specialization = String.valueOf(documentChange.getDocument().get("Doctor_profession"));

                        if (category.equals("Doctor")) {

                            if (Specialization.equals(Category)) {

                                doctor_names.add(String.valueOf(documentChange.getDocument().get("Full Name")));
                                doctor_specializations.add(String.valueOf(documentChange.getDocument().get("Doctor_profession")));
                                doctor_UID.add(documentChange.getDocument().getId());
                                doctor_phone_no.add(String.valueOf(documentChange.getDocument().get("Phone #")));


                            }
                            if (doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0) {
                                no_doctor_image.setVisibility(View.VISIBLE);
                                no_doctors_available.setVisibility(View.VISIBLE);
                                doctor_profile_recycler.setVisibility(View.INVISIBLE);
                            } else {
                                no_doctor_image.setVisibility(View.INVISIBLE);
                                no_doctors_available.setVisibility(View.INVISIBLE);
                                doctor_profile_recycler.setVisibility(View.VISIBLE);
                            }
                        }
                        book_appointment_helper_class.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    public void snackBarShow(View snack_bar_layout, String msg) {
        Snackbar.make(snack_bar_layout, msg, Snackbar.LENGTH_SHORT).show();
    }


}