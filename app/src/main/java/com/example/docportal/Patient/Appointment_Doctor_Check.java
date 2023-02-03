package com.example.docportal.Patient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Appointment_Doctor_Check extends AppCompatActivity {

    SearchView search_doctor;
    String search_HINT_color = "#434242";
    String search_color = "#434242";

    RecyclerView doctor_profile_recycler;

    ArrayList<String> doctor_names;
    ArrayList<String> doctor_specializations;
    ArrayList<String> doctor_UID;
    List<Bitmap> doctor_profile;

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String user_id;

    ImageView cardio_category;
    ImageView neuro_category;
    ImageView nephro_category;
    ImageView oncol_category;
    ImageView pedri_category;
    ImageView physio_category;
    ImageView physco_category;
    bookAppointmentHelperClass book_appointment_helper_class;
    boolean cardio_check = false;
    View snack_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_doctor_check);

        cardio_category = findViewById(R.id.cardio_category);
        nephro_category = findViewById(R.id.nephro_category);
        neuro_category = findViewById(R.id.neuro_category);
        oncol_category = findViewById(R.id.oncol_category);
        pedri_category = findViewById(R.id.pedri_category);
        physio_category = findViewById(R.id.physio_category);
        physco_category = findViewById(R.id.physco_category);

        snack_bar_layout = findViewById(android.R.id.content);
        search_doctor = findViewById(R.id.search_doctor);
        int id = search_doctor.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search_doctor.findViewById(id);
        textView.setTextColor(Color.parseColor(search_color));
        textView.setTextSize(14);
        textView.setHintTextColor(Color.parseColor(search_HINT_color));
        Typeface tf = ResourcesCompat.getFont(this,R.font.pt_sans_regular);
        textView.setTypeface(tf);

        firestore = FirebaseFirestore.getInstance();
        doctor_profile_recycler = findViewById(R.id.doctor_list_recycler);

        doctor_names = new ArrayList<>();
        doctor_specializations = new ArrayList<>();
        doctor_UID = new ArrayList<>();

        doctor_profile_recycler.setLayoutManager(new LinearLayoutManager(Appointment_Doctor_Check.this));
        book_appointment_helper_class = new bookAppointmentHelperClass(doctor_names, doctor_specializations, doctor_UID, new bookAppointmentHelperClass.ItemClickListener() {
            @Override
            public void onItemClick(String details) {
                Log.d(details,"Works");
            }
        });
        doctor_profile_recycler.setAdapter(book_appointment_helper_class);
        /*Categories*/

        cardio_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireStoreUsers("Cardiologist");
                snackBarShow(snack_bar_layout,"Cardiologist Selected");
            }
        });

        neuro_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarShow(snack_bar_layout,"Neurologist Selected");
            }
        });

        nephro_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarShow(snack_bar_layout,"Nephrologist Selected");
            }
        });

        oncol_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarShow(snack_bar_layout,"Oncologist Selected");
            }
        });

        pedri_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarShow(snack_bar_layout,"Pediatrician Selected");
            }
        });

        physio_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarShow(snack_bar_layout,"Physiologist Selected");
            }
        });

        physco_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBarShow(snack_bar_layout,"Physcologist Selected");
            }
        });



        /*Getting Data from Fire store*/


    }

    private void FireStoreUsers(String category) {
        firestore.collection("Doctor").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error != null){
                        Toast.makeText(Appointment_Doctor_Check.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }

                      for(DocumentChange dc: value.getDocumentChanges()){

                          if(dc.getType() == DocumentChange.Type.ADDED){

                              String speciality = String.valueOf(dc.getDocument().get("Specialization"));

                              if(category.equals(speciality)){
                                  doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                                  doctor_specializations.add(String.valueOf(dc.getDocument().get("Specialization")));
                                  doctor_UID.add(dc.getDocument().getId());
                              }

                              else{
                                  doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                                  doctor_specializations.add(String.valueOf(dc.getDocument().get("Specialization")));
                                  doctor_UID.add(dc.getDocument().getId());
                              }


                          }
                          book_appointment_helper_class.notifyDataSetChanged();

                      }


            }
        });

    }

    public void FireStoreCategoryCheck(){

    }

    public void snackBarShow(View snack_bar_layout,String msg){
        Snackbar.make(snack_bar_layout,msg,Snackbar.LENGTH_SHORT).show();
    }




}