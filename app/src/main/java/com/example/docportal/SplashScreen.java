package com.example.docportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;


public class SplashScreen extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    ProgressBar progressBarSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
         progressBarSplash = findViewById(R.id.progressBarSplash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), mainstartScreen.class);
                startActivity(intent);

            }
        },1000);
    }
}