package com.example.InterEaseApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    private Button userManagementButton;
    private Button orderManagementButton;
    private Button reviewManagementButton; // Add reference to the review management button
    private Button logoutButton; // Add reference to the logout button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        userManagementButton = findViewById(R.id.user_management_button);
        orderManagementButton = findViewById(R.id.order_management_button);
        reviewManagementButton = findViewById(R.id.review_management_button); // Initialize the review management button
        logoutButton = findViewById(R.id.logout); // Initialize the logout button

        userManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open UserManagementActivity
                Intent intent = new Intent(AdminHomeActivity.this, UserManagementActivity.class);
                startActivity(intent);
            }
        });

        orderManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open OrderManagementActivity
                Intent intent = new Intent(AdminHomeActivity.this, OrderManagementActivity.class);
                startActivity(intent);
            }
        });

        reviewManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(AdminHomeActivity.this, ManageReviewsActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AdminHomeActivity.this, "Error opening Manage Reviews", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout the user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the LoginActivity
                Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }
}
