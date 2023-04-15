package com.example.docportal.Patient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class patientAppointmentBook extends AppCompatActivity {
    EditText full_name;
    EditText Phone;
    EditText Date;
    EditText Time;
    EditText description;
    Button Submit;

    FirebaseFirestore FStore;
    FirebaseAuth FAuth;
    String patient_UID;

    DatePickerDialog.OnDateSetListener setListener;

    int year;   // to store year
    int month; //to store date
    int day;   //to store date

    int hour; //to store hours
    int minute; //to store date


    String booker_name;
    String booker_phone;
    String booker_description;
    String doctor_UIDD;
    String TimeZone;
    String TIME;
    String DATE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_appointment);
        full_name = (EditText) findViewById(R.id.AppointeeFirstName);
        Phone = (EditText) findViewById(R.id.Phone);
        Date = (EditText) findViewById(R.id.date);
        Time = (EditText) findViewById(R.id.timee);
        description = (EditText) findViewById(R.id.Description);
        Submit = (Button) findViewById(R.id.Confirm);

        booker_name = full_name.getText().toString();
        booker_phone = Phone.getText().toString();
        booker_description = description.getText().toString();

        Bundle bundle = getIntent().getExtras();
        doctor_UIDD = bundle.getString("Doctor_ID");
        full_name.setText(doctor_UIDD);

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get((Calendar.MINUTE));
              //  boolean Is24hourFormat = DateFormat.is24HourFormat(patientAppointmentBook.this);
                TimePickerDialog timePickerDialog = new TimePickerDialog(patientAppointmentBook.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        if(calendar.get(Calendar.AM_PM) == Calendar.AM){
                                TimeZone = "AM";
                        }
                        else if(calendar.get(Calendar.AM_PM) == Calendar.PM){
                                TimeZone = "PM";
                        }
                        TIME = Hour+":"+Minute+" "+TimeZone;
                        Time.setTextColor(Color.BLACK);
                        Time.setText(TIME);
                    }
                },hour,minute,false);
                timePickerDialog.show();

            }
        });


       Date.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Calendar calendar = Calendar.getInstance();
               day = calendar.get(Calendar.DAY_OF_MONTH);
               month = calendar.get(Calendar.MONTH);
               year = calendar.get(Calendar.YEAR);

               DatePickerDialog datePickerDialog = new DatePickerDialog(patientAppointmentBook.this, new DatePickerDialog.OnDateSetListener() {

                   @Override
                   public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {

                       DATE = DAY+"/"+MONTH+"/"+YEAR;
                       Date.setTextColor(Color.BLACK);
                       Date.setText(DATE);

                   }
               },year,month,day);
               datePickerDialog.show();

           }
       });

       Submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               booker_name = full_name.getText().toString();
               booker_phone = Phone.getText().toString();
               booker_description = description.getText().toString();
//               patient_UID = FAuth.getCurrentUser().getUid();
               DocumentReference D_Ref = FStore.collection("Appointments").document(doctor_UIDD);

               D_Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                   @Override
                   public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                       Map<String, Object> appointments = new HashMap<>();
                       appointments.put("Patient Name",booker_name);
                       appointments.put("Patient Phone",booker_phone);
                       appointments.put("Appointment Date",DATE);
                       appointments.put("Appointment Time",TIME);
                       appointments.put("Appointment Description",booker_description);
                       D_Ref.set(appointments);
                   }
               });


               Toast.makeText(patientAppointmentBook.this, booker_name + booker_phone +DATE+TIME+ booker_description, Toast.LENGTH_SHORT).show();


           }
       });


    }
}






