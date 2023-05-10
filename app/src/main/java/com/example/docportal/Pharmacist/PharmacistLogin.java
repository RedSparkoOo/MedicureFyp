package com.example.docportal.Pharmacist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.Entrance;
import com.example.docportal.R;
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

public class PharmacistLogin extends AppCompatActivity {

    Button login;
    TextView Register;
    EditText Email;
    EditText Password;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser FUser;

    TextView pharmacist_forget_password;
    String userId;
    ProgressBar progress_check;
    ImageView back_to_selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        login = findViewById(R.id.doctorLogin);
        Register = findViewById(R.id.Registration);
        pharmacist_forget_password = (findViewById(R.id.doctorForgetPassword));
        Email =  findViewById(R.id.doctorEmail);
        Password = findViewById(R.id.doctorPassword);
        progress_check = findViewById(R.id.progress_check);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        back_to_selection = findViewById(R.id.back_to_selection);
        mAuth  = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            FUser = mAuth.getCurrentUser();
        }


        progress_check.setVisibility(View.INVISIBLE);

        back_to_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistLogin.this, Entrance.class);
                startActivity(intent);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PharmacistLogin.this, PharmacistRegistration.class));
            }
        });

        Login();
        forgetPassword();

    }

    private void Login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress_check.setVisibility(View.VISIBLE);
                TextView[] textViews ={Email, Password};
                try {
                    if (new CheckEvent().isEmpty(textViews));
                    else {
                        mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { // node45
                                if (task.isSuccessful()) {
                                    userId = mAuth.getCurrentUser().getUid();

                                    DocumentReference documentReference = fStore.collection("Professions").document(userId);
                                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if (error != null) {
                                                Toast.makeText(PharmacistLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            else if (FUser.isEmailVerified())
                                            {
                                                        Intent intent = new Intent(PharmacistLogin.this, PharmacistDashboard.class);
                                                        startActivity(intent);
                                                    } else {
                                                        progress_check.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(PharmacistLogin.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                                                    }

                                                }


                                        });

                                    }

                                }

                        });
                    }
                }catch(Exception e){
                    Toast.makeText(PharmacistLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void forgetPassword() {

        pharmacist_forget_password.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(PharmacistLogin.this, "Reset link sent to "+Email, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PharmacistLogin.this, "Error! Link not sent "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


    }


}
