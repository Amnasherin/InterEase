package com.example.InterEaseApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView emailTextView = convertView.findViewById(R.id.emailTextView);

        // Populate the data into the template view using the data object
        if (user != null) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
        }

        return convertView;
    }
}
