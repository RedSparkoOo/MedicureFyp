package com.example.docportal.Pharmacist;

import static com.example.docportal.R.layout.spinner_item;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.CheckEvent;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditEquipment extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CheckEvent checkEvent;
    EditText title, price, quantity, description;
    ImageView imageView;
    Button edit;
    Uri content_uri;
    String id ,Title , Image, Price, Quantity, Description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipment);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("Id");


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        edit = findViewById(R.id.Add);
        imageView = findViewById(R.id.equi_picture);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        TextView[] textViews = {title, description, price, quantity};
        checkEvent = new CheckEvent();
        DocumentReference documentReference = firebaseFirestore.collection("Medical_Equipment").document(id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Title = value.getString("Title");
                Image = value.getString("Image");
                Price = value.getString("Price");
                Quantity = value.getString("Quantity");
                Description = value.getString("Description");
                title.setText(Title);
                Picasso.get().load(Uri.parse(Image)).into(imageView);
                price.setText(Price);
                quantity.setText(Quantity);
                description.setText(Description);

            }


        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Title = title.getText().toString();
                Image = String.valueOf(content_uri);
                Price = price.getText().toString();
                Quantity = quantity.getText().toString();
                Description = description.getText().toString();
                HashMap<String, String> map = new HashMap<>();
                map.put("Title", Title);
                map.put("Image", Image);
                map.put("Price", Price);
                map.put("Quantity", Quantity);
                map.put("Description",Description);
                DocumentReference documentReference = firebaseFirestore.collection("Medical_Equipment").document(id);
                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditEquipment.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MedicalEquipmentList.class));
                    }
                });
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1000);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                content_uri = data.getData();
                imageView.setImageURI(content_uri);
            }
        }
    }

}