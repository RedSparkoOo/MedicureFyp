package com.example.docportal.Doctor;

public class Appointment {
    private String AppointedDoctorId;
    private String AppointedPatientId;
    private String ApprovedDoctorCell;
    private String ApprovedDoctorName;
    private String ApprovedPatientCell;
    private String ApprovedPatientName;
    private String ApprovedAppointmentDate;
    private String ApprovedAppointmentTime;
    public Appointment(String appointedDoctorId, String appointedPatientId, String approvedDoctorCell, String approvedDoctorName, String approvedPatientCell, String approvedPatientName, String approvedAppointmentDate, String approvedAppointmentTime) {
        AppointedDoctorId = appointedDoctorId;
        AppointedPatientId = appointedPatientId;
        ApprovedDoctorCell = approvedDoctorCell;
        ApprovedDoctorName = approvedDoctorName;
        ApprovedPatientCell = approvedPatientCell;
        ApprovedPatientName = approvedPatientName;
        ApprovedAppointmentDate = approvedAppointmentDate;
        ApprovedAppointmentTime = approvedAppointmentTime;
    }

    public Appointment() {
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
