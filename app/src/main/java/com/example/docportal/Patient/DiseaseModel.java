package com.example.docportal.Patient;

public class DiseaseModel {
    public DiseaseModel() {
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public DiseaseModel(String symptom, String disease, String description, String organ) {
        this.symptom = symptom;
        this.disease = disease;
        this.description = description;
        this.organ = organ;
    }

    private String symptom, disease, description, organ;

}
