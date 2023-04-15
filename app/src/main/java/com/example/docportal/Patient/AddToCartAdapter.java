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

public class AddToCartAdapter extends FirestoreRecyclerAdapter<Medicine, AddToCartAdapter.MedicineListViewHolder> {
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
    public AddToCartAdapter(@NonNull FirestoreRecyclerOptions<Medicine> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position, @NonNull Medicine model) {


        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());
        holder.milligram.setText(model.getMilligram());
        holder.quantity.setText("1");
        String imageUri;
        imageUri = model.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);



    }


    @NonNull
    @Override
    public MedicineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_to_cart,parent,false);
        return new MedicineListViewHolder(view);
    }

    public int getTotalPosition() {
        return RecyclerView.NO_POSITION;
    }



    public class  MedicineListViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price, quantity, milligram;
        ImageView imageView;



        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.atitle);
            description = itemView.findViewById(R.id.adescription);
            price = itemView.findViewById(R.id.aprice);
            quantity = itemView.findViewById(R.id.aquantity);
            imageView = itemView.findViewById(R.id.aimage);
            milligram = itemView.findViewById(R.id.amilligram);


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
