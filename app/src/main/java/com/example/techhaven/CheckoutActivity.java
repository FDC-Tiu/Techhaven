package com.example.techhaven;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private Button checkoutBtn;
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private ArrayList <Payment> paymentArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        recyclerView = findViewById(R.id.recyclerView);
        paymentAdapter = new PaymentAdapter(paymentArrayList, CheckoutActivity.this);
        recyclerView.setAdapter(paymentAdapter);

    }
}