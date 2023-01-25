package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.docportal.R;

public class urgentCare extends AppCompatActivity {
   LottieAnimationView _urgentCare;
   ImageView patientUrgent_backToDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_care);

        _urgentCare = findViewById(R.id.patientUrgentCare);
        _urgentCare.setRepeatCount(Animation.INFINITE);
    }
}