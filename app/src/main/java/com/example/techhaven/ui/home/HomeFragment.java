package com.example.techhaven.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.techhaven.DetailsActivity;
import com.example.techhaven.Product;
import com.example.techhaven.ProductAdapter;
import com.example.techhaven.R;
import com.example.techhaven.VolleySingleton;
import com.example.techhaven.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<Product> productList;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private String mUrl;
    private TextView mNoData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mNoData = root.findViewById(R.id.no_data);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getProducts("");

        return root;
    }

    public void getProducts(String query) {
        final ProgressDialog progressDialog = null;
        productList = new ArrayList<>();
        if (query.equals("")){
            mUrl="https://dummyjson.com/products";
        } else{
            mUrl="https://dummyjson.com/products/search?q=" + query;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, mUrl,
                response -> {
                    try {
                        Log.e("Products: ", response);
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        //check if array is not empty or 0
                        if(jsonArray.length()==0) {
                            Log.d("Tiu", "BLANK");
                            mNoData.setVisibility(View.VISIBLE);
                        } else {
                            mNoData.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            Product product = new Product();
                            product.setId(object.getInt("id"));
                            product.setTitle(object.getString("title"));
                            product.setPrice(object.getDouble("price"));
                            product.setStock(object.getInt("stock"));
                            product.setThumbnail(object.getString("thumbnail"));

                            productList.add(product);
                        }
                        adapter = new ProductAdapter(requireActivity(), productList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + e, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Failed" + error, Toast.LENGTH_SHORT).show();
                });
        VolleySingleton.getmInstance().addToRequestQueue(stringRequest);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}