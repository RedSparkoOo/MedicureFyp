package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import android.content.Context;
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
import com.example.docportal.Patient.BuyMedicalAdapter;
import com.example.docportal.Patient.BuyMedicine;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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



    ManageAppointmentAdapter adapter;

    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();


    ImageView back_to_doctor_dashboard;

    LinearLayout mail_box_show;



    int notification_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointment);
        //------------------Assigning Hooks------------------------------------

        mail_box_show = findViewById(R.id.mail_box_show);
        back_to_doctor_dashboard = findViewById(R.id.back_to_doctor_dashboard);
        CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Appointment");
        Query query = noteBookref.whereEqualTo("AppointedDoctorID", firestoreHandler.getCurrentUser()). orderBy("PatientName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<AppointmentHolder> options = new FirestoreRecyclerOptions.Builder<AppointmentHolder>()
                .setQuery(query, AppointmentHolder.class).build();
        adapter = new ManageAppointmentAdapter(options);
        patient_appointment_recycler_view = findViewById(R.id.patient_appointment_recycler);

        patient_appointment_recycler_view.setLayoutManager(new WrapContentLinearLayoutManager(ManageAppointment.this, LinearLayoutManager.VERTICAL, false));
        patient_appointment_recycler_view.setAdapter(adapter);

        mail_box_show.setVisibility(View.INVISIBLE);







        back_to_doctor_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(ManageAppointment.this, DoctorNurseDashboard.class);
            }
        });


    }



//    public void store_notification_count() {
//        CollectionReference reference = firestoreHandler.getFirestoreInstance().collection("Appointment");
//        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null)
//                    singleton.showToast(ManageAppointment.this, "Listen failed");
//                for (DocumentChange dc : value.getDocumentChanges()) {
//                    if (dc.getType() == DocumentChange.Type.ADDED) {
//                        Timber.tag(TAG).d("New message: " + dc.getDocument().getData());
//                        String Title = "New";
//                    }
//                }
//            }
//        });
//
//    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}