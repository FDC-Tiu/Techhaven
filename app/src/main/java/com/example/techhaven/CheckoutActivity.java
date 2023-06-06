package com.example.techhaven;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CheckoutActivity extends AppCompatActivity {

    private Button checkout_Btn;
    private RecyclerView recycler_product_list_view;
    private ConstraintLayout header_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }
}