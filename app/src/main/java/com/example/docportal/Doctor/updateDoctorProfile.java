package com.example.docportal.Doctor;

import static com.example.docportal.R.layout.spinner_item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class updateDoctorProfile extends AppCompatActivity {

    Button Update;
    Button upload_profile;

    EditText update_full_name;
    EditText update_email_address;
    EditText update_Password;
    EditText update_Phone_No;
    EditText update_License;
    Spinner update_Specializations;
    TextView login;

    String user_id;


    String update_fName;
    String update_emailAddress;
    String update_Passcode;
    String update_phoneNo;
    String update_license;
    String update_specializations;



    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    String specialization_on_top;
    String specialization_we_got;
    int Array_size = 0;
    String present_specialization;
    String[] Specializations = {"Cardiologist","Oncologist","Nephrologist","Neurologist","Pedriatican","physiologist"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_profile);


        Update = findViewById(R.id.Update);
        update_full_name = findViewById(R.id.update_full_name);
        update_email_address = findViewById(R.id.update_email_address);
        update_Password = findViewById(R.id.update_password);
        update_Phone_No = findViewById(R.id.update_phone_no);
        update_License = findViewById(R.id.update_license);
        update_Specializations = findViewById(R.id.update_specialist_Category);


//        ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(this, spinner_item,Specializations);
//        arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        update_Specializations.setAdapter(arrayAdapterSpecialization);

        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        Map<String,Object> doctor = new HashMap<>();
        DocumentReference documentReference = firestore.collection("Doctor").document(user_id);




        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                update_full_name.setText(documentSnapshot.getString("Full Name"));
                update_email_address.setText(documentSnapshot.getString("Email Address"));
                update_Password.setText(documentSnapshot.getString("Password"));
                update_Phone_No.setText(documentSnapshot.getString("Phone #"));
                update_License.setText(documentSnapshot.getString("License #"));
                present_specialization = documentSnapshot.getString("Specialization");

                try {

                        for (int i=0; i<Specializations.length; i++) {
                            if (Specializations[i].equals(present_specialization)) {

                                update_Specializations.setSelection(i);
                                specialization_on_top = Specializations[0];
                                specialization_we_got = Specializations[i];
                                Specializations[0] = specialization_we_got;
                                Specializations[i] = specialization_on_top;

                                ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(updateDoctorProfile.this, spinner_item,Specializations);
                                arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                update_Specializations.setAdapter(arrayAdapterSpecialization);

                            }
                        }

                }
                catch (Exception e){
                    Toast.makeText(updateDoctorProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


            }

        });



        Update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                try {
                    if (update_full_name.getText().toString().isEmpty() && update_email_address.getText().toString().isEmpty() && update_Password.getText().toString().isEmpty() && update_Phone_No.getText().toString().isEmpty() && update_License.getText().toString().isEmpty() && update_Specializations.getSelectedItem().toString().isEmpty()) {

                        update_full_name.setError("Full Name can't be empty");
                        update_email_address.setError("Email Address can't be empty");
                        update_Password.setError("Password can't be empty");
                        update_Phone_No.setError("Phone no can't be empty");
                        update_License.setError("license No can't be empty");
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                } catch (Exception e) {
                    Toast.makeText(updateDoctorProfile.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

//---------------------------------------FULL NAME LOGIC----------------------------------------------------------------------------
                if (update_full_name.getText().toString().isEmpty()) {

                    update_full_name.setError("First Name can't be empty");


                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");

                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");

                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }


                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }


//-------------------------------------------------------Email Address logic--------------------------------------------------
                else if (update_email_address.getText().toString().isEmpty()) {

                    update_email_address.setError("Email Address can't be empty");


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }


                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");

                    }


                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");

                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }


                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }

//-----------------------------------------------------------Password Logic-------------------------------------------------
                else if (update_Password.getText().toString().isEmpty()) {
                    update_Password.setError("Password can't be empty");


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");

                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }

                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }
//-------------------------------------------------Phone No logic--------------------------------------
                else if (update_Phone_No.getText().toString().isEmpty()) {

                    update_Phone_No.setError("Phone no can't be empty");
                    ;


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }

                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }


//---------------------------------------------LICENSE Logic------------------------------------------------------
                else if (update_License.getText().toString().isEmpty()) {


                    update_License.setError("license No can't be empty");


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");
                    }

                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }
//-------------------------------------------Specializations---------------------------------------------
                else if (update_Specializations.getSelectedItem().toString().isEmpty()) {

                    ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");

                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }


                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");
                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }

                } else {


                    update_fName = update_full_name.getText().toString();
                    update_emailAddress = update_email_address.getText().toString();
                    update_Passcode = update_Password.getText().toString();
                    update_phoneNo = update_Phone_No.getText().toString();
                    update_license = update_License.getText().toString();
                    update_specializations = update_Specializations.getSelectedItem().toString();


                    doctor.put("Full Name",update_fName);
                    doctor.put("Email Address",update_emailAddress);
                    doctor.put("Password",update_Passcode);
                    doctor.put("Phone #",update_phoneNo);
                    doctor.put("License #",update_license);
                    doctor.put("Specialization",update_specializations);

                    documentReference.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(updateDoctorProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


    }
}




