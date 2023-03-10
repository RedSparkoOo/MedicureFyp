package com.example.docportal.Patient;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.HelperFunctions;
import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class patientAppointmentBook extends AppCompatActivity {
    EditText patient_full_name;
    EditText patient_phone_no;
    EditText appointment_date;
    EditText appointment_time;
    EditText appointment_description;
    Button book_appointment;

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
    String doctor_phone;
    String doctor_name;
    String doctor_id;
    String TimeZone;
    String TIME;
    String DATE;

    View snack_bar_layout;
    FirebaseAuth firebaseAuth;
    HelperFunctions helperFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_appointment);
        patient_full_name = (EditText) findViewById(R.id.patient_first_name);
        patient_phone_no = (EditText) findViewById(R.id.patient_phone);
        appointment_date = (EditText) findViewById(R.id.appointment_date);
        appointment_time = (EditText) findViewById(R.id.appointment_time);
        appointment_description = (EditText) findViewById(R.id.appointment_description);
        book_appointment = (Button) findViewById(R.id.book);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        doctor_phone = bundle.getString("Doctor_phone");
        doctor_name = bundle.getString("Doctor_name");
        doctor_id = bundle.getString("Doctor_Id");


        snack_bar_layout = findViewById(android.R.id.content);
        helperFunctions = new HelperFunctions();



        appointment_time.setOnClickListener(new View.OnClickListener() {
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
                        TIME = String.format(Hour+":"+Minute+" "+TimeZone );
                        appointment_time.setTextColor(Color.BLACK);
                        appointment_time.setText(TIME);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });


       appointment_date.setOnClickListener(new View.OnClickListener() {
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
                       appointment_date.setTextColor(Color.BLACK);
                       appointment_date.setText(DATE);

                   }
               },year,month,day);
               datePickerDialog.show();
           }
       });


       book_appointment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Dialog dialog = new Dialog(patientAppointmentBook.this);
               dialog.setContentView(R.layout.alert_box_layout);
               dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.edges));
               dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
               dialog.setCancelable(true);
               dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
               Button confirm = dialog.findViewById(R.id.alert_confirm);
               TextView cancel = dialog.findViewById(R.id.alert_cancel);
               TextView alert_msg = dialog.findViewById(R.id.alert_msg);
               alert_msg.setText("Are you sure you want to logout?");

               confirm.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


                       FirebaseFirestore.getInstance().clearPersistence();
                       FStore = FirebaseFirestore.getInstance();
                       FAuth = FirebaseAuth.getInstance();

                       if(booker_name!= null && booker_phone!= null && booker_description!= null){
                           booker_name = null;
                           booker_phone = null;
                           booker_description = null;
                       }

                       booker_name = patient_full_name.getText().toString();
                       booker_phone = patient_phone_no.getText().toString();
                       booker_description = appointment_description.getText().toString();
                       patient_UID = FAuth.getCurrentUser().getUid();

                       DocumentReference D_Ref = FStore.collection("Appointment").document();


                       D_Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                           @Override
                           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                               Map<String, Object> appointment = new HashMap<>();
                               appointment.put("Patient ID",patient_UID);
                               appointment.put("Patient Name",booker_name);
                               appointment.put("Patient Phone No",booker_phone);
                               appointment.put("Appointed Doctor ID", doctor_id);
                               appointment.put("Doctor Name",doctor_name);
                               appointment.put("Doctor Phone No",doctor_phone);
                               appointment.put("Appointment Date",DATE);
                               appointment.put("Appointment Time",TIME);
                               appointment.put("Appointment Description",booker_description);



                               D_Ref.set(appointment);
                           }
                       });
                       dialog.dismiss();
                        helperFunctions.snackBarShow(snack_bar_layout,"Appointment Booked");

                   }
               });

               cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       dialog.dismiss();
                   }
               });

               dialog.show();





           }
       });


    }
}






