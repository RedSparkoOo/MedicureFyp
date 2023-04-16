package com.example.docportal.Patient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class labTestManagementAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.labTestManagementAdapter.ViewHolder> implements Filterable {

    private List<String> test_name_list;
    private List<String> test_desc_list;
    private List<String> test_price_list;
    private List<String> test_name_list_all;
    Context context;


    public labTestManagementAdapter(List<String> test_name,List<String> test_description,List<String> test_cost) {
        test_name_list = test_name;
        test_desc_list = test_description;
        test_price_list = test_cost;

        this.test_name_list_all = new ArrayList<>(test_name_list);

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
                filteredList.addAll(test_name_list_all);
            }
            else{
                for(String  movie: test_name_list_all){
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
            test_name_list.clear();
            test_name_list.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView lab_test_name;
        private final TextView lab_test_description;
        private final TextView lab_test_price;
        private final TextView add_to_cart;





        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            lab_test_name = (TextView) view.findViewById(R.id.lab_test_name);
            lab_test_description = (TextView) view.findViewById(R.id.lab_test_description);
            lab_test_price = (TextView) view.findViewById(R.id.labTestPrice);
            add_to_cart = (TextView) view.findViewById(R.id.add_to_cart);


        }

        public TextView getLab_test_description() {
            return lab_test_description;
        }

        public TextView getLab_test_price() {
            return lab_test_price;
        }

        public TextView getAdd_to_cart() {
            return add_to_cart;
        }

        public TextView getLab_test_name() {
            return lab_test_name;

        }


    }





    // Create new views (invoked by the layout manager)
    @Override
    public com.example.docportal.Patient.labTestManagementAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.labtest_text, viewGroup, false);

        return new com.example.docportal.Patient.labTestManagementAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(com.example.docportal.Patient.labTestManagementAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getLab_test_name().setText(test_name_list.get(position));
        viewHolder.getLab_test_description().setText(test_desc_list.get(position));
        viewHolder.getLab_test_price().setText(test_price_list.get(position));

        viewHolder.getAdd_to_cart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                Intent add_to_cart = new Intent(context,AddToCart.class);
                context.startActivity(add_to_cart);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return test_name_list.size();

    }
}

