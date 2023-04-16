package com.example.docportal.Patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class labsAdapter extends RecyclerView.Adapter<labsAdapter.ViewHolder> implements Filterable {

    private final List<String> lab_name;
    private final List<String> lab_region;
    private final List<String> lab_time;
    private final List<String> lab_latitude;
    private final List<String> lab_longitude;
    private final List<Integer> lab_logos;

    private final List<String> lab_names_all;
    Context context;

    public labsAdapter(List<String> fetched_lab_names, List<String> fetch_lab_regions, List<String> fetch_lab_time, List<String> fetch_labs_latitude,List<String> fetch_labs_longitude,List<Integer> fetch_lab_logos)  {
        lab_name = fetched_lab_names;
        lab_region = fetch_lab_regions;
        lab_time = fetch_lab_time;
        lab_latitude = fetch_labs_latitude;
        lab_longitude = fetch_labs_longitude;
        lab_logos = fetch_lab_logos;
        this.lab_names_all = new ArrayList<>(lab_name);


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
                filteredList.addAll(lab_names_all);
            }
            else{
                for(String  movie: lab_names_all){
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
            lab_name.clear();
            lab_name.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView lab_names;
        private final TextView lab_regions;
        private final TextView lab_time;
        private final ImageView display_logo;
        private final Button lab_location;
        private final Button lab_services;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            lab_names = (TextView) view.findViewById(R.id.lab_name);
            lab_regions = (TextView) view.findViewById(R.id.lab_region);
            lab_time = (TextView) view.findViewById(R.id.lab_time);
            lab_location = (Button) view.findViewById(R.id.directions);
            lab_services = (Button) view.findViewById(R.id.services);
            display_logo = (ImageView) view.findViewById(R.id.display_lab_logo);

            display_logo.setElevation(5);
            display_logo.setTranslationZ(5);
        }

        public TextView getLab_names() {
            return lab_names;
        }

        public TextView getLab_regions() {
            return lab_regions;
        }

        public TextView getLab_time() {
            return lab_time;
        }

        public Button getLab_location() {
            return lab_location;
        }

        public Button getLab_services() {
            return lab_services;
        }

        public ImageView getDisplay_logo() {
            return display_logo;
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public labsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.labs_bank, viewGroup, false);

        return new labsAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(labsAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getLab_names().setText(lab_name.get(position));
        viewHolder.getLab_regions().setText(lab_region.get(position));
        viewHolder.getLab_time().setText(lab_time.get(position));
        viewHolder.getDisplay_logo().setImageResource(lab_logos.get(position));


        viewHolder.getLab_location().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              try {
                  context = v.getContext();
                  double latitude = Double.parseDouble(lab_latitude.get(position));
                  double longitude = Double.parseDouble(lab_longitude.get(position));
                  String lab = lab_name.get(position);

                  Bundle bundle = new Bundle();
                  bundle.putDouble("latitude",latitude);
                  bundle.putDouble("longitude",longitude);
                  bundle.putString("lab_name",lab);

                  Intent intent = new Intent(context,allMaps.class);
                  intent.putExtras(bundle);
                  context.startActivity(intent);
              }
              catch (Exception e){
                  Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
              }

                Toast.makeText(context, "Data Stored", Toast.LENGTH_SHORT).show();



            }
        });

        viewHolder.getLab_services().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   context = v.getContext();
                   Intent options_intent = new Intent(context,labTestManagement_options.class);
                   context.startActivity(options_intent);
               }
               catch (Exception e){
                   Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return lab_name.size();

    }

}

