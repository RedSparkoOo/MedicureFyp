package com.example.docportal.Patient;

import static java.sql.Types.TIME;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReminderStored extends AppCompatActivity {

    RecyclerView reminder_recycler;
    ImageView back_to_patient_dashboard;
    ArrayList<String> medicine_name;
    ArrayList<String> medicine_type;
    ArrayList<String> medicine_duration;
    ArrayList<String> medicine_time;
    ArrayList<String> reminder_id;
    ReminderStoredAdapter storedAdapter;

    Singleton singleton = new Singleton();
    LinearLayout empty_reminder_show;

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // Do your task here
            MedicineNotify();

            mHandler.postDelayed(mRunnable, 30000);
        }
    };
    String med_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_stored);

        reminder_recycler = findViewById(R.id.reminder_recycler);
        back_to_patient_dashboard = findViewById(R.id.back_to_patient_dashboard);
        empty_reminder_show = findViewById(R.id.empty_reminder_show);



        back_to_patient_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(ReminderStored.this, patientDashboard.class);
            }
        });
        RecievedReminders();
        empty_reminder_show.setVisibility(View.VISIBLE);
        mHandler.postDelayed(mRunnable, 30000);


    }

    private void RecievedReminders() {

        medicine_name = new ArrayList<>();
        medicine_type = new ArrayList<>();
        medicine_duration = new ArrayList<>();
        medicine_time = new ArrayList<>();
        reminder_id = new ArrayList<>();

        FirestoreHandler firestoreHandler = new FirestoreHandler();

        firestoreHandler.getFirestoreInstance().collection("Medicine Reminder").orderBy("Medicine Name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value !=null){

                    for (DocumentChange dc : value.getDocumentChanges()) {


                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            String recieved_patient_id = String.valueOf(dc.getDocument().get("Patient ID"));

                            if (recieved_patient_id.equals(firestoreHandler.getCurrentUser())) {

                                empty_reminder_show.setVisibility(View.INVISIBLE);
                                medicine_name.add(String.valueOf(dc.getDocument().get("Medicine Name")));
                                medicine_type.add(String.valueOf(dc.getDocument().get("Medicine Type")));
                                medicine_duration.add(String.valueOf(dc.getDocument().get("Medicine Duration")));
                                medicine_time.add(String.valueOf(dc.getDocument().get("Medicine Time")));
                                reminder_id.add(dc.getDocument().getId());
                                med_duration = String.valueOf(dc.getDocument().get("Medicine Duration"));

                            }
                        }

                        reminder_recycler.setLayoutManager(new LinearLayoutManager(ReminderStored.this));
                        storedAdapter = new ReminderStoredAdapter(medicine_name, medicine_type, medicine_duration, medicine_time, reminder_id);
                        reminder_recycler.setAdapter(storedAdapter);
                        storedAdapter.notifyDataSetChanged();

                    }
                    MedicineNotify();
                }
                else {
                    empty_reminder_show.setVisibility(View.VISIBLE);
                }

            }
        });


    }


    private void MedicineNotify() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String timeString = timeFormat.format(calendar.getTime());


        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());

        if(med_duration != null){
            String[] duration = med_duration.split(" ");

            for (String key : duration) {

                if(dayOfWeek.equals(key)){

                    for (String time: medicine_time) {

                        if(time.equals(timeString)){
                            addNotifications();
                        }

                    }

                }
            }

        }

    }


    private void addNotifications() {

        Intent i = new Intent(this, HealthTracker.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Medicure")
                .setSmallIcon(R.drawable.medicure_logo_2)
                .setContentTitle("Medicine Reminder")
                .setContentText("Time to take your medicine")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(123, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}