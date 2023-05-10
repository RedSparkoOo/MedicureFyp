package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;
import static com.example.docportal.R.layout.spinner_item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.CheckEvent;
import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    RecyclerView prescription_recycler;
    EditText medicine_usage;
    Button select_total;
    Button send_total;
    TextView selected_medics;


    String patient_name;
    String patient_email;
    String doctor_name;
    String doctor_email;
    String medic_purpose;
    String selected_medicine_name;
    String selected_medicine_weight;
    String selected_medicine_usage;
    String[] medicine_names = {"","Panadol","Paracetamol","Bruffin","Amoxil","Telynol"};
    String[] medicines_weight = {"","10mg","20mg","30mg","40mg"};

    List<String> patient_email_list;
    List<String> patient_id_list;

    FirebaseFirestore FStore;
    FirebaseAuth FAuth;
    String user_id;
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

                TextView[] textViews = {prescribed_patient_name, prescribed_patient_email, prescribing_doctor_name,prescribing_doctor_email,medicine_usage,medicine_purpose};
                CheckEvent checkEvent = new CheckEvent();

                if (checkEvent.isEmpty(textViews) || !(checkEvent.checkName(prescribed_patient_name) || checkEvent.checkEmail(prescribed_patient_email) || checkEvent.checkName(prescribing_doctor_name) || checkEvent.checkPassword(prescribing_doctor_email) || first_medicine_name.getSelectedItem().equals("")) || first_medicine_weight.getSelectedItem().equals("")){

                    TextView errorText = (TextView)first_medicine_name.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select at least one");//changes the selected item text to this

                    TextView errorText1 = (TextView)first_medicine_weight.getSelectedView();
                    errorText1.setError("");
                    errorText1.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText1.setText("Select at least one");//changes the selected item text to this
                }
                else {
                    checkPatient();
                }




            }
        });

        medicineAssignment(medicine_names,medicines_weight);

    }

    private void checkPatient() {

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

        FStore = FirebaseFirestore.getInstance();
        FAuth = FirebaseAuth.getInstance();
        user_id = FAuth.getCurrentUser().getUid();

        DocumentReference documentReference = FStore.collection("Patient").document();

        FStore.collection("Patient").orderBy("Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(Prescription.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if(dc != null) {

                        if (dc.getType() == DocumentChange.Type.ADDED) {


                            String patient_id = dc.getDocument().getId();
                            String selected_patient_email = String.valueOf(dc.getDocument().get("Patient Email Address"));

                            if (selected_patient_email.equals(patient_email)) {
                                flag = true;
                                DocumentReference DC = FStore.collection("Prescription Sent").document();
                                DC.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        Map<String, Object> prescription = new HashMap<>();
                                        prescription.put("Patient Name",patient_name);
                                        prescription.put("Patient Email",patient_email);
                                        prescription.put("Doctor Name",doctor_name);
                                        prescription.put("Doctor Email",doctor_email);
                                        prescription.put("Medicine Prescribed",selected_medicine_name);
                                        prescription.put("Medicine Weight",selected_medicine_weight);
                                        prescription.put("Medicine Purpose",medic_purpose);
                                        prescription.put("Medicine Usage",selected_medicine_usage);
                                        prescription.put("Prescription Date",formattedDate);
                                        prescription.put("Doctor Id",user_id);
                                        prescription.put("Patient Id",patient_id);
                                        DC.set(prescription);

                                        Toast.makeText(Prescription.this, "Prescription sent", Toast.LENGTH_SHORT).show();
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
                if(flag.equals(false)){
                    Toast.makeText(Prescription.this, "No User found. Please check patient's email!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void medicineAssignment(String[] medicineName,String[] medicineWeight){


        ArrayAdapter medicine_assignment = new ArrayAdapter(this, spinner_item, medicineName);
        medicine_assignment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_medicine_name.setAdapter(medicine_assignment);

        ArrayAdapter medicine_weight_assignment = new ArrayAdapter(this, spinner_item, medicineWeight);
        medicine_weight_assignment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_medicine_weight.setAdapter(medicine_weight_assignment);


    }
}