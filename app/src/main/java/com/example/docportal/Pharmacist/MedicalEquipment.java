package com.example.docportal.Pharmacist;

public class MedicalEquipment {


    private String Image;
    private String Title;
    private String Price;
    private String Quantity;
    private String Description;

    public MedicalEquipment(String image, String title, String price, String quantity, String description) {
        Image = image;
        Title = title;
        Price = price;
        Quantity = quantity;
        Description = description;
    }

    public MedicalEquipment() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
