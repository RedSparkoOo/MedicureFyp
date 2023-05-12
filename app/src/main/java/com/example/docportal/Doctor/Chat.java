package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Patient.WrapContentLinearLayoutManager;
import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Chat extends AppCompatActivity {

    String recieverid;
    String senderRoom, recieverRoom;

    MessageAdapter messageAdapter;
    RecyclerView message;
    Button send;
    EditText sendMessage;


    FirestoreHandler firestoreHandler = new FirestoreHandler();

    String names;


    DocumentReference documentReferenceSender;
    DocumentReference documentReferenceReciever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        Bundle bundle = getIntent().getBundleExtra("mBundle");
        names = bundle.getString("names");
        System.out.println("ID" + recieverid + "/n" + "names" + names);

        senderRoom = firestoreHandler.getCurrentUser() + recieverid;
        recieverRoom = recieverid + firestoreHandler.getCurrentUser();


        documentReferenceSender = firestoreHandler.getFirestoreInstance().collection("Chat").document(senderRoom);
        documentReferenceReciever = firestoreHandler.getFirestoreInstance().collection("Chat").document(recieverRoom);

        documentReferenceSender = firestoreHandler.getFirestoreInstance().collection("Chat").document(senderRoom);
        documentReferenceReciever = firestoreHandler.getFirestoreInstance().collection("Chat").document(recieverRoom);


        documentReferenceSender.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Error listening to sender's document: ", error);
                    return;
                }


                firestoreHandler.getFirestoreInstance().collection("Chat")
                        .document(senderRoom)
                        .collection(recieverRoom)
                        .orderBy("time", Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                                if (value != null) {
                                    // Clear the existing data
                                    messageAdapter.clear();

                                    for (QueryDocumentSnapshot document : value) {
                                        MessageModel messageModel = document.toObject(MessageModel.class);
                                        messageAdapter.add(messageModel);
                                    }

                                    messageAdapter.notifyDataSetChanged();
                                    // Scroll to the last message
                                    message.scrollToPosition(messageAdapter.getItemCount() - 1);
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


        documentReferenceSender = firestoreHandler.getFirestoreInstance().collection("Chat").document(senderRoom).collection(recieverRoom).document(messageId);
        documentReferenceSender.set(messageModel);


        documentReferenceReciever = firestoreHandler.getFirestoreInstance().collection("Chat").document(recieverRoom).collection(senderRoom).document(messageId);
        documentReferenceReciever.set(messageModel);


    }

}
