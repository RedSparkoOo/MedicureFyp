package com.example.docportal.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class addToCartAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.addToCartAdapter.ViewHolder> implements Filterable {

    private List<String> MoviesList;
    private List<String> MoviesList2;
    private List<String> MoviesList3;
    private List<String> MoviesListAll;


    public addToCartAdapter(List<String> dataSet, List<String> dataSet1, List<String> dataSet2) {
        MoviesList = dataSet;
        MoviesList2 = dataSet1;
        MoviesList3 = dataSet2;
        this.MoviesListAll = new ArrayList<>(MoviesList);

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
                filteredList.addAll(MoviesListAll);
            }
            else{
                for(String  movie: MoviesListAll){
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
            MoviesList.clear();
            MoviesList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView _productName;
        private final TextView _productCharges;
        private final TextView _productQuantity;



        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            _productName = (TextView) view.findViewById(R.id.productName);
            _productCharges = (TextView) view.findViewById(R.id.productPrice);
            _productQuantity = (TextView) view.findViewById(R.id.productQuantity);


        }

        public TextView getTextView() {
            return _productName;

        }

        public TextView getTextView1() {
            return _productCharges;

        }

        public TextView getTextView2() {
            return _productQuantity;

        }
    }





    // Create new views (invoked by the layout manager)
    @Override
    public com.example.docportal.Patient.addToCartAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.addtocart, viewGroup, false);

        return new com.example.docportal.Patient.addToCartAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(com.example.docportal.Patient.addToCartAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(MoviesList.get(position));
        viewHolder.getTextView1().setText(MoviesList2.get(position));
        viewHolder.getTextView2().setText(MoviesList3.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return MoviesList.size();

    }
}
