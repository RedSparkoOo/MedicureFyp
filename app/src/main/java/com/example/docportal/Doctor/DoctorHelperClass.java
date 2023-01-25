package com.example.docportal.Doctor;

import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DoctorHelperClass {

  String _FirstName;
  String _LastName;
  String _emailAddress;
  String _HomeAddress;
  String _Password;
  String _PhoneNo;
  String _Cnic;
  String _DOB;
  String _Gender;
  String _License;
  String _Category;

    //Appointment Class;



    public DoctorHelperClass(String _FirstName, String _LastName, String _emailAddress,String _HomeAddress, String _Password, String _PhoneNo,String _Cnic, String _Gender,String _DOB,String _License,String _Category) {

        this._FirstName = _FirstName;
        this._LastName = _LastName;
        this._emailAddress = _emailAddress;
        this._HomeAddress = _HomeAddress;
        this._Password = _Password;
        this._PhoneNo = _PhoneNo;
        this._Cnic = _Cnic;
        this._DOB = _DOB;
        this._Gender = _Gender;
        this._License = _License;
        this._Category = _Category;
    }


    public String get_FirstName() {
        return _FirstName;
    }

    public void set_FirstName(String _FirstName) {
        this._FirstName = _FirstName;
    }

    public String get_LastName() {
        return _LastName;
    }

    public void set_LastName(String _LastName) {
        this._LastName = _LastName;
    }

    public String get_emailAddress() {
        return _emailAddress;
    }

    public void set_emailAddress(String _emailAddress) {
        this._emailAddress = _emailAddress;
    }

    public String get_HomeAddress() {
        return _HomeAddress;
    }

    public void set_HomeAddress(String _HomeAddress) {
        this._HomeAddress = _HomeAddress;
    }

    public String get_Password() {
        return _Password;
    }

    public void set_Password(String _Password) {
        this._Password = _Password;
    }

    public String get_PhoneNo() {
        return _PhoneNo;
    }

    public void set_PhoneNo(String _PhoneNo) {
        this._PhoneNo = _PhoneNo;
    }

    public String get_Cnic() {
        return _Cnic;
    }

    public void set_Cnic(String _Cnic) {
        this._Cnic = _Cnic;
    }

    public String get_DOB() {
        return _DOB;
    }

    public void set_DOB(String _DOB) {
        this._DOB = _DOB;
    }

    public String get_Gender() {
        return _Gender;
    }

    public void set_Gender(String _Gender) {
        this._Gender = _Gender;
    }

    public String get_License() {
        return _License;
    }

    public void set_License(String _License) {
        this._License = _License;
    }

    public String get_Category() {
        return _Category;
    }

    public void set_Category(String _Category) {
        this._Category = _Category;
    }
}
