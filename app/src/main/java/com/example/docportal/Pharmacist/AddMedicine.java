package com.example.docportal.Pharmacist;

import static com.example.docportal.R.layout.spinner_item;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AddMedicine extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText price;
    EditText quantity;
    Button add;
    ImageView medicinePic;
    Spinner medicineMilligrams;
    FirebaseFirestore fStore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Bitmap imagetoStore;
    CheckEvent checkEvent;
    String _title;
    String _description;
    String _price;
    String _milligram;
    String _quantity;
    String[] Milligrams = {"","10mg","20mg","25mg","40mg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        add = findViewById(R.id.Add);
        medicinePic = findViewById(R.id.medi_picture);
        medicineMilligrams = findViewById(R.id.medicine_milligram);
        fStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        TextView[] textViews = {title,description,price};
        checkEvent = new CheckEvent();

        ArrayAdapter arrayAdapterMilligrams = new ArrayAdapter(this, spinner_item, Milligrams);
        arrayAdapterMilligrams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicineMilligrams.setAdapter(arrayAdapterMilligrams);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkEvent.isEmpty(textViews) || !(checkEvent.checkItemName(title))) ;
                    else {
                        _title = title.getText().toString();
                        _description = title.getText().toString();
                        _price = price.getText().toString();
                        _quantity = quantity.getText().toString();
                        _milligram = medicineMilligrams.getSelectedItem().toString();
                        Map<String, String> med = new HashMap<>();
                        med.put("Image", String.valueOf(imagetoStore));
                        med.put("Title", _title);
                        med.put("Description", _description);
                        med.put("Quantity", _quantity);
                        med.put("Price", _price);
                        med.put("Milligram", _milligram);
                        FirebaseFirestore.getInstance().collection("Medicine").add(med).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(AddMedicine.this, "data entered successfully", Toast.LENGTH_SHORT).show();
                                
                            }
                        });
                    }
                }
                catch(Exception ex){
                    Toast.makeText(AddMedicine.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        });

        medicinePic.setOnClickListener(new View.OnClickListener() {
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
                Uri content_uri = data.getData();
                try {
                    imagetoStore = MediaStore.Images.Media.getBitmap(getContentResolver(),content_uri);
                    medicinePic.setImageBitmap(imagetoStore);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}