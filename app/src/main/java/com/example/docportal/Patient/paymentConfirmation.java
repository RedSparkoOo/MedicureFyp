package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.docportal.R;

public class paymentConfirmation extends AppCompatActivity {
    Button _paymentToDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        _paymentToDashboard = findViewById(R.id.paymentToDashboard);
        _paymentToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(paymentConfirmation.this,patientDashboard.class);
                startActivity(intent);

            }
        });
    }
}