package com.example.techhaven;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        initializeAdapter();
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

    @Override
    protected void onResume() {
        super.onResume();

        checkList();
    }

    private void checkList() {
        if (cartList.size() == 0) {
            payBtn.setVisibility(View.GONE);
        }
        else{
            payBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initializeAdapter() {
        paymentAdapter = new PaymentAdapter(cartList, CheckoutActivity.this);
        recyclerView.setAdapter(paymentAdapter);
    }

    private void getProductIdFromCart() {
        ArrayList<CartCheckout> cartCheckoutList = new ArrayList<>();
        cartCheckoutList = cartList;
        ArrayList<String> mIdList = new ArrayList<>();
        for (CartCheckout cart : cartCheckoutList) {
            String mId = cart.getId();
            mIdList.add(mId);
        }

        for (String mId : mIdList) {
            deleteFunc(mId);
        }
    }
    private void deleteFunc(String mId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Cart").child(mId);

        // Delete the value
        databaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Tiuuu", "onComplete: " + mId);
                    cartList = new ArrayList<>();
                    initializeAdapter();
                    overAllPrice.setText("");
                    checkList();
                } else {
                    Log.d("Tiuuu", "unsuccessful: ");
                }
            }
        });
    }

    private void redirectPayPal() {
        // Create an intent to start the PayPal activity
        Intent intent = new Intent(getApplicationContext(), PaypalActivity.class);
        intent.putExtra("total_price", total);
        mLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Log.d("Tiuuu", "ressuuuult: ");
            getProductIdFromCart();
        }
    });
}