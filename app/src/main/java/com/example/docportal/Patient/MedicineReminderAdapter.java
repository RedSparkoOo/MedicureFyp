package com.example.docportal.Patient;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineReminderAdapter extends RecyclerView.Adapter<MedicineReminderAdapter.ViewHolder> {


    private final List<String> medicine_names;
    private final List<String> medicine_duration;
    private final List<String> medicine_time;
    private final List<String> medicine_type;
    String ID;
    FirebaseFirestore FStore;
    Context context;

    Button Totaled;
    TextView medicine_selected;
    FirebaseAuth FAuth;


    public MedicineReminderAdapter(List<String> medicine_name, List<String> med_duration, List<String> med_type, List<String> med_time, String uid, Button medicine_confirmation, TextView Selected) {
        medicine_names = medicine_name;
        medicine_duration = med_duration;
        medicine_type = med_type;
        medicine_time = med_time;
        Totaled = medicine_confirmation;
        medicine_selected = Selected;
        ID = uid;

    }

//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//            List<String> filteredList = new ArrayList<>();
//
//            if(charSequence.toString().isEmpty()){
//                filteredList.addAll(medicine_names_all);
//            }
//            else{
//                for(String  movie: medicine_names_all){
//                    if(movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                        filteredList.add(movie);
//                    }
//                }
//            }
//
//
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = filteredList;
//
//
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            doctor_name.clear();
//            doctor_name.addAll((Collection<? extends String>) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };

    // Create new views (invoked by the layout manager)
    @Override
    public MedicineReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.medicine_reminder, viewGroup, false);

        return new MedicineReminderAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MedicineReminderAdapter.ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getMed_name().setText(medicine_names.get(position));
        viewHolder.getMed_dur().setText(medicine_duration.get(position));
        viewHolder.getMed_type().setText(medicine_type.get(position));


        viewHolder.getCutout_med().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                context = v.getContext();


                medicine_type.remove(position);
                medicine_names.remove(position);
                medicine_time.remove(position);
                medicine_duration.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, medicine_names.size());


                if (medicine_names.size() == 0) {
                    Totaled.setVisibility(View.INVISIBLE);
                    medicine_selected.setVisibility(View.INVISIBLE);
                }

                FStore = FirebaseFirestore.getInstance();


            }
        });

        Totaled.setOnClickListener(new View.OnClickListener() {
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
                alert_msg.setText("Confirm Selection?");

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MedicineReminder();
//                        MedicineCheck();
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

    private void MedicineCheck() {
        DataSnapshot dataSnapshot = null;
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Medicine Reminder").document(ID);
        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("Medicine Name")) {
                Array array = documentSnapshot.get("Medicine Name", Array.class);
                if (array != null) {
                    Toast.makeText(context, "Not Empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Array field is empty
                }
            } else {
                // Document or array field does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle document retrieval failure
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return medicine_names.size();

    }

    public void MedicineReminder() {

        FAuth = FirebaseAuth.getInstance();
        FStore = FirebaseFirestore.getInstance();
        FStore.clearPersistence();


        DocumentReference documentReference = FStore.collection("Medicine Reminder").document(ID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.contains("Medicine Name") && value.contains("Medicine Duration") && value.contains("Medicine Type") && value.contains("Medicine Time") && value.exists()) {
                    Toast.makeText(context, "Data Exists", Toast.LENGTH_SHORT).show();

                    Object medicine_duration_last_value = medicine_duration.get(medicine_duration.size() - 1);
                    Object medicine_type_last_value = medicine_type.get(medicine_type.size() - 1);
                    Object medicine_time_last_value = medicine_time.get(medicine_time.size() - 1);
                    for (String new_values : medicine_names) {

                        Object medicine_name_last_value = new_values;
                        documentReference.update("Medicine Name", FieldValue.arrayUnion(medicine_name_last_value));
                    }


                    documentReference.update("Medicine Time", FieldValue.arrayUnion(medicine_time_last_value));
                    documentReference.update("Medicine Type", FieldValue.arrayUnion(medicine_type_last_value));
                    documentReference.update("Medicine Duration", FieldValue.arrayUnion(medicine_duration_last_value));
                } else {
                    Map<Object, List<String>> Reminder = new HashMap<>();

                    Reminder.put("Medicine Name", medicine_names);
                    Reminder.put("Medicine Type", medicine_type);
                    Reminder.put("Medicine Duration", medicine_duration);
                    Reminder.put("Medicine Time", medicine_time);
                    documentReference.set(Reminder);
                }


            }
        });
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView med_name;
        private final TextView med_dur;
        private final TextView med_type;
        private final ImageView cutout_med;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            med_name = view.findViewById(R.id.medicine_name);
            med_dur = view.findViewById(R.id.medicine_duration);
            med_type = view.findViewById(R.id.medicine_type);
            cutout_med = view.findViewById(R.id.cancel_medicine);


        }


        public TextView getMed_name() {
            return med_name;
        }

        public TextView getMed_dur() {
            return med_dur;
        }

        public ImageView getCutout_med() {
            return cutout_med;
        }

        public TextView getMed_type() {
            return med_type;
        }

    }


}
