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

public class pharmacyAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.pharmacyAdapter.ViewHolder> implements Filterable {

    private List<String> productList;
    private List<String> priceList;
    private List<String> productListAll;


    public pharmacyAdapter(List<String> dataSet, List<String> dataSet1) {
        productList = dataSet;
        priceList = dataSet1;

        this.productListAll = new ArrayList<>(productList);

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
                filteredList.addAll(productListAll);
            }
            else{
                for(String  movie: productListAll){
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
            productList.clear();
            productList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView _productName;
//        private final TextView _productCount;
        private final TextView _productPrice;
        private final Button _addProduct;
        private final Button _subProduct;




        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            _productName = (TextView) view.findViewById(R.id.productName);
//            _productCount = (TextView) view.findViewById(R.id.productCount);
            _productPrice = (TextView) view.findViewById(R.id.productPrice);
            _addProduct = (Button) view.findViewById(R.id.addProduct);
            _subProduct = (Button) view.findViewById(R.id.subProduct);




        }

        public TextView getTextView() {
            return _productName;

        }

        public TextView getTextView1() {
            return _productPrice;

        }


    }





    // Create new views (invoked by the layout manager)
    @Override
    public com.example.docportal.Patient.pharmacyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pharmacylayout, viewGroup, false);

        return new com.example.docportal.Patient.pharmacyAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(com.example.docportal.Patient.pharmacyAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(productList.get(position));
        viewHolder.getTextView1().setText(priceList.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return productList.size();

    }
}

