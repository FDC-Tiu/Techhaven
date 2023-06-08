package com.example.techhaven;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;

import java.math.BigDecimal;

public class PaypalActivity extends Activity {

    private static final int REQUEST_CODE_PAYPAL_PAYMENT = 1;
    private static final String RESULT_CONFIRMATION = "result_confirmation";
    private static final int RESULT_EXTRAS_INVALID = 2;


    private PayPalConfiguration config;
    private double mTotalPrice;
    private TextView mPaypalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        if (getIntent() != null) {
            mTotalPrice = getIntent().getDoubleExtra("total_price", 0.0);
        }

        mPaypalResult = findViewById(R.id.paypal_result);

        // Set up the PayPal configuration
        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK) // // Use sandbox for testing
                .clientId("AevzWoTWHhPRZ5DAAQ8YwmQAsyUmCdblI3Xc67wUHH49DareXQeIspHcUEQpC4cUTsHmEt7Sr7ymS6df");  // Replace with your PayPal client ID

        // Start the PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        // Create a PayPalPayment object with the desired payment details
        PayPalPayment payment = new PayPalPayment(new BigDecimal(mTotalPrice), "PHP", "Overall Price",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Create an intent for the PaypalActivity
        Intent paymentIntent = new Intent(this, PaymentActivity.class);

        // Pass the PayPal configuration and payment details to the intent
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Start the PaypalActivity with the intent and a request code
        startActivityForResult(paymentIntent, REQUEST_CODE_PAYPAL_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of the PayPal payment
        if (requestCode == REQUEST_CODE_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    // Payment was successful, extract relevant information
                    String paymentId = confirmation.getProofOfPayment().getPaymentId();
                    String state = confirmation.getProofOfPayment().getState();

                    // Process the payment details as needed
                    mPaypalResult.setText("Payment successful. Payment ID: " + paymentId + ", State: " + state);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Payment was canceled by the user
                mPaypalResult.setText("Payment unsuccessful");
            } else if (resultCode == PaypalActivity.RESULT_EXTRAS_INVALID) {
                // Invalid payment configuration detected
                Toast.makeText(getApplicationContext(), "Invalid payment configuration", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the PayPal service
        stopService(new Intent(this, PayPalService.class));
    }
}
