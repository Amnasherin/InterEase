package com.example.InterEaseApp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CartFragment extends Fragment {

    private ImageButton backButton;
    private TableLayout cartItemsContainer;
    private TextView totalTextView;
    private Button buyButton;
    private FirebaseFirestore db;
    private int totalPrice = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        backButton = view.findViewById(R.id.back_button2);
        cartItemsContainer = view.findViewById(R.id.cart_items_container);
        totalTextView = view.findViewById(R.id.total_text_view);
        buyButton = view.findViewById(R.id.buy);

        // Set back button click listener
        backButton.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new SidemenuFragment());
            fragmentTransaction.commit();
        });

        // Set buy button click listener
        buyButton.setOnClickListener(view12 -> showPaymentMethodDialog()); // Call the method to show payment method dialog

        // Add table headings
        addTableHeadings();

        // Fetch cart details for the current user
        fetchCartDetails();

        return view;
    }

    private void fetchCartDetails() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("/cart")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String productName = document.getString("product");
                            int price = document.getLong("price").intValue();
                            String documentId = document.getId();
                            displayCartItem(productName, price, documentId);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error fetching cart details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayCartItem(String product, int price, String documentId) {
        TableRow row = new TableRow(getContext());
        TextView productTextView = new TextView(getContext());
        productTextView.setText(product);
        TextView priceTextView = new TextView(getContext());
        priceTextView.setText(String.valueOf(price));
        priceTextView.setTypeface(null, android.graphics.Typeface.BOLD); // Make price text bold

        ImageButton removeButton = new ImageButton(getContext());
        removeButton.setImageResource(R.drawable.ic_delete);
        removeButton.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        removeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // Ensure image fits well
        removeButton.setPadding(0, 0, 0, 0); // Remove padding
        removeButton.setAdjustViewBounds(true); // Adjust bounds to maintain aspect ratio
        removeButton.setOnClickListener(view -> removeCartItem(documentId, price, row));

        // Set the size for the delete icon
        int iconSize = (int) getResources().getDimension(R.dimen.delete_icon_size); // Ensure dimension resource is defined
        TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(iconSize, iconSize);
        buttonParams.setMargins(6, 6, 6, 6);
        removeButton.setLayoutParams(buttonParams);

        TableRow.LayoutParams textParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        textParams.setMargins(12, 6, 10, 6);
        productTextView.setLayoutParams(textParams);
        priceTextView.setLayoutParams(textParams);

        row.addView(productTextView);
        row.addView(priceTextView);
        row.addView(removeButton);

        cartItemsContainer.addView(row);
        totalPrice += price;
        updateTotalPrice();
    }

    private void removeCartItem(String documentId, int price, TableRow row) {
        db.collection("/cart").document(documentId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cartItemsContainer.removeView(row);
                totalPrice -= price;
                updateTotalPrice();
                Toast.makeText(getContext(), "Item removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error removing item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice() {
        totalTextView.setText("Total: ₹" + totalPrice);
    }

    private void addTableHeadings() {
        TableRow headingRow = new TableRow(getContext());

        TextView productHeading = new TextView(getContext());
        productHeading.setText("Product");
        productHeading.setTypeface(null, android.graphics.Typeface.BOLD); // Set text bold
        productHeading.setTextSize(20); // Set text size
        productHeading.setPadding(10, 10, 10, 10); // Add padding for better readability

        TextView priceHeading = new TextView(getContext());
        priceHeading.setText("     Price");
        priceHeading.setTypeface(null, android.graphics.Typeface.BOLD); // Set text bold
        priceHeading.setTextSize(20); // Set text size
        priceHeading.setPadding(10, 10, 10, 10); // Add padding for better readability

        TextView actionHeading = new TextView(getContext());
        actionHeading.setText("     Action");
        actionHeading.setTypeface(null, android.graphics.Typeface.BOLD); // Set text bold
        actionHeading.setTextSize(20); // Set text size
        actionHeading.setPadding(10, 10, 10, 10); // Add padding for better readability

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(10, 10, 10, 10);
        productHeading.setLayoutParams(params);
        priceHeading.setLayoutParams(params);
        actionHeading.setLayoutParams(params);

        headingRow.addView(productHeading);
        headingRow.addView(priceHeading);
        headingRow.addView(actionHeading);

        cartItemsContainer.addView(headingRow, 0);
    }

    private void placeOrder(String paymentMethod) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("/cart")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            order.setPaymentMethod(paymentMethod);
                            order.setUid(uid); // Ensure the UID is set in the order

                            db.collection("/orders")
                                    .add(order)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                document.getReference().delete();
                                            } else {
                                                Toast.makeText(getContext(), "Error placing order", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        Toast.makeText(getContext(), "Order Successful", Toast.LENGTH_SHORT).show();
                        cartItemsContainer.removeAllViews();
                        totalPrice = 0;
                        updateTotalPrice();
                        addTableHeadings();
                    } else {
                        Toast.makeText(getContext(), "Error fetching cart details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Payment Method");

        builder.setPositiveButton("Cash on Delivery", (dialog, which) -> {
            placeOrder("Cash on Delivery");
        });

        builder.setNegativeButton("Online Payment", (dialog, which) -> {
            showOnlinePaymentDialog();
        });

        builder.show();
    }

    private void showOnlinePaymentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Online Payment");

        View view = getLayoutInflater().inflate(R.layout.dialog_payment, null);

        EditText cardHolderNameEditText = view.findViewById(R.id.cardHolderNameEditText);
        EditText cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        EditText cvvEditText = view.findViewById(R.id.cvvEditText);
        EditText expiryYearEditText = view.findViewById(R.id.expiryYearEditText);
        Button submitPaymentButton = view.findViewById(R.id.submitPaymentButton);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        submitPaymentButton.setOnClickListener(v -> {
            String cardHolderName = cardHolderNameEditText.getText().toString().trim();
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String cvv = cvvEditText.getText().toString().trim();
            String expiryYear = expiryYearEditText.getText().toString().trim();

            if (cardHolderName.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty() || expiryYear.isEmpty()) {
                Toast.makeText(getContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
            } else {
                // Here you would process the payment and place the order
                placeOrder("Online Payment");
                dialog.dismiss();
                Toast.makeText(getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}

