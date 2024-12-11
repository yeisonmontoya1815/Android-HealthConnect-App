package com.example.healthconnect.utils.database;

import java.io.Serializable;
import java.util.Calendar;

public class Patient implements Serializable {
    private int id;
    private String name;
    private int dateOfBirth; // Format: YYYYMMDD
    private String gender;
    private String phoneNumber;
    private String email;
    private double height;
    private double weight;

    public Patient(int id, String name, int dateOfBirth, String gender, String phoneNumber, String email, double height, double weight) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.height = height;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(int age) {
        this.dateOfBirth = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        int yearOfBirth = dateOfBirth / 10000;
        int monthOfBirth = (dateOfBirth % 10000) / 100;
        int dayOfBirth = dateOfBirth % 100;

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - yearOfBirth;

        if (currentMonth < monthOfBirth || (currentMonth == monthOfBirth && currentDay < dayOfBirth)) {
            age--;
        }

        return age;
    }
}

