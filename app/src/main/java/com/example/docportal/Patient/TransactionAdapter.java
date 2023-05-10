package com.example.docportal.Patient;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class TransactionAdapter extends FirestoreRecyclerAdapter<TransactionModel, TransactionAdapter.MedicineListViewHolder> {




    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TransactionAdapter(@NonNull FirestoreRecyclerOptions<TransactionModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position, @NonNull TransactionModel model) {
        holder.title.setText(model.getItem());
        holder.price.setText(model.getPrice());
        holder.time.setText(model.getTime());



    }


    @NonNull
    @Override
    public MedicineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_recycler_layout,parent,false);
        return new MedicineListViewHolder(view);
    }
    public class  MedicineListViewHolder extends RecyclerView.ViewHolder {
        TextView title,  time, price;

        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemName);

            price = itemView.findViewById(R.id.itemPrice);
            time = itemView.findViewById(R.id.itemTime);


        }


    }

}
