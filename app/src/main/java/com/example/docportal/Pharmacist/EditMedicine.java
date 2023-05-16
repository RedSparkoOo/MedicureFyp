package com.example.docportal.Pharmacist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class
EditMedicine extends AppCompatActivity {

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CheckEvent checkEvent;
    EditText title, price, quantity, description;
    ImageView imageView;
    Spinner milligram;
    Button edit;
    String _milligram, image_uri;
    Uri content_uri;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    String id, Title, Image, Price, Quantity, Description;
    String[] Milligrams = {"10mg", "20mg", "25mg", "40mg"};
    Singleton singleton = new Singleton();
    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("Id");


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        edit = findViewById(R.id.Add);
        imageView = findViewById(R.id.equip_picture);
        milligram = findViewById(R.id.medicine_milligram);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        TextView[] textViews = {title, description, price, quantity};
        checkEvent = new CheckEvent();
        singleton.setAdatper(this, milligram, Milligrams);
        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medicine").document(id);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Title = value.getString("Title");

                Price = value.getString("Price");
                Quantity = value.getString("Quantity");
                Description = value.getString("Description");
                String Milligram = value.getString("Milligram");
                title.setText(Title);
                int i =0;
                for (String mg :Milligrams ) {

                    if (value.getString("Milligram").equals(mg)) {
                        milligram.setSelection(i);
                        break;
                    }

                    i++;
                }

                Image = value.getString("Image");
                image_uri = value.getString("Image");
                Picasso.get()
                        .load(Uri.parse(Image))
                        .resize(400, 300) // Adjust the desired image size
                        .onlyScaleDown() // Resize only if the image is larger than the target size
                        .into(imageView);
                price.setText(Price);
                quantity.setText(Quantity);
                description.setText(Description);

            }


        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkEvent.isEmpty(textViews) || !(checkEvent.checkItemName(title))) {
                        System.out.println("mango");
                    } else {
                        firestoreHandler = new FirestoreHandler();
                        if (image_uri != null) {
                            Drawable drawable = imageView.getDrawable();
                            if (drawable instanceof BitmapDrawable) {
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                content_uri = getImageUriFromBitmap(bitmap);


                        Title = title.getText().toString();
                        Image = String.valueOf(content_uri);
                        Price = price.getText().toString();
                        Quantity = quantity.getText().toString();
                        Description = description.getText().toString();

                        // Check if the title already exists


                                if (content_uri != null) {
                                    // Create a target width and height for the resized bitmap
                                    int targetWidth = 800;
                                    int targetHeight =800;

                                    // Load the bitmap from the content URI

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
                                                    med.put("Title", Title);
                                                    med.put("Image", Image);
                                                    med.put("Price", Price);
                                                    med.put("Quantity", Quantity);
                                                    med.put("Description", Description);
                                                    med.put("Milligram", milligram.getSelectedItem().toString());

                                                    DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medicine").document(id);
                                                    documentReference.set(med).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            singleton.showToast(EditMedicine.this, "Data updated successfully");
                                                            singleton.openActivity(EditMedicine.this, MedicineList.class);
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    singleton.showToast(EditMedicine.this, "Please select an image");
                                }
                            }
                        }
                    }


                } catch (Exception ex) {
                    singleton.showToast(EditMedicine.this, ex.getMessage());
                }
            }
        });
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                singleton = new Singleton();
//                Title = title.getText().toString();
//                Image = String.valueOf(content_uri);
//                Price = price.getText().toString();
//                Quantity = quantity.getText().toString();
//                Description = description.getText().toString();
//                HashMap<String, String> map = new HashMap<>();
//                map.put("Title", Title);
//                map.put("Image", Image);
//                map.put("Price", Price);
//                map.put("Quantity", Quantity);
//                map.put("Description", Description);
//                map.put("Milligram", milligram.getSelectedItem().toString());
//                DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Medicine").document(id);
//                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        singleton.showToast(EditMedicine.this, "Data updated successfully");
//                        singleton.openActivity(getApplicationContext(), MedicineList.class);
//                    }
//                });
//
//            }
//        });

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
                imageView.setImageBitmap(resizedBitmap);
            }
        }
    }


}