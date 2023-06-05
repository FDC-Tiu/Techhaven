package com.example.techhaven;

import static java.lang.reflect.Array.get;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techhaven.ui.cart.CartFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartCheckoutAdapter extends RecyclerView.Adapter<CartCheckoutAdapter.CartViewHolder> {

    private ArrayList<CartCheckout> cartCheckoutArrayList;
    private Activity mActivity;
    private CartCheckout cartCheckout;
    List<String> quantityList;
    List<Integer> itemCounts = new ArrayList<>();

    public CartCheckoutAdapter(Activity activity, ArrayList<CartCheckout> list) {
        this.mActivity = activity;
        this.cartCheckoutArrayList = list;
    }


    @NonNull
    @Override
    public CartCheckoutAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartCheckoutAdapter.CartViewHolder holder, int position) {
        cartCheckout = cartCheckoutArrayList.get(position);
        quantityList = Collections.singletonList(cartCheckout.getQuantity());

        //return the quantity of each position
        for (int i = 0; i < quantityList.size(); i++) {
            int itemCount = Integer.parseInt(quantityList.get(i));
            itemCounts.add(itemCount);
        }

        Picasso.get().load(cartCheckout.getImageUrl()).into(holder.productImage);
        holder.productName.setText(cartCheckout.getProductName());
        holder.productPrice.setText(cartCheckout.getProductPrice());
        holder.productQuantity.setText(cartCheckout.getQuantity());
        holder.productTotal.setText(cartCheckout.getTotal());

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                int itemCount = itemCounts.get(position);

                // Check if itemCount is less than 99
                if (itemCount < 99) {
                    itemCount++;
                    itemCounts.set(position, itemCount);
                    holder.productQuantity.setText(String.valueOf(itemCount));
                }

                if (itemCount >= 99) {
                    holder.productQuantity.setText("0");
                    itemCounts.set(position, 0);
                }
            }
        });

        holder.subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                int itemCount = itemCounts.get(position);

                if (itemCount > 0) {
                    itemCount--;
                    itemCounts.set(position, itemCount);
                    holder.productQuantity.setText(String.valueOf(itemCount));
                }
            }
        });

        holder.deleteBtn.setTag(cartCheckout.getId());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TIUUU", "productId: " + (String) v.getTag());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the product from Firebase
                        Toast.makeText(v.getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();

                        String productId = (String) v.getTag();
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                        Query query = databaseRef.child("Cart")
                                .orderByChild("product_id")
                                .equalTo(productId);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String cartItemId = childSnapshot.getKey();
                                        databaseRef.child("Cart").child(cartItemId).removeValue();
                                    }
                                } else {
                                    Toast.makeText(v.getContext(), "Product not found in cart", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(v.getContext(), "Error deleting product: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
            }
        });

        holder.productQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Get the position of the item in the list or array
                int position = holder.getAdapterPosition();

                // Retrieve the current text from the EditText
                String quantityText = s.toString();

                // Parse the quantityText to an integer
                int itemCount;
                try {
                    itemCount = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {
                    // Invalid input, set itemCount to 0 or handle the error
                    itemCount = 0;
                }

                // Update the itemCounts list with the new value
                itemCounts.set(position, itemCount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (cartCheckoutArrayList==null) ? 0 : cartCheckoutArrayList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        EditText productQuantity;
        TextView productTotal;
        ImageView addBtn;
        ImageView subtractBtn;

        ImageView deleteBtn;

        public CartViewHolder(@NonNull View v) {
            super(v);
            productImage = v.findViewById(R.id.product_image);
            productName = v.findViewById(R.id.product_name);
            productPrice = v.findViewById(R.id.product_price);
            productQuantity = v.findViewById(R.id.product_quantity);
            productTotal = v.findViewById(R.id.product_total);
            addBtn = v.findViewById(R.id.add_btn);
            subtractBtn = v.findViewById(R.id.subtract_btn);
            deleteBtn = v.findViewById(R.id.delete_btn);

        }
    }
}
