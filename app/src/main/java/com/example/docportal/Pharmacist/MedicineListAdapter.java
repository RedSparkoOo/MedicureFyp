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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list_layout, parent, false);
        return new MedicineListViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicineListViewHolder holder, int position, @NonNull Medicine model) {

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.price_main.setText(model.getPrice());
        holder.milligram.setText(model.getMilligram());
        Picasso.get()
                .load(model.getImage())
                .resize(400, 300)
                .onlyScaleDown()
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Called when the image is successfully loaded
                        // Get the loaded bitmap from the ImageView
                        Bitmap bitmap = null;
                        Drawable drawable = holder.image.getDrawable();
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
                                holder.image.setImageBitmap(scaledBitmap);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle any errors that occurred during image loading
                    }
                });
        if (model.getQuantity().equals("0"))
            holder.quantity.setText("Out of stock");
        else {
            holder.quantity.setText(model.getQuantity());
            holder.quantity.setTextColor(Color.parseColor("#00FF00"));
        }
        String imageUri;
        imageUri = model.getImage();

    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();


    }

    public void setOnItemLongClickListener(onItemLongClickListener listener) {
        this.listener = listener;
    }


    public interface onItemClickListener {

        void onItemClick(DocumentSnapshot snapshot, int position);
    }

    public interface onItemLongClickListener {
        void onitemlongClick(DocumentSnapshot documentSnapshot, int position);
    }

    public class MedicineListViewHolder extends RecyclerView.ViewHolder {
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
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onitemlongClick(getSnapshots().getSnapshot(position), position);

                    }
                    return false;
                }
            });
        }

    }

}