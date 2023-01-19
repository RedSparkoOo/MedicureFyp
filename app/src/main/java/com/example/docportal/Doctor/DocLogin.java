package com.example.docportal.Doctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.CheckEvent;
import com.example.docportal.Pharmacist.PharmacistDashboard;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

public class DocLogin extends AppCompatActivity {
    Button login;
    TextView Register;
    EditText Email;
    EditText Password;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    TextView doctor_forget_password;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_login);
        login = findViewById(R.id.doctorLogin);
        Register = findViewById(R.id.Registration);
        doctor_forget_password = (findViewById(R.id.doctorForgetPassword));
        Email =  findViewById(R.id.doctorEmail);
        Password = findViewById(R.id.doctorPassword);
        TextView[] textViews ={Email, Password};
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        doctor_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText reset_mail = new EditText(view.getContext());
                AlertDialog.Builder reset_password_dialog = new AlertDialog.Builder(view .getContext());
                reset_password_dialog.setTitle("Reset Password?");
                reset_password_dialog.setMessage("Enter the mail to receive the reset link");
                reset_password_dialog.setView(reset_mail);
                reset_password_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Email = reset_mail.getText().toString();
                        mAuth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DocLogin.this, "Reset link sent to "+Email, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DocLogin.this, "Error! Link not sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                reset_password_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){}
                });
                reset_password_dialog.create().show();
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocLogin.this, Registeration.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new CheckEvent().isEmpty(textViews));
                    else {
                        mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { // node45
                                if (task.isSuccessful()) {
                                    userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("Doctor").document(userId);
                                    documentReference.addSnapshotListener(DocLogin.this, new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            String category = value.getString("Specialization");
                                            if (category.equals("Pharmacist")) startActivity(new Intent(getApplicationContext(), PharmacistDashboard.class));
                                            else startActivity(new Intent(getApplicationContext(), OptionsActivity.class));
                                        }
                                    });
                                }
                            }
                        });
                    }
                }catch(Exception e){
                    Toast.makeText(DocLogin.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
