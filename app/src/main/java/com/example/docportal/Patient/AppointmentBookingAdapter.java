package com.example.docportal.Patient;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Doctor.Chat;
import com.example.docportal.R;
import com.example.docportal.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AppointmentBookingAdapter extends RecyclerView.Adapter<AppointmentBookingAdapter.ViewHolder> implements Filterable {

    private final List<String> appointed_doctor_name;
    private final List<String> appointed_doctor_specialization;
    private final List<String> appointed_doctor_ID;
    private final List<String> appointed_doctor_phone;
    private final List<String> appointed_doctor_name_all;
    private final ItemClickListener clickListener;
    String phone;
    String doctor_name;
    String UID, RID;
    Context context;
    Singleton singleton = new Singleton();
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(appointed_doctor_name_all);
            } else {
                for (String movie : appointed_doctor_name_all) {
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
            appointed_doctor_name.clear();
            appointed_doctor_name.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public AppointmentBookingAdapter(List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, List<String> nameDataSet3, ItemClickListener itemClickListener) {
        appointed_doctor_name = nameDataSet;
        appointed_doctor_specialization = nameDataSet1;
        appointed_doctor_ID = nameDataSet2;
        appointed_doctor_phone = nameDataSet3;

        this.clickListener = itemClickListener;
        this.appointed_doctor_name_all = new ArrayList<>(appointed_doctor_name);


    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private void chatAppointment(View v) {


    }

    // Create new views (invoked by the layout manager)
    @Override
    public AppointmentBookingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.appointmnet_doctor_check_layout, viewGroup, false);

        return new AppointmentBookingAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AppointmentBookingAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAppointed_doctor().setText(appointed_doctor_name.get(position));
        viewHolder.getAppointed_doctor_category().setText(appointed_doctor_specialization.get(position));

        viewHolder.to_chat_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context = view.getContext();

                RID = appointed_doctor_ID.get(position);
                Bundle bundle = new Bundle();

                bundle.putString("names", appointed_doctor_name.get(position));
                context.startActivity(singleton.getIntent(context, Chat.class).putExtra("ID", RID).putExtra("mBundle", bundle));

            }
        });


        viewHolder.getto_appointment_reschedule().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(appointed_doctor_name.get(position));
                clickListener.onItemClick(appointed_doctor_phone.get(position));
                clickListener.onItemClick(appointed_doctor_ID.get(position));

                phone = appointed_doctor_phone.get(position);
                doctor_name = appointed_doctor_name.get(position);
                UID = appointed_doctor_ID.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("Doctor_phone", phone);
                bundle.putString("Doctor_name", doctor_name);
                bundle.putString("Doctor_Id", UID);

                context.startActivity(singleton.getIntent(context, AppointmentBooking.class).putExtras(bundle));

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return appointed_doctor_name.size();

    }

    public interface ItemClickListener {
        void onItemClick(String details);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView appointed_doctor;
        private final TextView appointed_doctor_category;
        private final Button book_appointment;
        private final ImageView doctor_profile;

        private final Button to_chat_reschedule;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            to_chat_reschedule = view.findViewById(R.id.chat_doctor_book);
            appointed_doctor = view.findViewById(R.id.appointment_doctor_name);
            appointed_doctor_category = view.findViewById(R.id.appointment_doctor_specialization);
            book_appointment = view.findViewById(R.id.appointment_doctor_book);
            doctor_profile = view.findViewById(R.id.doctor_profile);


        }


        public TextView getAppointed_doctor() {
            return appointed_doctor;
        }

        public TextView getAppointed_doctor_category() {
            return appointed_doctor_category;
        }

        public Button getto_appointment_reschedule() {
            return book_appointment;
        }

        public ImageView getDoctor_profile() {
            return doctor_profile;
        }
    }

}

