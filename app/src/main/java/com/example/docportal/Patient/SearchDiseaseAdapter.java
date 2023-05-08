package com.example.docportal.Patient;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SearchDiseaseAdapter extends FirestoreRecyclerAdapter<DiseaseModel, SearchDiseaseAdapter.SearchDiseaseViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SearchDiseaseAdapter(@NonNull FirestoreRecyclerOptions<DiseaseModel> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchDiseaseViewHolder holder, int position, @NonNull DiseaseModel model) {


            holder.name.setText(model.getDisease());
            holder.description.setText(model.getDescription());


    }


    @NonNull
    @Override
    public SearchDiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom_layout, parent, false);
        return new SearchDiseaseViewHolder(view);


    }

    public class  SearchDiseaseViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;


        public SearchDiseaseViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.disease_name);
            description = itemView.findViewById(R.id.symptom_description);


        }
    }


}
