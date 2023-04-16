package com.example.docportal.Pharmacist;

public class Medicine {
    private String Title;
    public Medicine(String title, String description, String price, String image, String milligram, String quantity) {
        this.Title = title;
        this.Description = description;
        this.Price = price;
        this.Image = image;
        this.Milligram = milligram;
        this.Quantity = quantity;
    }
    public Medicine(){}


    private String Description;
    private String Price;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    private String Image;
    private String Milligram;


    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    private String Quantity;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }


    public String getMilligram() {
        return Milligram;
    }

    public void setMilligram(String milligram) {
        this.Milligram = milligram;
    }


}
