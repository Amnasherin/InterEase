package com.example.InterEaseApp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageReviewsActivity extends AppCompatActivity {

    private static final String TAG = "ManageReviewsActivity";

    private ListView reviewList;
    private FirebaseFirestore db;
    private List<String> reviewIds;
    private ArrayAdapter<String> adapter;
    private List<String> reviews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reviews);

        reviewList = findViewById(R.id.review_list);
        db = FirebaseFirestore.getInstance();
        reviewIds = new ArrayList<>();
        reviews = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reviews);
        reviewList.setAdapter(adapter);

        fetchReviews();

        reviewList.setOnItemClickListener((parent, view, position, id) -> showReviewOptionsDialog(position));
    }

    private void fetchReviews() {
        db.collection("/reviews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviews.clear();
                        reviewIds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String product = document.getString("product");
                            String review = document.getString("review");
                            if (product != null && review != null) {
                                reviews.add("Product: " + product + "\nReview: " + review);
                                reviewIds.add(document.getId());
                            } else {
                                Log.e(TAG, "Missing fields in document: " + document.getId());
                            }
                        }
                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Reviews fetched successfully.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Log.e(TAG, "Error fetching reviews: ", task.getException());
                        runOnUiThread(() -> Toast.makeText(this, "Error fetching reviews", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch reviews: ", e);
                    runOnUiThread(() -> Toast.makeText(this, "Failed to fetch reviews", Toast.LENGTH_SHORT).show());
                });
    }

    private void showReviewOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Review Options")
                .setItems(new String[]{"Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        deleteReview(position);
                    }
                })
                .show();
    }

    private void deleteReview(int position) {
        String reviewId = reviewIds.get(position);

        db.collection("/reviews").document(reviewId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviews.remove(position);
                        reviewIds.remove(position);
                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Review deleted", Toast.LENGTH_SHORT).show();
                        });
                        Log.d(TAG, "Review deleted successfully.");
                    } else {
                        Log.e(TAG, "Error deleting review: ", task.getException());
                        runOnUiThread(() -> Toast.makeText(this, "Error deleting review", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete review: ", e);
                    runOnUiThread(() -> Toast.makeText(this, "Failed to delete review", Toast.LENGTH_SHORT).show());
                });
    }
}
