package com.example.docportal.Patient;

public class BloodBankModel {
    private String labName;
    private String name;
    private String quantity;
    private String logo;

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    private String endtime;
    private String acceptor;
    private String category;
    private String description;
    private String donor;
    private String latitude;
    private String logoUrl;
    private String longitude;
    private String price;
    private String testName;
    private String timing;

    public BloodBankModel() {


    }

    public BloodBankModel(String acceptor, String category, String description, String donor, String labName, String latitude, String logoUrl, String longitude, String price, String testName, String timing, String bloodBankName, String logo, String quantity, String endtime) {
        this.acceptor = acceptor;
        this.category = category;
        this.description = description;
        this.donor = donor;
        this.labName = labName;
        this.latitude = latitude;
        this.logoUrl = logoUrl;
        this.longitude = longitude;
        this.price = price;
        this.testName = testName;
        this.timing = timing;
        this.name = bloodBankName;
        this.logo = logo;
        this.quantity = quantity;
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }
}
