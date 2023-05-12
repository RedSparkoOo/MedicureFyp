package com.example.docportal.Doctor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ViewAppointmentAdapter extends RecyclerView.Adapter<ViewAppointmentAdapter.ViewHolder> implements Filterable {

    private final List<String> AppointmentNames;
    private final List<String> ApprovedPatientID;
    private final List<String> AppointmentPhones;
    private final List<String> AppointmentDate;
    private final List<String> AppointmentTime;
    private final String Patient_ids;
    private final List<String> AppointmentNamesAll;
    private final List<String> Appointment_IDs;

    ItemClickListenerCheck listenerCheck;
    String appointment_id;

    Context context;
    //  private final List<String> AppointmentPhone;

    // private final List<String> AppointmentDate;
    // private final List<String> AppointmentTime;
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(AppointmentNamesAll);
            } else {
                for (String movie : AppointmentNamesAll) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
            }


            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            AppointmentNames.clear();
            AppointmentNames.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public ViewAppointmentAdapter(String patient_id, List<String> app_patient_id, List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, List<String> nameDataSet3, List<String> Appointment_ID, ItemClickListenerCheck itemClickListenerCheck) {
        AppointmentNames = nameDataSet;
        AppointmentPhones = nameDataSet1;
        AppointmentDate = nameDataSet2;
        AppointmentTime = nameDataSet3;
        Appointment_IDs = Appointment_ID;
        ApprovedPatientID = app_patient_id;
        Patient_ids = patient_id;
        this.listenerCheck = itemClickListenerCheck;
        this.AppointmentNamesAll = new ArrayList<>(AppointmentNames);


//, List<String> phoneDataSet, List<String> dateDataSet, List<String> timeDataSet

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewAppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_appointments_recycler, viewGroup, false);

        return new ViewAppointmentAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewAppointmentAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getApoint_names().setText(AppointmentNames.get(position));
        viewHolder.getApoint_ph().setText(AppointmentPhones.get(position));
        viewHolder.getAppointment_date().setText(AppointmentDate.get(position));
        viewHolder.getAppointment_time().setText(AppointmentTime.get(position));
        viewHolder.to_chat_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = view.getContext();

                Bundle bundle = new Bundle();

                bundle.putString("names", AppointmentNames.get(position));
                Singleton singleton = new Singleton();
                context.startActivity(singleton.getIntent(context, Chat.class).putExtra("mBundle", bundle).putExtra("ID", Patient_ids));


            }
        });
        viewHolder.getto_appointment_reschedule().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.alert_box_layout);
                dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.edges));
                dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
                Button confirm = dialog.findViewById(R.id.alert_confirm);
                TextView cancel = dialog.findViewById(R.id.alert_cancel);
                TextView alert_msg = dialog.findViewById(R.id.alert_msg);
                alert_msg.setText("Confirm Deletion");

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        context = v.getContext();
                        appointment_id = listenerCheck.onItemClick(Appointment_IDs.get(position));
                        appointment_id = Appointment_IDs.get(position);
                        deleteAppointment(appointment_id);


                        AppointmentNames.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, AppointmentNames.size());


                        AppointmentPhones.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, AppointmentNames.size());


                        AppointmentDate.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, AppointmentNames.size());


                        AppointmentTime.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, AppointmentNames.size());

                        deleteAppointment(appointment_id);
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
        });

        viewHolder.getSendPrescription().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                String ID = ApprovedPatientID.get(position);

                FirebaseFirestore FStore = FirebaseFirestore.getInstance();
                FirebaseAuth FAuth = FirebaseAuth.getInstance();

                DocumentReference documentReference = FStore.collection("Patient").document(ID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        assert value != null;
                        String patient_email_address = value.getString("Patient Email Address");
                        String patient_name = value.getString("Patient Name");

                        Bundle bundle = new Bundle();
                        bundle.putString("Email", patient_email_address);
                        bundle.putString("Name", patient_name);


                        Intent intent = new Intent(context, Prescription.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    }
                });


            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return AppointmentNames.size();

    }

    private void deleteAppointment(String RID) {
        FirestoreHandler firestoreHandler = new FirestoreHandler();

        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Approved Appointments").document(RID);
        documentReference.delete();
        Toast.makeText(context, "Appointment Deleted", Toast.LENGTH_SHORT).show();
    }

    public interface ItemClickListenerCheck {
        String onItemClick(String details);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView apoint_names;
        private final TextView apoint_ph;

        private final TextView appointment_date;
        private final TextView appointment_time;
        private final Button to_appointment_reschedule;
        private final Button to_chat_reschedule;
        private final Button sendPrescription;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            apoint_names = view.findViewById(R.id.appointmentNames);
            apoint_ph = view.findViewById(R.id.appointeePhone);
            appointment_date = view.findViewById(R.id.appointment_date);
            appointment_time = view.findViewById(R.id.appointment_time);
            to_appointment_reschedule = view.findViewById(R.id.deleteAppointment);
            to_chat_reschedule = view.findViewById(R.id.chatAppointment);
            sendPrescription = view.findViewById(R.id.sendPrescription);


        }

        public Button getSendPrescription() {
            return sendPrescription;
        }

        public TextView getApoint_names() {
            return apoint_names;
        }

        public TextView getApoint_ph() {
            return apoint_ph;
        }

        public Button getto_appointment_reschedule() {
            return to_appointment_reschedule;
        }

        public TextView getAppointment_date() {
            return appointment_date;
        }

        public TextView getAppointment_time() {
            return appointment_time;
        }

    }


}
