package com.example.techhaven;

import static java.lang.Double.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<CartCheckout> productList;
    private Activity mActivity;


    public PaymentAdapter(List<CartCheckout> productList, Activity activity) {
        this.productList = productList;
        this.mActivity = activity;


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
        CartCheckout cartCheckout = productList.get(position);
        holder.productNames.setText(cartCheckout.getProductName());
        holder.productPrices.setText((cartCheckout.getProductPrice()));
        holder.productQuantity.setText(cartCheckout.getQuantity());

        int quantity = Integer.parseInt(cartCheckout.getQuantity());
        String priceString = cartCheckout.getProductPrice().replace("₱", "").replace(",", ".");
        double price = Double.parseDouble(priceString);
        double total = quantity * price;
        String totalString = String.valueOf(total);
        holder.productTotal.setText(totalString);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        public TextView productNames, productPrices, productQuantity, productTotal;
        public Button payBtn;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            productNames = itemView.findViewById(R.id.product_names);
            productPrices = itemView.findViewById(R.id.product_prices);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productTotal = itemView.findViewById(R.id.product_total);


        }
    }
}
