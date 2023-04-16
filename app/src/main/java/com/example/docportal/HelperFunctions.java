package com.example.docportal;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class HelperFunctions {

    public void snackBarShow(View snack_bar_layout, String msg){
        Snackbar.make(snack_bar_layout,msg,Snackbar.LENGTH_SHORT).show();

    }


}