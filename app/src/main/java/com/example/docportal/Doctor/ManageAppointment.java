package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import timber.log.Timber;


public class ManageAppointment extends AppCompatActivity {


    RecyclerView patient_appointment_recycler_view;

    ArrayList<String> patient_name;
    ArrayList<String> patient_phone;
    ArrayList<String> appointment_date;
    ArrayList<String> appointment_time;
    ArrayList<String> appointment_description;
    ArrayList<String> patient_id;
    ArrayList<String> Doctor_name;
    ArrayList<String> Doctor_phone;
    ArrayList appointment_ids;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();

    String appointed_doctor_id;
    ImageView back_to_doctor_dashboard;

    LinearLayout mail_box_show;

    ManageAppointmentAdapter checkAppointmentAdapter;

    int notification_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointment);
        //------------------Assigning Hooks------------------------------------

        mail_box_show = findViewById(R.id.mail_box_show);
        back_to_doctor_dashboard = findViewById(R.id.back_to_doctor_dashboard);


        patient_name = new ArrayList<>();

        patient_phone = new ArrayList<>();

        Doctor_name = new ArrayList<>();

        Doctor_phone = new ArrayList<>();

        appointment_date = new ArrayList<>();

        appointment_time = new ArrayList<>();

        appointment_description = new ArrayList<>();

        patient_id = new ArrayList<>();
        appointment_ids = new ArrayList();


        patient_appointment_recycler_view = findViewById(R.id.patient_appointment_recycler);
        patient_appointment_recycler_view.setLayoutManager(new LinearLayoutManager(ManageAppointment.this));
        FireStoreAppointments();

        back_to_doctor_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(ManageAppointment.this, DoctorNurseDashboard.class);
            }
        });


    }


    // Appointment User
    public void FireStoreAppointments() {
        firestoreHandler.getFirestoreInstance().clearPersistence();

        firestoreHandler.getFirestoreInstance().collection("Appointment").orderBy("Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    singleton.showToast(ManageAppointment.this, error.toString());

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc != null) {

                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            appointed_doctor_id = String.valueOf(dc.getDocument().get("Appointed Doctor ID"));


                            if (appointed_doctor_id.equals(firestoreHandler.getCurrentUser())) {
                                mail_box_show.setVisibility(View.VISIBLE);
                                patient_appointment_recycler_view.setVisibility(View.VISIBLE);
                                appointment_ids.add(dc.getDocument().getId());
                                patient_id.add(String.valueOf(dc.getDocument().get("Patient ID")));
                                patient_name.add(String.valueOf(dc.getDocument().get("Patient Name")));
                                patient_phone.add(String.valueOf(dc.getDocument().get("Patient Phone No")));
                                Doctor_name.add(String.valueOf(dc.getDocument().get("Doctor Name")));
                                Doctor_phone.add(String.valueOf(dc.getDocument().get("Doctor Phone No")));
                                appointment_date.add(String.valueOf(dc.getDocument().get("Appointment Date")));
                                appointment_time.add(String.valueOf(dc.getDocument().get("Appointment Time")));
                                appointment_description.add(String.valueOf(dc.getDocument().get("Appointment Description")));

                                checkAppointmentAdapter = new ManageAppointmentAdapter(appointment_ids, patient_name, patient_phone, Doctor_name, Doctor_phone, appointment_date, appointment_time, appointment_description, firestoreHandler.getCurrentUser(), patient_id, new ManageAppointmentAdapter.ItemClickListenerCheck() {
                                    @Override
                                    public String onItemClick(String details) {
                                        Log.d(TAG, "onItemClick: Works ");
                                        return details;
                                    }
                                });
                                patient_appointment_recycler_view.setAdapter(checkAppointmentAdapter);
                                store_notification_count();
                                checkAppointmentAdapter.notifyDataSetChanged();


                            }

                        }
                    }
                }

            }
        });

    }


    public void store_notification_count() {
        CollectionReference reference = firestoreHandler.getFirestoreInstance().collection("Appointment");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    singleton.showToast(ManageAppointment.this, "Listen failed");
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Timber.tag(TAG).d("New message: " + dc.getDocument().getData());
                        String Title = "New";
                    }
                }
            }
        });

    }


}