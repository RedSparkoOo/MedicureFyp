package com.example.docportal.Patient;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DetailAdapter extends FirestoreRecyclerAdapter<CheckBoxModel, DetailAdapter.CheckBoxListViewHolder> {

    private DetailListener detailListener;

    public DetailAdapter(@NonNull FirestoreRecyclerOptions<CheckBoxModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckBoxListViewHolder holder, int position, @NonNull CheckBoxModel model) {
        if (model.getSymptom() != null) {
            holder.header.setText(model.getSymptom() + " Details");
        }
    }

    @NonNull
    @Override
    public CheckBoxListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom_detail, parent, false);
        return new CheckBoxListViewHolder(view);
    }

    public void setDetailListener(DetailListener listener) {
        this.detailListener = listener;
    }

    public class CheckBoxListViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        EditText detail;

        public CheckBoxListViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.symptom_header);
            detail = itemView.findViewById(R.id.symptom_detail);


            detail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String detailText = s.toString();
                    System.out.println(detailText);
                    int itemPosition = getAdapterPosition();
                    if (detailListener != null && itemPosition != RecyclerView.NO_POSITION) {
                        String headerText = header.getText().toString();
                        detailListener.onDetailFetched(headerText, detailText, itemPosition);
                    }
                }
            });
        }
    }

    public interface DetailListener {
        void onDetailFetched(String header, String detail, int position);
    }
}
