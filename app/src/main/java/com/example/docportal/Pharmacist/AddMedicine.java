package com.example.docportal.Pharmacist;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    Singleton singleton = new Singleton();
    FirebaseStorage firebaseStorage;
    FirestoreHandler firestoreHandler;
    StorageReference storageReference;
    CheckEvent checkEvent;
    String _title;
    String _description;

    String _price;
    String _milligram;
    String _quantity;
    Uri content_uri;

    String[] Milligrams = {"", "10mg", "20mg", "25mg", "40mg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        title = findViewById(R.id.title);
        medicineMilligrams = findViewById(R.id.medicine_milligram);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        add = findViewById(R.id.Add);
//        int targetWidth = 800; // Set your desired width
//        int targetHeight = 600; // Set your desired height
//        Bitmap resizedBitmap = null;
//        if (content_uri != null) {
//            try {
//                // Load the bitmap from the content URI
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), content_uri);
//                // Resize the bitmap
//                resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle the error loading the bitmap
//                singleton.showToast(AddMedicine.this, "Error loading image");
//            }
//        } else {
//            // Handle the case when content_uri is null
//            singleton.showToast(AddMedicine.this, "Image not provided");
//        }

        singleton.setAdatper(AddMedicine.this, medicineMilligrams, Milligrams);
        medicineMilligrams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _milligram = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        medicinePic = findViewById(R.id.equip_picture);


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        TextView[] textViews = {title, description, price, quantity};
        checkEvent = new CheckEvent();

        add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       try {
                                           if (checkEvent.isEmpty(textViews) || !(checkEvent.checkItemName(title))) {
                                               System.out.println("mango");
                                           } else {
                                               firestoreHandler = new FirestoreHandler();

                                               _title = title.getText().toString();
                                               _description = title.getText().toString();
                                               _price = price.getText().toString();
                                               _quantity = quantity.getText().toString();

                                               // Check if the title already exists
                                               firestoreHandler.getFirestoreInstance()
                                                       .collection("Medicine")
                                                       .whereEqualTo("Title", _title)
                                                       .get()
                                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                               if (task.isSuccessful()) {
                                                                   if (!task.getResult().isEmpty()) {
                                                                       // Title already exists
                                                                       singleton.showToast(AddMedicine.this, "Item already exists");
                                                                   } else {
                                                                       if (content_uri != null) {
                                                                           // Create a target width and height for the resized bitmap
                                                                           int targetWidth = 400;
                                                                           int targetHeight = 300;

                                                                           // Load the bitmap from the content URI
                                                                           Bitmap bitmap = null;
                                                                           try {
                                                                               bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), content_uri);
                                                                               // Resize the bitmap
                                                                               Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                                                                               // Convert the resized bitmap to a byte array
                                                                               ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                                               resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                                               byte[] imageData = baos.toByteArray();

                                                                               // Upload the resized image to Firebase Storage
                                                                               StorageReference filepath = firebaseStorage.getReference().child("medicineImage").child(content_uri.getLastPathSegment());
                                                                               UploadTask uploadTask = filepath.putBytes(imageData);

                                                                               // Add onSuccessListener for the upload task
                                                                               uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                   @Override
                                                                                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                       StorageReference imageRef = firebaseStorage.getReference().child("medicineImage").child(content_uri.getLastPathSegment());
                                                                                       imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                           @Override
                                                                                           public void onSuccess(Uri uri) {
                                                                                               Map<String, String> med = new HashMap<>();
                                                                                               med.put("Id", firestoreHandler.getCurrentUser());
                                                                                               med.put("Image", uri.toString());
                                                                                               med.put("Title", _title);
                                                                                               med.put("Description", _description);
                                                                                               med.put("Quantity", _quantity);
                                                                                               med.put("Price", _price);
                                                                                               med.put("Milligram", _milligram);

                                                                                               DocumentReference documentReference = firestoreHandler.getFirestoreInstance()
                                                                                                       .collection("Medicine")
                                                                                                       .document();
                                                                                               documentReference.set(med)
                                                                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                           @Override
                                                                                                           public void onSuccess(Void unused) {
                                                                                                               singleton.showToast(AddMedicine.this, "Data inserted successfully");
                                                                                                           }
                                                                                                       });
                                                                                           }
                                                                                       });
                                                                                   }
                                                                               });
                                                                           } catch (IOException e) {
                                                                               e.printStackTrace();
                                                                               // Handle the error loading the bitmap
                                                                               singleton.showToast(AddMedicine.this, "Error loading image");
                                                                           }


                                                                       }
                                                                   }
                                                               }
                                                           }
                                                       });
                                           }
                                       } catch (Exception ex) {
                                           singleton.showToast(AddMedicine.this, ex.getMessage());
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

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                content_uri = data.getData();
                int targetWidth = 400;
                int targetHeight = 300;

                // Load the bitmap from the content URI
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), content_uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Resize the bitmap
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                // Convert the resized bitmap to a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();
                medicinePic.setImageBitmap(resizedBitmap);


            }
        }
    }
}