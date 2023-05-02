package com.example.docportal.Patient;

public class TransactionModel {


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public TransactionModel(String item, String time, String price, String seller) {
        this.item = item;
        this.time = time;
        this.price = price;
        this.seller = seller;
    }
    public TransactionModel() {
    }
    private String item;
    private String time;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    private String seller;
    private String price;

}
