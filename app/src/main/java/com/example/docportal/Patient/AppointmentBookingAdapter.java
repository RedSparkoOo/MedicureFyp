package com.example.docportal.Patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppointmentBookingAdapter extends RecyclerView.Adapter<AppointmentBookingAdapter.ViewHolder> implements Filterable {

    private final List<String> appointed_doctor_name;
    private final List<String> appointed_doctor_specialization;
    private final List<String> appointed_doctor_ID;
    private final List<String> appointed_doctor_phone;
    private final List<String> appointed_doctor_start_time;
    private final List<String> appointed_doctor_close_time;
    private final List<String> appointed_doctor_bio;
    private final List<String> appointed_doctor_img;
    private final ItemClickListener clickListener;

    private final List<String> appointed_doctor_name_all;
    FirebaseFirestore firestore;
    String phone;
    String doctor_name;
    String UID, RID;
    Context context;


    public AppointmentBookingAdapter(List<String> nameDataSet, List<String> nameDataSet1, List<String> nameDataSet2, List<String> nameDataSet3,List<String> nameDataSet4,List<String> nameDataSet5,List<String> nameDataSet6, List<String> nameDataSet7,ItemClickListener itemClickListener)  {
        appointed_doctor_name = nameDataSet;
        appointed_doctor_specialization = nameDataSet1;
        appointed_doctor_ID = nameDataSet2;
        appointed_doctor_phone = nameDataSet3;
        appointed_doctor_start_time = nameDataSet4;
        appointed_doctor_close_time = nameDataSet5;
        appointed_doctor_bio = nameDataSet6;
        appointed_doctor_img = nameDataSet7;

        this.clickListener = itemClickListener;
        this.appointed_doctor_name_all = new ArrayList<>(appointed_doctor_name);


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
                filteredList.addAll(appointed_doctor_name_all);
            }
            else{
                for(String  movie: appointed_doctor_name_all){
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
            appointed_doctor_name.clear();
            appointed_doctor_name.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };
    private void chatAppointment( View v){


    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView appointed_doctor;
        private final TextView appointed_doctor_category;
        private final TextView doctor_start_time;
        private final TextView doctor_close_time;
        private final TextView doctor_bio;
        private final Button book_appointment;
        private final ImageView doctor_profile;

        private final Button to_chat_reschedule;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            to_chat_reschedule = view.findViewById(R.id.chat_doctor_book);
            appointed_doctor = (TextView) view.findViewById(R.id.appointment_doctor_name);
            appointed_doctor_category = (TextView) view.findViewById(R.id.appointment_doctor_specialization);
            doctor_start_time = (TextView) view.findViewById(R.id.doctor_start_time);
            doctor_close_time = (TextView) view.findViewById(R.id.doctor_close_time);
            doctor_bio = (TextView) view.findViewById(R.id.doctor_bio);
            book_appointment = (Button) view.findViewById(R.id.appointment_doctor_book);
            doctor_profile = (ImageView) view.findViewById(R.id.doctor_profile);
        }

        public TextView getDoctor_start_time() {
            return doctor_start_time;
        }

        public TextView getDoctor_close_time() {
            return doctor_close_time;
        }

        public TextView getDoctor_bio() {
            return doctor_bio;
        }

        public TextView getAppointed_doctor() {
            return appointed_doctor;
        }
        public  TextView getAppointed_doctor_category() {
            return appointed_doctor_category;
        }
        public Button getto_appointment_reschedule() {
            return book_appointment;
        }
        public ImageView getDoctor_profile() {
            return doctor_profile;
        }
    }




    // Create new views (invoked by the layout manager)
    @Override
    public AppointmentBookingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_appointment_doctor_nurse_recycler, viewGroup, false);

        return new AppointmentBookingAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AppointmentBookingAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getAppointed_doctor().setText(appointed_doctor_name.get(position));
        viewHolder.getAppointed_doctor_category().setText(appointed_doctor_specialization.get(position));
        viewHolder.getDoctor_start_time().setText(appointed_doctor_start_time.get(position));
        viewHolder.getDoctor_close_time().setText(appointed_doctor_close_time.get(position));
        viewHolder.getDoctor_bio().setText(appointed_doctor_bio.get(position));

        String imageUri = appointed_doctor_img.get(position);
        if (imageUri != null && !imageUri.isEmpty()) {
            Picasso.get().load(imageUri).into(viewHolder.getDoctor_profile());
        }
        viewHolder.to_chat_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = view.getContext();
                firestore = FirebaseFirestore.getInstance();
                RID = appointed_doctor_ID.get(position);
                Bundle bundle = new Bundle();

                bundle.putString("names",appointed_doctor_name.get(position));


                Intent intent = new Intent(context, Chat.class );
                intent.putExtra("ID", RID);
                intent.putExtra("mBundle", bundle);

                context.startActivity(intent);
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
                Context context = v.getContext();
                Intent intent = new Intent(context, AppointmentBooking.class);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return appointed_doctor_name.size();

    }

    public interface ItemClickListener{
        void onItemClick(String details);
    }

}

