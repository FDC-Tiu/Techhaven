package com.example.techhaven;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvDesc;
    private TextView tvStock;
    private String pid;
    private ImageView minusNumber;
    private EditText itemCountView;
    private ImageView addNumber;
    private int mItemCount = 0;
    private FloatingActionButton floatBtn;
    private TextView backBtn;
    private TextView backCartBtn;
    private int productId;
    private String imageUrl;
    private String title;
    private String desc;
    private double price;
    private int stock;

    public DetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        imageView = findViewById(R.id.image);
        tvTitle = findViewById(R.id.tv_title);
        tvPrice = findViewById(R.id.tv_price);
        tvDesc = findViewById(R.id.tv_desc);
        tvStock = findViewById(R.id.tv_stock);
        minusNumber = findViewById(R.id.minus_number);
        itemCountView = findViewById(R.id.item_count);
        addNumber = findViewById(R.id.add_number);
        floatBtn = findViewById(R.id.float_btn);
        backBtn = findViewById(R.id.back_btn);
        backCartBtn = findViewById(R.id.back_cart_btn);


        minusNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mItemCount > 0){
                    mItemCount--;
                    itemCountView.setText(String.valueOf(mItemCount));
                    if (mItemCount != 0) {
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Quantity set to zero", Toast.LENGTH_SHORT).show();
                }
            }
        });
        itemCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIU", "test " + mItemCount);
                if (mItemCount < 99) {
                    mItemCount++;
                    itemCountView.setText(String.valueOf(mItemCount));
                }
                Log.d("TIU", "onClick " + mItemCount);
                if (mItemCount >= 99) {
                    itemCountView.setText("0");
                    mItemCount = 0;
                }
            }
        });

        itemCountView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String item = itemCountView.getText().toString();
                if(!item.isEmpty()) {
                    Log.d("TIU", "textchasnged " + item);
                    mItemCount = Integer.parseInt(item);
                    Log.d("TIU", "afterTextChanged" + mItemCount);
                }
            }
        });

        floatBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String item = itemCountView.getText().toString();
                int count = Integer.parseInt(item);
                if (count > 0) {

                    String productName = tvTitle.getText().toString();
                    String productPrice = tvPrice.getText().toString();
                    String quantity = itemCountView.getText().toString();

                    HashMap<String, Object> productDetails = new HashMap<>();
                    productDetails.put("image_url", imageUrl);
                    productDetails.put("product_name", productName);
                    productDetails.put("product_price", productPrice);
                    productDetails.put("product_quantity", quantity);
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                    databaseRef.child("Cart")
                            .orderByChild("product_name")
                            .equalTo(productName) //check if product name is already added and update the quantity
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // The product already exists in Firebase
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            String cartItemId = childSnapshot.getKey();
                                            String previousQuantity = childSnapshot.child("product_quantity").getValue(String.class);

                                            int updatedQuantity = Integer.parseInt(previousQuantity) + Integer.parseInt(quantity);

                                            // Update the product_quantity in Firebase
                                            databaseRef.child("Cart").child(cartItemId).child("product_quantity").setValue(String.valueOf(updatedQuantity));

                                            Toast.makeText(DetailsActivity.this, "Product quantity updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // The product doesn't exist, you can proceed with adding the product to Firebase
                                        String newUserId = databaseRef.child("Cart").push().getKey();
                                        productDetails.put("product_id", newUserId);
                                        databaseRef.child("Cart").child(newUserId).setValue(productDetails)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("TIU", "onSuccess: ");
                                                        Toast.makeText(DetailsActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("TIU", "onFailure: ");
                                                    }
                                                });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    itemCountView.setText("0");
                    mItemCount = 0;
                } else if (count==0) {
                    Toast.makeText(DetailsActivity.this, "Add an item", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Intent intent = getIntent();
        pid = intent.getStringExtra(ProductAdapter.PID);


        getProductById(Integer.parseInt(pid));
    }

    public void getProductById(int id) {

        String baseUrl = "https://dummyjson.com/products";
        String url = baseUrl + "/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.e("Products: ", response);
                        JSONObject jsonObject = new JSONObject(response);

                        productId = jsonObject.getInt("id");
                        imageUrl = jsonObject.getString("thumbnail");
                        title = jsonObject.getString("title");
                        desc = jsonObject.getString("description");
                        price = jsonObject.getDouble("price");
                        stock = jsonObject.getInt("stock");

                        Product product = new Product();
                        product.setId(productId);
                        product.setTitle(title);
                        product.setPrice(price);
                        product.setStock(stock);
                        product.setThumbnail(imageUrl);
                        product.setDesc(desc);

                        Picasso.get().load(imageUrl).into(imageView);
                        tvTitle.setText(title);
                        tvPrice.setText("₱" + String.format("%.2f", price));
                        tvStock.setText("Stock: " + stock);
                        tvDesc.setText(desc);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed" + e, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed" + error, Toast.LENGTH_SHORT).show();
                });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        backCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data1",1);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        VolleySingleton.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}