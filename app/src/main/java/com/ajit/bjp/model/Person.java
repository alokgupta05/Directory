package com.ajit.bjp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    private String namePerson;
    private String mobileNo;
    private String officeNo;
    private String homeNo;
    private String floor;
    private String deskNo;
    private String paName;
    private String paNo;
    private String email;
    private String dept;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    private String designation;
    private String charge;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person(){

    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(String officeNo) {
        this.officeNo = officeNo;
    }

    public String getHomeNo() {
        return homeNo;
    }

    public void setHomeNo(String homeNo) {
        this.homeNo = homeNo;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDeskNo() {
        return deskNo;
    }

    public void setDeskNo(String deskNo) {
        this.deskNo = deskNo;
    }

    public String getPaName() {
        return paName;
    }

    public void setPaName(String paName) {
        this.paName = paName;
    }

    public String getPaNo() {
        return paNo;
    }

    public void setPaNo(String paNo) {
        this.paNo = paNo;
    }

    protected Person(Parcel in) {
        namePerson = in.readString();
        mobileNo = in.readString();
        officeNo = in.readString();
        homeNo = in.readString();
        floor = in.readString();
        deskNo = in.readString();
        paName = in.readString();
        paNo = in.readString();
        email = in.readString();
        dept = in.readString();
        designation =in.readString();
        charge = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namePerson);
        dest.writeString(mobileNo);
        dest.writeString(officeNo);
        dest.writeString(homeNo);
        dest.writeString(floor);
        dest.writeString(deskNo);
        dest.writeString(paName);
        dest.writeString(paNo);
        dest.writeString(email);
        dest.writeString(dept);
        dest.writeString(designation);
        dest.writeString(charge);
    }
}
