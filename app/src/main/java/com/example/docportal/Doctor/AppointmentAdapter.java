package com.example.docportal.Doctor;

import android.content.Context;
import android.content.Intent;
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

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> implements Filterable {

    private final List<String> AppointmentNames;
    private final List<String> AppointmentPhones;
    private final List<String> AppointmentNamesAll;
    Context context;
   //  private final List<String> AppointmentPhone;

   // private final List<String> AppointmentDate;
   // private final List<String> AppointmentTime;




    public AppointmentAdapter(List<String> nameDataSet, List<String> nameDataSet1)  {
        AppointmentNames = nameDataSet;
        AppointmentPhones = nameDataSet1;

        this.AppointmentNamesAll = new ArrayList<>(AppointmentNames);

//, List<String> phoneDataSet, List<String> dateDataSet, List<String> timeDataSet

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
                filteredList.addAll(AppointmentNamesAll);
            }
            else{
                for(String  movie: AppointmentNamesAll){
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
            AppointmentNames.clear();
            AppointmentNames.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView apoint_names;
        private final TextView apoint_ph;
        private final Button to_appointment_reschedule;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            apoint_names = (TextView) view.findViewById(R.id.appointmentNames);
            apoint_ph = (TextView) view.findViewById(R.id.appointeePhone);
            to_appointment_reschedule = (Button) view.findViewById(R.id.viewAppointment);




        }

        public TextView getApoint_names() {
            return apoint_names;
        }
        public  TextView getApoint_ph() {
            return apoint_ph;
        }
        public Button getto_appointment_reschedule() {
            return to_appointment_reschedule;
        }
    }




    // Create new views (invoked by the layout manager)
    @Override
    public AppointmentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointment_text_row_item, viewGroup, false);

        return new AppointmentAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AppointmentAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getApoint_names().setText(AppointmentNames.get(position));
        viewHolder.getApoint_ph().setText(AppointmentPhones.get(position));

        viewHolder.getto_appointment_reschedule().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, checkAppointment.class);

                context.startActivity(intent);

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return AppointmentNames.size();

    }



}
