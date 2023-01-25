package com.example.docportal.Patient;

import android.view.LayoutInflater;

public class AppointmentHelperClass {
    String appointeeName;
    String appointeePhone;
    String date;
    String time;


    public AppointmentHelperClass(String appointeeName, String appointeePhone, String date, String time) {
        this.appointeeName = appointeeName;
        this.appointeePhone = appointeePhone;
        this.date = date;
        this.time = time;
    }

    public String getAppointeeName() {


        return appointeeName;
    }

    public void setAppointeeName(String appointeeName) {
        this.appointeeName = appointeeName;
    }

    public String getAppointeePhone() {
        return appointeePhone;
    }

    public void setAppointeePhone(String appointeePhone) {
        this.appointeePhone = appointeePhone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
