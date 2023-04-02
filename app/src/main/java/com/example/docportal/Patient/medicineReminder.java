package com.example.docportal.Patient;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Broadcasts;
import com.example.docportal.R;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

public class medicineReminder extends AppCompatActivity {
    private MaterialTimePicker picker;
    private Button set_time;
    private Button set_alarm;
    private Button cancel_alarm;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView selected_time;
    String TimeZone;
    String TIME;
    int hour;
    int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_reminder);

        set_time = findViewById(R.id.set_time);
        set_alarm = findViewById(R.id.set_alarm);
        cancel_alarm = findViewById(R.id.cancel_alarm);
        selected_time = findViewById(R.id.selected_time);
        createNotificationChannel();

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        cancel_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelAlarm();
            }
        });

    }

    private void cancelAlarm() {

        Intent intent = new Intent(this, Broadcasts.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null){

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();

    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, Broadcasts.class);

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this, "Alarm time set", Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {

       calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get((Calendar.MINUTE));
        //  boolean Is24hourFormat = DateFormat.is24HourFormat(patientAppointmentBook.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(medicineReminder.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                if(calendar.get(Calendar.AM_PM) == Calendar.AM){
                    TimeZone = "AM";
                }
                else if(calendar.get(Calendar.AM_PM) == Calendar.PM){
                    TimeZone = "PM";
                }
                TIME = String.format(Hour+":"+Minute+" "+TimeZone );
                selected_time.setTextColor(Color.BLACK);
                selected_time.setText(TIME);
            }
        },hour,minute,false);
        timePickerDialog.show();

//        picker = new MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .setHour(12)
//                .setMinute(0)
//                .setTitleText("Select Reminder Time")
//                .build();
//
//        picker.show(getSupportFragmentManager(),"Medicure");
//
//        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(picker.getHour() > 12){
//                    selected_time.setText(String.format("%02d",(picker.getHour()-12)+" : "+String.format("%02d",picker.getMinute())+" PM"));
//                }
//
//                else {
//                    selected_time.setText(picker.getHour()+" : "+picker.getMinute()+" AM");
//                }
//
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
//                calendar.set(Calendar.MINUTE,picker.getMinute());
//                calendar.set(Calendar.SECOND,0);
//                calendar.set(Calendar.MILLISECOND,0);
//            }
//        });

    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MediCure Medicine Reminder";
            String description = "Channel for Medicine Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Medicure",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }
}