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


import com.example.docportal.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class patientPrescriptionAdapter extends RecyclerView.Adapter<patientPrescriptionAdapter.ViewHolder> implements Filterable {

    private final List<String> doctor_name;
    private final List<String> medicine_prescribed;
    private final List<String> medicines_usage;

    private final List<String> prescription_date;


    private final List<String> medicine_names_all;
    Context context;

    Button Totaled;


    public patientPrescriptionAdapter(List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, List<String> nameDataSet3)  {
        doctor_name = nameDataSet;
        medicine_prescribed = nameDataSet1;
        medicines_usage = nameDataSet2;
        prescription_date = nameDataSet3;
        this.medicine_names_all = new ArrayList<>(doctor_name);

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
            doctor_name.clear();
            doctor_name.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView doc_name;
        private final TextView med_name;
        private final TextView med_usage;
        private final TextView presc_date;

        private final ImageView cutout_prescription;
        private final Button buy_medicine;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            doc_name = (TextView) view.findViewById(R.id.medicine_name);
            med_name = (TextView) view.findViewById(R.id.medicine_weight);
            med_usage = (TextView) view.findViewById(R.id.medicine_usage);
            presc_date = (TextView) view.findViewById(R.id.prescription_date);
            cutout_prescription = (ImageView) view.findViewById(R.id.cutout_prescription);
            buy_medicine = (Button) view.findViewById(R.id.buy_medicine);


        }

        public TextView getDoc_name() {
            return doc_name;
        }
        public  TextView getMed_name() {
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
    public void onBindViewHolder(patientPrescriptionAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getDoc_name().setText(doctor_name.get(position));
        viewHolder.getMed_name().setText(medicine_prescribed.get(position));
        viewHolder.getMed_usage().setText(medicines_usage.get(position));
        viewHolder.getPresc_date().setText(prescription_date.get(position));

        viewHolder.getCutout_prescription().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                doctor_name.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, doctor_name.size());

                if(doctor_name.size() == 0){
                    Totaled.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return doctor_name.size();

    }


}
