package com.example.docportal.Doctor;

public class AppointmentHolder {
    private String PatientImage;
    private String PatientName;
    private String PatientPhoneNo;
    private String AppointedDoctorID;
    private String DoctorName;
    private String DoctorPhoneNo;
    private String AppointmentDate;
    private String AppointmentTime;
    private String AppointmentDescription;
    private String PatientID;

    public AppointmentHolder() {

    }

    public AppointmentHolder(String patientName, String patientPhoneNo, String appointedDoctorID, String doctorName, String doctorPhoneNo, String appointmentDate, String appointmentTime, String appointmentDescription, String patientID, String patientImage) {
        PatientName = patientName;
        PatientPhoneNo = patientPhoneNo;
        AppointedDoctorID = appointedDoctorID;
        DoctorName = doctorName;
        DoctorPhoneNo = doctorPhoneNo;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
        AppointmentDescription = appointmentDescription;
        PatientID = patientID;
        PatientImage = patientImage;
    }

    public String getPatientImage() {
        return PatientImage;
    }

    public void setPatientImage(String patientImage) {
        PatientImage = patientImage;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientPhoneNo() {
        return PatientPhoneNo;
    }

    public void setPatientPhoneNo(String patientPhoneNo) {
        PatientPhoneNo = patientPhoneNo;
    }

    public String getAppointedDoctorID() {
        return AppointedDoctorID;
    }

    public void setAppointedDoctorID(String appointedDoctorID) {
        AppointedDoctorID = appointedDoctorID;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDoctorPhoneNo() {
        return DoctorPhoneNo;
    }

    public void setDoctorPhoneNo(String doctorPhoneNo) {
        DoctorPhoneNo = doctorPhoneNo;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getAppointmentDescription() {
        return AppointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        AppointmentDescription = appointmentDescription;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
}
