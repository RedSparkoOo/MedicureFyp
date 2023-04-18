package com.example.docportal.Patient;

import android.app.Application;
import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51MxSIWIwnwbkCpSTcbeqEZPR6i6rbjMHc5RdodkjLFI7mzc7hoO2P1OZtcciSFx9EdCBx2dVZARCpc5MI9BTf1vD0042mbYWDW"
        );
    }
}