package com.example.docportal.Pharmacist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddMedicalEquipment extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText price;
    EditText quantity;
    Button add;
    ImageView equipmentPic;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CheckEvent checkEvent;
    String _title;
    FirestoreHandler firestoreHandler;
    String _description;
    String _price;
    Singleton singleton = new Singleton();
    String _quantity;
    Uri content_uri;
    ImageView back_to_pharmacist_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_equipment);
        equipmentPic = findViewById(R.id.equi_picture);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        add = findViewById(R.id.Add);
        back_to_pharmacist_dashboard = findViewById(R.id.back_to_pharmacist_dashboard);

        back_to_pharmacist_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedicalEquipment.this,PharmacistDashboard.class);
                startActivity(intent);
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        TextView[] textViews = {title, description, price, quantity};
        checkEvent = new CheckEvent();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkEvent.isEmpty(textViews) || !(checkEvent.checkItemName(title))) ;
                    else {
                        firestoreHandler = new FirestoreHandler();
                        _title = title.getText().toString();
                        _description = title.getText().toString();
                        _price = price.getText().toString();
                        _quantity = quantity.getText().toString();
                        StorageReference filepath = firebaseStorage.getReference().child("equipmentImage").child(content_uri.getLastPathSegment());
                        filepath.putFile(content_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Map<String, String> med = new HashMap<>();
                                        med.put("Image", task.getResult().toString());
                                        med.put("Title", _title);
                                        med.put("Description", _description);
                                        med.put("Quantity", _quantity);
                                        med.put("Price", _price);
                                        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medical_Equipment").document();
                                        documentReference.set(med).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                singleton.showToast(AddMedicalEquipment.this, "Data inserted successfully");

                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                } catch (Exception ex) {
                    singleton.showToast(AddMedicalEquipment.this, ex.getMessage());

                }


            }

        });

        equipmentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                content_uri = data.getData();
                equipmentPic.setImageURI(content_uri);
            }
        }
    }
}


