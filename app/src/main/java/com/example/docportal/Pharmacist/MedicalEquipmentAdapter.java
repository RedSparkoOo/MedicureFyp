package com.example.docportal.Pharmacist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MedicalEquipmentAdapter extends FirestoreRecyclerAdapter<MedicalEquipment, MedicalEquipmentAdapter.EquipmentListViewHolder> {
    private MedicineListAdapter.onItemLongClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MedicalEquipmentAdapter(@NonNull FirestoreRecyclerOptions<MedicalEquipment> options) {
        super(options);
    }

    @NonNull
    @Override
    public EquipmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_list_layout, parent, false);
        return new EquipmentListViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull EquipmentListViewHolder holder, int position, @NonNull MedicalEquipment model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.price.setText(model.getPrice());
        holder.quantity.setText(model.getQuantity());
        String imageUri;
        imageUri= model.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();


    }

    public class EquipmentListViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price, quantity;
        ImageView imageView;

        public EquipmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.etitle);
            description = itemView.findViewById(R.id.edescription);
            price = itemView.findViewById(R.id.ePrice_main);
            quantity = itemView.findViewById(R.id.eQuantity);
            imageView = itemView.findViewById(R.id.equip_picture);
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
}


