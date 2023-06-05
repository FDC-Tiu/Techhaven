package com.example.techhaven.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techhaven.CartCheckout;
import com.example.techhaven.CartCheckoutAdapter;
import com.example.techhaven.R;
import com.example.techhaven.databinding.FragmentCartBinding;
import com.example.techhaven.databinding.FragmentCartBinding;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CartViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.recycler_product_list_view);

        fetchCart();

        return root;
    }

    private void fetchCart() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("Cart").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<CartCheckout> cartList = new ArrayList<>();

                        Gson gson = new Gson();

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
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

                        Log.d("teeest", "onDataChange: " + new Gson().toJson(cartList));
                        cartCheckoutAdapter = new CartCheckoutAdapter(getActivity(), cartList);
                        recyclerView.setAdapter(cartCheckoutAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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