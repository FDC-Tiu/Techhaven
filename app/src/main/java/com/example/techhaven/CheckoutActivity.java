package com.example.techhaven;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private Button payBtn;
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private  ArrayList<CartCheckout> cartList;
    private TextView paymentBackbtn;
    private  TextView overAllPrice;
    private Button paypalBtn;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();

        // Retrieve the ArrayList from the intent in the target activity
        if (intent != null) {
            cartList = intent.getParcelableArrayListExtra("cart_list");
        }
        recyclerView = findViewById(R.id.recycler_checkout_view);
        payBtn = findViewById(R.id.pay_btn);
        paymentAdapter = new PaymentAdapter(cartList, CheckoutActivity.this);
        recyclerView.setAdapter(paymentAdapter);
        paymentBackbtn = findViewById(R.id.payment_back_btn);
        overAllPrice = findViewById(R.id.overall_price_textview);
        paypalBtn = findViewById(R.id.pay_btn);

        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                String productPrice = cartList.get(i).getProductPrice().replace("₱", "");
                double productPriceToDouble = Double.parseDouble(productPrice);
                int quantity = Integer.parseInt(cartList.get(i).getQuantity());
                double result = productPriceToDouble * quantity;

                total += result;
            }
            overAllPrice.setText("Total P" + String.valueOf(total));
        }

        Log.d("Tiuuu", "onCreate: " + cartList);
        Log.d("Tiuuu", "onCreate: " + total);

        paymentBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        paypalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the PayPal redirection here
                redirectPayPal();
            }
        });


    }
    private void redirectPayPal() {
        // Create an intent to start the PayPal activity
        Intent intent = new Intent(getApplicationContext(), PaypalActivity.class);
        intent.putExtra("total_price", total);
        startActivity(intent);
    }
}