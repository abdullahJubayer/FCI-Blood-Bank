package com.fci.androCroder.BD;

import java.io.File;
import java.io.Serializable;

public class UserModel{
    private String name;
    private String imagePath;
    private String department;
    private String batch;
    private String phone1;
    private String address;
    private String gender;
    private String bloodGroup;
    private String lastDonateDate;
    private String give_blood;
    private String password;
    private String uId;

    public UserModel(String name, String imagePath, String department, String batch, String phone1, String address, String gender, String bloodGroup, String lastDonateDate, String give_blood, String password, String uId) {
        this.name = name;
        this.imagePath = imagePath;
        this.department = department;
        this.batch = batch;
        this.phone1 = phone1;
        this.address = address;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.lastDonateDate = lastDonateDate;
        this.give_blood = give_blood;
        this.password = password;
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDepartment() {
        return department;
    }

    public String getBatch() {
        return batch;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getLastDonateDate() {
        return lastDonateDate;
    }

    public String getGive_blood() {
        return give_blood;
    }

    public String getPassword() {
        return password;
    }

    public String getuId() {
        return uId;
    }
}
