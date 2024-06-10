package com.example.InterEaseApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private FirebaseFirestore db;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bindReview(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView reviewText;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            reviewText = itemView.findViewById(R.id.review_content);
        }

        public void bindReview(Review review) {
            // Get the username from the users database using the UID associated with the review
            db.collection("/users")
                    .document(review.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("name");
                            name.setText(username);  // Set the username
                        }
                    });

            reviewText.setText(review.getReview());
        }
    }
}
