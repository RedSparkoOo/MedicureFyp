package com.example.docportal.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class patientPrescriptionAdapter extends RecyclerView.Adapter<patientPrescriptionAdapter.ViewHolder> implements Filterable {

    private final List<String> doctor_name;
    private final List<String> medicine_prescribed;
    private final List<String> medicines_usage;
    private final List<String> medicine_weight;
    private final List<String> prescription_date;
    private final List<String> medicine_names_all;
    String ID;
    Context context;
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(medicine_names_all);
            } else {
                for (String movie : medicine_names_all) {
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
            doctor_name.clear();
            doctor_name.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public patientPrescriptionAdapter(List<String> doc_name, List<String> medicine_name, List<String> medi_weight, List<String> usage, List<String> pres_date, String uid) {
        doctor_name = doc_name;
        medicine_prescribed = medicine_name;
        medicine_weight = medi_weight;
        medicines_usage = usage;
        ID = uid;
        prescription_date = pres_date;
        this.medicine_names_all = new ArrayList<>(doctor_name);

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public patientPrescriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.patient_prescription_layout, viewGroup, false);

        return new patientPrescriptionAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(patientPrescriptionAdapter.ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getDoc_name().setText(doctor_name.get(position));
        viewHolder.getMed_name().setText(medicine_prescribed.get(position));
        viewHolder.getMed_usage().setText(medicines_usage.get(position));
        viewHolder.getPresc_date().setText(prescription_date.get(position));
        viewHolder.getMed_weight().setText(medicine_weight.get(position));

        viewHolder.getCutout_prescription().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHandler firestoreHandler = new FirestoreHandler();


                context = v.getContext();

                doctor_name.remove(position);
                prescription_date.remove(position);
                medicine_prescribed.remove(position);
                medicine_weight.remove(position);
                medicines_usage.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, doctor_name.size());
                firestoreHandler.getFirestoreInstance().collection("Prescriptions Sent").document(ID).update("Prescribed Doctor Name", FieldValue.arrayRemove(position));
                firestoreHandler.getFirestoreInstance().collection("Prescriptions Sent").document(ID).update("Prescription Date", FieldValue.arrayRemove(position));
                firestoreHandler.getFirestoreInstance().collection("Prescriptions Sent").document(ID).update("Medicines Prescribed Weight", FieldValue.arrayRemove(position));
                firestoreHandler.getFirestoreInstance().collection("Prescriptions Sent").document(ID).update("Medicines Prescribed Usage", FieldValue.arrayRemove(position));


            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return doctor_name.size();

    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView doc_name;
        private final TextView med_name;
        private final TextView med_usage;
        private final TextView presc_date;
        private final TextView med_weight;
        private final ImageView cutout_prescription;
        private final Button buy_medicine;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            doc_name = view.findViewById(R.id.doctor_name);
            med_name = view.findViewById(R.id.medicine_name);
            med_usage = view.findViewById(R.id.medicine_usage);
            presc_date = view.findViewById(R.id.prescription_date);
            med_weight = view.findViewById(R.id.medicine_weight);
            cutout_prescription = view.findViewById(R.id.cutout_prescription);
            buy_medicine = view.findViewById(R.id.buy_medicine);


        }

        public TextView getMed_weight() {
            return med_weight;
        }

        public TextView getDoc_name() {
            return doc_name;
        }

        public TextView getMed_name() {
            return med_name;
        }

        public TextView getMed_usage() {
            return med_usage;
        }

        public ImageView getCutout_prescription() {
            return cutout_prescription;
        }

        public TextView getPresc_date() {
            return presc_date;
        }

        public Button getBuy_medicine() {
            return buy_medicine;
        }
    }


}
