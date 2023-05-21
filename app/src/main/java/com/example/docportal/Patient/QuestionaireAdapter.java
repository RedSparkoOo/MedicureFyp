package com.example.docportal.Patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class QuestionaireAdapter extends FirestoreRecyclerAdapter<CheckBoxModel, QuestionaireAdapter.CheckBoxListViewHolder> {

    private OnCheckedChangeListener listener;

    public QuestionaireAdapter(@NonNull FirestoreRecyclerOptions<CheckBoxModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CheckBoxListViewHolder holder, int position, @NonNull CheckBoxModel model) {
        holder.symptom.setText(model.getSymptom());
        holder.radioGroup.clearCheck(); // Clear the previous selection
        if (model.getCheckedId() != -1) {
            holder.radioGroup.check(model.getCheckedId());
        }

        // Set the listener to update the model's checkedId field
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                model.setCheckedId(checkedId);
                RadioButton radioButton = group.findViewById(checkedId);
                boolean checked = radioButton != null && radioButton.isChecked();
                if (listener != null) {
                    int adapterPosition = holder.getAbsoluteAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onCheckedChanged(adapterPosition, checkedId, checked);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public CheckBoxListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questionaire, parent, false);
        return new CheckBoxListViewHolder(view);
    }

    public class CheckBoxListViewHolder extends RecyclerView.ViewHolder {
        TextView symptom;
        RadioButton low, mild, severe, dontknow;
        RadioGroup radioGroup;

        public CheckBoxListViewHolder(@NonNull View itemView) {
            super(itemView);
            symptom = itemView.findViewById(R.id.g_symptoms);
            low = itemView.findViewById(R.id.low);
            mild = itemView.findViewById(R.id.mild);
            severe = itemView.findViewById(R.id.severe);
            dontknow = itemView.findViewById(R.id.dont_know);
            radioGroup = itemView.findViewById(R.id.radio_Group);
            low.setOnCheckedChangeListener(radioButtonListener);
            mild.setOnCheckedChangeListener(radioButtonListener);
            severe.setOnCheckedChangeListener(radioButtonListener);
            dontknow.setOnCheckedChangeListener(radioButtonListener);
            radioGroup.setOnCheckedChangeListener(null); // Remove previous listener to avoid triggering unnecessary events

            // Set a new listener to handle radio button selection
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = group.findViewById(checkedId);
                    boolean checked = radioButton != null && radioButton.isChecked();
                    if (listener != null) {
                        int adapterPosition = getAbsoluteAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            listener.onCheckedChanged(adapterPosition, checkedId, checked);
                        }
                    }
                }
            });
        }
        private final CompoundButton.OnCheckedChangeListener radioButtonListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int checkedId = buttonView.getId();
                    if (listener != null) {
                        int adapterPosition = getAbsoluteAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            listener.onCheckedChanged(adapterPosition, checkedId, isChecked);
                        }
                    }
                }
            }
        };
    }
    public String getSymptomValue(int position) {
        CheckBoxModel model = getItem(position);
        if (model != null) {
            return model.getSymptom();
        }
        return null;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position, int checkedId, boolean checked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}