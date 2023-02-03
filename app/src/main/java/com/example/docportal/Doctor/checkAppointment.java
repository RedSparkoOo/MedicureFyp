package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

public class checkAppointment extends AppCompatActivity {


    RecyclerView patient_appointment_recycler_view;
    String User_id;
    ArrayList<String> patient_name;
    ArrayList<String> patient_phone;
    ArrayList<String> appointment_date;
    ArrayList<String> appointment_time;
    ArrayList<String> appointment_description;
    ArrayList<String> patient_id;


    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    String appointed_doctor_id;


    checkAppointmentAdapter checkAppointmentAdapter;
    DocumentReference doc_ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_appointment);
        //------------------Assigning Hooks------------------------------------

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        User_id = firebaseAuth.getCurrentUser().getUid();

        nullCheck();
        patient_name = new ArrayList<>();

        patient_phone = new ArrayList<>();

        appointment_date = new ArrayList<>();

        appointment_time = new ArrayList<>();

        appointment_description = new ArrayList<>();

        patient_id = new ArrayList<>();



        patient_appointment_recycler_view = findViewById(R.id.patient_appointment_recycler);
        patient_appointment_recycler_view.setLayoutManager(new LinearLayoutManager(checkAppointment.this));
        FireStoreAppointments();


    }


   // Appointment User
    private void FireStoreAppointments() {

        firestore.collection("Appointment").orderBy("Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(checkAppointment.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {


                        appointed_doctor_id = String.valueOf(dc.getDocument().get("Appointed Doctor ID"));

                        if(appointed_doctor_id.equals(User_id)){

                            patient_id.add(dc.getDocument().getId());
                            patient_name.add(String.valueOf(dc.getDocument().get("Patient Name")));
                            patient_phone.add(String.valueOf(dc.getDocument().get("Patient Phone No")));
                            appointment_date.add(String.valueOf(dc.getDocument().get("Appointment Date")));
                            appointment_time.add(String.valueOf(dc.getDocument().get("Appointment Time")));
                            appointment_description.add(String.valueOf(dc.getDocument().get("Appointment Description")));

                            checkAppointmentAdapter = new checkAppointmentAdapter(patient_name, patient_phone, appointment_date, appointment_time, appointment_description,User_id,patient_id, new checkAppointmentAdapter.ItemClickListenerCheck(){

                                @Override
                                public String onItemClick(String details) {
                                    Log.d(TAG, "onItemClick: Works ");
                                    return details;
                                }
                            });
                            patient_appointment_recycler_view.setAdapter(checkAppointmentAdapter);

                        }


                    }

                }

                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.REMOVED){
                        checkAppointmentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

private void nullCheck(){
    patient_name = null;
    patient_phone = null;
    appointment_date = null;
    appointment_time = null;
    appointment_description = null;
    patient_id = null;
}


}