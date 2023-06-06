package com.example.techhaven;

public class Product {
    public int id;
    public String title;
    public String desc;
    public double price;
    public int stock;
    public String brand;
    public String category;
    public String thumbnail;

    public Product() {
        this.id = 0;
        this.title = "";
        this.desc = "";
        this.price = 0.0;
        this.stock = 0;
        this.brand = "";
        this.category = "";
        this.thumbnail = "";
    }

    public Product(int id, String title, String desc, double price, int stock, String brand, String category, String thumbnail) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getName() {
        return 0;
    }
}
