package com.example.docportal;

import android.util.Patterns;
import android.widget.TextView;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CheckEvent {
    private String empty = "String is Empty";
    private String iUserName = "invalid username";
    private String iEmail = "invalid email address";
    private String iPhone = "invalid phone number";
    private String iPassword = "The password must contain at least one lowercase character, one uppercase character, one digit, one special character, and a length between 8 to 20.";
    private String iCNIC = "invalid CNIC";
    private String iItem = "invalid Title";

    private String nameRegex = "^[aA-zZ]{1,25} [aA-zZ]{1,25}([aA-zZ]{1,25})$";
    private String phoneRegex = "^[0-9]{10}$";
    private String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private String CNICRegex = "^[1-9][0-9]{4}[0-9]{7}[1-9]$";
    private String itemName = "^[aA-zZ ]{1,30}$";

    public Boolean isEmpty(TextView[] textViews) {
        Arrays.stream(textViews).filter(t->t.getText().toString().isEmpty()).forEach(t->t.setError(empty));
        return Arrays.stream(textViews).anyMatch(""::equals);
    }

    public Boolean checkName(TextView textView){return patternMatcher(textView, nameRegex, iUserName);}

    public Boolean checkPhone(TextView textView){return patternMatcher(textView, phoneRegex, iPhone);}
    public Boolean checkPassword(TextView textView){return patternMatcher(textView, passwordRegex, iPassword);}

    public Boolean checkCNIC(TextView textView){return patternMatcher(textView, CNICRegex, iCNIC);}
    public Boolean checkItemName(TextView textView){return patternMatcher(textView, itemName, iItem);}

    public Boolean checkEmail(TextView textView){
        if(!Patterns.EMAIL_ADDRESS.matcher(textView.getText().toString()).matches()){
            textView.setError(iEmail);
            return false;
        }
        return true;
    }
    private boolean patternMatcher(TextView textView, String regex, String setError){
        if(!Pattern.compile(regex).matcher(textView.getText().toString()).matches()){
            textView.setError(setError);
            return false;
        }
        return true;
    }

}
