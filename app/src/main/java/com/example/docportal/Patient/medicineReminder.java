package com.example.docportal.Patient;

import static com.example.docportal.R.layout.spinner_item;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.docportal.CheckEvent;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class medicineReminder extends AppCompatActivity {
    ImageView back_to_health_tracker;
    Spinner medicine_type;
    Spinner medicine_weight;
    EditText medicine_name;
    EditText medicine_time;
    TextView Sunday;
    TextView Monday;
    TextView Tuesday;
    TextView Wednesday;
    TextView Thursday;
    TextView Friday;
    TextView Saturday;
    CheckBox every_day;
    Button medicine_to_recycler;
    String TimeZone;
    String TIME;
    int hour;
    int minute;
    String[] medicine_types = {"Syrup","Capsule", "Tablet", "Sachet", "Drops", "Cream"};
    String[] medicines_weight = {"No mg","10 mg","20 mg","100 mg","250 mg","375 mg","500 mg","1000 mg"};
    ArrayList<String> medicinesNames;
    ArrayList<String> medicinesDuration;
    ArrayList<String> medicinesTime;
    ArrayList<String> medicineType;
    String day1_Sunday;
    String day2_Monday;
    String day3_Tuesday;
    String day4_Wednesday;
    String day5_Thursday;
    String day6_Friday;
    String day7_Saturday;
    String med_types;
    String medicine_name_entered;
    String duration = "";
    String med_weight;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    ArrayList<String> specificDuration;
    boolean all_duration_check= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);


        back_to_health_tracker = findViewById(R.id.back_to_health_tracker);
        medicine_to_recycler = findViewById(R.id.medicine_to_recycler);
        medicine_name = findViewById(R.id.medicine_name);
        Sunday = findViewById(R.id.Sunday);
        Monday = findViewById(R.id.Monday);
        Tuesday = findViewById(R.id.Tuesday);
        Wednesday = findViewById(R.id.Wednesday);
        Thursday = findViewById(R.id.Thursday);
        Friday = findViewById(R.id.Friday);
        Saturday = findViewById(R.id.Saturday);
        every_day = findViewById(R.id.every_day);
        medicine_time = findViewById(R.id.medicine_time);
        medicine_type = findViewById(R.id.medicine_type);
        medicine_weight = findViewById(R.id.medicine_weight);

        medicinesNames = new ArrayList<>();
        medicineType = new ArrayList<>();
        medicinesTime = new ArrayList<>();
        medicinesDuration = new ArrayList<>();
        specificDuration = new ArrayList<>();

        ArrayAdapter arrayAdapterTypes = new ArrayAdapter(this, spinner_item, medicine_types);
        arrayAdapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine_type.setAdapter(arrayAdapterTypes);

        ArrayAdapter arrayAdapterWeight = new ArrayAdapter(this, spinner_item, medicines_weight);
        arrayAdapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine_weight.setAdapter(arrayAdapterWeight);


        medicine_to_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView[] textViews = {medicine_name, medicine_time};
                CheckEvent checkEvent = new CheckEvent();

                if(all_duration_check){
                    medicine_name_entered = medicine_name.getText().toString();
                    med_types = medicine_type.getSelectedItem().toString();
                    med_weight = medicine_weight.getSelectedItem().toString();
                    checkDuration();
                    if(checkEvent.isEmpty(textViews) || !checkEvent.checkMedicine(medicine_name) || duration.isEmpty()){

                        Toast.makeText(medicineReminder.this, "Please fill out all details", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RemindertoFirebase(medicine_name_entered,med_types,TIME,duration,med_weight);
                    }

                }
                else {
                    medicine_name_entered = medicine_name.getText().toString();
                    med_types = medicine_type.getSelectedItem().toString();
                    med_weight = medicine_weight.getSelectedItem().toString();
                    checkSpecificDuration();
                    if(checkEvent.isEmpty(textViews) || !checkEvent.checkMedicine(medicine_name) ||  duration.isEmpty()){
                        Toast.makeText(medicineReminder.this, "Please fill out all details", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RemindertoFirebase(medicine_name_entered,med_types,TIME,duration,med_weight);
                    }

                }



            }
        });

        medicine_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        every_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllDuration();

            }
        });

        showTimePicker();
        SpecificDuration();



        back_to_health_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(medicineReminder.this,patientDashboard.class);
                startActivity(intent);
            }
        });

    }


    private void RemindertoFirebase(String medic_name, String medic_type, String medic_time, String medic_duration, String medic_weight) {

        firestoreHandler.getFirestoreInstance().clearPersistence();
        Map<String, Object> reminder = new HashMap<>();

        CollectionReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medicine Reminder");
        documentReference.add(reminder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                reminder.put("Medicine Name", medic_name);
                reminder.put("Medicine Type", medic_type);
                reminder.put("Medicine Weight", medic_weight);
                reminder.put("Medicine Time", medic_time);
                reminder.put("Medicine Duration", medic_duration);
                reminder.put("Patient ID", firestoreHandler.getCurrentUser());
                documentReference.set(reminder);
                Intent intent = new Intent(medicineReminder.this, ReminderStored.class);
                startActivity(intent);
            }
        });

    }

    private void checkDuration() {

        if (day1_Sunday != null) {
            medicinesDuration.add(day1_Sunday);
        }

        if (day2_Monday != null) {
            medicinesDuration.add(day2_Monday);
        }

        if (day3_Tuesday != null) {
            medicinesDuration.add(day3_Tuesday);
        }

        if (day4_Wednesday != null) {
            medicinesDuration.add(day4_Wednesday);
        }

        if (day5_Thursday != null) {
            medicinesDuration.add(day5_Thursday);
        }

        if (day6_Friday != null) {
            medicinesDuration.add(day6_Friday);
        }

        if (day7_Saturday != null) {
            medicinesDuration.add(day7_Saturday);
        }

        for (String value : medicinesDuration) {
            duration += value + " ";

        }


    }
    private void checkSpecificDuration() {

        for (String value : specificDuration) {
            duration += value + " ";

        }


    }



    private void SpecificDuration() {


        Sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Sunday")) {
                    specificDuration.remove("Sunday");
                    Sunday.setBackground(null);
                    Sunday.setTextColor(Color.parseColor("#434242"));
                    day1_Sunday = "";

                } else {
                    specificDuration.add("Sunday");
                    day1_Sunday = "Sunday";
                    Sunday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Sunday.setTextColor(Color.parseColor("#FFFFFFFF"));

                }

            }
        });


        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Monday")) {
                    specificDuration.remove("Monday");
                    Monday.setBackground(null);
                    Monday.setTextColor(Color.parseColor("#434242"));
                    day2_Monday = "";

                } else {
                    specificDuration.add("Monday");
                    Toast.makeText(medicineReminder.this, "Added", Toast.LENGTH_SHORT).show();
                    Monday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Monday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day2_Monday = "Monday";

                }

            }
        });

        Tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Tuesday")) {
                    specificDuration.remove("Tuesday");
                    Tuesday.setBackground(null);
                    Tuesday.setTextColor(Color.parseColor("#434242"));
                    day3_Tuesday = "";

                } else {
                    specificDuration.add("Tuesday");
                    Tuesday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Tuesday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day3_Tuesday = "Tuesday";

                }

            }
        });

        Wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Wednesday")) {
                    specificDuration.remove("Wednesday");
                    Wednesday.setBackground(null);
                    Wednesday.setTextColor(Color.parseColor("#434242"));
                    day4_Wednesday = "";

                } else {
                    specificDuration.add("Wednesday");
                    Wednesday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Wednesday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day4_Wednesday = "Wednesday";

                }

            }
        });

        Thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Thursday")) {
                    specificDuration.remove("Thursday");
                    Thursday.setBackground(null);
                    Thursday.setTextColor(Color.parseColor("#434242"));
                    day5_Thursday = "";

                } else {
                    specificDuration.add("Thursday");
                    Thursday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Thursday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day5_Thursday = "Thursday";
                }

            }
        });

        Friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Friday")) {
                    specificDuration.remove("Friday");
                    Friday.setBackground(null);
                    Friday.setTextColor(Color.parseColor("#434242"));
                    day6_Friday = "";

                } else {
                    specificDuration.add("Friday");
                    Friday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Friday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day6_Friday = "Friday";
                }

            }
        });

        Saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificDuration.contains("Saturday")) {
                    specificDuration.remove("Saturday");
                    Saturday.setBackground(null);
                    Saturday.setTextColor(Color.parseColor("#434242"));
                    day7_Saturday = "";

                } else {
                    specificDuration.add("Saturday");
                    Saturday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Saturday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day7_Saturday = "Saturday";
                }


            }
        });



    }




    //--------------------------------ALL DAYS------------------------------------------//
    private void AllDuration() {
        if (every_day.isChecked()) {
            // Clear the list before adding days
            medicinesDuration.clear();
            // Add the selected days
            medicinesDuration.add("Sunday");
            medicinesDuration.add("Monday");
            medicinesDuration.add("Tuesday");
            medicinesDuration.add("Wednesday");
            medicinesDuration.add("Thursday");
            medicinesDuration.add("Friday");
            medicinesDuration.add("Saturday");
            // Update the UI
            updateDaySelectionUI();
            checkDuration();
            all_duration_check = true;

        } else {
            // Clear the list
            medicinesDuration.clear();
            // Update the UI
            updateDaySelectionUI();
            checkDuration();
            all_duration_check = false;
        }
    }



    private void updateDaySelectionUI() {
        // Update the UI based on the selected days
        Sunday.setBackground(medicinesDuration.contains("Sunday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Sunday.setTextColor(medicinesDuration.contains("Sunday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));

        Monday.setBackground(medicinesDuration.contains("Monday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Monday.setTextColor(medicinesDuration.contains("Monday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));

        Tuesday.setBackground(medicinesDuration.contains("Tuesday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Tuesday.setTextColor(medicinesDuration.contains("Tuesday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));

        Wednesday.setBackground(medicinesDuration.contains("Wednesday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Wednesday.setTextColor(medicinesDuration.contains("Wednesday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));

        Thursday.setBackground(medicinesDuration.contains("Thursday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Thursday.setTextColor(medicinesDuration.contains("Thursday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));

        Friday.setBackground(medicinesDuration.contains("Friday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Friday.setTextColor(medicinesDuration.contains("Friday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));

        Saturday.setBackground(medicinesDuration.contains("Saturday") ? ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight) : null);
        Saturday.setTextColor(medicinesDuration.contains("Saturday") ? Color.parseColor("#FFFFFFFF") : Color.parseColor("#434242"));
    }


    private void showTimePicker() {

        medicine_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
                int start_minute = calendar.get((Calendar.MINUTE));

                //  boolean Is24hourFormat = DateFormat.is24HourFormat(AppointmentBooking.this);
                TimePickerDialog timePickerDialog = new TimePickerDialog(medicineReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                        int hourOfDay = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        TIME = timeFormat.format(calendar.getTime());
                        medicine_time.setText(TIME);
                    }
                }, start_hour, start_minute, false);
                timePickerDialog.show();

            }

        });

    }




}
