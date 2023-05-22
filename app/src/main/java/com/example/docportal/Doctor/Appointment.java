package com.example.docportal.Doctor;

import java.util.HashMap;

public class Appointment {
    private String AppointedDoctorId;
    private String AppointedPatientId;
    private String ApprovedDoctorCell;
    private String Disease_name;
    private String Additional_Description;

    public String getApprovedPatientImage() {
        return ApprovedPatientImage;
    }

    public void setApprovedPatientImage(String approvedPatientImage) {
        ApprovedPatientImage = approvedPatientImage;
    }

    private String ApprovedPatientImage;
    private String ApprovedDoctorName;

    public HashMap<String, Object> getSymptom_Severity() {
        return Symptom_Severity;
    }

    public void setSymptom_Severity(HashMap<String, Object> symptom_Severity) {
        Symptom_Severity = symptom_Severity;
    }

    private HashMap<String, Object> Symptom_Severity = new HashMap<>();

    public HashMap<String, Object> getSymptom_details() {
        return Symptom_details;
    }

    public void setSymptom_details(HashMap<String, Object> symptom_details) {
        Symptom_details = symptom_details;
    }

    private HashMap<String, Object> Symptom_details = new HashMap<>();
    private String ApprovedPatientCell;
    private String ApprovedPatientName;
    private String ApprovedAppointmentDate;
    private String ApprovedAppointmentTime;
    public Appointment(String appointedDoctorId, String appointedPatientId, String approvedDoctorCell, String approvedDoctorName, String approvedPatientCell, String approvedPatientName, String approvedAppointmentDate, String approvedAppointmentTime, String approvedPatientImage, HashMap<String, Object> Symptom_Severity, HashMap<String, Object> Symptom_details, String disease, String description) {
        AppointedDoctorId = appointedDoctorId;
        AppointedPatientId = appointedPatientId;
        ApprovedDoctorCell = approvedDoctorCell;
        ApprovedDoctorName = approvedDoctorName;
        ApprovedPatientCell = approvedPatientCell;
        ApprovedPatientName = approvedPatientName;
        ApprovedAppointmentDate = approvedAppointmentDate;
        ApprovedAppointmentTime = approvedAppointmentTime;
        ApprovedPatientImage = approvedPatientImage;
        Disease_name = disease;
        Additional_Description = description;
        this.Symptom_details = Symptom_details;
        this.Symptom_Severity = Symptom_Severity;
    }

    public Appointment() {
    }

    public String getDisease_name() {
        return Disease_name;
    }

    public void setDisease_name(String disease_name) {
        Disease_name = disease_name;
    }

    public String getAdditional_Description() {
        return Additional_Description;
    }

    public void setAdditional_Description(String additional_Description) {
        Additional_Description = additional_Description;
    }

    public String getAppointedDoctorId() {
        return AppointedDoctorId;
    }

    public void setAppointedDoctorId(String appointedDoctorId) {
        AppointedDoctorId = appointedDoctorId;
    }

    public String getAppointedPatientId() {
        return AppointedPatientId;
    }

    public void setAppointedPatientId(String appointedPatientId) {
        AppointedPatientId = appointedPatientId;
    }

    public String getApprovedDoctorCell() {
        return ApprovedDoctorCell;
    }

    public void setApprovedDoctorCell(String approvedDoctorCell) {
        ApprovedDoctorCell = approvedDoctorCell;
    }

    public String getApprovedDoctorName() {
        return ApprovedDoctorName;
    }

    public void setApprovedDoctorName(String approvedDoctorName) {
        ApprovedDoctorName = approvedDoctorName;
    }

    public String getApprovedPatientCell() {
        return ApprovedPatientCell;
    }

    public void setApprovedPatientCell(String approvedPatientCell) {
        ApprovedPatientCell = approvedPatientCell;
    }

    public String getApprovedPatientName() {
        return ApprovedPatientName;
    }

    public void setApprovedPatientName(String approvedPatientName) {
        ApprovedPatientName = approvedPatientName;
    }

    public String getApprovedAppointmentDate() {
        return ApprovedAppointmentDate;
    }

    public void setApprovedAppointmentDate(String approvedAppointmentDate) {
        ApprovedAppointmentDate = approvedAppointmentDate;
    }

    public String getApprovedAppointmentTime() {
        return ApprovedAppointmentTime;
    }

    public void setApprovedAppointmentTime(String approvedAppointmentTime) {
        ApprovedAppointmentTime = approvedAppointmentTime;
    }


}