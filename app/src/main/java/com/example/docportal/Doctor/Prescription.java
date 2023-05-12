package com.example.docportal.Doctor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Prescription extends AppCompatActivity {

    Spinner first_medicine_name;
    Spinner first_medicine_weight;
    EditText prescribed_patient_name;
    EditText prescribed_patient_email;
    EditText prescribing_doctor_name;
    EditText prescribing_doctor_email;
    EditText medicine_purpose;

    EditText medicine_usage;
    Button select_total;


    String patient_name;
    String patient_email;
    String doctor_name;
    String doctor_email;
    String medic_purpose;
    String selected_medicine_name;
    String selected_medicine_weight;
    String selected_medicine_usage;
    String[] medicine_names = {"", "Panadol", "Paracetamol", "Bruffin", "Amoxil", "Telynol"};
    String[] medicines_weight = {"", "10mg", "20mg", "30mg", "40mg"};


    Date calendar;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        first_medicine_name = findViewById(R.id.first_medicine_name);
        first_medicine_weight = findViewById(R.id.first_medicine_weight);
        medicine_usage = findViewById(R.id.first_medicine_usage);
        prescribed_patient_name = findViewById(R.id.prescribed_patient_name);
        prescribed_patient_email = findViewById(R.id.prescribed_patient_email_address);
        prescribing_doctor_name = findViewById(R.id.prescribing_doctor_name);
        prescribing_doctor_email = findViewById(R.id.prescribing_doctor_email_address);
        medicine_purpose = findViewById(R.id.medicine_purpose);
        select_total = findViewById(R.id.select_total);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            prescribed_patient_email.setText(bundle.getString("Email"));
            prescribed_patient_name.setText(bundle.getString("Name"));

        }

        select_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView[] textViews = {prescribed_patient_name, prescribed_patient_email, prescribing_doctor_name, prescribing_doctor_email, medicine_usage, medicine_purpose};
                CheckEvent checkEvent = new CheckEvent();

                if (checkEvent.isEmpty(textViews) || !(checkEvent.checkName(prescribed_patient_name) || checkEvent.checkEmail(prescribed_patient_email) || checkEvent.checkName(prescribing_doctor_name) || checkEvent.checkPassword(prescribing_doctor_email) || first_medicine_name.getSelectedItem().equals("")) || first_medicine_weight.getSelectedItem().equals("")) {

                    TextView errorText = (TextView) first_medicine_name.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select at least one");//changes the selected item text to this

                    TextView errorText1 = (TextView) first_medicine_weight.getSelectedView();
                    errorText1.setError("");
                    errorText1.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText1.setText("Select at least one");//changes the selected item text to this
                } else {
                    checkPatient();
                }


            }
        });

        medicineAssignment(medicine_names, medicines_weight);

    }

    private void checkPatient() {
        FirestoreHandler firestoreHandler = new FirestoreHandler();
        Singleton singleton = new Singleton();

        calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(calendar);
        patient_name = prescribed_patient_name.getText().toString();
        patient_email = prescribed_patient_email.getText().toString();
        doctor_name = prescribing_doctor_name.getText().toString();
        doctor_email = prescribing_doctor_email.getText().toString();
        selected_medicine_name = first_medicine_name.getSelectedItem().toString();
        selected_medicine_weight = first_medicine_weight.getSelectedItem().toString();
        medic_purpose = medicine_purpose.getText().toString();
        selected_medicine_usage = medicine_usage.getText().toString();


        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Patient").document();

        firestoreHandler.getFirestoreInstance().collection("Patient").orderBy("Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    singleton.showToast(Prescription.this, error.toString());

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc != null) {

                        if (dc.getType() == DocumentChange.Type.ADDED) {


                            String patient_id = dc.getDocument().getId();
                            String selected_patient_email = String.valueOf(dc.getDocument().get("Patient Email Address"));

                            if (selected_patient_email.equals(patient_email)) {
                                flag = true;
                                DocumentReference DC = firestoreHandler.getFirestoreInstance().collection("Prescription Sent").document();
                                DC.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        Map<String, Object> prescription = new HashMap<>();
                                        prescription.put("Patient Name", patient_name);
                                        prescription.put("Patient Email", patient_email);
                                        prescription.put("Doctor Name", doctor_name);
                                        prescription.put("Doctor Email", doctor_email);
                                        prescription.put("Medicine Prescribed", selected_medicine_name);
                                        prescription.put("Medicine Weight", selected_medicine_weight);
                                        prescription.put("Medicine Purpose", medic_purpose);
                                        prescription.put("Medicine Usage", selected_medicine_usage);
                                        prescription.put("Prescription Date", formattedDate);
                                        prescription.put("Doctor Id", firestoreHandler.getCurrentUser());
                                        prescription.put("Patient Id", patient_id);
                                        DC.set(prescription);
                                        singleton.showToast(Prescription.this, "Prescription sent");
                                        first_medicine_name.setSelection(0);
                                        first_medicine_weight.setSelection(0);
                                        medicine_purpose.setText(null);
                                        medicine_usage.setText(null);

                                    }
                                });

                            }

                        }

                    }
                }
                if (flag.equals(false))
                    singleton.showToast(Prescription.this, "No User found. Please check patient's email!");
            }
        });


    }

    public void medicineAssignment(String[] medicineName, String[] medicineWeight) {
        Singleton singleton = new Singleton();
        singleton.setAdatper(this, first_medicine_name, medicineName);
        singleton.setAdatper(this, first_medicine_name, medicineWeight);

    }
}