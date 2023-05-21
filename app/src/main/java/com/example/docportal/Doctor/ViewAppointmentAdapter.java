package com.example.docportal.Doctor;

import android.content.Context;
import android.os.Bundle;
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
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class ViewAppointmentAdapter extends FirestoreRecyclerAdapter<Appointment, ViewAppointmentAdapter.ViewHolder> {

    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();
    private final ItemClickListenerCheck listenerCheck;
    private Context context;


    public ViewAppointmentAdapter(@NonNull FirestoreRecyclerOptions<Appointment> options, ItemClickListenerCheck itemClickListenerCheck) {
        super(options);
        this.listenerCheck = itemClickListenerCheck;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Appointment model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_appointments_recycler, parent, false);
        return new ViewHolder(view);
    }

    public interface ItemClickListenerCheck {
        String onItemClick(String details);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView apoint_names;
        private final TextView apoint_ph;
        private final TextView appointment_date;
        private final TextView appointment_time;
        private final Button to_appointment_reschedule;
        private final Button to_chat_reschedule;
        private final Button sendPrescription;
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            apoint_names = itemView.findViewById(R.id.appointmentNames);
            apoint_ph = itemView.findViewById(R.id.appointeePhone);
            image = itemView.findViewById(R.id.appointment_patient_profile);
            appointment_date = itemView.findViewById(R.id.appointment_date);
            appointment_time = itemView.findViewById(R.id.appointment_time);
            to_appointment_reschedule = itemView.findViewById(R.id.deleteAppointment);
            to_chat_reschedule = itemView.findViewById(R.id.chatAppointment);
            sendPrescription = itemView.findViewById(R.id.sendPrescription);
        }

        public void bind(Appointment appointment) {
            apoint_names.setText(appointment.getApprovedPatientName());
            apoint_ph.setText(appointment.getApprovedPatientCell());
            appointment_date.setText(appointment.getApprovedAppointmentDate());
            appointment_time.setText(appointment.getApprovedAppointmentTime());
            String imageUri = appointment.getApprovedPatientImage();
            if (imageUri != null && !imageUri.isEmpty()) {
                Picasso.get().load(imageUri).into(image);
            }
            sendPrescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference documentReference1 = firestoreHandler.getFirestoreInstance().collection("Patient").document(appointment.getAppointedPatientId());
                    documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            String name = value.getString("Patient Name");
                            String email = value.getString("Patient Email Address");
                            Bundle bundle = new Bundle();
                            bundle.putString("name", name);
                            bundle.putString("email", email);
                            v.getContext().startActivity(singleton.getIntent(v.getContext(), Prescription.class).putExtras(bundle));
                        }
                    });
                }
            });

            to_appointment_reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context = v.getContext();
                    Appointment appointment = getItem(getBindingAdapterPosition());

                    if (appointment != null) {
                        String documentId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
                        System.out.println(documentId);
                        DocumentReference documentReference = firestoreHandler.getFirestoreInstance()
                                .collection("Approved Appointments")
                                .document(documentId);

                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Notify the adapter about the item removal
                                notifyItemRemoved(getBindingAdapterPosition());
                                notifyItemRangeChanged(getBindingAdapterPosition(), getItemCount());
                            }
                        });
                    }
                }
            });
            to_chat_reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context = v.getContext();

                    Bundle bundle = new Bundle();
                    bundle.putString("names", appointment.getApprovedPatientName());

                    context.startActivity(singleton.getIntent(context, Chat.class).putExtra("mBundle", bundle).putExtra("ID", appointment.getAppointedDoctorId()));

                }
            });

        }


    }
}
