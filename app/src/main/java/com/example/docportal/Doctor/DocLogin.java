package com.example.docportal.Doctor;

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


public class DocLogin extends AppCompatActivity {
    Button login;
    TextView Register;
    EditText Email;
    EditText Password;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser FUser;
    TextView doctor_forget_password;
    String userId;
    ProgressBar progress_check;
    ImageView back_to_selection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_login);
        login = findViewById(R.id.doctorLogin);
        Register = findViewById(R.id.Registration);

        doctor_forget_password = (findViewById(R.id.doctorForgetPassword));
        Email =  findViewById(R.id.doctorEmail);
        Password = findViewById(R.id.doctorPassword);
        progress_check = findViewById(R.id.progress_check);
        TextView[] textViews ={Email, Password};
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        back_to_selection = findViewById(R.id.back_to_selection);
        FUser = mAuth.getCurrentUser();



        progress_check.setVisibility(View.INVISIBLE);


        back_to_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocLogin.this, Entrance.class);
                startActivity(intent);
            }
        });


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

                progress_check.setVisibility(View.VISIBLE);

                try {
                    if (new CheckEvent().isEmpty(textViews));
                    else {
                        mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { // node45
                                if (task.isSuccessful()) {
                                    userId = mAuth.getCurrentUser().getUid();

                                    //patient login check -----START-----

                                    DocumentReference documentReference = fStore.collection("Professions").document(userId);
                                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if(error != null){
                                                Toast.makeText(DocLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }


                                            else {

                                                String Category = value.getString("Profession");
                                                if (Category.equals("Doctor")) {

                                                    if (FUser.isEmailVerified()) {
                                                        Intent intent = new Intent(DocLogin.this, OptionsActivity.class);

                                                        startActivity(intent);
                                                    } else {
                                                        progress_check.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(DocLogin.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                                                    }

                                                } else if (Category.equals("Nurse")) {

                                                    if (FUser.isEmailVerified()) {
                                                        Intent intent = new Intent(DocLogin.this, OptionsActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        progress_check.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(DocLogin.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                                else {
                                                    Toast.makeText(DocLogin.this, "No User Exists", Toast.LENGTH_SHORT).show();
                                                    progress_check.setVisibility(View.INVISIBLE);
                                                }
                                            }



                                        }
                                    });


                                }
                                if(!task.isSuccessful()){
                                    Toast.makeText(DocLogin.this, "Insert correct email and password", Toast.LENGTH_SHORT).show();
                                    progress_check.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                }catch(Exception e){
                    Toast.makeText(DocLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
