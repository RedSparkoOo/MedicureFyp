package com.example.docportal.Patient;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.docportal.Broadcasts;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HealthTracker extends AppCompatActivity {


    TextView tv_steps;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener eventListener;
    private double MagnitudePrevious = 0;

    ProgressBar step_progress_bar;
    CountDownTimer countDownTimer;
    TextView steps_status;
    TextView water_glass;
    private Button med_remind_activity;
    private Button med_remind_check;

    FirebaseFirestore firestore;
    FirebaseAuth FAuth;
    String UID;

    long stepsCount;

    ImageView back_to_patient_dashboard;

    Button add_water;
    Button sub_water;
    Long waterIntake;
    long stepsProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tracker);


        tv_steps = findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        step_progress_bar = findViewById(R.id.step_progress_bar);
        steps_status = findViewById(R.id.steps_status);
        med_remind_activity = findViewById(R.id.med_remind_activity);
        med_remind_check = findViewById(R.id.med_remind_check);
        back_to_patient_dashboard = findViewById(R.id.back_to_patient_dashboard);
        add_water = findViewById(R.id.add_water);
        sub_water = findViewById(R.id.sub_water);
        water_glass = findViewById(R.id.water_glass);


        firestore = FirebaseFirestore.getInstance();
        FAuth = FirebaseAuth.getInstance();
        UID = FAuth.getCurrentUser().getUid();


        back_to_patient_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNotifications();
            }
        });




        med_remind_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthTracker.this, ReminderStored.class);
                startActivity(intent);
            }
        });

        med_remind_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthTracker.this, medicineReminder.class);
                startActivity(intent);
            }
        });


        steps_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsCount = 0;
                step_progress_bar.setProgress(0);
            }
        });
        FirestoreStepsStatus();
        waterIntake();
        stepsCounter();
        addNotifications();

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




    public void waterIntake() {

        add_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++waterIntake;
                water_glass.setText(String.valueOf(waterIntake));


            }
        });

        sub_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(waterIntake == 0){

                    sub_water.setBackgroundResource(R.drawable.but_disbaled);
                    sub_water.setEnabled(false);
                }

                else {
                    sub_water.setEnabled(true);
                    sub_water.setBackgroundResource(R.drawable.but);
                    --waterIntake;
                    water_glass.setText(String.valueOf(waterIntake));

                }


            }
        });
    }

    private void updateWater(long update) {
        DocumentReference documentReference = firestore.collection("Water Intake").document(UID);

        Map<String,Object> water = new HashMap<>();
        water.put("Water Glass", update);

        documentReference.set(water)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Update Successful");
                        } else {
                            System.out.println("Update Successful");
                        }
                    }
                });
    }



    private void WaterStatus() {
        DocumentReference documentReference = firestore.collection("Water Intake").document(UID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null) {
                    // Document does not exist
                    CreateWaterIntake(0);
                    return;
                }

                String ID = value.getId();
                if (!ID.equals(UID)) {
                    // Document belongs to a different user
                    CreateWaterIntake(0);
                    return;
                }

                Long water_intake = value.getLong("Water Glass");
                if (water_intake == null) {
                    // "Water Glass" field is missing or null
                    CreateWaterIntake(0);
                    return;
                }
                waterIntake = water_intake;
                water_glass.setText(String.valueOf(water_intake));

                if (water_intake == 0) {
                    sub_water.setBackgroundResource(R.drawable.but_disbaled);
                    sub_water.setEnabled(false);
                } else {
                    sub_water.setBackgroundResource(R.drawable.but);
                    sub_water.setEnabled(true);
                }
            }
        });
    }


    private void CreateWaterIntake(long create_water) {


        DocumentReference documentReference = firestore.collection("Water Intake").document(UID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<String,Object> water = new HashMap<>();
                water.put("Water Glass",create_water);
                documentReference.set(water);


            }
        });
    }

    private void stepsCounter() {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(step_progress_bar, "progress", 0, 20);
        progressAnimator.setDuration(100);




        eventListener = new SensorEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event !=null){
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);

                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;


                    if(MagnitudeDelta > 6){


                        stepsCount++;
                        tv_steps.setText(String.valueOf(stepsCount));
                        steps_status.setText(String.valueOf(stepsProgress));


                        if(stepsCount >=100 && stepsCount <200 ){
                            countDownTimer.start();
                            steps_status.setText("20");
                        }
                        if (stepsCount >= 200 && stepsCount <300) {
                            countDownTimer.start();
                            steps_status.setText("40");
                        }

                        if (stepsCount >= 300 && stepsCount <400) {

                            countDownTimer.start();
                            steps_status.setText("60");
                        }

                        if (stepsCount >= 400 && stepsCount <500) {

                            countDownTimer.start();
                            steps_status.setText("80");
                        }

                        if (stepsCount == 500) {

                            countDownTimer.start();
                            steps_status.setText("100");
                        }
                    }

                }
            }



            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(eventListener,sensor,SensorManager.SENSOR_DELAY_UI);





        countDownTimer = new CountDownTimer(11*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                stepsProgress = stepsProgress + 20;
                step_progress_bar.setProgress(Math.toIntExact(stepsProgress));
                step_progress_bar.setMax(100);
                countDownTimer.cancel();





            }

            @Override
            public void onFinish() {


            }
        };



    }

    private void FirestoreStepsStatus() {

        DocumentReference documentReference = firestore.collection("Steps Status").document(UID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null) {
                    // Document does not exist
                    FireStoreCreateSteps(0,0);
                    return;
                }

                String ID = value.getId();
                if (!ID.equals(UID)) {
                    // Document belongs to a different user
                    FireStoreCreateSteps(0,0);
                    return;
                }

                Long steps_count = value.getLong("Steps");
                Long progress_count = value.getLong("Progress");
                if (steps_count == null) {
                    // "Water Glass" field is missing or null
                    FireStoreCreateSteps(0,0);
                    return;
                }
                stepsCount = steps_count;
                stepsProgress = progress_count;
                tv_steps.setText(String.valueOf(steps_count));
                steps_status.setText(String.valueOf(stepsProgress));
                step_progress_bar.setProgress(Math.toIntExact(stepsProgress));

            }
        });

    }





    private void FireStoreCreateSteps(long step_count_stored,long step_count_progress) {

        DocumentReference documentReference = firestore.collection("Steps Status").document(UID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<String,Object> steps = new HashMap<>();
                steps.put("Steps",step_count_stored);
                steps.put("Progress",step_count_progress);
                documentReference.set(steps);


            }
        });

    }

    private void FireStoreUpdateSteps(long step, long progress) {

        DocumentReference documentReference = firestore.collection("Steps Status").document(UID);
        Map<String,Object> water = new HashMap<>();
        water.put("Steps", step);
        water.put("Progress", progress);

        documentReference.set(water)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Update Successful");
                        } else {
                            System.out.println("Update Successful");
                        }
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(eventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.putInt("stepsCount",stepCount);
//        editor.putInt("stepsProgress",step_progress_bar.getProgress());
//        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        stepCount = preferences.getInt("stepsCount",stepCount);
//        CurrentProgress = preferences.getInt("stepsProgress",0);
//        step_progress_bar.setProgress(CurrentProgress);
//        stepCount--;


    }

    @Override
    protected void onStart() {
        super.onStart();
        WaterStatus();
        FirestoreStepsStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateWater(waterIntake);
        FireStoreUpdateSteps(stepsCount,stepsProgress);
        sensorManager.unregisterListener(eventListener);
    }
}