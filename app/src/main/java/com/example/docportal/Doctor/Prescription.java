package com.example.docportal.Doctor;

import static com.example.docportal.R.layout.spinner_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.docportal.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Prescription extends AppCompatActivity {

    Spinner first_medicine_name;
    Spinner first_medicine_weight;

    EditText prescribed_patient_name;
    EditText prescribed_patient_email;
    EditText prescribing_doctor_name;
    EditText prescribing_doctor_email;
    RecyclerView prescription_recycler;
    EditText medicine_usage;
    Button select_total;
    Button send_total;

    String selected_medicine_name;
    String selected_medicine_weight;
    String selected_medicine_usage;

    ArrayList<String> selected_medicines_list;
    List<String> selected_medicines_weight_list;
    List<String> selected_medicines_usage_list;

    String[] medicine_names = {"","Panadol","Paracetamol","Bruffin","Amoxil","Telynol"};
    String[] medicines_weight = {"","10mg","20mg","30mg","40mg"};
    PrescriptionAdapter prescriptionAdapter;
    boolean flag;
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
        prescription_recycler = findViewById(R.id.prescription_recycler);
        select_total = findViewById(R.id.select_total);
        send_total = findViewById(R.id.send_total);
        send_total.setVisibility(View.INVISIBLE);

        selected_medicines_list = new ArrayList<>();
        selected_medicines_weight_list = new ArrayList<>();
        selected_medicines_usage_list = new ArrayList<>();

        select_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_medicine_name = first_medicine_name.getSelectedItem().toString();
                selected_medicine_weight = first_medicine_weight.getSelectedItem().toString();
                selected_medicine_usage = medicine_usage.getText().toString();
                medicineSelection(selected_medicine_name,selected_medicine_weight,selected_medicine_usage);
                send_total.setVisibility(View.VISIBLE);

            }
        });

        medicineAssignment(medicine_names,medicines_weight);

    }

    public void medicineAssignment(String[] medicineName,String[] medicineWeight){


        ArrayAdapter medicine_assignment = new ArrayAdapter(this, spinner_item, medicineName);
        medicine_assignment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_medicine_name.setAdapter(medicine_assignment);

        ArrayAdapter medicine_weight_assignment = new ArrayAdapter(this, spinner_item, medicineWeight);
        medicine_weight_assignment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first_medicine_weight.setAdapter(medicine_weight_assignment);


    }

    public void medicineSelection(String medicine_name_dataset, String medicine_weight_dataset,String medicine_usage_dataset){

        selected_medicines_list.add(medicine_name_dataset);
        selected_medicines_weight_list.add(medicine_weight_dataset);
        selected_medicines_usage_list.add(medicine_usage_dataset);

        prescription_recycler.setLayoutManager(new LinearLayoutManager(this));
        prescriptionAdapter = new PrescriptionAdapter(selected_medicines_list,selected_medicines_weight_list,selected_medicines_usage_list,send_total);
        prescription_recycler.setAdapter(prescriptionAdapter);

    }
}