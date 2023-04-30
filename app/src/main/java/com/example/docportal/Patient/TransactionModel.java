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

    public TransactionModel(String item, String time, String price) {
        this.item = item;
        this.time = time;
        this.price = price;
    }
    public TransactionModel() {
    }
    private String item;
    private String time;
    private String price;

}
