package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.docportal.Patient.BuyMedicalAdapter;
import com.example.docportal.Patient.BuyMedicine;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.R;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Chat extends AppCompatActivity {

    String recieverid;
    StorageReference storageReference;
    String senderRoom, recieverRoom;
    FirebaseFirestore firestore;
    MessageAdapter messageAdapter;
    RecyclerView message;
    Button send;
    EditText sendMessage;


    FirebaseAuth firebaseAuth;
    Object currentUserId;
    String names;


    DocumentReference documentReferenceSender;
    DocumentReference documentReferenceReciever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        sendMessage = findViewById(R.id.edit_gchat_message);
        messageAdapter = new MessageAdapter(this);
        message = findViewById(R.id.recycler_gchat);

        send = findViewById(R.id.button_gchat_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = sendMessage.getText().toString();
                if (message.trim().length() > 0) {
                    sendMessage(message);
                    sendMessage.setText("");

                }
            }
        });

        message.setAdapter(messageAdapter);
        message.setLayoutManager(new WrapContentLinearLayoutManager(Chat.this, LinearLayoutManager.VERTICAL, false));
        recieverid = getIntent().getStringExtra("ID");
        names = getIntent().getStringExtra("name");
        System.out.println(names);


        System.out.println(recieverid);

        Object currentUser = firebaseAuth.getCurrentUser();
        senderRoom = firebaseAuth.getUid() + recieverid;
        recieverRoom = recieverid + firebaseAuth.getUid();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }

        documentReferenceSender = firestore.collection("Chat").document(senderRoom);
        documentReferenceReciever = firestore.collection("Chat").document(recieverRoom);


        documentReferenceSender.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {



                firestore.collection("Chat").document(senderRoom).collection(recieverRoom).orderBy("time", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("bang bang");
                                    for (QueryDocumentSnapshot document : task.getResult()) {   // LOOP
                                        MessageModel messageModel = document.toObject(MessageModel.class);
                                        System.out.println(messageModel);
                                        messageAdapter.add(messageModel);

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }


                        });




            }
        });


    }

    private void sendMessage(String messages) {
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(currentTime);

        String messageId = UUID.randomUUID().toString();
        Date currentDate = new Date();

// Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd");

// Format the date and month
        String formattedDate = dateFormat.format(currentDate);


        MessageModel messageModel = new MessageModel(messageId, FirebaseAuth.getInstance().getUid(), messages, formattedTime, formattedDate, names);
        messageAdapter.add(messageModel);


        documentReferenceSender = firestore. collection("Chat").document(senderRoom).  collection(recieverRoom).document(messageId) ;
        documentReferenceSender.set(messageModel);

        documentReferenceReciever= firestore.collection("Chat").document(recieverRoom).collection(senderRoom).document(messageId);
        documentReferenceReciever.set(messageModel);


    }
}
