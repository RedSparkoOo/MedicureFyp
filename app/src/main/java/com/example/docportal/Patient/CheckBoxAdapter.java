package com.example.docportal.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxAdapter extends FirestoreRecyclerAdapter<CheckBoxModel, CheckBoxAdapter.CheckBoxListViewHolder> {


    private OnCheckedChangeListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CheckBoxAdapter(@NonNull FirestoreRecyclerOptions<CheckBoxModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckBoxListViewHolder holder, int position, @NonNull CheckBoxModel model) {
        if (model.getSymptom() != null) {
            holder.checkBox.setText(model.getSymptom());


            // Set the checkbox value based on the list position


            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onCheckedChanged(isChecked, position);
                }
            });
        }
    }


    @NonNull
    @Override
    public CheckBoxListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_box, parent, false);
        return new CheckBoxListViewHolder(view);
    }

    public class CheckBoxListViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public CheckBoxListViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (listener != null) {

                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {
                            listener.onCheckedChanged(isChecked, position);


                        }// Use getBindingAdapterPosition() instead

                    }
                }
            });

        }


    }







    public interface OnCheckedChangeListener {
        void onCheckedChanged(Boolean isChecked, int position);
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}
