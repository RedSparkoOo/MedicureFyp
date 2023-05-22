package com.example.docportal.Patient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class AppointmentDoctorNurseSelection extends AppCompatActivity {

    SearchView search_doctor;
    String search_HINT_color = "#434242";
    String search_color = "#434242";

    RecyclerView doctor_profile_recycler;

    ArrayList<String> doctor_names;
    ArrayList<String> doctor_specializations;
    ArrayList<String> doctor_UID;
    ArrayList<String> doctor_phone_no;
    ArrayList<String> imageList;
    ArrayList<String> doctor_start_time;
    ArrayList<String> doctor_close_time;
    ArrayList<String> doctor_bio;
    ArrayList<String> doctor_img;
    FirebaseFirestore firestore;
    ImageView no_doctor_image;
    ImageView back_to_patient_dashboard;
    AppointmentBookingAdapter book_appointment_helper_class;
    String UID;
    FirebaseAuth firebaseAuth;
    View snack_bar_layout;
    TextView no_doctors_available;
    Spinner doctor_nurse_selection;
    Spinner doctor_nurse_profession;
    String[] category = {"Doctor","Nurse"};
    String[] doctors = {"All","Cardiologist","Nephrologist","Neurologist","Physiologist", "Psychologist"};
    String[] nurses = {"All","Mental Health Nurse (MHN)", "Learning Disability Nurse (LDN)", "Adult Nurse (AN)", "Children Nurse (CN)", "Critical Care Nurse (CCN)"};
    Singleton singleton = new Singleton();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_nurse_selection);

        back_to_patient_dashboard = findViewById(R.id.back_to_patient_dashboard);
        doctor_nurse_selection = findViewById(R.id.doctor_nurse_selection);
        doctor_nurse_profession = findViewById(R.id.doctor_nurse_profession);
        no_doctor_image = findViewById(R.id.no_doctor_image);
        no_doctors_available = findViewById(R.id.no_doctors_available);

        back_to_patient_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentDoctorNurseSelection.this,patientDashboard.class);
                startActivity(intent);
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
        Typeface tf = ResourcesCompat.getFont(this,R.font.pt_sans_regular);
        textView.setTypeface(tf);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        doctor_profile_recycler = findViewById(R.id.doctor_list_recycler);
        UID = firebaseAuth.getCurrentUser().getUid();

        doctor_names = new ArrayList<>();
        doctor_specializations = new ArrayList<>();
        doctor_UID = new ArrayList<>();
        doctor_phone_no = new ArrayList<>();
        imageList = new ArrayList<>();
        doctor_start_time = new ArrayList<>();
        doctor_close_time = new ArrayList<>();
        doctor_bio = new ArrayList<>();
        doctor_img = new ArrayList<>();

        doctor_profile_recycler.setLayoutManager(new LinearLayoutManager(AppointmentDoctorNurseSelection.this));
        book_appointment_helper_class = new AppointmentBookingAdapter(doctor_names, doctor_specializations, doctor_UID, doctor_phone_no,doctor_start_time,doctor_close_time,doctor_bio,doctor_img, new AppointmentBookingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String details) {
                Log.d(details,"Works");
            }
        });
        doctor_profile_recycler.setAdapter(book_appointment_helper_class);

        //-------------------------------------NURSE----------------------------------



        FireStoreUsers();
        Selection();


        /*Getting Data from Fire store*/


        search_doctor.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_doctor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        String query = String.valueOf(search_doctor.getQuery());
                        newText = query;
                        book_appointment_helper_class.getFilter().filter(newText);
                        return false;

                    }
                });
            }
        });

    }

    private void Selection() {

        singleton.setAdatper(this,doctor_nurse_selection,category);

        doctor_nurse_selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(doctor_nurse_selection.getSelectedItem().equals("Doctor")){
                    singleton.setAdatper(AppointmentDoctorNurseSelection.this,doctor_nurse_profession,doctors);
                }
                if(doctor_nurse_selection.getSelectedItem().equals("Nurse")){
                    singleton.setAdatper(AppointmentDoctorNurseSelection.this,doctor_nurse_profession,nurses);
                }


                if(doctor_nurse_selection.getSelectedItem().equals("Doctor")){

                    doctor_nurse_profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(doctor_nurse_profession.getSelectedItem().equals("All")){
                                doctor_names.clear();
                                doctor_specializations.clear();
                                doctor_UID.clear();
                                doctor_phone_no.clear();
                                doctor_start_time.clear();
                                doctor_close_time.clear();
                                doctor_bio.clear();
                                doctor_img.clear();
                                FireStoreUsers();

                            }
                            if(doctor_nurse_profession.getSelectedItem().equals("Cardiologist")){
                                handleDoctor("Cardiologist","Cardiologist Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Nephrologist")){
                                handleDoctor("Nephrologist","Nephrologist Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Neurologist")){
                                handleDoctor("Neurologist","Neurologist Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Physiologist")){
                                handleDoctor("Physiologist","Physiologist Selected");
                            }
                            if(doctor_nurse_profession.getSelectedItem().equals("Psychologist")){
                                handleDoctor("Psychologist","Psychologist Selected");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                if(doctor_nurse_selection.getSelectedItem().equals("Nurse")){


                    doctor_nurse_profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(doctor_nurse_profession.getSelectedItem().equals("All")){
                                doctor_names.clear();
                                doctor_specializations.clear();
                                doctor_UID.clear();
                                doctor_phone_no.clear();
                                doctor_start_time.clear();
                                doctor_close_time.clear();
                                doctor_bio.clear();
                                doctor_img.clear();
                                NurseFireStoreUsers();
                                snackBarShow(snack_bar_layout,"All Nurses Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Mental Health Nurse (MHN)")){
                                handleNurse("Mental Health Nurse (MHN)","Mental Health Nurse (MHN) Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Learning Disability Nurse (LDN)")){
                                handleNurse("Learning Disability Nurse (LDN)","Learning Disability Nurse (LDN) Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Adult Nurse (AN)")){
                                handleNurse("Adult Nurse (AN)","Adult Nurse (AN) Selected");
                            }

                            if(doctor_nurse_profession.getSelectedItem().equals("Children Nurse (CN)")){
                                handleNurse("Children Nurse (CN)","Children Nurse (CN) Selected");
                            }
                            if(doctor_nurse_profession.getSelectedItem().equals("Critical Care Nurse (CCN)")){
                                handleNurse("Critical Care Nurse (CCN)","Critical Care Nurse (CCN) Selected");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    private void NurseFireStoreUsersSpecific(String Category) {

        firestore.collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){

                        String category = String.valueOf(documentChange.getDocument().get("Profession"));
                        String Specialization = String.valueOf(documentChange.getDocument().get("Doctor_profession"));


                        if(category.equals("Nurse")){

                            if(Specialization.equals(Category)){

                                doctor_names.add(String.valueOf(documentChange.getDocument().get("Full Name")));
                                doctor_specializations.add(String.valueOf(documentChange.getDocument().get("Doctor_profession")));
                                doctor_UID.add(documentChange.getDocument().getId());
                                doctor_phone_no.add(String.valueOf(documentChange.getDocument().get("Phone #")));
                                doctor_start_time.add(String.valueOf(documentChange.getDocument().get("Start Time")));
                                doctor_close_time.add(String.valueOf(documentChange.getDocument().get("Close Time")));
                                doctor_bio.add(String.valueOf(documentChange.getDocument().get("Bio Details")));
                                doctor_img.add(String.valueOf(documentChange.getDocument().get("Image")));


                            }
                            if(doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0){
                                no_doctor_image.setVisibility(View.VISIBLE);
                                no_doctors_available.setVisibility(View.VISIBLE);
                                doctor_profile_recycler.setVisibility(View.INVISIBLE);
                            }
                            else {
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
    private void handleDoctor(String category, String message){
        doctor_names.clear();
        doctor_specializations.clear();
        doctor_UID.clear();
        doctor_phone_no.clear();
        doctor_start_time.clear();
        doctor_close_time.clear();
        doctor_bio.clear();
        doctor_img.clear();
        FireStoreUsersSpecific(category);
        snackBarShow(snack_bar_layout,message);
    }
    private void handleNurse(String category, String message){

        doctor_names.clear();
        doctor_specializations.clear();
        doctor_UID.clear();
        doctor_phone_no.clear();
        doctor_start_time.clear();
        doctor_close_time.clear();
        doctor_bio.clear();
        doctor_img.clear();
        NurseFireStoreUsersSpecific(category);
        snackBarShow(snack_bar_layout,message);
    }

    private void NurseFireStoreUsers() {
        firestore.collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(AppointmentDoctorNurseSelection.this, error.toString(), Toast.LENGTH_SHORT).show();
                }


                for(DocumentChange dc: value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){

                        String Category = String.valueOf(dc.getDocument().get("Profession"));

                        if(Category.equals("Nurse")){
                            doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                            doctor_specializations.add(String.valueOf(dc.getDocument().get("Doctor_profession")));
                            doctor_UID.add(dc.getDocument().getId());
                            doctor_phone_no.add(String.valueOf(dc.getDocument().get("Phone #")));
                            doctor_start_time.add(String.valueOf(dc.getDocument().get("Start Time")));
                            doctor_close_time.add(String.valueOf(dc.getDocument().get("Close Time")));
                            doctor_bio.add(String.valueOf(dc.getDocument().get("Bio Details")));
                            doctor_img.add(String.valueOf(dc.getDocument().get("Image")));


                            if(doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0){
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

    private void fetchProfile() {

        FirebaseStorage storage = FirebaseStorage.getInstance();


        StorageReference storageReference = storage.getReference().child("Professions");

        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Iterate over the items in the list
                for (StorageReference item : listResult.getItems()) {
                    // Get the download URL of the image
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Add the download URL to the ArrayList
                            imageList.add(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur while retrieving the download URL
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur while retrieving the data from Firebase
            }
        });



    }

    private void FireStoreUsers() {
        firestore.collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(AppointmentDoctorNurseSelection.this, error.toString(), Toast.LENGTH_SHORT).show();
                }


                for(DocumentChange dc: value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){

                        String Category = String.valueOf(dc.getDocument().get("Profession"));

                        if(Category.equals("Doctor")){
                            doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                            doctor_specializations.add(String.valueOf(dc.getDocument().get("Doctor_profession")));
                            doctor_UID.add(dc.getDocument().getId());
                            doctor_phone_no.add(String.valueOf(dc.getDocument().get("Phone #")));
                            doctor_start_time.add(String.valueOf(dc.getDocument().get("Start Time")));
                            doctor_close_time.add(String.valueOf(dc.getDocument().get("Close Time")));
                            doctor_bio.add(String.valueOf(dc.getDocument().get("Bio Details")));
                            if (dc.getDocument() != null && dc.getDocument().get("Image") != null) {
                                doctor_img.add(String.valueOf(dc.getDocument().get("Image")));
                            }

                            if(doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0){
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

    public void FireStoreUsersSpecific(String Category){

        firestore.collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){

                        String category = String.valueOf(documentChange.getDocument().get("Profession"));
                        String Specialization = String.valueOf(documentChange.getDocument().get("Doctor_profession"));

                        if(category.equals("Doctor")){

                            if(Specialization.equals(Category)){

                                doctor_names.add(String.valueOf(documentChange.getDocument().get("Full Name")));
                                doctor_specializations.add(String.valueOf(documentChange.getDocument().get("Doctor_profession")));
                                doctor_UID.add(documentChange.getDocument().getId());
                                doctor_phone_no.add(String.valueOf(documentChange.getDocument().get("Phone #")));
                                doctor_start_time.add(String.valueOf(documentChange.getDocument().get("Start Time")));
                                doctor_close_time.add(String.valueOf(documentChange.getDocument().get("Close Time")));
                                doctor_bio.add(String.valueOf(documentChange.getDocument().get("Bio Details")));
                                doctor_img.add(String.valueOf(documentChange.getDocument().get("Image")));



                            }
                            if(doctor_names.size() == 0 && doctor_specializations.size() == 0 || doctor_phone_no.size() == 0){
                                no_doctor_image.setVisibility(View.VISIBLE);
                                no_doctors_available.setVisibility(View.VISIBLE);
                                doctor_profile_recycler.setVisibility(View.INVISIBLE);
                            }
                            else {
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

    public void snackBarShow(View snack_bar_layout,String msg){
        Snackbar.make(snack_bar_layout,msg,Snackbar.LENGTH_SHORT).show();
    }




}