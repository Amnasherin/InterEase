package com.example.InterEaseApp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminReviewActivity extends AppCompatActivity {
    private List<Review> reviewList;
    private RecyclerView recyclerView;
    private AdminReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_review);

        recyclerView = findViewById(R.id.admin_recycler_view);
        // Initialize RecyclerView, adapter, and layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);

        // Code to fetch all reviews from the database and populate reviewList
    }

    // Code to handle deleting a review
    private void deleteReview(int position) {
        // Remove the review from the database
        // Remove the review from reviewList
        // Notify the adapter of the change
    }
}

