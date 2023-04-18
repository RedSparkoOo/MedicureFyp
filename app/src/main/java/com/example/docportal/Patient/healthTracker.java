package com.example.docportal.Patient;

import android.animation.ObjectAnimator;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;

public class healthTracker extends AppCompatActivity{


    TextView tv_steps;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener eventListener;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;

    private int CurrentProgress = 0;
    ProgressBar step_progress_bar;
    CountDownTimer countDownTimer;
    TextView steps_status;
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
        med_remind_activity= findViewById(R.id.med_remind_activity);
        med_remind_check = findViewById(R.id.med_remind_check);

        med_remind_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(healthTracker.this,ReminderStored.class);
                startActivity(intent);
            }
        });

        med_remind_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(healthTracker.this,medicineReminder.class);
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

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(step_progress_bar, "progress", 0, 20);
        progressAnimator.setDuration(100);

        eventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event !=null){
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);

                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if(MagnitudeDelta > 0){

                        tv_steps.setText(stepCount.toString());
                        stepCount++;

                        if(stepCount == 100){
                            countDownTimer.start();
                            steps_status.setText("20");
                        }
                        else if (stepCount == 200) {
                            countDownTimer.start();
                            steps_status.setText("40");
                        }

                        else if (stepCount == 300) {

                            countDownTimer.start();
                            steps_status.setText("60");
                        }

                        else if (stepCount == 400) {

                            countDownTimer.start();
                            steps_status.setText("80");
                        }

                        else if (stepCount == 500) {

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

    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.putInt("stepsCount",stepCount);
//        editor.putInt("stepsProgress",step_progress_bar.getProgress());
//        editor.apply();
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
}