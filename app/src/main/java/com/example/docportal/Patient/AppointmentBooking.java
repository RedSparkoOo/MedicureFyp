package com.example.docportal.Patient;

import static com.example.docportal.R.layout.spinner_item;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.AppointmentCheckEvent;
import com.example.docportal.Doctor.DoctorNurseDashboard;
import com.example.docportal.Doctor.DoctorNurseRegistration;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.HelperFunctions;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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

public class AppointmentBooking extends AppCompatActivity {
    EditText patient_full_name;
    EditText patient_phone_no;
    EditText appointment_date;
    Spinner appointment_time;
    EditText appointment_description;

    List<String> checkboxValues = new ArrayList<>();
    Map<String, String> symptomDetail =new HashMap<>();
    Button book_appointment;
    ArrayList<String> disease = new ArrayList<>();

    FirebaseFirestore FStore;
    RecyclerView checkbox;

    FirebaseAuth FAuth;

    String patient_UID;
    ImageView back_to_doc_nur_Selection;

    DatePickerDialog.OnDateSetListener setListener;

    int year;   // to store year
    int month; //to store date
    int day;   //to store date

    int hour; //to store hours
    int minute; //to store date
    private String organ = "General";
    String Time[] = {"10:00 am","11:00 am","12:00 pm","01:00 pm","02:00 pm","03:00 pm","04:00 pm","05:00 pm","06:00 pm"};
    String booker_name;
    String booker_phone; String text;
    String patient_img;
    String booker_description;
    String doctor_phone;
    String doctor_name;
    String doctor_id;
    String TimeZone;
    String TIME;
    DetailAdapter detailAdapter;
    Map<String, String> severity =new HashMap<>();
    RecyclerView checkDisease, symptomDetails;
    CheckBoxAdapter checkBoxAdapter;
    private String _disease;
    Spinner spinner;
    String DATE;
    Singleton singleton = new Singleton();
    FirebaseFirestore firebaseFirestore;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("SearchDisease");
    View snack_bar_layout;
    FirebaseAuth firebaseAuth;
    Object currentUserId;
    QuestionaireAdapter questionaireAdapter;
    HelperFunctions helperFunctions;
    CollectionReference appointment_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_booking);
        patient_full_name = (EditText) findViewById(R.id.patient_first_name);
        patient_phone_no = (EditText) findViewById(R.id.patient_phone);
        appointment_date = (EditText) findViewById(R.id.appointment_date);
        appointment_time = findViewById(R.id.appointment_time);
        spinner = findViewById(R.id.s_disease);

        ArrayAdapter arrayAdapterTime = new ArrayAdapter(this, spinner_item, Time);
        arrayAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appointment_time.setAdapter(arrayAdapterTime);

        appointment_description = (EditText) findViewById(R.id.appointment_description);
        book_appointment = (Button) findViewById(R.id.book);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        firebaseFirestore = FirebaseFirestore.getInstance();
        back_to_doc_nur_Selection = findViewById(R.id.back_to_doc_nur_Selection);
        doctor_phone = bundle.getString("Doctor_phone");
        doctor_name = bundle.getString("Doctor_name");
        doctor_id = bundle.getString("Doctor_Id");
        fetchData();
        checkDisease = findViewById(R.id.check_disease);
        checkDisease.setLayoutManager(new WrapContentLinearLayoutManager(AppointmentBooking.this, LinearLayoutManager.VERTICAL, false));
        symptomDetails = findViewById(R.id.check_details);
        symptomDetails.setLayoutManager(new WrapContentLinearLayoutManager(AppointmentBooking.this, LinearLayoutManager.VERTICAL, false));

        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
        TextView[] textViews = {patient_full_name, patient_phone_no, appointment_date,appointment_description};
        AppointmentCheckEvent checkEvent = new AppointmentCheckEvent();
        DocumentReference documentReference = firebaseFirestore.collection("Patient").document(currentUserId.toString());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                patient_full_name.setText(value.getString("Patient Name"));
                patient_phone_no.setText(value.getString("Patient phone_no"));
                patient_img = value.getString("Image");

            }
        });


        snack_bar_layout = findViewById(android.R.id.content);
        helperFunctions = new HelperFunctions();

        back_to_doc_nur_Selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentBooking.this, AppointmentBooking.class);
                startActivity(intent);
            }
        });


        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentBooking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {

                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);

                        String formated_date = sdf.format(date);
                        appointment_date.setText(formated_date);

                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _disease = parent.getItemAtPosition(position).toString();
                if(_disease != null) {
                    checkDisease.setAdapter(null);
                    symptomDetails.setAdapter(null );
                }
                severity.clear();
                symptomDetail.clear();
                    setUpRecycler();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkEvent.isEmpty(textViews) && !(checkEvent.checkName(patient_full_name) || checkEvent.checkPhone(patient_phone_no))){
                    appointment_description.setError("String is empty");
                }
                else {

                    checkAppointmentAvailbility();

                }


            }
        });


    }

    private void confirmAppointment() {

        Dialog dialog = new Dialog(AppointmentBooking.this);
        dialog.setContentView(R.layout.alert_box_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.edges));
        dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
        Button confirm = dialog.findViewById(R.id.alert_confirm);
        TextView cancel = dialog.findViewById(R.id.alert_cancel);
        TextView alert_msg = dialog.findViewById(R.id.alert_msg);
        alert_msg.setText("Confirm Booking?");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseFirestore.getInstance().clearPersistence();
                FStore = FirebaseFirestore.getInstance();
                FAuth = FirebaseAuth.getInstance();

                if(booker_name!= null && booker_phone!= null && booker_description!= null){
                    booker_name = null;
                    booker_phone = null;
                    booker_description = null;
                }

                booker_name = patient_full_name.getText().toString();
                booker_phone = patient_phone_no.getText().toString();
                booker_description = appointment_description.getText().toString();
                patient_UID = FAuth.getCurrentUser().getUid();
                TIME = appointment_time.getSelectedItem().toString();
                DATE = appointment_date.getText().toString();

                StringBuilder stringBuilder2 = new StringBuilder();
                for (int i = 0; i < checkboxValues.size(); i++) {
                    stringBuilder2.append(checkboxValues.get(i));
                    if (i < checkboxValues.size() - 1) {
                        stringBuilder2.append(", "); // Add a delimiter between values (e.g., comma and space)
                    }
                }

                String result2 = stringBuilder2.toString();

                Map<String, Object> appointment = new HashMap<>();
                appointment.put("PatientID", firestoreHandler.getCurrentUser());
                appointment.put("PatientImage", patient_img);
                appointment.put("PatientName", booker_name);
                appointment.put("PatientPhoneNo", booker_phone);
                appointment.put("AppointedDoctorID", doctor_id);
                appointment.put("DoctorName", doctor_name);
                appointment.put("DoctorPhoneNo", doctor_phone);
                appointment.put("AppointmentDate", DATE);
                appointment.put("AppointmentTime", TIME);
                appointment.put("Disease", _disease);
                appointment.put("Symptoms", result2);
                appointment.put("Symptom_severity", severity );
                appointment.put("Symptom_Details", symptomDetail);
                appointment.put("AppointmentDescription", booker_description);


                DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Appointment").document(doctor_id);
                documentReference.set (appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        helperFunctions.snackBarShow(snack_bar_layout, "Appointment Booked");
                        Intent intent = new Intent(AppointmentBooking.this, patientDashboard.class);
                        startActivity(intent);
                    }
                });
                dialog.dismiss();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();






    }

    private void checkAppointmentAvailbility() {
        FirebaseFirestore.getInstance().clearPersistence();
        FStore = FirebaseFirestore.getInstance();
        FAuth = FirebaseAuth.getInstance();


        booker_name = patient_full_name.getText().toString();
        booker_phone = patient_phone_no.getText().toString();
        booker_description = appointment_description.getText().toString();
        patient_UID = FAuth.getCurrentUser().getUid();
        TIME = appointment_time.getSelectedItem().toString();
        DATE = appointment_date.getText().toString();
        ArrayList<String> time = new ArrayList<>();
        ArrayList<String> doc_id = new ArrayList<>();

        appointment_check = firestoreHandler.getFirestoreInstance().collection("Approved Appointments");
        appointment_check.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value != null) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        time.add(document.getString("ApprovedAppointmentTime"));
                        doc_id.add(document.getString("AppointedDoctorId"));

                    }

                    if (time.contains(TIME)){

                        if(doc_id.contains(doctor_id)){
                            Toast.makeText(AppointmentBooking.this, "Person is not free at that time. please select another time", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            confirmAppointment();

                        }

                    }
                    else {
                        confirmAppointment();

                    }

                }
                else {
                    confirmAppointment();
                }

            }
        });


    }

    private void setRecycler3(){
    if( checkboxValues!= null && ! checkboxValues.isEmpty()) {
        Query combinedQuery = noteBookref.whereIn("symptom", checkboxValues).whereEqualTo("disease", _disease);

        FirestoreRecyclerOptions<CheckBoxModel> options = new FirestoreRecyclerOptions.Builder<CheckBoxModel>()
                .setQuery(combinedQuery, CheckBoxModel.class).build();
        detailAdapter = new DetailAdapter(options);

        symptomDetails.setAdapter(detailAdapter );
        detailAdapter.setDetailListener(new DetailAdapter.DetailListener() {
            @Override
            public void onDetailFetched(String header, String detail, int position) {
                symptomDetail.put(header, detail);
            }

        });
        detailAdapter .startListening();
    }
    else{
        symptomDetails.setAdapter(null);
    }
}
    private void setUpRecycler2() {


        if( checkboxValues!= null && ! checkboxValues.isEmpty()){
        Query combinedQuery = noteBookref.whereIn("symptom", checkboxValues).whereEqualTo("disease", _disease);

        FirestoreRecyclerOptions<CheckBoxModel> options = new FirestoreRecyclerOptions.Builder<CheckBoxModel>()
                .setQuery(combinedQuery, CheckBoxModel.class).build();
        questionaireAdapter = new QuestionaireAdapter(options);

            checkDisease.setAdapter(questionaireAdapter);
            questionaireAdapter.setOnCheckedChangeListener(new QuestionaireAdapter.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(int position, int checkedId, boolean checked) {
                    String severityLevel = getSeverityLevel(checkedId);
                    String symptomValue = questionaireAdapter.getSymptomValue(position);
                    if (checked)
                            severity.put(symptomValue, severityLevel);
                    else
                        severity.put(symptomValue, "Don't Know");


                }
            });


        questionaireAdapter.startListening();

        }
        else{
            checkDisease.setAdapter(null);
        }
    }
    private void setUpRecycler() {

        Query query = noteBookref.whereEqualTo("disease", _disease);
        FirestoreRecyclerOptions<CheckBoxModel> options = new FirestoreRecyclerOptions.Builder<CheckBoxModel>()
                .setQuery(query, CheckBoxModel.class).build();
        checkBoxAdapter = new CheckBoxAdapter(options);
        checkbox = findViewById(R.id.check_symptom);

        checkbox.setLayoutManager(new WrapContentLinearLayoutManager(AppointmentBooking.this, LinearLayoutManager.VERTICAL, false));
        checkbox.setAdapter(checkBoxAdapter);

        checkBoxAdapter.setOnCheckedChangeListener(new CheckBoxAdapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Boolean isChecked, int position) {
                if (checkboxValues == null) {
                    checkboxValues = new ArrayList<>();
                }
                Boolean price =((CheckBox) checkbox.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.checkBox)).isChecked();
               String mango = ((CheckBox) checkbox.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.checkBox)).getText().toString();
                if (isChecked) {
                    checkboxValues.add(mango);
                } else {
                    severity.clear();
                    symptomDetail.clear();
                    checkboxValues.remove(mango);
                }

                setUpRecycler2();
                setRecycler3();
            }


        });

        checkBoxAdapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private String getSeverityLevel(int checkedId) {
        switch (checkedId) {
            case R.id.low:
                return "low";
            case R.id.mild:
                return "mild";
            case R.id.severe:
                return "severe";
            case R.id.dont_know:
                return "don't know";
            default:
                return "";
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        checkBoxAdapter.stopListening();
    }

    private void fetchData() {
        firestoreHandler.getFirestoreInstance().collection("SearchDisease").orderBy("organ", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    Toast.makeText(AppointmentBooking.this, error.toString(), Toast.LENGTH_SHORT).show();
                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (organ.equals("General") && dc.getDocument().get("organ").equals("General") && !disease.contains(dc.getDocument().get("disease"))) {
                        disease.add(String.valueOf( dc.getDocument().get("disease")));
                        setAdapter(disease);
                    }

                }
                //   book_appointment_helper_class.notifyDataSetChanged();
            }
        });
    }
    private void setAdapter(List<String> list) {
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
    }
}






