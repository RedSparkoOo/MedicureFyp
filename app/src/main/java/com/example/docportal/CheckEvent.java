package com.example.docportal;

import android.util.Patterns;
import android.widget.TextView;

import java.util.Arrays;
import java.util.regex.Pattern;

public class CheckEvent {
    private final String empty = "Field is Empty";
    private final String iUserName = "invalid username";
    private final String iEmail = "invalid email address";
    private final String iPhone = "invalid phone number";
    private final String license = "invalid license number";
    private final String medicine = "invalid medicine name";
    private final String iPassword = "The password must contain at least one lowercase character, one uppercase character, one digit, one special character, and a length between 8 to 20.";
    private final String iItem = "First letter should be capital";

    private final String nameRegex = "^[aA-zZ]{1,25} [aA-zZ]{1,25}([aA-zZ]{1,25})$";
    private final String medicineRegex = "^[aA-zZ]{3,25}$";
    private final String phoneRegex = "^[0-9]{11}$";
    private final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()[{}]:;',?/*~$^+=<>]).{8,20}$";


    private final String CNICRegex = "^[1-9][0-9]{4}[0-9]{7}[1-9]$";
    private final String licenseRegex = "^PMC-\\d{4}-\\d{4}$";

    private final String itemName = "^[A-Z][aA-zZ ]{1,30}$";


    public Boolean checkName(TextView textView) {
        return patternMatcher(textView, nameRegex, iUserName);
    }

    public Boolean checkPhone(TextView textView) {
        return patternMatcher(textView, phoneRegex, iPhone);
    }

    public Boolean checkPassword(TextView textView) {
        return patternMatcher(textView, passwordRegex, iPassword);
    }

    public Boolean checkLicense(TextView textView) {
        return patternMatcher(textView, licenseRegex, license);
    }

    public Boolean checkMedicine(TextView textView) {
        return patternMatcher(textView, medicineRegex, medicine);
    }


    public Boolean checkItemName(TextView textView) {
        return patternMatcher(textView, itemName, iItem);
    }

    public Boolean checkEmail(TextView textView) {
        if (!Patterns.EMAIL_ADDRESS.matcher(textView.getText().toString()).matches()) {
            textView.setError(iEmail);
            return false;
        }
        return true;
    }

    private boolean patternMatcher(TextView textViews, String regex, String setError) {
        if (!Pattern.compile(regex).matcher(textViews.getText().toString()).matches()) {
            textViews.setError(setError);
            textViews.requestFocus();
            return false;
        }
        return true;
    }

    public Boolean isEmpty(TextView[] textViews) {
        Arrays.stream(textViews).filter(t -> t.getText().toString().isEmpty()).forEach(t -> t.setError(empty));
        for (TextView textView : textViews) {
            if (textView.getText().toString().isEmpty()) {
                textView.requestFocus();
                return true;
            }
        }
        return false;
    }
}


