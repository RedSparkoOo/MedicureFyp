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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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

    bookAppointmentHelperClass book_appointment_helper_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_doctor_check);

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

        /*Getting Data from Fire store*/

        FireStoreUsers();
    }

    private void FireStoreUsers() {
        firestore.collection("Doctor").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error != null){
                        Toast.makeText(Appointment_Doctor_Check.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }

                    for(DocumentChange dc: value.getDocumentChanges()){

                        if(dc.getType() == DocumentChange.Type.ADDED){

                            doctor_names.add(String.valueOf(dc.getDocument().get("Full Name")));
                            doctor_specializations.add(String.valueOf(dc.getDocument().get("Specialization")));
                            doctor_UID.add(dc.getDocument().getId());
                        }
                        book_appointment_helper_class.notifyDataSetChanged();

                    }

            }
        });

    }




}