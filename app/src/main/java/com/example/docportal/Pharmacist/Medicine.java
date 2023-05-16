package com.example.docportal.Pharmacist;

public class Medicine {
    private String Title;
    private String seller;
    private String Identifier;
    private String Description;
    private String Price;
    private String Image;
    private String Milligram;
    private String Quantity;

    public Medicine(String title, String description, String price, String image, String milligram, String quantity, String identifier, String seller) {
        this.Title = title;
        this.Description = description;
        this.Price = price;
        this.Image = image;
        this.Milligram = milligram;
        this.Quantity = quantity;
        this.Identifier = identifier;
        this.seller = seller;
    }


    public Medicine() {
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

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
