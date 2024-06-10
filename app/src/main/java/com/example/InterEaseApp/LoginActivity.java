package com.example.InterEaseApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, resetEmailText;
    private Button signInButton, signUpButton, resetPasswordBtn;
    private TextView forgetPasswordText;
    private FirebaseAuth mAuth;
    private boolean isResetVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.idText);
        passwordEditText = findViewById(R.id.pwText);
        signInButton = findViewById(R.id.signInBtn);
        signUpButton = findViewById(R.id.signUpBtn);
        forgetPasswordText = findViewById(R.id.forgetPasswordText);
        resetEmailText = findViewById(R.id.resetEmailText);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleResetFieldsVisibility();
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetLink();
            }
        });
    }

    private void signIn() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Check if email and password are not empty
        if (!email.isEmpty() && !password.isEmpty()) {
            // Check if the user is admin
            if (email.equals("admin@gmail.com") && password.equals("Admin123")) {
                // If the user is admin, navigate to AdminHomeActivity
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                // Finish the LoginActivity to prevent going back
                finish();
            } else {
                // If the user is not admin, proceed with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    // Finish the LoginActivity to prevent going back
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else {
            // Email or password is empty, show a toast message
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        // Start SignUpActivity when the "Sign up" button is clicked
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void toggleResetFieldsVisibility() {
        if (isResetVisible) {
            resetEmailText.setVisibility(View.GONE);
            resetPasswordBtn.setVisibility(View.GONE);
        } else {
            resetEmailText.setVisibility(View.VISIBLE);
            resetPasswordBtn.setVisibility(View.VISIBLE);
        }
        isResetVisible = !isResetVisible;
    }

    private void sendResetLink() {
        String email = resetEmailText.getText().toString().trim();
        if (email.isEmpty()) {
            resetEmailText.setError("Email is required");
            resetEmailText.requestFocus();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Unable to send reset link", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}


