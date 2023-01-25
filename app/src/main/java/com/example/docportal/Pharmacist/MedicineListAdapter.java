package com.example.docportal.Pharmacist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MedicineListAdapter extends FirestoreRecyclerAdapter<Medicine, MedicineListAdapter.MedicineListViewHolder> {

private onItemLongClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MedicineListAdapter(@NonNull FirestoreRecyclerOptions<Medicine> options) {
        super(options);

    }

    @NonNull
    @Override
    public MedicineListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list_layout,parent,false);
        return new MedicineListViewHolder(view);
    }
    @Override
    protected void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position, @NonNull Medicine model) {

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.price_main.setText(model.getPrice());
        holder.milligram.setText(model.getMilligram());
        holder.quantity.setText(model.getQunatity());
        String imageUri;
        imageUri= model.getImage();
        Picasso.get().load(imageUri).into(holder.image);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();


    }


    public class MedicineListViewHolder extends RecyclerView.ViewHolder  {
        TextView title, description, price_main, quantity, milligram;
        ImageView image;
        public MedicineListViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.medi_picture);
            title = itemView.findViewById(R.id._title);
            description = itemView.findViewById(R.id._description);
            quantity = itemView.findViewById(R.id._Quantity);
            milligram = itemView.findViewById(R.id._milligram);
            price_main = itemView.findViewById(R.id._Price_main);
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
    public void setOnItemLongClickListener(onItemLongClickListener listener){
        this.listener = listener;
    }

}