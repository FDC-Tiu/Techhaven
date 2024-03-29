package com.example.techhaven.ui.cart;

import static java.lang.Double.parseDouble;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techhaven.CartCheckout;
import com.example.techhaven.CartCheckoutAdapter;
import com.example.techhaven.CheckoutActivity;
import com.example.techhaven.PaymentAdapter;
import com.example.techhaven.PaypalActivity;
import com.example.techhaven.R;
import com.example.techhaven.databinding.FragmentCartBinding;
import com.example.techhaven.databinding.FragmentCartBinding;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private RecyclerView recyclerView;
    private CartCheckoutAdapter cartCheckoutAdapter;
    private TextView checkoutBtn;
    private ArrayList<CartCheckout> cartList;
    private PaymentAdapter paymentAdapter;
    private SpinKitView spinKitView;
    private TextView noData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.recycler_product_list_view);
        checkoutBtn = root.findViewById(R.id.checkout_btn);
        spinKitView = root.findViewById(R.id.spin_kit);
        noData = root.findViewById(R.id.no_data);

        fetchCart();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tiuuu", "onClick: " + new Gson().toJson(cartList));
                //TODO pass cartList
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putParcelableArrayListExtra("cart_list",cartList);
                startActivity(intent);
            }
        });

        return root;
    }

    private void fetchCart() {
        spinKitView.setVisibility(View.VISIBLE);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList = new ArrayList<>();

                Gson gson = new Gson();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String values = gson.toJson(dataSnapshot.getValue());

                    Map<String, Object> data = gson.fromJson(values, Map.class);
                    String imageUrl = (String) data.get("image_url");
                    String productName = (String) data.get("product_name");
                    String productPrice = (String) data.get("product_price");
                    String productQuantity = (String) data.get("product_quantity");

                    String uniqueKey = dataSnapshot.getKey();
                    Log.d("TIUUU", "onDataChange: " + uniqueKey);
                    CartCheckout cartCheckout = dataSnapshot.getValue(CartCheckout.class);
                    if (cartCheckout != null) {
                        cartCheckout.setId(uniqueKey);
                        cartCheckout.setImageUrl(imageUrl);
                        cartCheckout.setProductName(productName);
                        cartCheckout.setProductPrice(productPrice);
                        cartCheckout.setQuantity(productQuantity);
                    }

                    cartList.add(cartCheckout);
                }
                spinKitView.setVisibility(View.GONE);
                if (cartList.isEmpty()) {
                    checkoutBtn.setEnabled(false);
                    checkoutBtn.setAlpha(0.5f);
                    noData.setVisibility(View.VISIBLE);

                } else {
                    checkoutBtn.setEnabled(true);
                    checkoutBtn.setAlpha(1.0f);
                    noData.setVisibility(View.GONE);
                }

                Log.d("teeest", "onDataChange: " + new Gson().toJson(cartList));
                cartCheckoutAdapter = new CartCheckoutAdapter(getActivity(), cartList);
                recyclerView.setAdapter(cartCheckoutAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                spinKitView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Hide the toolbar back button icon
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
