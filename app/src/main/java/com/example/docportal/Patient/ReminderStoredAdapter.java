package com.example.docportal.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class ReminderStoredAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.ReminderStoredAdapter.ViewHolder> {

    private final List<String> list_medicine_name;
    private final List<String> list_medicine_type;
    private final List<String> list_medicine_duration;
    private final List<String> list_medicine_weight;
    private final List<String> list_medicine_time;
    private final List<String> list_reminder_id;
    Context context;



    public ReminderStoredAdapter(List<String> medic_names, List<String> medic_type, List<String> medic_duration, List<String> medic_time, List<String> remind_id, List<String> medic_weight) {

        list_medicine_name = medic_names;
        list_medicine_type = medic_type;
        list_medicine_duration = medic_duration;
        list_medicine_time = medic_time;
        list_reminder_id = remind_id;
        list_medicine_weight = medic_weight;


    }

    @NonNull
    @Override
    public com.example.docportal.Patient.ReminderStoredAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reminder_layout, viewGroup, false);

        return new com.example.docportal.Patient.ReminderStoredAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.example.docportal.Patient.ReminderStoredAdapter.ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getText_medicine_name().setText(list_medicine_name.get(position));
        viewHolder.getText_medicine_type().setText(list_medicine_type.get(position));
        viewHolder.getText_medicine_time().setText(list_medicine_time.get(position));
        viewHolder.getText_medicine_duration().setText(list_medicine_duration.get(position));
        viewHolder.getMedicine_weight().setText(list_medicine_weight.get(position));



        viewHolder.getRemove_reminder().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context = v.getContext();
                DeleteData(list_reminder_id.get(position));
                list_medicine_name.remove(position);
                list_medicine_type.remove(position);
                list_medicine_time.remove(position);
                list_medicine_duration.remove(position);
                list_medicine_weight.remove(position);
                list_reminder_id.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());



            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return list_medicine_type.size();
    }

    private void DeleteData(String RID) {
        FirestoreHandler firestoreHandler = new FirestoreHandler();

        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medicine Reminder").document(RID);
        documentReference.delete();


    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_medicine_name;
        private final TextView text_medicine_type;
        private final TextView text_medicine_duration;
        private final TextView text_medicine_time;
        private final ImageView remove_reminder;
        private final TextView medicine_weight;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            text_medicine_name = view.findViewById(R.id.medicine_name);
            text_medicine_type = view.findViewById(R.id.medicine_type);
            text_medicine_duration = view.findViewById(R.id.medicine_duration);
            text_medicine_time = view.findViewById(R.id.medicine_time);
            remove_reminder = view.findViewById(R.id.remove_reminder);
            medicine_weight = view.findViewById(R.id.medicine_weight);

        }

        public TextView getMedicine_weight() {
            return medicine_weight;
        }

        public TextView getText_medicine_name() {
            return text_medicine_name;
        }

        public TextView getText_medicine_type() {
            return text_medicine_type;
        }

        public TextView getText_medicine_time() {
            return text_medicine_time;
        }

        public TextView getText_medicine_duration() {
            return text_medicine_duration;
        }


        public ImageView getRemove_reminder() {
            return remove_reminder;
        }
    }


}


