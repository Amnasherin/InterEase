package com.example.InterEaseApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SidemenuFragment extends Fragment {

    private ListView listView;
    private ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);

        // Initialize views
        listView = view.findViewById(R.id.listview);
        backButton = view.findViewById(R.id.back_button7);

        // Set up menu items
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Profile", R.drawable.ic_profile));
        menuItems.add(new MenuItem("My Cart", R.drawable.ic_cart));
        menuItems.add(new MenuItem("My Orders", R.drawable.ic_orders));
        menuItems.add(new MenuItem("FAQ", R.drawable.ic_faq));
        menuItems.add(new MenuItem("Logout", R.drawable.ic_logout));

        MenuAdapter adapter = new MenuAdapter(requireContext(), menuItems);
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            MenuItem selectedItem = (MenuItem) parent.getItemAtPosition(position);
            handleMenuItemClick(selectedItem.getText());
        });

        // Set back button click listener
        backButton.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new MainFragment());
            fragmentTransaction.commit();
        });

        return view;
    }

    private void handleMenuItemClick(String selectedItem) {
        // Handle menu item click based on selection
        switch (selectedItem) {
            case "Profile":
                // Navigate to profile fragment
                FragmentManager profileFragmentManager = getParentFragmentManager();
                FragmentTransaction profileFragmentTransaction = profileFragmentManager.beginTransaction();
                profileFragmentTransaction.replace(R.id.fragment_place, new ProfileFragment());
                profileFragmentTransaction.commit();
                break;
            case "My Cart":
                // Navigate to cart fragment
                FragmentManager cartFragmentManager = getParentFragmentManager();
                FragmentTransaction cartFragmentTransaction = cartFragmentManager.beginTransaction();
                cartFragmentTransaction.replace(R.id.fragment_place, new CartFragment());
                cartFragmentTransaction.commit();
                break;
            case "My Orders":
                // Navigate to orders fragment
                FragmentManager ordersFragmentManager = getParentFragmentManager();
                FragmentTransaction ordersFragmentTransaction = ordersFragmentManager.beginTransaction();
                ordersFragmentTransaction.replace(R.id.fragment_place, new OrdersFragment());
                ordersFragmentTransaction.commit();
                break;
            case "FAQ":
                // Navigate to FAQ fragment
                FragmentManager faqFragmentManager = getParentFragmentManager();
                FragmentTransaction faqFragmentTransaction = faqFragmentManager.beginTransaction();
                faqFragmentTransaction.replace(R.id.fragment_place, new FAQFragment());
                faqFragmentTransaction.commit();
                break;

            case "Logout":
                // Handle Logout click
                Toast.makeText(requireContext(), "Logging Out", Toast.LENGTH_SHORT).show();
                logoutUser();
                break;
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        // Navigate to login screen
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);
        requireActivity().finish(); // Finish current activity
    }
}

