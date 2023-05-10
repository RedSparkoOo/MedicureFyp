package com.example.docportal.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {
    EditText phoneNumberByUser;
    TextView phoneNoOtp;
    Button Verify_OTP;
    FirebaseAuth mAuth;
    String verificationCodeBySystem;

    FirebaseDatabase RootNode;
    DatabaseReference reference;

    String _UserFirstName;
    String _UserLastName;
    String _UserEmailAddress;
    String _UserHomeAddress;
    String _UserPasscode;
    String _UserPhoneNumber;
    String _UserCNIC;
    String _UserGender;
    String _UserDOB;
    String _UserLicense;
    String _UserSpecializations;
    // Getting Data by calling a constructor

    DoctorHelperClass doctorHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);



        phoneNumberByUser = findViewById(R.id.patientOTP); //
        Verify_OTP = findViewById(R.id.patientVerify);     //
        mAuth = FirebaseAuth.getInstance();         // Authentication by Firebase
        phoneNoOtp = findViewById(R.id.OTP_Phone);  // To Display Phone Number


        // #2 Get the phone number entered by user
        Intent intent = getIntent();
        _UserFirstName = intent.getStringExtra("userFirstName");
         _UserLastName = intent.getStringExtra("userLastName");
        _UserEmailAddress = intent.getStringExtra("userEmailAddress");
         _UserHomeAddress = intent.getStringExtra("userHomeAddress");
         _UserPasscode = intent.getStringExtra("userPasscode");
         _UserPhoneNumber = intent.getStringExtra("userPhoneNo");
        _UserCNIC = intent.getStringExtra("userCnic");
         _UserGender = intent.getStringExtra("userGender");
         _UserDOB = intent.getStringExtra("userDOB");
        _UserLicense = intent.getStringExtra("userLicense");
         _UserSpecializations = intent.getStringExtra("userSpecialization");

        phoneNoOtp.setText(_UserPhoneNumber);
        // #3 Send verification code to user

            SendVerificationCodetoUser(_UserPhoneNumber);
        Verify_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(phoneNumberByUser.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(VerifyPhoneNo.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(phoneNumberByUser.getText().toString());
                }
            }
        });
    }


  //#3 Add Code of Authentication

        private void SendVerificationCodetoUser(String phoneNumber) {

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+92"+phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }


    // #4 Add Firebase Auth Object and mCallbacks Object with two functions.

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        // #6 Go to Generate select override methods click the second OnCodeSent Method() -> for different devices
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s; // Sent all the time code will be stored in global varibale
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //# 6 store the code entered by the user got through SMS and call it in a verifyCode Function.
            String Code = phoneAuthCredential.getSmsCode();
            if(Code!=null){
              //  phoneNumberByUser.setText(Code);
                verifyCode(Code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // Add Toast to check for Errors;
            Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String CodeByUser){
    // #7 Code by the system and Code by the user
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,CodeByUser);
    // #8 Check credentials entered by the user
    signIntheUserbyCredentials(credential);
    }





private void signIntheUserbyCredentials(PhoneAuthCredential credential) {

       mAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
           @Override
          public void onComplete(@NonNull Task<AuthResult> task) {


                 if(task.isSuccessful()){

                     Toast.makeText(VerifyPhoneNo.this, "Got it!", Toast.LENGTH_SHORT).show();
                     RootNode = FirebaseDatabase.getInstance("https://doc-portal-9515b-default-rtdb.firebaseio.com/");
                     reference = RootNode.getReference("Doctor");
//                     reference.child(_UserPhoneNumber).setValue(_UserFirstName);
//                     reference.child(_UserPhoneNumber).setValue(_UserLastName);
//                     reference.child(_UserPhoneNumber).setValue(_UserEmailAddress);
//                     reference.child(_UserPhoneNumber).setValue(_UserHomeAddress);
//                     reference.child(_UserPhoneNumber).setValue(_UserPasscode);
//                     reference.child(_UserPhoneNumber).setValue(_UserPhoneNumber);
//                     reference.child(_UserPhoneNumber).setValue(_UserCNIC);
//                     reference.child(_UserPhoneNumber).setValue(_UserGender);
//                     reference.child(_UserPhoneNumber).setValue(_UserDOB);
//                     reference.child(_UserPhoneNumber).setValue(_UserLicense);
//                     reference.child(_UserPhoneNumber).setValue(_UserSpecializations);
                     DoctorHelperClass helperClass = new DoctorHelperClass(_UserFirstName, _UserLastName, _UserEmailAddress, _UserHomeAddress, _UserPasscode,_UserPhoneNumber,_UserCNIC, _UserGender,_UserDOB, _UserLicense, _UserSpecializations);
                     reference.child(_UserPhoneNumber).setValue(helperClass);
                     Intent intent = new Intent(VerifyPhoneNo.this, DoctorLogin.class);
                     startActivity(intent);

                  }
                   else{Toast.makeText(VerifyPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                 }
           }
      });
    }


}