package com.example.docportal;

import static com.example.docportal.R.layout.spinner_item;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Singleton {
    private ArrayAdapter adapter;

    public void setAdatper(Context context, Spinner spinner, String[] string) {
        adapter = new ArrayAdapter(context, spinner_item, string);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    public void showToast(Context context, String message) {
        runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }


    public Intent getIntent(Context context, Class activity) {
        return new Intent(context, activity);
    }

    public void openActivity(Context context, Class<?> activityClass) {
        context.startActivity(new Intent(context, activityClass));
    }

    public void logout(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_box_layout);
        dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.edges));
        dialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.alert_animation;
        Button confirm = dialog.findViewById(R.id.alert_confirm);
        TextView cancel = dialog.findViewById(R.id.alert_cancel);
        TextView alert_msg = dialog.findViewById(R.id.alert_msg);
        alert_msg.setText("Are you sure you want to logout?");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(context, SplashScreenEntrance.class);
                dialog.dismiss();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
