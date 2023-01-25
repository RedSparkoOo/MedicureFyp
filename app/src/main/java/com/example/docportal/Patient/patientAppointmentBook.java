package com.example.docportal.Patient;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class patientAppointmentBook extends AppCompatActivity {
    EditText full_name;
    EditText Phone;
    EditText Date;
    EditText Time;
    EditText description;
    Button Submit;



    // to access root Node EXP: Doctor Portal
    FirebaseDatabase RootNode;
    // to access sub root node
    DatabaseReference reference;


    DatePickerDialog.OnDateSetListener setListener;

    int year;   // to store year
    int month; //to store date
    int day; //to store date

    int hour; //to store hours
    int minute; //to store date


    String appointeeName;
    String appointeePhone;
    String appointeeDescription;
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

        appointeeName = full_name.getText().toString();
        appointeePhone = Phone.getText().toString();
        appointeeDescription = description.getText().toString();

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

               appointeeName = full_name.getText().toString();
               appointeePhone = Phone.getText().toString();
               appointeeDescription = description.getText().toString();

                 Bundle appointment_booking_bundle = new Bundle();
                 appointment_booking_bundle.putString("User_Name",appointeeName);
                 appointment_booking_bundle.putString("User_Phone",appointeePhone);
                 appointment_booking_bundle.putString("User_Date",DATE);
                 appointment_booking_bundle.putString("User_Time",TIME);
                 appointment_booking_bundle.putString("User_Description",appointeeDescription);

               Toast.makeText(patientAppointmentBook.this, appointeeName+appointeePhone+DATE+TIME+appointeeDescription, Toast.LENGTH_SHORT).show();


           }
       });


    }
}






