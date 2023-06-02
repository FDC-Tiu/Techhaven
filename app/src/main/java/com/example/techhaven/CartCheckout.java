package com.example.techhaven;

public class CartCheckout {

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
}
