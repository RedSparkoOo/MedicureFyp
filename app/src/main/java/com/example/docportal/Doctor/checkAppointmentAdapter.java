package com.example.docportal.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkAppointmentAdapter extends RecyclerView.Adapter<com.example.docportal.Doctor.checkAppointmentAdapter.ViewHolder>{

    private final List<String> list_patient_name;
    private final List<String> list_patient_phone;
    private final List<String> list_patient_date;
    private final List<String> list_patient_time;
    private final List<String> list_patient_desc;
    private String doctor_id;
    private String doctor_id_stored;
    private final List<String> patient_id;

    FirebaseFirestore FStore;
    Context context;
    int position_changed;

    private ItemClickListenerCheck listenerCheck;
    
    public checkAppointmentAdapter(List<String> name_dataSet, List<String> phone_dataSet, List<String> date_dataSet, List<String> time_dataSet, List<String> description_dataSet, String doc_UID, List<String> patient_UID, ItemClickListenerCheck itemClickListenerCheck) {

        list_patient_name = name_dataSet;
        list_patient_phone = phone_dataSet;
        list_patient_date = date_dataSet;
        list_patient_time = time_dataSet;
        list_patient_desc = description_dataSet;
        doctor_id = doc_UID;
        patient_id = patient_UID;
        this.listenerCheck = itemClickListenerCheck;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
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
            // Define click listener for the ViewHolder's View

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

    @NonNull
    @Override
    public com.example.docportal.Doctor.checkAppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.check_appointment_layout, viewGroup, false);

        return new com.example.docportal.Doctor.checkAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.docportal.Doctor.checkAppointmentAdapter.ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        position_changed = position;
        viewHolder.getText_patient_name().setText(list_patient_name.get(position));
        viewHolder.getText_patient_phone().setText(list_patient_phone.get(position));
        viewHolder.getText_patient_date().setText(list_patient_date.get(position));
        viewHolder.getText_patient_time().setText(list_patient_time.get(position));
        viewHolder.getText_patient_description().setText(list_patient_desc.get(position));

        viewHolder.getApprove_appointment().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context = v.getContext();
                doctor_id_stored = listenerCheck.onItemClick(patient_id.get(position));
                String patient_name = listenerCheck.onItemClick(list_patient_name.get(position));
                String patient_phone = listenerCheck.onItemClick(list_patient_phone.get(position));
                String appointment_date = listenerCheck.onItemClick(list_patient_date.get(position));
                String appointment_time = listenerCheck.onItemClick(list_patient_time.get(position));
                doctor_id_stored = patient_id.get(position);
                approvedAppointments(doctor_id_stored,patient_name,patient_phone,appointment_date,appointment_time);

            }
        });


        viewHolder.getDeny_appointment().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context = v.getContext();
                listenerCheck.onItemClick(patient_id.get(position));
                doctor_id_stored = patient_id.get(position);
                DeleteData(doctor_id_stored);
                Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show();



            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return list_patient_name.size();
    }


    public void approvedAppointments(String ID,String patient_name,String patient_phone,String appointment_date,String appointment_time){

        FStore = FirebaseFirestore.getInstance();

        DocumentReference Doc_Ref = FStore.collection("Approved Appointments").document(ID);

        Doc_Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String, Object> approved_appointments = new HashMap<>();


                approved_appointments.put("Approved Patient Name",patient_name);
                approved_appointments.put("Approved Patient Cell",patient_phone);
                approved_appointments.put("Approved Appointment Date",appointment_date);
                approved_appointments.put("Approved Appointment Time",appointment_time);
                Doc_Ref.set(approved_appointments);
                DeleteData(ID);

            }
        });

    }


    private void DeleteData(String RID){
        FStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = FStore.collection("Appointment").document(RID);
        documentReference.delete();



    }
    public interface ItemClickListenerCheck{
        String onItemClick(String details);
    }



}


