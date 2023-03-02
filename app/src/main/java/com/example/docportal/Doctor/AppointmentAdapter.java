package com.example.docportal.Doctor;

import static androidx.core.content.ContextCompat.getDrawable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.example.docportal.SplashScreenEntrance;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> implements Filterable {

    private final List<String> AppointmentNames;
    private final List<String> AppointmentPhones;
    private final List<String> AppointmentDate;
    private final List<String> AppointmentTime;
    private final List<String> AppointmentNamesAll;
    private final List<String> Appointment_IDs;
    ItemClickListenerCheck listenerCheck;
    String appointment_id;
    FirebaseFirestore FStore;
    Context context;



    public AppointmentAdapter(List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, List<String> nameDataSet3, List<String> Appointment_ID, ItemClickListenerCheck itemClickListenerCheck)  {
        AppointmentNames = nameDataSet;
        AppointmentPhones = nameDataSet1;
        AppointmentDate = nameDataSet2;
        AppointmentTime = nameDataSet3;
        Appointment_IDs = Appointment_ID;
        this.listenerCheck = itemClickListenerCheck;
        this.AppointmentNamesAll = new ArrayList<>(AppointmentNames);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(AppointmentNamesAll);
            }
            else{
                for(String  movie: AppointmentNamesAll){
                    if(movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
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


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            apoint_names = (TextView) view.findViewById(R.id.appointmentNames);
            apoint_ph = (TextView) view.findViewById(R.id.appointeePhone);
            appointment_date = (TextView) view.findViewById(R.id.appointment_date);
            appointment_time = (TextView) view.findViewById(R.id.appointment_time);
            to_appointment_reschedule = (Button) view.findViewById(R.id.deleteAppointment);




        }

        public TextView getApoint_names() {
            return apoint_names;
        }
        public  TextView getApoint_ph() {
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




    // Create new views (invoked by the layout manager)
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.manage_appointments_row, viewGroup, false);

        return new AppointmentAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getApoint_names().setText(AppointmentNames.get(position));
        viewHolder.getApoint_ph().setText(AppointmentPhones.get(position));
        viewHolder.getAppointment_date().setText(AppointmentDate.get(position));
        viewHolder.getAppointment_time().setText(AppointmentTime.get(position));

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


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return AppointmentNames.size();

    }

    private void deleteAppointment(String RID){
        FStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = FStore.collection("Approved Appointments").document(RID);
        documentReference.delete();
        Toast.makeText(context, "Appointment Deleted", Toast.LENGTH_SHORT).show();
    }

    public interface ItemClickListenerCheck{
        String onItemClick(String details);
    }



}
