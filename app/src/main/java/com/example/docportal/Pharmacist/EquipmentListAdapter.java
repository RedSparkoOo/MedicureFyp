package com.example.docportal.Pharmacist;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EquipmentListAdapter extends FirestoreRecyclerAdapter<MedicalEquipment, EquipmentListAdapter.EquipmentListViewHolder> {
    private MedicineListAdapter.onItemLongClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EquipmentListAdapter(@NonNull FirestoreRecyclerOptions<MedicalEquipment> options) {
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
        if (model.getQuantity().equals("0"))
            holder.quantity.setText("Out of stock");
        else {
            holder.quantity.setText(model.getQuantity());
            holder.quantity.setTextColor(Color.parseColor("#00FF00"));
        }
        String imageUri;
        imageUri = model.getImage();
        Picasso.get()
                .load(model.getImage())
                .resize(800, 800)
                .onlyScaleDown()
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Called when the image is successfully loaded
                        // Get the loaded bitmap from the ImageView
                        Bitmap bitmap = null;
                        Drawable drawable = holder.imageView.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                        }

                        // Check if the bitmap is null or has been recycled
                        if (bitmap != null && !bitmap.isRecycled()) {
                            // Check if the bitmap size exceeds the maximum allowed size
                            int maxSize = 1920 * 1080; // Adjust the maximum size according to your needs
                            int bitmapSize = bitmap.getByteCount();
                            if (bitmapSize > maxSize) {
                                // Calculate the scale factor to resize the bitmap
                                float scaleFactor = (float) Math.sqrt(maxSize / (float) bitmapSize);

                                // Create the scaled-down bitmap
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                                        Math.round(bitmap.getWidth() * scaleFactor),
                                        Math.round(bitmap.getHeight() * scaleFactor),
                                        false);

                                // Set the scaled-down bitmap to the ImageView
                                holder.imageView.setImageBitmap(scaledBitmap);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle any errors that occurred during image loading
                    }
                });
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();


    }

    public void setOnItemLongClickListener(MedicineListAdapter.onItemLongClickListener listener) {
        this.listener = listener;
    }

    public interface onItemLongClickListener {
        void onitemlongClick(DocumentSnapshot documentSnapshot, int position);
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
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onitemlongClick(getSnapshots().getSnapshot(position), position);

                    }
                    return false;
                }
            });
        }

    }
}


