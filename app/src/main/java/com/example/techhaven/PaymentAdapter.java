package com.example.techhaven;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Product> productList;

    public PaymentAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_details, parent, false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));

        // Set other product details

        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {double totalPrice = 0.0;
                StringBuilder productDetailsBuilder = new StringBuilder();

                for (Product product : productList) {
                    totalPrice += product.getPrice();
                    productDetailsBuilder.append(product.getName()).append(" - $").append(product.getPrice()).append("\n");
                }

                String paymentDetails = "Total Price: $" + totalPrice + "\n\nProduct Details:\n" + productDetailsBuilder.toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext()); // Use the context from the root view
                builder.setTitle("Payment Details");
                builder.setMessage(paymentDetails);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice;
        public Button payBtn;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            payBtn = itemView.findViewById(R.id.product_quantity);
        }
    }
}
