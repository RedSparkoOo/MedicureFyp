package com.example.docportal.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class bloodBankAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.bloodBankAdapter.ViewHolder> implements Filterable {

    private List<String> rec_blood_group_names;
    private List<String> rec_blood_group_donors;
    private List<String> rec_blood_group_acceptors;
    private List<String> rec_blood_unit_price;
    private List<String> rec_blood_group_names_all;
    private List<Integer> rec_blood_group_quantity;

    int quantity = 0;
    Context context;

    public bloodBankAdapter(List<String> fetched_blood_group,List<String> fetched_donors,List<String> fetched_acceptors,List<String> unit_price) {
        rec_blood_group_names = fetched_blood_group;
        rec_blood_group_donors = fetched_donors;
        rec_blood_group_acceptors = fetched_acceptors;
        rec_blood_unit_price = unit_price;

        this.rec_blood_group_names_all = new ArrayList<>(rec_blood_group_names);

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
                filteredList.addAll(rec_blood_group_names_all);
            }
            else{
                for(String  movie: rec_blood_group_names_all){
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
            rec_blood_group_names.clear();
            rec_blood_group_names.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bg_name;
        private final TextView bg_donors;
        private final TextView bg_acceptors;
        private final TextView bg_unit_price;
        private final TextView bg_unit_quantity;
        private final ImageView _addProduct;
        private final ImageView _subProduct;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            bg_name = (TextView) view.findViewById(R.id.blood_group_name);
            bg_donors = (TextView) view.findViewById(R.id.blood_donor_group);
            bg_acceptors = (TextView) view.findViewById(R.id.lab_acceptor_group);
            bg_unit_price = (TextView) view.findViewById(R.id.blood_unit_price);
            bg_unit_quantity = (TextView) view.findViewById(R.id.blood_unit_quantity);
            _addProduct = (ImageView) view.findViewById(R.id.add_sign);
            _subProduct = (ImageView) view.findViewById(R.id.sub_sign);


        }

        public TextView getBg_name() {
            return bg_name;
        }

        public TextView getBg_donors() {
            return bg_donors;
        }

        public TextView getBg_acceptors() {
            return bg_acceptors;
        }

        public TextView getBg_unit_price() {
            return bg_unit_price;
        }


        public ImageView get_addProduct() {
            return _addProduct;
        }

        public TextView getBg_unit_quantity() {
            return bg_unit_quantity;
        }

        public ImageView get_subProduct() {
            return _subProduct;
        }
    }





    // Create new views (invoked by the layout manager)
    @Override
    public com.example.docportal.Patient.bloodBankAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bloodbanklayout, viewGroup, false);

        return new com.example.docportal.Patient.bloodBankAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(com.example.docportal.Patient.bloodBankAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getBg_name().setText(rec_blood_group_names.get(position));
        viewHolder.getBg_donors().setText(rec_blood_group_donors.get(position));
        viewHolder.getBg_acceptors().setText(rec_blood_group_acceptors.get(position));
        viewHolder.getBg_unit_price().setText(rec_blood_unit_price.get(position));
        viewHolder.getBg_unit_quantity().setText(Integer.toString(quantity));

        viewHolder.get_addProduct().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                    ++quantity;
                    viewHolder.getBg_unit_quantity().setText(Integer.toString(quantity));

            }
        });

        viewHolder.get_subProduct().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();

                if(quantity == 0){
                    Toast.makeText(context, "quantity already 0", Toast.LENGTH_SHORT).show();
                }
                else {
                    --quantity;
                    viewHolder.getBg_unit_quantity().setText(Integer.toString(quantity));
                }

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return rec_blood_group_names.size();

    }
}

