package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpcomingNotificationsAdapter extends RecyclerView.Adapter<UpcomingNotificationsAdapter.ViewHolder> implements Filterable {

    private final List<String> AppointmentNames;
    private final List<String> AppointmentPhones;
    private final List<String> AppointmentDate;
    private final List<String> AppointmentTime;
    private final List<String> AppointmentNamesAll;
    Context context;


    public UpcomingNotificationsAdapter(List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, List<String> nameDataSet3)  {
        AppointmentNames = nameDataSet;
        AppointmentPhones = nameDataSet1;
        AppointmentDate = nameDataSet2;
        AppointmentTime = nameDataSet3;
        this.AppointmentNamesAll = new ArrayList<>(AppointmentNames);
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
        private final TextView appointment_date;
        private final TextView appointment_time;
        private final ImageView profile_image;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            apoint_names = (TextView) view.findViewById(R.id.appointmentNames);
            appointment_date = (TextView) view.findViewById(R.id.appointment_date);
            appointment_time = (TextView) view.findViewById(R.id.appointment_time);
            profile_image = (ImageView) view.findViewById(R.id.appointment_patient_profile);


        }

        public ImageView getProfile_image() {
            return profile_image;
        }

        public TextView getApoint_names() {
            return apoint_names;
        }

        public TextView getAppointment_date() {
            return appointment_date;
        }

        public TextView getAppointment_time() {
            return appointment_time;
        }

    }




    // Create new views (invoked by the layout manager)
    @Override
    public UpcomingNotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.upcoming_notifications_layout, viewGroup, false);

        return new UpcomingNotificationsAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UpcomingNotificationsAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getApoint_names().setText(AppointmentNames.get(position));
        viewHolder.getAppointment_date().setText(AppointmentDate.get(position));
        viewHolder.getAppointment_time().setText(AppointmentTime.get(position));




    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return AppointmentNames.size();

    }

    public void Profile(){



    }


}
