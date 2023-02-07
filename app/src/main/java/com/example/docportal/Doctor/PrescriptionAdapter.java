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

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Patient.bookAppointmentHelperClass;
import com.example.docportal.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> implements Filterable {

    private final List<String> medicine_names;
    private final List<String> medicine_weight;
    private final List<String> medicines_usage;

    private final List<String> medicine_names_all;
    Context context;

    Button Totaled;


    public PrescriptionAdapter(List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, Button Sent_totaled)  {
        medicine_names = nameDataSet;
        medicine_weight = nameDataSet1;
        medicines_usage = nameDataSet2;
        this.medicine_names_all = new ArrayList<>(medicine_names);
        Totaled = Sent_totaled;

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
                }

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return medicine_names.size();

    }


}
