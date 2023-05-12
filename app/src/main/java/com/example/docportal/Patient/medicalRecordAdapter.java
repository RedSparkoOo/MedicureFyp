package com.example.docportal.Patient;

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

public class medicalRecordAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.medicalRecordAdapter.ViewHolder> implements Filterable {

    private final List<String> MoviesList;

    private final List<String> MoviesListAll;
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(MoviesListAll);
            } else {
                for (String movie : MoviesListAll) {
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
            MoviesList.clear();
            MoviesList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public medicalRecordAdapter(List<String> dataSet) {
        MoviesList = dataSet;

        this.MoviesListAll = new ArrayList<>(MoviesList);

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public com.example.docportal.Patient.medicalRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.medicalrecordlayout, viewGroup, false);

        return new com.example.docportal.Patient.medicalRecordAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(com.example.docportal.Patient.medicalRecordAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(MoviesList.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return MoviesList.size();

    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView _medicalFileName;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            _medicalFileName = view.findViewById(R.id.medicalFileName);


        }

        public TextView getTextView() {
            return _medicalFileName;

        }


    }
}
