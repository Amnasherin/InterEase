package com.example.InterEaseApp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {
    private static final String TAG = "ReviewFragment";
    private List<Review> reviewList;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Ensure reviewList is initialized
        reviewList = new ArrayList<>();

        // Initialize adapter with an empty list
        adapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);

        fetchReviews();

        return view;
    }

    private void fetchReviews() {
        FirebaseFirestore.getInstance().collection("/reviews")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviewList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Review review = document.toObject(Review.class);
                            reviewList.add(review);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error fetching reviews: ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to fetch reviews: ", e));
    }

    public void submitReview(String username, String content) {
        Review review = new Review(username, content);

        FirebaseFirestore.getInstance()
                .collection("/reviews")
                .add(review)
                .addOnSuccessListener(documentReference -> {
                    reviewList.add(review);
                    adapter.notifyItemInserted(reviewList.size() - 1);
                    Log.d(TAG, "Review added successfully.");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error adding review: ", e));
    }
}
