package com.example.docportal;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FirestoreHandler {
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final Singleton singleton = new Singleton();
    private DocumentReference documentReference;
    private String currentUserId;

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseFirestore getFirestoreInstance() {
        return fStore;
    }

    public String getCurrentUser() {
        if (firebaseAuth.getCurrentUser() != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
        return currentUserId;
    }

    public void deleteProfile(Context context) {
        getFirebaseUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    singleton.showToast(context, "Account deleted");
                    singleton.openActivity(context, mainstartScreen.class);
                }
            }
        });

    }

    public FirebaseUser getFirebaseUser() {

        if (firebaseAuth.getCurrentUser() != null)
            return firebaseAuth.getCurrentUser();
        return null;
    }

    public void firebaseLogin(Context context, Class activity, String email, String password, String path, ProgressBar progressBar) {


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // node45
                if (task.isSuccessful()) {
                    documentReference = fStore.collection(path).document(getCurrentUser());
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                            if (error != null)
                                singleton.showToast(context, error.getMessage());
                            else if (getFirebaseUser().isEmailVerified())
                                singleton.openActivity(context, activity);
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                singleton.showToast(context, "Please verify your email first!");
                            }

                        }


                    });

                }
                if (!task.isSuccessful()) {
                    singleton.showToast(context, "Invalid credentials");
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

        });
    }

    public void forgotPassword(View view) {
        EditText reset_mail = new EditText(view.getContext());
        AlertDialog.Builder reset_password_dialog = new AlertDialog.Builder(view.getContext());
        reset_password_dialog.setTitle("Reset Password?");
        reset_password_dialog.setMessage("Enter the mail to receive the reset link");
        reset_password_dialog.setView(reset_mail);
        reset_password_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Email = reset_mail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        singleton.showToast(view.getContext(), "Reset link sent to " + Email);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        singleton.showToast(view.getContext(), "Error! Link not sent " + Email);
                    }
                });
            }
        });
        reset_password_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        reset_password_dialog.create().show();
    }

}
