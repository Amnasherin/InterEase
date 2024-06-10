package com.example.InterEaseApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> faqList;
    private HashMap<String, List<String>> answerList;
    private int lastExpandedPosition = -1;
    private ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);
        prepareFAQData();
        expandableListAdapter = new FAQAdapter(requireContext(), faqList, answerList);
        expandableListView.setAdapter(expandableListAdapter);
        backButton = view.findViewById(R.id.back_button9);

        // Handle group expand/collapse
        expandableListView.setOnGroupExpandListener(groupPosition -> {
            if (lastExpandedPosition != -1 && lastExpandedPosition != groupPosition) {
                expandableListView.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // Handle click on child item (answer)
            String question = faqList.get(groupPosition);
            String answer = answerList.get(question).get(childPosition);
            Toast.makeText(requireContext(), answer, Toast.LENGTH_SHORT).show();
            return true; // Return true to consume the event
        });

        // Add email TextView
        TextView emailTextView = new TextView(requireContext());
        emailTextView.setText("");
        emailTextView.setPadding(16, 16, 16, 16);
        ((ViewGroup) view).addView(emailTextView);

        backButton.setOnClickListener(v -> goBackToSidemenu());


        return view;
    }

    private void prepareFAQData() {
        faqList = new ArrayList<>();
        answerList = new HashMap<>();

        // Add questions and answers
        faqList.add("      What should I do if I forget my password?");
        List<String> answer1 = new ArrayList<>();
        answer1.add("Click on the \"Forgot Password\" link on the login screen, enter your registered email address, and follow the instructions sent to your email to reset your password.");
        answerList.put(faqList.get(0), answer1);

        faqList.add("      How Can I view product details?");
        List<String> answer2 = new ArrayList<>();
        answer2.add("Yes, you can click on the \"Details\" button associated with each product to view more information about the product before adding it to your cart.");
        answerList.put(faqList.get(1), answer2);

        faqList.add("      How do I add a product to my wishlist?");
        List<String> answer3 = new ArrayList<>();
        answer3.add("Click on the heart icon or \"Add to Wishlist\" button on the product details page. The icon should change to indicate the product has been added to your wishlist.");
        answerList.put(faqList.get(2), answer3);

        faqList.add("      How do I make a payment for my order?");
        List<String> answer4 = new ArrayList<>();
        answer4.add("After selecting the items you want to purchase, proceed to the checkout page. Choose your preferred payment method and follow the on-screen instructions to complete the payment process.");
        answerList.put(faqList.get(3), answer4);

        // Add more questions and answers as needed
    }


    private void goBackToSidemenu() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, new SidemenuFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
