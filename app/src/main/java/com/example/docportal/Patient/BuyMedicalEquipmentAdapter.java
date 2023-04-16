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
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class BuyMedicalEquipmentAdapter extends FirestoreRecyclerAdapter<MedicalEquipment, BuyMedicalEquipmentAdapter.EquipmentListViewHolder> {
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
    public BuyMedicalEquipmentAdapter(@NonNull FirestoreRecyclerOptions<MedicalEquipment> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EquipmentListViewHolder holder, int position, @NonNull MedicalEquipment model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());
        holder.quantity.setText("1");
        holder.gg.setText("");
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
                if(i  ==  Integer.parseInt(model.getQuantity()))
                    i = Integer.parseInt(model.getQuantity());
                else
                    i++;
                price  = Integer.parseInt(model.getPrice())  * i;
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
                if(i  ==  1)
                    i = 1;
                else
                    i--;
                price  = Integer.parseInt(model.getPrice())  * i;
                holder.count.setText(i.toString());
                holder.price.setText(price.toString());

            }
        });




    }

    @NonNull
    @Override
    public EquipmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacylayout,parent,false);
        return new EquipmentListViewHolder(view);
    }

    public class EquipmentListViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price, quantity, count, gg;
        ImageView imageView;
        Button negative, positive;


        public EquipmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.productName);
            gg= itemView.findViewById(R.id.gg);

            price = itemView.findViewById(R.id.productPrice);
            description = itemView.findViewById(R.id.productDescription);
            quantity = itemView.findViewById(R.id.productQuantity);
            imageView = itemView.findViewById(R.id.productImage);
            negative = itemView.findViewById(R.id.subProduct);
            positive = itemView.findViewById(R.id.addProduct);
            count = itemView.findViewById(R.id.productCount);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener1.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener1.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onitemlongClick(getSnapshots().getSnapshot(position),position);

                    }
                    return false;
                }
            });
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
