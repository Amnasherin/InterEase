package com.example.InterEaseApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView productTextView = convertView.findViewById(R.id.productTextView);
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
        TextView paymentMethodTextView = convertView.findViewById(R.id.paymentMethodTextView);


        // Populate the data into the template view using the data object
        if (order != null) {
            nameTextView.setText(order.getName());
            productTextView.setText(order.getProduct());
            priceTextView.setText(order.getPrice());
            paymentMethodTextView.setText(order.getPaymentMethod());


        }

        return convertView;
    }
}
