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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private Button payBtn;
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private  ArrayList<CartCheckout> cartList;
    private TextView paymentBackbtn;

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

        paymentBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}