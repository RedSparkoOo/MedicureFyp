package com.example.docportal.Pharmacist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

public class UpdatePharmacist extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    TextView Token;
    String token;
    EditText update_full_name;
    EditText update_email_address;
    EditText update_Password;
    EditText update_Phone_No;
    EditText update_License;
    EditText update_doctor_bio;
    Spinner update_Specializations;
    Spinner update_gender;
    ImageView doctor_profile;
    String user_id;
    String update_fName;
    String update_emailAddress;
    String update_Passcode;
    String update_phoneNo;
    String update_license;
    String update_specializations;
    String update_gend;
    String update_bio;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    Object currentUserId;
    String specialization_on_top;
    String specialization_we_got;
    String present_specialization;
    String Selected_gender;
    String[] Specializations = {"Cardiologist","Oncologist","Nephrologist","Neurologist","Pedriatican","physiologist"};
    String[] Genders = {"Male","Female"};
    StorageReference storageReference;
    FirebaseUser doctor_user;
    String old_email;
    String old_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pharmacist);
        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        Object currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = fAuth.getCurrentUser().getUid();
        }
        DocumentReference documentReference = firestore.collection("Patient").document(currentUserId.toString());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
             //   name.setText(value.getString("Patient Name"));
             //   email.setText(value.getString("Patient Email Address"));
              //  phone.setText(value.getString("Patient phone_no"));

            }


        });
    }

}