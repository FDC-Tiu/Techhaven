package com.example.techhaven;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CartCheckout implements Parcelable {

    private String mId;
    private String productId;
    private String imageUrl;
    private String productName;
    private String productPrice;
    private String quantity;
    private String total;

    public CartCheckout() {

    }


    public CartCheckout(String id, String productId, String imageUrl, String productName, String productPrice, String quantity, String total) {
        this.mId = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.total = total;
    }

    protected CartCheckout(Parcel in) {
        mId = in.readString();
        productId = in.readString();
        imageUrl = in.readString();
        productName = in.readString();
        productPrice = in.readString();
        quantity = in.readString();
        total = in.readString();
    }

    public static final Creator<CartCheckout> CREATOR = new Creator<CartCheckout>() {
        @Override
        public CartCheckout createFromParcel(Parcel in) {
            return new CartCheckout(in);
        }

        @Override
        public CartCheckout[] newArray(int size) {
            return new CartCheckout[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(productId);
        dest.writeString(imageUrl);
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeString(quantity);
        dest.writeString(total);
    }

    public void setTotalPrice(String valueOf) {
    }
}
