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
 * Use the {@link LivingroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LivingroomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton back_button_LvRoom;
    private ImageButton bed1;
    private ImageButton bed2;
    private ImageButton bed3, bed4;
    private ImageButton wishlist1, wishlist2, wishlist3, wishlist4;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public LivingroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LivingroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LivingroomFragment newInstance(String param1, String param2) {
        LivingroomFragment fragment = new LivingroomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static LivingroomFragment newInstance() {
        return new LivingroomFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_livingroom, container, false);

        back_button_LvRoom = v.findViewById(R.id.back_button1);
        back_button_LvRoom.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MainFragment());
            fragmentTransaction.commit();
        });

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        bed1 = v.findViewById(R.id.bed1);
        bed2 = v.findViewById(R.id.bed2);
        bed3 = v.findViewById(R.id.bed3);
        bed4 = v.findViewById(R.id.bed4);

        bed1.setOnClickListener(view -> selectObject("models/bed1.obj", "models/bed_texture1.png"));
        bed2.setOnClickListener(view -> selectObject("models/bed2.obj", "models/table_texture4.png"));
        bed3.setOnClickListener(view -> selectObject("models/bed3.obj", "models/bed_texture3.png"));
        bed4.setOnClickListener(view -> selectObject("models/bed4.obj", "models/table_texture4.png"));

        wishlist1 = v.findViewById(R.id.wishlist1);
        wishlist2 = v.findViewById(R.id.wishlist2);
        wishlist3 = v.findViewById(R.id.wishlist3);
        wishlist4 = v.findViewById(R.id.wishlist4);

        checkWishlistState("bed1", wishlist1);
        checkWishlistState("bed2", wishlist2);
        checkWishlistState("bed3", wishlist3);
        checkWishlistState("bed4", wishlist4);

        wishlist1.setOnClickListener(view -> toggleWishlist("bed1", wishlist1));
        wishlist2.setOnClickListener(view -> toggleWishlist("bed2", wishlist2));
        wishlist3.setOnClickListener(view -> toggleWishlist("bed3", wishlist3));
        wishlist4.setOnClickListener(view -> toggleWishlist("bed4", wishlist4));

        Button cart1 = v.findViewById(R.id.cart1);
        Button cart2 = v.findViewById(R.id.cart2);
        Button cart3 = v.findViewById(R.id.cart3);
        Button cart4 = v.findViewById(R.id.cart4);

        cart1.setOnClickListener(view -> addToCart("bed1", 20000));
        cart2.setOnClickListener(view -> addToCart("bed2", 15000));
        cart3.setOnClickListener(view -> addToCart("bed3", 25000));
        cart4.setOnClickListener(view -> addToCart("bed4", 25000));

        Button reviewButton1 = v.findViewById(R.id.review1);
        reviewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("bed1");
            }
        });

        Button reviewButton2 = v.findViewById(R.id.review2);
        reviewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("bed2");
            }
        });
        Button reviewButton3 = v.findViewById(R.id.review3);
        reviewButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("bed3");
            }
        });

        Button reviewButton4 = v.findViewById(R.id.review4);
        reviewButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("bed4");
            }
        });
        Button detailsButton1 = v.findViewById(R.id.details1);
        detailsButton1.setOnClickListener(view -> showDetailsDialog(R.drawable.bed1, "APRODZ Solid Wood Without Storage Queen Size Bed Elmore for Bedroom Stylish | White Finish  \n" +
                "Size\t : Queen Bed\n" +
                "Material\t : Wood\n" +
                "Product Dimensions\t : 2.06L x 0.99W x 1.09H Meters\n" +
                "Special Feature\t : storage\n" +
                "Colour\t : White\n" +
                "Finish Type\t : Painted\n" +
                "Included Components\t : Bed\n" +
                "Brand\t : APRODZ\n" +
                "Furniture Finish\t : White\n" ));

        Button detailsButton2 = v.findViewById(R.id.details2);
        detailsButton2.setOnClickListener(view -> showDetailsDialog(R.drawable.bed2, "home by Nilkamal Marbito King Bed Engineered Wood with Headboard & Box Storage (White)  \n" +
                "Size\t : King\n" +
                "Material\t : Engineered Wood\n" +
                "Product Dimensions\t : 2.25L x 1.91W x 0.3H Meters\n" +
                "Special Feature\t : storage\n" +
                "Colour\t : White\n" +
                "Finish Type\t : Melamine\n" +
                "Included Components\t : 1 Bed\n" +
                "Compatible with mattress size\t : 78 X 72 inches\n" +
                "Brand\t : home\n"  ));

        Button detailsButton3 = v.findViewById(R.id.details3);
        detailsButton3.setOnClickListener(view -> showDetailsDialog(R.drawable.bed3, "APRODZ Solid Wood Without Storage Queen Size Bed Elmore for Bedroom Stylish | White Finish  \n" +
                "Size\t : Queen Bed\n" +
                "Material\t : Wood\n" +
                "Product Dimensions\t : 2.06L x 0.99W x 1.09H Meters\n" +
                "Special Feature\t : storage\n" +
                "Colour\t : White\n" +
                "Finish Type\t : Painted\n" +
                "Included Components\t : Bed\n" +
                "Brand\t : APRODZ\n" +
                "Furniture Finish\t : White\n" ));
        Button detailsButton4 = v.findViewById(R.id.details4);
        detailsButton4.setOnClickListener(view -> showDetailsDialog(R.drawable.bed4, "Nilkamal SLEEP Striker Single Size Metal Bed  \n" +
                "Size\t : Double\n" +
                "Material\t :  Mild Wood\n" +
                "Product Dimensions\t : 199L x 98W x 91H Centimeters\n" +
                "Special Feature\t : Basic Headboard\n" +
                "Colour\t : Wooden\n" +
                "Finish Type\t : Matte\n" +
                "Included Components\t : Bed\n" +
                "Compatible with mattress size\t : 75X36X6\n" +
                "Brand\t : Nilkamal SLEEP\n"));

        return v;
    }

    private void selectObject(String objFile, String pngFile) {
        if (cnt == 0) {
            obj_file = objFile;
            png_file = pngFile;
            isObjectReplaced = true;
        }
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
            db.collection("/wishlist").whereEqualTo("uid", uid).whereEqualTo("product", productName)
                    .get()
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
                            db.collection("/wishlist")
                                    .add(wishlistItem)
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
            db.collection("/wishlist").whereEqualTo("uid", uid).whereEqualTo("product", productName)
                    .get()
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





