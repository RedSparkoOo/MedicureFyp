package com.example.docportal.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManageAppointmentAdapter extends FirestoreRecyclerAdapter<AppointmentHolder, ManageAppointmentAdapter.ViewHolder> {



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

    private void approvedAppointments(String patient_name, String patient_phone, String appointment_date, String appointment_time, String patientId, String doctorId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("Approved Appointments").document();

        Map<String, Object> approved_appointments = new HashMap<>();
        approved_appointments.put("ApprovedPatientName", patient_name);
        approved_appointments.put("ApprovedPatientCell", patient_phone);
        approved_appointments.put("ApprovedDoctorName", "Doctor Name");
        approved_appointments.put("ApprovedDoctorCell", "Doctor Phone");
        approved_appointments.put("ApprovedAppointmentDate", appointment_date);
        approved_appointments.put("ApprovedAppointmentTime", appointment_time);

        approved_appointments.put("AppointedPatientId", patientId);
        approved_appointments.put("AppointedDoctorId",doctorId);
        docRef.set(approved_appointments);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AppointmentHolder model) {

        FirestoreHandler firestoreHandler = new FirestoreHandler();
        holder.getText_patient_name().setText(model.getPatientName());
        holder.getText_patient_phone().setText(model.getPatientPhoneNo());
        holder.getText_patient_date().setText(model.getAppointmentDate());
        holder.getText_patient_time().setText(model.getAppointmentTime());
        holder.getText_patient_description().setText(model.getAppointmentDescription());

        holder.getApprove_appointment().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String patient_name = model.getPatientName();
                String patient_phone = model.getPatientPhoneNo();
                String appointment_date = model.getAppointmentDate();
                String appointment_time = model.getAppointmentTime();
                String appointmentId = model.getAppointedDoctorID();

                approvedAppointments(patient_name, patient_phone, appointment_date, appointment_time, appointmentId, firestoreHandler.getCurrentUser());
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                DocumentReference documentReference = firestore.collection("Appointment").document(appointmentId);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        notifyItemRemoved(position);
                        Toast.makeText(v.getContext(), "Appointment Approved", Toast.LENGTH_SHORT).show();
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
        private final TextView text_patient_phone;
        private final TextView text_patient_date;
        private final TextView text_patient_time;
        private final TextView text_patient_description;
        private final Button approve_appointment;
        private final Button deny_appointment;

        public ViewHolder(View view) {
            super(view);

            text_patient_name = view.findViewById(R.id.appointment_patient_name);
            text_patient_phone = view.findViewById(R.id.appointment_patient_phone);
            text_patient_date = view.findViewById(R.id.appointment_patient_date);
            text_patient_time = view.findViewById(R.id.appointment_patient_time);
            text_patient_description = view.findViewById(R.id.appointment_patient_description);
            approve_appointment = view.findViewById(R.id.approve_appointment);
            deny_appointment = view.findViewById(R.id.deny_appointment);
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