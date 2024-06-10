package com.example.InterEaseApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, nameEditText, addressEditText, passwordEditText, passwordCheckEditText;
    private Button signUpButton, cancelButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.idText);
        nameEditText = findViewById(R.id.signUpName);
        addressEditText = findViewById(R.id.address);
        passwordEditText = findViewById(R.id.pwText);
        passwordCheckEditText = findViewById(R.id.signUpPwCheck);
        signUpButton = findViewById(R.id.register);
        cancelButton = findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the SignUpActivity
            }
        });
    }

    private void signUp() {
        String email = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordCheck = passwordCheckEditText.getText().toString().trim();

        // Check if any field is empty
        if (email.isEmpty() || name.isEmpty() || address.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(passwordCheck)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, save user details to Firestore
                            saveUserToFirestore(name, email, address);
                            Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            // Optionally, navigate to the login page or other activity
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToFirestore(String name, String email, String address) {

        String userId = mAuth.getCurrentUser().getUid();

        // Create a User object with the captured details
        Map<String, Object> user = new HashMap<>();
        user.put("uid", userId);
        user.put("name", name);
        user.put("email", email);
        user.put("address", address);

        // Get the current user's UID

        // Add user details to the "users" collection
        db.collection("/users").document(userId).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User details saved successfully to Firestore
                            Toast.makeText(SignUpActivity.this, "User details saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error occurred while saving user details to Firestore
                            Toast.makeText(SignUpActivity.this, "Error saving user details to Firestore: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
