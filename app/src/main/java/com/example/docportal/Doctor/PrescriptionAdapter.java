package com.example.docportal.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Patient.bookAppointmentHelperClass;
import com.example.docportal.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> implements Filterable {

    private final List<String> medicine_names;
    private final List<String> medicine_weight;
    private final List<String> medicines_usage;
    private final List<String> medicine_purpose;
    private final List<String> medicine_names_all;

    String patient_name;
    String patient_email;
    String doctor_name;
    String doctor_email;

    Context context;

    Button Totaled;
    TextView Selected;
    FirebaseFirestore FStore;

    String fire_med_name;
    String fire_med_weight;

    public PrescriptionAdapter(String pat_name,String pat_email,String doc_name, String doc_email,List<String> med_names, List<String> med_weight, List<String> med_purpose,List<String> med_usage, Button Sent_totaled, TextView selected_medics)  {

        patient_name = pat_name;
        patient_email = pat_email;
        doctor_name = doc_name;
        doctor_email = doc_email;
        medicine_names = med_names;
        medicine_weight = med_weight;
        medicine_purpose = med_purpose;
        medicines_usage = med_usage;
        this.medicine_names_all = new ArrayList<>(medicine_names);
        Totaled = Sent_totaled;
        Selected = selected_medics;

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
                filteredList.addAll(medicine_names_all);
            }
            else{
                for(String  movie: medicine_names_all){
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
            medicine_names.clear();
            medicine_names.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView med_name;
        private final TextView med_weight;
        private final TextView med_usage;
        private final ImageView cancel_medicine;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            med_name = (TextView) view.findViewById(R.id.medicine_name);
            med_weight = (TextView) view.findViewById(R.id.medicine_weight);
            med_usage = (TextView) view.findViewById(R.id.medicine_usage);
            cancel_medicine = (ImageView) view.findViewById(R.id.cancel_medicine);


        }

        public TextView getMed_name() {
            return med_name;
        }
        public  TextView getMed_weight() {
            return med_weight;
        }
        public TextView getMed_usage() {
            return med_usage;
        }

        public ImageView getCancel_medicine() {
            return cancel_medicine;
        }
    }




    // Create new views (invoked by the layout manager)
    @Override
    public PrescriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.prescription_layout, viewGroup, false);

        return new PrescriptionAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PrescriptionAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getMed_name().setText(medicine_names.get(position));
        viewHolder.getMed_weight().setText(medicine_weight.get(position));
        viewHolder.getMed_usage().setText(medicines_usage.get(position));

        viewHolder.getCancel_medicine().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                medicine_names.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,medicine_names.size());

                if(medicine_names.size() == 0){
                    Totaled.setVisibility(View.INVISIBLE);
                    Selected.setVisibility(View.INVISIBLE);
                }

                Totaled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        prescriptionFireStore();
                    }
                });

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return medicine_names.size();

    }

    public void prescriptionFireStore(){

        FStore = FirebaseFirestore.getInstance();
        FStore.clearPersistence();

        DocumentReference documentReference = FStore.collection("Prescriptions Sent").document();
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Map<Object,String> prescription = new HashMap<>();
                prescription.put("Prescribed Patient Name",patient_name);
                prescription.put("Prescribed Patient Email",patient_email);
                prescription.put("Prescribing Doctor Name",doctor_name);
                prescription.put("Prescribed Doctor Email",doctor_email);
                prescription.put("Date", String.valueOf(new Timestamp(new Date())));
                prescription.put("Medicine Name", );

                documentReference.set(prescription);




            }
        });
    }

}
