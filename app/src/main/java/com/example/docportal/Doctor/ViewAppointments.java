package com.example.docportal.Doctor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ViewAppointments extends AppCompatActivity {
    RecyclerView appointment_recycler_view;
    List<String> approved_patient_id;
    List<String> approved_patient_names;
    SearchView search_patient;
    List<String> approved_patient_phone_no;
    List<String> approved_appointment_date;
    List<String> approved_appointment_time;
    List<String> approved_appointment_ID;
    ViewAppointmentAdapter appointmentadapter;
    Singleton singleton = new Singleton();

    String recieved_doctor_id;
    LinearLayout appointments_viewed;

    String search_HINT_color = "#434242";
    String search_color = "#434242";
    ImageView back_to_doctor_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);

        appointments_viewed = findViewById(R.id.letter_box);
        search_patient = findViewById(R.id.search_patient);
        back_to_doctor_dashboard = findViewById(R.id.back_to_doctor_dashboard);

        approved_patient_names = new ArrayList<>();
        approved_patient_phone_no = new ArrayList<>();
        approved_appointment_date = new ArrayList<>();
        approved_appointment_time = new ArrayList<>();
        approved_appointment_ID = new ArrayList<>();
        approved_patient_id = new ArrayList<>();

        appointment_recycler_view = findViewById(R.id.manage_appointment_recycler);
        appointment_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        appointments_viewed.setVisibility(View.VISIBLE);

        search_patient = findViewById(R.id.search_patient);
        int id = search_patient.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = search_patient.findViewById(id);
        textView.setTextColor(Color.parseColor(search_color));
        textView.setTextSize(14);
        textView.setHintTextColor(Color.parseColor(search_HINT_color));
        Typeface tf = ResourcesCompat.getFont(this, R.font.pt_sans_regular);
        textView.setTypeface(tf);

        FireStoreApprovedAppointments();

        appointment_recycler_view.setVisibility(View.INVISIBLE);
        search_patient.setVisibility(View.INVISIBLE);
        search_patient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  appointmentadapter.getFilter().filter(newText);
                return false;
            }
        });

        back_to_doctor_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(ViewAppointments.this, DoctorNurseDashboard.class);
            }
        });
    }

    private void FireStoreApprovedAppointments() {
        FirestoreHandler firestoreHandler = new FirestoreHandler();

        firestoreHandler.getFirestoreInstance().collection("Approved Appointments").orderBy("Approved Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    singleton.showToast(ViewAppointments.this, error.toString());

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc != null) {


                        recieved_doctor_id = String.valueOf(dc.getDocument().get("Appointed Doctor Id"));
                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            if (recieved_doctor_id.equals(firestoreHandler.getCurrentUser())) {

                                appointments_viewed.setVisibility(View.INVISIBLE);
                                appointment_recycler_view.setVisibility(View.VISIBLE);
                                search_patient.setVisibility(View.VISIBLE);

                                System.out.println(dc.getDocument().get("Appointed Patient Id") == null ? "ff" : "mang");
                                System.out.println(dc.getDocument().get("Appointed Patient Id").toString());
                                approved_patient_id.add(String.valueOf(dc.getDocument().get("Appointed Patient Id")));
                                approved_patient_names.add(String.valueOf(dc.getDocument().get("Approved Patient Name")));
                                approved_patient_phone_no.add(String.valueOf(dc.getDocument().get("Approved Patient Cell")));
                                approved_appointment_date.add(String.valueOf(dc.getDocument().get("Approved Appointment Date")));
                                approved_appointment_time.add(String.valueOf(dc.getDocument().get("Approved Appointment Time")));
                                approved_appointment_ID.add(dc.getDocument().getId());
                                appointmentadapter = new ViewAppointmentAdapter(dc.getDocument().get("Appointed Patient Id").toString(), approved_patient_id, approved_patient_names, approved_patient_phone_no, approved_appointment_date, approved_appointment_time, approved_appointment_ID, new ViewAppointmentAdapter.ItemClickListenerCheck() {
                                    @Override
                                    public String onItemClick(String details) {
                                        return null;
                                    }
                                });
                                appointment_recycler_view.setAdapter(appointmentadapter);
                            }

                        }

                    } else
                        singleton.showToast(ViewAppointments.this, "No Appointments to show");
                }
            }
        });
    }


}

