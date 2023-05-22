package com.example.docportal.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ManageAppointmentAdapter extends FirestoreRecyclerAdapter<AppointmentHolder, ManageAppointmentAdapter.ViewHolder> {
    String doctorImage;
    FirestoreHandler firestoreHandler = new FirestoreHandler();

    public ManageAppointmentAdapter(@NonNull FirestoreRecyclerOptions<AppointmentHolder> options) {
        super(options);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_appointment_layout, parent, false);
        return new ViewHolder(view);
    }

    private void approvedAppointments(String patient_name, String patient_phone, String appointment_date, String appointment_time, String patientId, String doctorId, String image, String doctorImg, String doctorName, String doctorPhone, HashMap<String, Object> Symptom_Severity,HashMap<String, Object> Symptom_Details, String Disease_Name, String description ) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("Approved Appointments").document();

        Map<String, Object> approved_appointments = new HashMap<>();
        approved_appointments.put("ApprovedPatientName", patient_name);
        approved_appointments.put("ApprovedPatientCell", patient_phone);
        approved_appointments.put("ApprovedDoctorName", doctorName);
        approved_appointments.put("ApprovedDoctorCell", doctorPhone);
        approved_appointments.put("ApprovedAppointmentDate", appointment_date);
        approved_appointments.put("ApprovedAppointmentTime", appointment_time);
        approved_appointments.put("ApprovedPatientImage", image);
        approved_appointments.put("AppointedPatientId", patientId);
        approved_appointments.put("AppointedDoctorId",doctorId);
        approved_appointments.put("AppointedDoctorImage",doctorImg);
        approved_appointments.put("Symptom_Severity",Symptom_Severity);
        approved_appointments.put("Symptom_details",Symptom_Details);
        approved_appointments.put("Disease_name",Disease_Name);
        approved_appointments.put("Additional_Description",description);
        docRef.set(approved_appointments);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AppointmentHolder model) {

        Map<String, Object> symptomDetail = (Map<String, Object>) model.getSymptom_Details();
        Map<String, Object> symptomSeverity = (Map<String, Object>) model.getSymptom_severity();

        StringBuilder symptomDetailsBuilder = new StringBuilder();
        StringBuilder severityDetailsBuilder = new StringBuilder();
        StringBuilder symptomDetailsBuilder2 = new StringBuilder();


        if (symptomSeverity != null) {
            if (!symptomSeverity.isEmpty() && !symptomDetail.isEmpty()) {
                // Sort symptom details in alphabetical order
                TreeMap<String, Object> sortedSymptomDetail = new TreeMap<>(symptomDetail);

                for (Map.Entry<String, Object> entry : sortedSymptomDetail.entrySet()) {
                    String symptomName = entry.getKey();
                    String symptomDetails2 = entry.getValue().toString();
                    symptomDetailsBuilder2.append(symptomName).append("\n").append(symptomDetails2).append("\n").append("\n");
                    holder.symptomDetail.setText(symptomDetailsBuilder2.toString());

                }

                // Sort symptom severity in alphabetical order
                TreeMap<String, Object> sortedSymptomSeverity = new TreeMap<>(symptomSeverity);

                for (Map.Entry<String, Object> entry : sortedSymptomSeverity.entrySet()) {
                    String symptomName = entry.getKey();
                    String symptomDetails = entry.getValue().toString();
                    symptomDetailsBuilder.append(symptomName).append(": ").append("\n");
                    severityDetailsBuilder.append(symptomDetails).append("\n");
                    holder.symptomTextView.setText(symptomDetailsBuilder.toString());
                    holder.severityTextView.setText(severityDetailsBuilder.toString());

                }
            }

            if (!symptomDetail.isEmpty()) {
                // Display the sorted symptom details
                holder.symptomTextView.setText(symptomDetailsBuilder.toString());
            }
        } else {
            holder.symptomTextView.setText("No symptom details found");
        }

        // Set the concatenated symptom details in the TextView



        holder.disease_name.setText(model.getDisease());
        holder.getText_patient_name().setText(model.getPatientName());
        holder.getText_patient_phone().setText(model.getPatientPhoneNo());
        holder.getText_patient_date().setText(model.getAppointmentDate());
        holder.getText_patient_time().setText(model.getAppointmentTime());
        holder.getText_patient_description().setText(model.getAppointmentDescription());
        String imageUri =  model.getPatientImage();
        if (imageUri != null && !imageUri.isEmpty()) {
            Picasso.get().load(imageUri).into(holder.image);
        }

        holder.getApprove_appointment().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String patient_name = model.getPatientName();
                String patient_phone = model.getPatientPhoneNo();
                String appointment_date = model.getAppointmentDate();
                String appointment_time = model.getAppointmentTime();
                String appointmentId = model.getPatientID();
                String appointmentImage = model.getPatientImage();
                String doctorname = model.getDoctorName();
                String doctorphone = model.getDoctorPhoneNo();
                String diseasename = model.getDisease();
                String description = model.getAppointmentDescription();
                HashMap<String, Object> Symptom_Severity = model.getSymptom_severity();
                HashMap<String, Object> Symptom_Details = model.getSymptom_Details();

                DocumentReference documentReference1 = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());
                documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        doctorImage = value.getString("Image");
                        approvedAppointments(patient_name, patient_phone, appointment_date, appointment_time, appointmentId, firestoreHandler.getCurrentUser(), appointmentImage, doctorImage, doctorname, doctorphone,Symptom_Severity,Symptom_Details,diseasename,description);

                        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Appointment").document(firestoreHandler.getCurrentUser());
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                notifyItemRemoved(position);
                                Toast.makeText(v.getContext(), "Appointment Approved", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });




            }
        });

        holder.getDeny_appointment().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appointmentId = model.getAppointedDoctorID();

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                DocumentReference documentReference = firestore.collection("Appointment").document(appointmentId);
                System.out.println(appointmentId);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        notifyItemRemoved(position);
                        Toast.makeText(v.getContext(), "Appointment Denied", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    public interface ItemClickListenerCheck {
        String onItemClick(Appointment appointment);
        Context getContext();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_patient_name;
        private final TextView disease_name;
        private final TextView text_patient_phone;
        private final TextView text_patient_date;
        private final TextView text_patient_time;
        private final TextView text_patient_description;
        private final TextView symptomTextView;
        private final TextView severityTextView;
        private final TextView symptomDetail;
        private final Button approve_appointment;
        private final Button deny_appointment;
        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.appointment_patient_profile);
            symptomTextView = view.findViewById(R.id.symptomTextView);
            text_patient_name = view.findViewById(R.id.appointment_patient_name);
            text_patient_phone = view.findViewById(R.id.appointment_patient_phone);
            text_patient_date = view.findViewById(R.id.appointment_patient_date);
            text_patient_time = view.findViewById(R.id.appointment_patient_time);
            text_patient_description = view.findViewById(R.id.appointment_patient_description);
            approve_appointment = view.findViewById(R.id.approve_appointment);
            deny_appointment = view.findViewById(R.id.deny_appointment);
            symptomDetail = view.findViewById(R.id.symptom_extra_detail);
            severityTextView = view.findViewById(R.id.severityTextView);
            disease_name = view.findViewById(R.id.disease_name);

        }

        public TextView getDisease_name() {
            return disease_name;
        }

        public TextView getSeverityTextView() {
            return severityTextView;
        }

        public TextView getText_patient_name() {
            return text_patient_name;
        }

        public TextView getText_patient_phone() {
            return text_patient_phone;
        }

        public TextView getText_patient_date() {
            return text_patient_date;
        }

        public TextView getText_patient_time() {
            return text_patient_time;
        }

        public TextView getText_patient_description() {
            return text_patient_description;
        }

        public Button getApprove_appointment() {
            return approve_appointment;
        }

        public Button getDeny_appointment() {
            return deny_appointment;
        }
    }
}