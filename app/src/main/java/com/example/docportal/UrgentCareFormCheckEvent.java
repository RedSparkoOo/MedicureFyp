package com.example.docportal;

import android.widget.TextView;

import java.util.Arrays;
import java.util.regex.Pattern;

public class UrgentCareFormCheckEvent {

    private String empty = "String is Empty";
    private String iPhone = "invalid phone number";
    private String iUserName = "invalid username";

    private String nameRegex = "^[aA-zZ]{1,25} [aA-zZ]{1,25}([aA-zZ]{1,25})$";
    private String phoneRegex = "^[0-9]{10}$";

    public Boolean checkName(TextView textView){return patternMatcher(textView, nameRegex, iUserName);}

    public Boolean checkPhone(TextView textView){return patternMatcher(textView, phoneRegex, iPhone);}

    private boolean patternMatcher(TextView textViews, String regex, String setError){
        if(!Pattern.compile(regex).matcher(textViews.getText().toString()).find()){
            textViews.setError(setError);
            return false;
        }
        return true;
    }

    public Boolean isEmpty(TextView[] textViews) {
        Arrays.stream(textViews).filter(t->t.getText().toString().isEmpty()).forEach(t->t.setError(empty));
        return Arrays.stream(textViews).anyMatch(""::equals);
    }

}


