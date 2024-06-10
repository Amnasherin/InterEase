package com.example.InterEaseApp;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.InterEaseApp.MainActivity.cnt;
import static com.example.InterEaseApp.MainActivity.isObjectReplaced;
import static com.example.InterEaseApp.MainActivity.obj_file;
import static com.example.InterEaseApp.MainActivity.png_file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KitchenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KitchenFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageButton desk1;
    private ImageButton desk2;
    private ImageButton back_button_Kitchen;
    private ImageButton wishlist1;
    private ImageButton wishlist2;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public KitchenFragment() {
        // Required empty public constructor
    }

    public static KitchenFragment newInstance(String param1, String param2) {
        KitchenFragment fragment = new KitchenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_kitchen, container, false);

        back_button_Kitchen = v.findViewById(R.id.back_button3);
        back_button_Kitchen.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MainFragment());
            fragmentTransaction.commit();
        });

        desk1 = v.findViewById(R.id.desk1);
        desk1.setOnClickListener(view -> {
            if (cnt == 0) {
                obj_file = "models/table3.obj";
                png_file = "models/table_texture6.png";
                isObjectReplaced = true;
            }
        });

        desk2 = v.findViewById(R.id.desk2);
        desk2.setOnClickListener(view -> {
            if (cnt == 0) {
                obj_file = "models/desk2.obj";
                png_file = "models/table_texture5.png";
                isObjectReplaced = true;
            }
        });

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Button cart1 = v.findViewById(R.id.cart1);
        cart1.setOnClickListener(view -> addToCart("chair1", 12000));

        Button cart2 = v.findViewById(R.id.cart2);
        cart2.setOnClickListener(view -> addToCart("chair2", 20000));

        wishlist1 = v.findViewById(R.id.wishlist1);
        wishlist2 = v.findViewById(R.id.wishlist2);

        wishlist1.setOnClickListener(view -> toggleWishlist("chair1", wishlist1));
        wishlist2.setOnClickListener(view -> toggleWishlist("chair2", wishlist2));

        checkWishlistState("chair1", wishlist1);
        checkWishlistState("chair2", wishlist2);
        Button reviewButton1 = v.findViewById(R.id.review1);
        reviewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("desk1");
            }
        });

        Button reviewButton2 = v.findViewById(R.id.review2);
        reviewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("desk2");
            }
        });
        Button detailsButton1 = v.findViewById(R.id.details1);
        detailsButton1.setOnClickListener(view -> showDetailsDialog(R.drawable.desk1, "MTANK Solid Wood Writing Desk - Home Office Workbench Desk with Drawer, Laptop Computer Work Study Table for Adults Wooden with Storage Big Brand\tMTANK\n" +
                "Product Dimensions\t : 120D x 55W x 75H Centimeters\n" +
                "Colour\t : Natural Brown\n" +
                "Style\t : Modern\n" +
                "Base Material\t : Wood\n" +
                "Top Material Type\t : Sheesham Wood\n" +
                "Finish Type\t : Polished\n" +
                "Special Feature\t : Lightweight\n" +
                "Room Type\t : Living Room, Study Room"));

        Button detailsButton2 = v.findViewById(R.id.details2);
        detailsButton2.setOnClickListener(view -> showDetailsDialog(R.drawable.desk2, "Lukzer Modern Engineered Wood Computer Desk with Built-in Bookshelf, Space-Saving Design, Oak Brown   \n" +
                "Brand\t : Lukzer\n" +
                "Shape\t : Rectangular\n" +
                "Product Dimensions\t : 50D x 100W x 78H Centimeters\n" +
                "Colour\t : Oak Brown\n" +
                "Style\t : Modern\n" +
                "Base Material\t : Enginereed \n" +
                "Top Material Type\t : Engineered Wood\n" +
                "Finish Type\t : Wood\n" +
                "Special Feature\t : Ergonomic, Lightweight\n" +
                "Room Type\t : Bedroom, Study Room, Childrens Room , Nursery"  ));

        return v;
    }

    private void addToCart(String productName, int price) {
        if (currentUser != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseFirestore.getInstance().collection("/users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");

                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("product", productName);
                            cartItem.put("price", price);
                            cartItem.put("uid", uid);
                            cartItem.put("name", userName);

                            db.collection("/cart")
                                    .add(cartItem)
                                    .addOnSuccessListener(documentReference ->
                                            Toast.makeText(getContext(), "Item added to cart!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(getContext(), "Failed to add item to cart!", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Failed to retrieve user data!", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Please log in to add items to cart!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleWishlist(String productName, ImageButton wishlistButton) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("/wishlist").whereEqualTo("uid", uid).whereEqualTo("product", productName).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            queryDocumentSnapshots.getDocuments().get(0).getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        wishlistButton.setImageResource(R.drawable.ic_wishlist_unselected);
                                        Toast.makeText(getContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(getContext(), "Failed to remove from wishlist", Toast.LENGTH_SHORT).show());
                        } else {
                            Map<String, Object> wishlistItem = new HashMap<>();
                            wishlistItem.put("product", productName);
                            wishlistItem.put("uid", uid);

                            db.collection("/wishlist").add(wishlistItem)
                                    .addOnSuccessListener(documentReference -> {
                                        wishlistButton.setImageResource(R.drawable.ic_wishlist_selected);
                                        Toast.makeText(getContext(), "Added to wishlist", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(getContext(), "Failed to add to wishlist", Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Failed to check wishlist state", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Please log in to manage your wishlist!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkWishlistState(String productName, ImageButton wishlistButton) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("/wishlist").whereEqualTo("uid", uid).whereEqualTo("product", productName).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            wishlistButton.setImageResource(R.drawable.ic_wishlist_selected);
                        } else {
                            wishlistButton.setImageResource(R.drawable.ic_wishlist_unselected);
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Failed to check wishlist state", Toast.LENGTH_SHORT).show());
        } else {
            wishlistButton.setImageResource(R.drawable.ic_wishlist_unselected);
        }
    }
    private void showReviewDialog(String productName) {
        if (currentUser != null) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_review);
            dialog.setTitle("Product Reviews");

            RecyclerView reviewRecyclerView = dialog.findViewById(R.id.review_recycler_view);
            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            List<Review> reviewList = new ArrayList<>();
            ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList);
            reviewRecyclerView.setAdapter(reviewAdapter);

            EditText reviewInput = dialog.findViewById(R.id.review_input);
            Button submitReviewButton = dialog.findViewById(R.id.submit_review_button);

            // Load reviews
            loadReviews(productName, reviewList, reviewAdapter);

            // Handle review submission
            submitReviewButton.setOnClickListener(v -> {
                String reviewText = reviewInput.getText().toString().trim();
                if (!reviewText.isEmpty()) {
                    addReview(productName, reviewText, reviewList, reviewAdapter);
                    reviewInput.setText(""); // Clear input field
                } else {
                    Toast.makeText(getContext(), "Please enter a review", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        } else {
            Toast.makeText(getContext(), "Please log in to view reviews!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadReviews(String productName, List<Review> reviewList, ReviewAdapter reviewAdapter) {
        db.collection("/reviews").whereEqualTo("product", productName).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reviewList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Review review = document.toObject(Review.class);
                        reviewList.add(review);
                    }
                    reviewAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load reviews", Toast.LENGTH_SHORT).show());
    }


    private void addReview(String productName, String reviewText, List<Review> reviewList, ReviewAdapter reviewAdapter) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String username = currentUser.getDisplayName();
            Map<String, Object> review = new HashMap<>();
            review.put("product", productName);
            review.put("review", reviewText);
            review.put("uid", uid);
            review.put("username", username);

            db.collection("/reviews").add(review)
                    .addOnSuccessListener(documentReference -> {
                        Review newReview = new Review(productName, reviewText, uid, username);
                        reviewList.add(newReview);
                        reviewAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Review submitted!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to submit review", Toast.LENGTH_SHORT).show());
        }
    }private void showDetailsDialog(int imageResId, String description) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_details);

        ImageView detailImage = dialog.findViewById(R.id.detail_image);
        TextView detailDescription = dialog.findViewById(R.id.detail_description);

        detailImage.setImageResource(imageResId);
        detailDescription.setText(description);

        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}




