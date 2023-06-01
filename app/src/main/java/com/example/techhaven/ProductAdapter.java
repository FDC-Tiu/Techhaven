package com.example.techhaven;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Activity context;
    private List<Product> productList;
    private AdapterView.OnItemClickListener listener;
    public static final String PID = "com.example.techhaven.extra.PID";

    public ProductAdapter(Activity context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_product_layout, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        String imageUrl = product.getThumbnail();

        Picasso.get().load(imageUrl).into(holder.imageViewProduct);

        holder.textViewProductName.setText(product.getTitle());
        holder.textViewStock.setText("Stock: " + product.getStock());
        holder.textViewPrice.setText("₱" + String.format("%.2f", product.getPrice()));

        holder.productContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(PID, String.valueOf(product.getId()));
            context.startActivityForResult(intent,MainActivity.DETAIL_REQUEST_CODE);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout productContainer;
        ImageView imageViewProduct;
        ImageView imageRight;
        TextView textViewProductName;
        TextView textViewStock;
        TextView textViewPrice;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productContainer = itemView.findViewById(R.id.product_container);
            imageViewProduct = itemView.findViewById(R.id.image);
            imageRight = itemView.findViewById(R.id.image_right);
            textViewProductName = itemView.findViewById(R.id.tv_title);
            textViewStock = itemView.findViewById(R.id.tv_stock);
            textViewPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
