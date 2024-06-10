package com.example.InterEaseApp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminReviewAdapter extends RecyclerView.Adapter<AdminReviewAdapter.AdminReviewViewHolder> {
    private List<Review> reviewList;

    public AdminReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }


    // Constructor and other methods

    @NonNull
    @Override
    public AdminReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single review item in admin view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_review, parent, false);
        return new AdminReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        // Bind review data to views in ViewHolder
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    // ViewHolder class to hold references to views in each review item in admin view
    public static class AdminReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView contentTextView;
        private TextView dateTextView;
        private ImageButton deleteButton;

        public AdminReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            nameTextView = itemView.findViewById(R.id.admin_review_name);
            contentTextView = itemView.findViewById(R.id.admin_review_content);
            deleteButton = itemView.findViewById(R.id.admin_delete_button);
        }

        public void bind(Review review) {
            // Bind review data to views
            nameTextView.setText(review.getUsername());
            contentTextView.setText(review.getReview());

            // Set onClickListener for delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle review deletion
                }
            });
        }
    }
}

