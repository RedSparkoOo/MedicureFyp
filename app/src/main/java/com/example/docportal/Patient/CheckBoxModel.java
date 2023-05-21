package com.example.docportal.Patient;

public class CheckBoxModel {
    private String symptom;
    private int checkedId;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private String details;

    public CheckBoxModel(String symptom, int checkedId, String details) {
        this.symptom = symptom;
        this.checkedId = checkedId;
        this.details = details;
    }

    public CheckBoxModel() {
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }

}
