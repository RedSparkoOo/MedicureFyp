package com.example.docportal.Pharmacist;

import android.graphics.Bitmap;

import org.checkerframework.common.subtyping.qual.Bottom;

public class Medicine {
    private String title;
    public Medicine(String title, String description, String price, Bitmap image, String milligram, String quantity) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.milligram = milligram;
        this.qunatity = quantity;
    }
    public Medicine(){}

    private String description;
    private String price;
    private Bitmap image;
    private String milligram;

    public String getQunatity() {
        return qunatity;
    }

    public void setQunatity(String qunatity) {
        this.qunatity = qunatity;
    }

    private String qunatity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getMilligram() {
        return milligram;
    }

    public void setMilligram(String milligram) {
        this.milligram = milligram;
    }


}
