package com.example.docportal.Doctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.List;

public class checkAppointmentAdapter extends RecyclerView.Adapter<com.example.docportal.Doctor.checkAppointmentAdapter.ViewHolder>{

    private final List<String> list_patient_name;
    private final List<String> list_patient_phone;
    private final List<String> list_patient_date;
    private final List<String> list_patient_time;
    private final List<String> list_patient_desc;

//,List<String> phone_dataSet,List<String> date_dataSet,List<String> time_dataSet,List<String> description_dataSet

    public checkAppointmentAdapter(List<String> name_dataSet,List<String> phone_dataSet,List<String> date_dataSet,List<String> time_dataSet,List<String> description_dataSet) {
        list_patient_name = name_dataSet;
        list_patient_phone = phone_dataSet;
        list_patient_date = date_dataSet;
        list_patient_time = time_dataSet;
        list_patient_desc = description_dataSet;
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


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            text_patient_name = view.findViewById(R.id.appointment_patient_name);
            text_patient_phone = view.findViewById(R.id.appointment_patient_phone);
            text_patient_date = view.findViewById(R.id.appointment_patient_date);
            text_patient_time = view.findViewById(R.id.appointment_patient_time);
            text_patient_description = view.findViewById(R.id.appointment_patient_description);

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
    public void onBindViewHolder(com.example.docportal.Doctor.checkAppointmentAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getText_patient_name().setText(list_patient_name.get(position));
        viewHolder.getText_patient_phone().setText(list_patient_phone.get(position));
        viewHolder.getText_patient_date().setText(list_patient_date.get(position));
        viewHolder.getText_patient_time().setText(list_patient_time.get(position));
        viewHolder.getText_patient_description().setText(list_patient_desc.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return list_patient_name.size();
    }

}


