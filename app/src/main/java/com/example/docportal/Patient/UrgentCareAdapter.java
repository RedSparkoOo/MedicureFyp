package com.example.docportal.Patient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.List;

public class UrgentCareAdapter extends RecyclerView.Adapter<com.example.docportal.Patient.UrgentCareAdapter.ViewHolder> {

    private List<String> list_contact_name;
    private List<String> list_contact_phone;
    private List<String> list_contact_relation;



    public UrgentCareAdapter(List<String> name_dataset,List<String> relation_data_set, List<String> phone_data_set) {
        list_contact_name = name_dataset;
        list_contact_relation = relation_data_set;
        list_contact_phone = phone_data_set;



    }

//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//
//            List<String> filteredList = new ArrayList<>();
//
//            if(charSequence.toString().isEmpty()){
//                filteredList.addAll(list_contact_name);
//            }
//            else{
//                for(String  movie: list_contact_name){
//                    if(movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
//                        filteredList.add(movie);
//                    }
//                }
//            }
//
//
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = filteredList;
//
//
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            list_contact_name.clear();
//            list_contact_name.addAll((Collection<? extends String>) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView contact_name;
        private final TextView contact_relation;
        private final TextView contact_phone;
        private final ImageView call_contact;
        private final ImageView message_contact;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            contact_name = (TextView) view.findViewById(R.id.contact_name);
            contact_relation = (TextView) view.findViewById(R.id.contact_relation);
            call_contact = (ImageView) view.findViewById(R.id.call_contact);
            contact_phone = (TextView) view.findViewById(R.id.contact_phone);
            message_contact = (ImageView) view.findViewById(R.id.message_contact);


        }

        public TextView getContact_name() {
            return contact_name;
        }

        public TextView getContact_relation() {
            return contact_relation;
        }

        public TextView getContact_phone() {
            return contact_phone;
        }

        public ImageView getCall_contact() {
            return call_contact;
        }

        public ImageView getMessage_contact() {
            return message_contact;
        }
    }





    // Create new views (invoked by the layout manager)
    @Override
    public com.example.docportal.Patient.UrgentCareAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.urgent_care_layout, viewGroup, false);

        return new com.example.docportal.Patient.UrgentCareAdapter.ViewHolder(view);
    }



    // Replace the contents of a view (invoked by the layout manager)
    //@Override
    public void onBindViewHolder(com.example.docportal.Patient.UrgentCareAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element


        viewHolder.getContact_name().setText(list_contact_name.get(position));
        viewHolder.getContact_phone().setText(list_contact_phone.get(position));
        viewHolder.getContact_relation().setText(list_contact_relation.get(position));

        viewHolder.getCall_contact().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String phone = list_contact_phone.get(position);
                Uri dialUri = Uri.parse("tel:" + phone);

                Intent dialIntent = new Intent(Intent.ACTION_DIAL, dialUri);
                context.startActivity(dialIntent);
            }
        });

        viewHolder.getMessage_contact().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String phone = list_contact_phone.get(position);
                String message = "I am sending you a distress signal. Please reach me out";
                Uri uri = Uri.parse("whatsapp://send?phone=" + phone);
                Intent sendIntent = new Intent(Intent.ACTION_SEND, uri);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return list_contact_name.size();

    }
}
