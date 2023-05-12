package com.example.docportal.Patient;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class healthTracker extends AppCompatActivity {


    TextView tv_steps;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener eventListener;
    ProgressBar step_progress_bar;
    CountDownTimer countDownTimer;
    TextView steps_status;
    FirebaseFirestore firestore;
    FirebaseAuth FAuth;
    String UID;
    long steps_count;
    double Steps;
    private double MagnitudePrevious = 0;
    private double stepCount = 0;
    private int CurrentProgress = 0;
    private Button med_remind_activity;
    private Button med_remind_check;

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


        med_remind_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(healthTracker.this, ReminderStored.class);
                startActivity(intent);
            }
        });

        med_remind_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(healthTracker.this, medicineReminder.class);
                startActivity(intent);
            }
        });


        steps_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepCount = 0;
                step_progress_bar.setProgress(0);
            }
        });
        FirestoreStepsStatus();


    }

    private void stepsCounter(double Steps_Count) {
        Steps = Steps_Count;
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(step_progress_bar, "progress", 0, 20);
        progressAnimator.setDuration(100);

        eventListener = new SensorEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null) {
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);

                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;


                    if (MagnitudeDelta > 7) {


                        FireStoreUpdateSteps();
                        Steps++;

                        if (Steps == 100) {
                            countDownTimer.start();
                            steps_status.setText("20");
                        } else if (Steps == 200) {
                            countDownTimer.start();
                            steps_status.setText("40");
                        } else if (Steps == 300) {

                            countDownTimer.start();
                            steps_status.setText("60");
                        } else if (Steps == 400) {

                            countDownTimer.start();
                            steps_status.setText("80");
                        } else if (Steps == 500) {

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

        sensorManager.registerListener(eventListener, sensor, SensorManager.SENSOR_DELAY_UI);


        countDownTimer = new CountDownTimer(11 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                CurrentProgress = CurrentProgress + 20;
                step_progress_bar.setProgress(CurrentProgress);
                step_progress_bar.setMax(100);

                countDownTimer.cancel();


            }

            @Override
            public void onFinish() {


            }
        };


    }

    private void FirestoreStepsStatus() {

        firestore = FirebaseFirestore.getInstance();
        FAuth = FirebaseAuth.getInstance();
        UID = FAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firestore.collection("Steps Status").document(UID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String ID = value.getId();

                if (ID.equals(UID)) {

                    if (value.contains("Steps") && value.exists()) {

                        steps_count = value.getLong("Steps");
                        stepCount = steps_count;
                        stepsCounter(stepCount);

                    } else {
                        FireStoreCreateSteps(stepCount);
                    }


                }


            }
        });


    }


    private void FireStoreCreateSteps(double step_count_stored) {

        DocumentReference documentReference = firestore.collection("Steps Status").document(UID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<String, Object> steps = new HashMap<>();
                steps.put("Steps", step_count_stored);
                documentReference.set(steps);


            }
        });

    }

    private void FireStoreUpdateSteps() {

        DocumentReference documentReference = firestore.collection("Steps Status").document(UID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<String, Object> steps = new HashMap<>();
                steps.put("Steps", Steps);
                documentReference.set(steps);
                tv_steps.setText(Double.toString(Steps));


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

        sensorManager.unregisterListener(eventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        stepCount = preferences.getInt("stepsCount",stepCount);
//        CurrentProgress = preferences.getInt("stepsProgress",0);
//        step_progress_bar.setProgress(CurrentProgress);
//        stepCount--;

        sensorManager.unregisterListener(eventListener);
    }
}