package com.example.smart_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Email_register extends AppCompatActivity {

    //Google
    private FirebaseAuth mAuth;

    //TextEdit
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText cpassword;

    //Button
    private Button registerBtn;
    private Button GTLBtn;

    //Firebase firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        mAuth = FirebaseAuth.getInstance();

        //initialize
        init();
    }

    private void init()
    {
        email = findViewById(R.id.ETEmail);
        name = findViewById(R.id.ETName);
        password = findViewById(R.id.ETPass);
        cpassword = findViewById(R.id.ETCPass);

        //register
        registerBtn = findViewById(R.id.btnReg);
        registerBtn.setOnClickListener(view -> {
            registerWithEmail();
        });

        //go to login
        GTLBtn = findViewById(R.id.btnGTL);
        GTLBtn.setOnClickListener(view -> {
            goToLogin();
        });
    }

    private void registerWithEmail()
    {
        //validate input
        if(email.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Email Field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(name.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Name Field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Password Field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(cpassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Confirm Password Field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email.getText().toString() , password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser user = mAuth.getCurrentUser();
                                saveDataFirestore(user);
                            }
                            else
                            {
                                Toast.makeText(Email_register.this, "Registration Failed, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void saveDataFirestore(FirebaseUser fbuser)
    {
        //save data in Map format
        Map<String, Object> user = new HashMap<>();
        user.put("Email", email.getText().toString());
        user.put("Name", name.getText().toString());
        user.put("createdAt", FieldValue.serverTimestamp());

        userInfo.UID = fbuser.getUid();
        userInfo.email = email.getText().toString();
        userInfo.name = name.getText().toString();

        //send data to firestore
        db.collection("users").document(fbuser.getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Email_register.this, "Successfully Register.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Email_register.this, ProfileActivity.class);
                            startActivity(intent);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Email_register.this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToLogin()
    {
        Intent intent = new Intent(Email_register.this, MainActivity.class);
        startActivity(intent);
    }
}