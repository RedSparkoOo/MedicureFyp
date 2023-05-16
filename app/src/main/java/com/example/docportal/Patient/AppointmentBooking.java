package com.example.docportal.Patient;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.AppointmentCheckEvent;
import com.example.docportal.Doctor.DoctorNurseRegistration;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.HelperFunctions;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppointmentBooking extends AppCompatActivity {
    EditText patient_full_name;
    EditText patient_phone_no;
    EditText appointment_date;
    EditText appointment_time;
    EditText appointment_description;
    Button book_appointment;

    FirebaseFirestore FStore;
    FirebaseAuth FAuth;
    String patient_UID;
    ImageView back_to_doc_nur_Selection;

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
    FirebaseFirestore firebaseFirestore;
    FirestoreHandler firestoreHandler = new FirestoreHandler();

    View snack_bar_layout;
    FirebaseAuth firebaseAuth;
    Object currentUserId;
    HelperFunctions helperFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_booking);
        patient_full_name = (EditText) findViewById(R.id.patient_first_name);
        patient_phone_no = (EditText) findViewById(R.id.patient_phone);
        appointment_date = (EditText) findViewById(R.id.appointment_date);
        appointment_time = (EditText) findViewById(R.id.appointment_time);
        appointment_description = (EditText) findViewById(R.id.appointment_description);
        book_appointment = (Button) findViewById(R.id.book);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        firebaseFirestore = FirebaseFirestore.getInstance();
        back_to_doc_nur_Selection = findViewById(R.id.back_to_doc_nur_Selection);
        doctor_phone = bundle.getString("Doctor_phone");
        doctor_name = bundle.getString("Doctor_name");
        doctor_id = bundle.getString("Doctor_Id");

        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
        TextView[] textViews = {patient_full_name, patient_phone_no, appointment_date, appointment_time,appointment_description};
        AppointmentCheckEvent checkEvent = new AppointmentCheckEvent();
        DocumentReference documentReference = firebaseFirestore.collection("Patient").document(currentUserId.toString());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                patient_full_name.setText(value.getString("Patient Name"));
                patient_phone_no.setText(value.getString("Patient phone_no"));

            }
        });

        snack_bar_layout = findViewById(android.R.id.content);
        helperFunctions = new HelperFunctions();

        back_to_doc_nur_Selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentBooking.this, AppointmentBooking.class);
                startActivity(intent);
            }
        });

        appointment_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
                int start_minute = calendar.get((Calendar.MINUTE));

                //  boolean Is24hourFormat = DateFormat.is24HourFormat(AppointmentBooking.this);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentBooking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                        int hourOfDay = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        TIME = timeFormat.format(calendar.getTime());

                    }
                }, start_hour, start_minute, false);
                timePickerDialog.show();

                checkTime();
            }


        });


        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentBooking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {

                        DATE = DAY+"/"+MONTH+"/"+YEAR;
                        appointment_date.setTextColor(Color.BLACK);
                        appointment_date.setText(DATE);
                        datePicker.setMinDate(calendar.getMinimum(DAY));

                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkEvent.isEmpty(textViews) || !(checkEvent.checkName(patient_full_name) || checkEvent.checkPhone(patient_phone_no))){
                    appointment_date.setError("String is rmpty");
                    appointment_time.setError("String is empty");
                    appointment_description.setError("String is empty");
                }
                else {

                    Dialog dialog = new Dialog(AppointmentBooking.this);
                    dialog.setContentView(R.layout.alert_box_layout);
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.edges));
                    dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                    Button confirm = dialog.findViewById(R.id.alert_confirm);
                    TextView cancel = dialog.findViewById(R.id.alert_cancel);
                    TextView alert_msg = dialog.findViewById(R.id.alert_msg);
                    alert_msg.setText("Confirm Booking?");

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

                            Map<String, Object> appointment = new HashMap<>();
                            appointment.put("PatientID", firestoreHandler.getCurrentUser());
                            appointment.put("PatientName", booker_name);
                            appointment.put("PatientPhoneNo", booker_phone);
                            appointment.put("AppointedDoctorID", doctor_id);
                            appointment.put("DoctorName", doctor_name);
                            appointment.put("DoctorPhoneNo", doctor_phone);
                            appointment.put("AppointmentDate", DATE);
                            appointment.put("AppointmentTime", TIME);
                            appointment.put("AppointmentDescription", booker_description);


                            DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Appointment").document(doctor_id);
                            documentReference.set (appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    helperFunctions.snackBarShow(snack_bar_layout, "Appointment Booked");
                                }
                            });
                            dialog.dismiss();


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


            }
        });


    }

    private void checkTime() {

// Convert user input string to LocalTime
        String userInput = appointment_time.getText().toString(); // Replace with user input
        Toast.makeText(this, TIME, Toast.LENGTH_SHORT).show();
// Validate user input
        if (TIME.isEmpty()) {
            // User did not provide any input
            // Perform necessary actions (e.g., display an error message)
            System.out.println("Please enter a valid time.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            try {
                LocalTime userSelectedTime = LocalTime.parse(TIME, formatter);

                // Define the restricted time range
                LocalTime minTime = LocalTime.parse("10:00 AM", formatter);
                LocalTime maxTime = LocalTime.parse("6:00 PM", formatter);

                // Compare the user-selected time with the restricted range
                if (userSelectedTime.isBefore(minTime) || userSelectedTime.isAfter(maxTime)) {
                    // User selected a time outside the allowed range
                    // Perform necessary actions (e.g., display an error message)
                    System.out.println("Selected time is not within the allowed range (10:00 AM - 6:00 PM).");
                    Toast.makeText(this, "Selected time is not within the allowed range (10:00 AM - 6:00 PM).", Toast.LENGTH_SHORT).show();
                } else {
                    // User selected a valid time
                    // Continue with further actions
                    Toast.makeText(this, "Selected time is valid.", Toast.LENGTH_SHORT).show();
                    appointment_time.setText(TIME);
                }
            } catch (DateTimeParseException e) {
                // User input could not be parsed
                // Perform necessary actions (e.g., display an error message)
                System.out.println("Please enter a valid time in the format 'hh:mm a'.");
                Toast.makeText(this, "Please enter a valid time in the format 'hh:mm a'.\"", Toast.LENGTH_SHORT).show();
            }
        }

    }
}






