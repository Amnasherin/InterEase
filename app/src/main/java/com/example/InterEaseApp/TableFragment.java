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

import static com.example.InterEaseApp.MainActivity.cnt;
import static com.example.InterEaseApp.MainActivity.isObjectReplaced;
import static com.example.InterEaseApp.MainActivity.obj_file;
import static com.example.InterEaseApp.MainActivity.png_file;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageButton table1;
    private ImageButton table2;
    private ImageButton table3;
    private ImageButton table4;
    private ImageButton table5;

    private ImageButton wishlist1;
    private ImageButton wishlist2;
    private ImageButton wishlist3;
    private ImageButton wishlist4;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private ImageButton back_button_Table;

    public TableFragment() {
        // Required empty public constructor
    }

    public static TableFragment newInstance(String param1, String param2) {
        TableFragment fragment = new TableFragment();
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
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_table, container, false);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        back_button_Table = v.findViewById(R.id.back_button6);
        back_button_Table.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MainFragment());
            fragmentTransaction.commit();
        });

        table1 = v.findViewById(R.id.table1);
        table1.setOnClickListener(view -> {
            if (cnt == 0) {
                obj_file = "models/table1.obj";
                png_file = "models/table_texture4.png";
                isObjectReplaced = true;
            }
        });

        table3 = v.findViewById(R.id.table3);
        table3.setOnClickListener(view -> {
            if (cnt == 0) {
                obj_file = "models/table3.obj";
                png_file = "models/table_texture6.png";
                isObjectReplaced = true;
            }
        });

        table4 = v.findViewById(R.id.table4);
        table4.setOnClickListener(view -> {
            if (cnt == 0) {
                obj_file = "models/table4.obj";
                png_file = "models/table_texture5.png";
                isObjectReplaced = true;
            }
        });

        table5 = v.findViewById(R.id.table5);
        table5.setOnClickListener(view -> {
            if (cnt == 0) {
                obj_file = "models/table5.obj";
                png_file = "models/table_texture5.png";
                isObjectReplaced = true;
            }
        });

        wishlist1 = v.findViewById(R.id.wishlist1);
        wishlist2 = v.findViewById(R.id.wishlist2);
        wishlist3 = v.findViewById(R.id.wishlist3);
        wishlist4 = v.findViewById(R.id.wishlist4);

        wishlist1.setOnClickListener(view -> toggleWishlist("table1", wishlist1));
        wishlist2.setOnClickListener(view -> toggleWishlist("table3", wishlist2));
        wishlist3.setOnClickListener(view -> toggleWishlist("table4", wishlist3));
        wishlist4.setOnClickListener(view -> toggleWishlist("table5", wishlist4));



        checkWishlistState("table1", wishlist1);
        checkWishlistState("table3", wishlist2);
        checkWishlistState("table4", wishlist3);
        checkWishlistState("table5", wishlist4);


        Button cart1 = v.findViewById(R.id.cart1);
        cart1.setOnClickListener(view -> addToCart("table1", 3500));

        Button cart3 = v.findViewById(R.id.cart3);
        cart3.setOnClickListener(view -> addToCart("table3", 5500));

        Button cart4 = v.findViewById(R.id.cart4);
        cart4.setOnClickListener(view -> addToCart("table4", 5500));

        Button cart5 = v.findViewById(R.id.cart5);
        cart5.setOnClickListener(view -> addToCart("table5", 5000));


        Button reviewButton1 = v.findViewById(R.id.review1);
        reviewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("table1");
            }
        });

        Button reviewButton2 = v.findViewById(R.id.review2);
        reviewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("table3");
            }
        });
        Button reviewButton3 = v.findViewById(R.id.review3);
        reviewButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("table5");
            }
        });

        Button reviewButton4 = v.findViewById(R.id.review4);
        reviewButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("table4");
            }
        });

        Button detailsButton1 = v.findViewById(R.id.details1);
        detailsButton1.setOnClickListener(view -> showDetailsDialog(R.drawable.table1, "Ikea Table for Multipurpose Particle Board Wood Acrylic Finish Desk/Workstation/Computer Table, (120x60, White)  \n" +
                "Brand\t : Ikea\n" +
                "Style\t  :Modern\n" +
                "Base Material\t : wood\n" +
                "Top Material Type\t : Wood\n" +
                "Finish Type\t : Powder Coated, Painted\n" +
                "Special Feature\t : drilled hole\n" +
                "Room Type\t : Office, Living Room, Bedroom\n" +
                "Number of Drawers\t : 1\n" +
                "Recommended Uses For Product\t : office\n" +
                "Mounting Type\t : Floor Mount"));

        Button detailsButton2 = v.findViewById(R.id.details2);
        detailsButton2.setOnClickListener(view -> showDetailsDialog(R.drawable.table3, "Vintage Sheesham Wood CNC Dining Table Without Chairs Dining Room Furniture Wooden Dinner Table Only for Living Room Home Hotel  \n" +
                "Brand\t : Vintage\n" +
                "Maximum Weight Recommendation\t : 300 Pounds\n" +
                "Frame Material\t : Wood\n" +
                "Colour\t : Honey Finish\n" +
                "Product Care Instructions\t : Wipe with Dry Cloth\n" +
                "Recommended Uses For Product\t : dining\n" +
                "Shape\t : Rectangular\n" +
                "Table design\t : Dining Table\n" +
                "Style\t : Contemporary\n" +
                "Base Type\t : Leg"  ));

        Button detailsButton4 = v.findViewById(R.id.details4);
        detailsButton4.setOnClickListener(view -> showDetailsDialog(R.drawable.table5, "MATTERHORN Engineered Wood Coffee Table, Centre Table, Drawing Room Table  \n" +
                "Brand\t : MATTERHORN\n" +
                "Product Dimensions\t : 70D x 40W x 40H Centimeters\n" +
                "Maximum Weight Recommendation\t : 15 Kilograms\n" +
                "Frame Material\t : Engineered Wood\n" +
                "Colour\t : Intal Beige\n" +
                "Product Care Instructions\t : Use damp cloth to clean, wipe clean with dry cloth\n" +
                "Shape\t : Rectangular\n" +
                "Table design\t : Coffee Table\n" +
                "Style\t : Contemporary\n" +
                "Base Type\t : Leg"));
        Button detailsButton3 = v.findViewById(R.id.details3);
        detailsButton3.setOnClickListener(view -> showDetailsDialog(R.drawable.table4, "RIBAVARY™ Wooden Pedestal Table, Accent Bedside Table,Modern Coffee End Table for Living Room, Bedroom and Balcony with Latest Round Design Table  Brand\tRIBAVARY\n" +
                "Product Dimensions\t : 40D x 40W x 51H Centimeters\n" +
                "Maximum Weight Recommendation\t : 100 Pounds\n" +
                "Frame Material\t : Engineered Wood\n" +
                "Colour\t : Walnut Brown\n" +
                "Product Care Instructions\t : Hand_Wash\n" +
                "Table design\t : Coffee Table\n" +
                "Style\t : Modern\n" +
                "Seating Capacity\t: 1.00\n" +
                "Finish Type\t : Glossy"));
        return v;
    }

    private void addToCart(String productName, int price) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("/users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");

                            Map<String, Object> cartItem = new HashMap<>();
                            cartItem.put("product", productName);
                            cartItem.put("price", price);
                            cartItem.put("uid", uid);
                            cartItem.put("name", userName);

                            db.collection("/cart").add(cartItem)
                                    .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Item added to cart!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add item to cart!", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to retrieve user data!", Toast.LENGTH_SHORT).show());
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
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to remove from wishlist", Toast.LENGTH_SHORT).show());
                        } else {
                            Map<String, Object> wishlistItem = new HashMap<>();
                            wishlistItem.put("product", productName);
                            wishlistItem.put("uid", uid);

                            db.collection("/wishlist").add(wishlistItem)
                                    .addOnSuccessListener(documentReference -> {
                                        wishlistButton.setImageResource(R.drawable.ic_wishlist_selected);
                                        Toast.makeText(getContext(), "Added to wishlist", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add to wishlist", Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to check wishlist state", Toast.LENGTH_SHORT).show());
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
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to check wishlist state", Toast.LENGTH_SHORT).show());
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
    }}


