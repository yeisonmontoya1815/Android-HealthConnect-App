package com.example.healthconnect.utils.database;

import java.io.Serializable;

public class Appointment implements Serializable {
    private int id;
    private Patient patient;
    private String appointmentType;
    private int appointmentDate;
    private int appointmentTime;
    private String notes;
    private String medicines;
    private String exams;
    private boolean isDone;

    public Appointment(int id, Patient patient, String appointmentType, int appointmentDate, int appointmentTime, String notes, String medicines, String exams, boolean isDone) {
        this.id = id;
        this.patient = patient;
        this.appointmentType = appointmentType;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.notes = notes;
        this.medicines = medicines;
        this.exams = exams;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public int getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(int appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public int getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(int appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getExams() {
        return exams;
    }

    public void setExams(String exams) {
        this.exams = exams;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
