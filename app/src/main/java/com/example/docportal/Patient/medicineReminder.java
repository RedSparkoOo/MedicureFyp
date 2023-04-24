package com.example.docportal.Patient;

import static com.example.docportal.R.layout.spinner_item;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Broadcasts;
import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class medicineReminder extends AppCompatActivity {
    ImageView back_to_health_tracker;
    Spinner medicine_type;
    RecyclerView medicine_added;
    EditText medicine_name;
    EditText medicine_time;
    TextView medicine_added_heading;
    TextView Sunday;
    TextView Monday;
    TextView Tuesday;
    TextView Wednesday;
    TextView Thursday;
    TextView Friday;
    TextView Saturday;
    CheckBox every_day;
    Button medicine_confirmation;
    Button medicine_to_recycler;

    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    String TimeZone;
    String TIME;
    int hour;
    int minute;
    String[] medicine_types = {"","Capsule","Adhesive","Syrup","Tablet"};
    ArrayList<String> medicinesNames;
    ArrayList<String> medicinesDuration;
    ArrayList<String> medicinesTime;
    ArrayList<String> medicineType;
    int Sunday_count = 0;
    int Monday_count = 0;
    int Tuesday_count = 0;
    int Wednesday_count = 0;
    int Thursday_count = 0;
    int Friday_count = 0;
    int Saturday_count = 0;

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
    String ID;
    FirebaseAuth auth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);


        back_to_health_tracker = findViewById(R.id.back_to_health_tracker);
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
        medicine_added = findViewById(R.id.medicine_added);
        medicine_added_heading = findViewById(R.id.medicine_added_heading);
        medicine_confirmation = findViewById(R.id.medicine_confirmation);
        medicine_to_recycler = findViewById(R.id.medicine_to_recycler);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ID = auth.getCurrentUser().getUid();

        medicinesNames = new ArrayList<>();
        medicineType = new ArrayList<>();
        medicinesTime = new ArrayList<>();
        medicinesDuration = new ArrayList<>();


        ArrayAdapter arrayAdapterTypes = new ArrayAdapter(this, spinner_item, medicine_types);
        arrayAdapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine_type.setAdapter(arrayAdapterTypes);

        medicine_added_heading.setVisibility(View.INVISIBLE);
        medicine_added.setVisibility(View.INVISIBLE);
        medicine_confirmation.setVisibility(View.INVISIBLE);

        medicine_to_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicine_name_entered = medicine_name.getText().toString();
                med_types = medicine_type.getSelectedItem().toString();
                checkDuration();

                RemindertoFirebase(medicine_name_entered,med_types,TIME,duration);





            }
        });





        SendMedicines();
        showTimePicker();




        back_to_health_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void RemindertoFirebase(String medic_name, String medic_type, String medic_time, String medic_duration) {
        firestore.clearPersistence();
        DocumentReference reference = firestore.collection("Medicine Reminder").document();
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<Object,String> reminder = new HashMap<>();
                reminder.put("Medicine Name",medic_name);
                reminder.put("Medicine Type",medic_type);
                reminder.put("Medicine Time",medic_time);
                reminder.put("Medicine Duration", medic_duration);
                reminder.put("Patient ID",ID);
                reference.set(reminder);
                Intent intent = new Intent(medicineReminder.this,ReminderStored.class);
                startActivity(intent);
            }
        });

    }

    private void checkDuration() {

        if(day1_Sunday != null){
            medicinesDuration.add(day1_Sunday);
        }

        if(day2_Monday != null){
            medicinesDuration.add(day2_Monday);
        }

        if(day3_Tuesday != null){
            medicinesDuration.add(day3_Tuesday);
        }

        if(day4_Wednesday != null){
            medicinesDuration.add(day4_Wednesday);
        }

        if(day5_Thursday != null){
            medicinesDuration.add(day5_Thursday);
        }

        if(day6_Friday != null){
            medicinesDuration.add(day6_Friday);
        }

        if(day7_Saturday != null){
            medicinesDuration.add(day7_Saturday);
        }

        for (String value : medicinesDuration) {
            duration += value + " " ;

        }



    }

    private void SendMedicines() {

        //--------------------------------ALL DAYS------------------------------------------//

        showTimePicker();
        every_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (every_day.isChecked()) {

                    Sunday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Sunday.setTextColor(Color.parseColor("#FFFFFFFF"));


                    Monday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Monday.setTextColor(Color.parseColor("#FFFFFFFF"));


                    Tuesday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Tuesday.setTextColor(Color.parseColor("#FFFFFFFF"));


                    Wednesday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Wednesday.setTextColor(Color.parseColor("#FFFFFFFF"));


                    Thursday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Thursday.setTextColor(Color.parseColor("#FFFFFFFF"));


                    Friday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Friday.setTextColor(Color.parseColor("#FFFFFFFF"));


                    Saturday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Saturday.setTextColor(Color.parseColor("#FFFFFFFF"));

                    day1_Sunday = "Sunday";
                    day2_Monday = "Monday";
                    day3_Tuesday = "Tuesday";
                    day4_Wednesday = "Wednesday";
                    day5_Thursday = "Thursday";
                    day6_Friday = "Friday";
                    day7_Saturday = "Saturday";

                    medicinesDuration.add(day1_Sunday);
                    medicinesDuration.add(day2_Monday);
                    medicinesDuration.add(day4_Wednesday);
                    medicinesDuration.add(day3_Tuesday);
                    medicinesDuration.add(day5_Thursday);
                    medicinesDuration.add(day6_Friday);
                    medicinesDuration.add(day7_Saturday);


                }

                if (!every_day.isChecked()) {

                    Sunday.setBackground(null);
                    Sunday.setTextColor(Color.parseColor("#434242"));


                    Monday.setBackground(null);
                    Monday.setTextColor(Color.parseColor("#434242"));


                    Tuesday.setBackground(null);
                    Tuesday.setTextColor(Color.parseColor("#434242"));

                    Wednesday.setBackground(null);
                    Wednesday.setTextColor(Color.parseColor("#434242"));

                    Thursday.setBackground(null);
                    Thursday.setTextColor(Color.parseColor("#434242"));

                    Friday.setBackground(null);
                    Friday.setTextColor(Color.parseColor("#434242"));

                    Saturday.setBackground(null);
                    Saturday.setTextColor(Color.parseColor("#434242"));

                    day1_Sunday = null;
                    day2_Monday = null;
                    day3_Tuesday = null;
                    day4_Wednesday = null;
                    day5_Thursday = null;
                    day6_Friday = null;
                    day7_Saturday = null;

                    medicinesDuration.clear();
                }

            }
        });

        Sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Sunday_count == 0) {
                    Sunday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Sunday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day1_Sunday = "Sunday";
                    Sunday_count = 1;

                } else if (Sunday_count == 1) {
                    Sunday.setBackground(null);
                    Sunday.setTextColor(Color.parseColor("#434242"));
                    day1_Sunday = "";
                    Sunday_count = 0;

                }

            }
        });

        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Monday_count == 0) {
                    Monday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Monday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day2_Monday = "Monday";
                    Monday_count = 1;

                } else if (Monday_count == 1) {
                    Monday.setBackground(null);
                    Monday.setTextColor(Color.parseColor("#434242"));
                    day2_Monday = "";
                    Monday_count = 0;
                }

            }
        });

        Tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Tuesday_count == 0) {
                    Tuesday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Tuesday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day3_Tuesday = "Tuesday";
                    Tuesday_count = 1;

                } else if (Tuesday_count == 1) {
                    Tuesday.setBackground(null);
                    Tuesday.setTextColor(Color.parseColor("#434242"));
                    day3_Tuesday = "";
                    Tuesday_count = 0;

                }

            }
        });


        Wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Wednesday_count == 0) {
                    Wednesday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Wednesday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day4_Wednesday = "Wednesday";
                    Wednesday_count = 1;

                } else if (Wednesday_count == 1) {
                    Wednesday.setBackground(null);
                    Wednesday.setTextColor(Color.parseColor("#434242"));
                    day4_Wednesday = "";
                    Wednesday_count = 0;

                }

            }
        });

        Thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Thursday_count == 0) {
                    Thursday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Thursday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day5_Thursday = "Thursday";
                    Thursday_count = 1;

                } else if (Thursday_count == 1) {
                    Thursday.setBackground(null);
                    Thursday.setTextColor(Color.parseColor("#434242"));
                    day5_Thursday = "";
                    Thursday_count = 0;

                }

            }
        });


        Friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Friday_count == 0) {
                    Friday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Friday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day6_Friday = "Friday";
                    Friday_count = 1;

                } else if (Friday_count == 1) {
                    Friday.setBackground(null);
                    Friday.setTextColor(Color.parseColor("#434242"));
                    day6_Friday = "";
                    Friday_count = 0;

                }

            }
        });

        Saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Saturday_count == 0) {
                    Saturday.setBackground(ContextCompat.getDrawable(medicineReminder.this, R.drawable.circle_highlight));
                    Saturday.setTextColor(Color.parseColor("#FFFFFFFF"));
                    day7_Saturday = "Saturday";
                    Saturday_count = 1;

                } else if (Saturday_count == 1) {
                    Saturday.setBackground(null);
                    Saturday.setTextColor(Color.parseColor("#434242"));
                    day7_Saturday = "";
                    Saturday_count = 0;

                }

            }
        });

    }
    private void cancelAlarm () {

        Intent intent = new Intent(this, Broadcasts.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager == null) {

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();

    }

    private void setAlarm () {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, Broadcasts.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm time set", Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker () {

        medicine_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get((Calendar.MINUTE));
                //  boolean Is24hourFormat = DateFormat.is24HourFormat(patientAppointmentBook.this);
                TimePickerDialog timePickerDialog = new TimePickerDialog(medicineReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                            TimeZone = "AM";
                        } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                            TimeZone = "PM";
                        }
                        TIME = String.format(Hour + ":" + Minute + " " + TimeZone);
                        medicine_time.setText(TIME);

                    }
                }, hour, minute, true);
                timePickerDialog.show();

            }

        });

    }


    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MediCure Medicine Reminder";
            String description = "Channel for Medicine Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Medicure", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }
}
