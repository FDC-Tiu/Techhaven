package com.example.techhaven;

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
                .inflate(R.layout.activity_checkout, parent, false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText((int) product.getPrice());

        // Set other product details

        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform the payment proceed action for the specific product
                // For example, show a dialog or navigate to a payment screen
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
            payBtn = itemView.findViewById(R.id.pay_btn);
        }
    }
}
