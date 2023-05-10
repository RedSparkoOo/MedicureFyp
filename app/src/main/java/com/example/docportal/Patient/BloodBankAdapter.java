package com.example.docportal.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class BloodBankAdapter extends FirestoreRecyclerAdapter<BloodBankModel, BloodBankAdapter.BloodBankViewHolder> {
    private MedicineListAdapter.onItemLongClickListener listener;

    private MedicineListAdapter.onItemClickListener listener1;

    public Integer getI() {
        return i;
    }

    public Integer getPrice() {
        return price;
    }

    private Integer i, price;
    private String Quantity, Price;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BloodBankAdapter(@NonNull FirestoreRecyclerOptions<BloodBankModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BloodBankViewHolder holder, int position, @NonNull BloodBankModel model) {

        try {
            holder.name.setText(model.getName());
            holder.acceptor.setText(model.getAcceptor());
            holder.donor.setText(model.getDonor());
            holder.price.setText(model.getPrice());
            if(model.getQuantity().equals("0"))
                holder.quantity.setText("Out of stock");
            else
                holder.quantity.setText(model.getQuantity());




            holder.positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(model.getQuantity().equals("0"));
                    else {
                        Quantity = holder.count.getText().toString();
                        Price = holder.price.getText().toString();

                        price = Integer.parseInt(Price);
                        i = Integer.parseInt(Quantity);

                        if (i == Integer.parseInt(model.getQuantity()))
                            i = Integer.parseInt(model.getQuantity());
                        else
                            i++;
                        price = Integer.parseInt(model.getPrice()) * i;
                        holder.count.setText(i.toString());
                        holder.price.setText(price.toString());

                    }
                }
            });
            holder.negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Quantity = holder.count.getText().toString();
                    Price = holder.price.getText().toString();
                    i = Integer.parseInt(Quantity);
                   if (i == 1)
                       i = 1;
                   else if( i==0)
                       i=0;
                   else
                     i--;
                    price = Integer.parseInt(model.getPrice()) * i;
                    holder.count.setText(i.toString());
                    holder.price.setText(price.toString());

                }
            });
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }


    @NonNull
    @Override
    public BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodbanklayout, parent, false);
        return new BloodBankViewHolder(view);


    }

    public class  BloodBankViewHolder extends RecyclerView.ViewHolder {

        TextView name, donor, acceptor, price, count, quantity;
        ImageView negative, positive;


        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            try{

                name = itemView.findViewById(R.id.blood_group_name);
                donor = itemView.findViewById(R.id.blood_donor_group);
                acceptor = itemView.findViewById(R.id.lab_acceptor_group);
                price = itemView.findViewById(R.id.blood_unit_price);
                count = itemView.findViewById(R.id.blood_unit_quantity);
                quantity = itemView.findViewById(R.id.lab_quantity_group);
                negative = itemView.findViewById(R.id.sub_sign);
                positive = itemView.findViewById(R.id.add_sign);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener1.onItemClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                });
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int position = getAbsoluteAdapterPosition();
                            if (position != RecyclerView.NO_POSITION && listener != null) {
                                listener1.onItemClick(getSnapshots().getSnapshot(position), position);

                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onitemlongClick(getSnapshots().getSnapshot(position), position);

                        }
                        return false;
                    }
                });
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }




    }

    public interface onItemLongClickListener {
        void onitemlongClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemLongClickListener(MedicineListAdapter.onItemLongClickListener listener){
        this.listener = listener;
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();


    }
    public void setOnItemClickListener(MedicineListAdapter.onItemClickListener listener){
        this.listener1 = listener;
    }
    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
}
