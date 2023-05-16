package com.example.docportal.Doctor;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;


public class ViewAppointments extends AppCompatActivity {
    RecyclerView appointment_recycler_view;

    SearchView search_patient;

    ViewAppointmentAdapter appointmentadapter;
    Singleton singleton = new Singleton();


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


//        search_patient = findViewById(R.id.search_patient);
//        int id = search_patient.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//        TextView textView = search_patient.findViewById(id);
//        textView.setTextColor(Color.parseColor(search_color));
//        textView.setTextSize(14);
//        textView.setHintTextColor(Color.parseColor(search_HINT_color));
//        Typeface tf = ResourcesCompat.getFont(this, R.font.pt_sans_regular);
//        textView.setTypeface(tf);

        FireStoreApprovedAppointments();


        search_patient.setVisibility(View.VISIBLE);
        appointments_viewed.setVisibility(View.INVISIBLE); // to remove
//        search_patient.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //  appointmentadapter.getFilter().filter(newText);
//                return false;
//            }
//        });

        back_to_doctor_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(ViewAppointments.this, DoctorNurseDashboard.class);
            }
        });
    }

    private void FireStoreApprovedAppointments() {

        FirestoreHandler firestoreHandler = new FirestoreHandler();
        CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Approved Appointments");
        Query query = noteBookref.orderBy("ApprovedPatientName", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
                .setQuery(query, Appointment.class).build();
        appointmentadapter = new ViewAppointmentAdapter(options, new ViewAppointmentAdapter.ItemClickListenerCheck() {
            @Override
            public String onItemClick(String details) {
                return null;
            }
        });
        appointment_recycler_view = findViewById(R.id.manage_appointment_recycler);
        appointment_recycler_view.setLayoutManager(new WrapContentLinearLayoutManager(ViewAppointments.this, LinearLayoutManager.VERTICAL, false));


        appointment_recycler_view.setAdapter(appointmentadapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        appointmentadapter.startListening();
    }
}

