package com.example.InterEaseApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;

public class OrdersFragment extends Fragment {

    private ImageButton backButton;
    private LinearLayout ordersContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // Initialize views
        backButton = view.findViewById(R.id.back_button2);
        ordersContainer = view.findViewById(R.id.order_placeholder);

        // Set back button click listener
        backButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new SidemenuFragment());
            fragmentTransaction.commit();
        });

        // Fetch and display orders
        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        // Get current user ID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query Firestore orders collection for the user's orders
        FirebaseFirestore.getInstance().collection("/orders")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve order details
                            String productName = document.getString("product");
                            int price = document.getLong("price").intValue();

                            // Display the order
                            displayOrder(productName,price);
                        }
                    } else {
                        // Handle errors
                    }
                });
    }

    private void displayOrder(String product, int price) {
        // Inflate order layout
        View orderView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, ordersContainer, false);

        // Get TextViews from order layout
        TextView productTextView = orderView.findViewById(R.id.order_product);
        TextView priceTextView = orderView.findViewById(R.id.order_price);
         // Make sure to add this in the item_order layout

        // Set product, price, and email
        productTextView.setText(String.format(Locale.getDefault(), "Product: %s", product));
        priceTextView.setText(String.format(Locale.getDefault(), "Price: %d", price));


        // Add order layout to orders container
        ordersContainer.addView(orderView);
    }

}
