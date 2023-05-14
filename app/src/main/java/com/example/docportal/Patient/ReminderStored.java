package com.example.docportal.Patient;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReminderStored extends AppCompatActivity {

    RecyclerView reminder_recycler;


    ArrayList<String> medicine_name;
    ArrayList<String> medicine_type;
    ArrayList<String> medicine_duration;
    ArrayList<String> medicine_time;
    ArrayList<String> reminder_id;
    ReminderStoredAdapter storedAdapter;
    ImageView back_to_health_tracker;
    Singleton singleton = new Singleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_stored);

        reminder_recycler = findViewById(R.id.reminder_recycler);


        medicine_name = new ArrayList<>();
        medicine_type = new ArrayList<>();
        medicine_duration = new ArrayList<>();
        medicine_time = new ArrayList<>();
        reminder_id = new ArrayList<>();
        back_to_health_tracker = findViewById(R.id.back_to_health_tracker);

        back_to_health_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(ReminderStored.this, HealthTracker.class);
            }
        });
        RecievedReminders();
    }

    private void RecievedReminders() {
        FirestoreHandler firestoreHandler = new FirestoreHandler();

        firestoreHandler.getFirestoreInstance().collection("Medicine Reminder").orderBy("Medicine Name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc != null)
                        singleton.showToast(ReminderStored.this, "Data is present");

                    if (dc.getType() == DocumentChange.Type.ADDED) {

                        String recieved_patient_id = String.valueOf(dc.getDocument().get("Patient ID"));

                        if (recieved_patient_id.equals(firestoreHandler.getCurrentUser())) {


                            medicine_name.add(String.valueOf(dc.getDocument().get("Medicine Name")));
                            medicine_type.add(String.valueOf(dc.getDocument().get("Medicine Type")));
                            medicine_duration.add(String.valueOf(dc.getDocument().get("Medicine Duration")));
                            medicine_time.add(String.valueOf(dc.getDocument().get("Medicine Time")));
                            reminder_id.add(dc.getDocument().getId());


                            reminder_recycler.setLayoutManager(new LinearLayoutManager(ReminderStored.this));
                            storedAdapter = new ReminderStoredAdapter(medicine_name, medicine_type, medicine_duration, medicine_time, reminder_id);
                            reminder_recycler.setAdapter(storedAdapter);

                        }
                    }
                }

            }
        });


    }
}