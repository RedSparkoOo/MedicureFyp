package com.example.docportal.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.MedicalEquipment;

import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;

public class BuyMedicalAdapter extends FirestoreRecyclerAdapter<Medicine, BuyMedicalAdapter.MedicineListViewHolder> {
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
    public BuyMedicalAdapter(@NonNull FirestoreRecyclerOptions<Medicine> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position, @NonNull Medicine model) {

        try {
            holder.title.setText(model.getTitle());
            holder.description.setText(model.getDescription());
            holder.price.setText(model.getPrice());
            holder.milligram.setText(model.getMilligram());
            holder.quantity.setText(model.getQuantity());
            String imageUri;
            imageUri = model.getImage();
            Picasso.get().load(imageUri).into(holder.imageView);


            holder.positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            });
            holder.negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Quantity = holder.count.getText().toString();
                    Price = holder.price.getText().toString();
                    i = Integer.parseInt(Quantity);
                    if (i == 1)
                        i = 1;
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
    public MedicineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacylayout, parent, false);
            return new MedicineListViewHolder(view);


    }

    public class  MedicineListViewHolder extends RecyclerView.ViewHolder {

            TextView title, description, price, quantity, milligram, count;
            ImageView imageView;
            Button negative, positive;


        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            try{

            title = itemView.findViewById(R.id.productName);

            price = itemView.findViewById(R.id.productPrice);
            description = itemView.findViewById(R.id.productDescription);
            quantity = itemView.findViewById(R.id.productQuantity);
            imageView = itemView.findViewById(R.id.productImage);
            milligram = itemView.findViewById(R.id.productGram);
            negative = itemView.findViewById(R.id.subProduct);
            positive = itemView.findViewById(R.id.addProduct);
            count = itemView.findViewById(R.id.productCount);
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
