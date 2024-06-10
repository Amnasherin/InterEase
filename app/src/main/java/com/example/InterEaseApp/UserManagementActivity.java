package com.example.InterEaseApp;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private ListView listView;
    private FirebaseFirestore db;
    private List<User> userList;
    private ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ListView and adapter
        listView = findViewById(R.id.user_list);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        // Set ListView adapter
        listView.setAdapter(adapter);

        // Fetch and display user list
        fetchUsers();
    }

    private void fetchUsers() {
        db.collection("/users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert document snapshot to User object and add to user list
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                    // Notify adapter that data set has changed
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }
}
