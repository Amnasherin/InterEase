package com.example.InterEaseApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.widget.Button;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private EditText addressEditText;
    private ImageButton backButton;
    private Button editButton;
    private Button saveButton;
    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        nameTextView = view.findViewById(R.id.name_text_view);
        emailTextView = view.findViewById(R.id.email_text_view);
        addressEditText = view.findViewById(R.id.address_edit_text);
        backButton = view.findViewById(R.id.back_button8);
        editButton = view.findViewById(R.id.edit_button);
        saveButton = view.findViewById(R.id.save_button);

        // Set click listeners
        backButton.setOnClickListener(v -> goBackToSidemenu());
        editButton.setOnClickListener(v -> enableEditing());
        saveButton.setOnClickListener(v -> saveAddress());

        // Fetch and display user details
        fetchAndDisplayUserDetails();

        return view;
    }

    private void fetchAndDisplayUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore.getInstance().collection("/users").document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String address = document.getString("address");

                                nameTextView.setText("Name: " + name);
                                emailTextView.setText("Email: " + email);
                                addressEditText.setText(address);
                                // Disable editing initially
                                addressEditText.setEnabled(false);
                            }
                        } else {
                            // Handle the error
                        }
                    });
        }
    }

    private void goBackToSidemenu() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, new SidemenuFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void enableEditing() {
        isEditing = true;
        addressEditText.setEnabled(true);
        addressEditText.requestFocus(); // Set focus to the EditText
    }

    private void saveAddress() {
        if (isEditing) {
            isEditing = false;
            addressEditText.setEnabled(false); // Disable editing after saving
            String newAddress = addressEditText.getText().toString().trim();
            // Update the address in Firestore
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String uid = currentUser.getUid();
                FirebaseFirestore.getInstance().collection("/users").document(uid)
                        .update("address", newAddress)
                        .addOnSuccessListener(aVoid -> {
                            // Address updated successfully
                            Toast.makeText(getContext(), "Address updated successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Handle the error
                            Toast.makeText(getContext(), "Failed to update address", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }
}
