package com.ajit.bjp.model.karyakarta;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class KaryaKarta implements Parcelable {

    private String fullName; //column 2
    private Date dob; // column 3
    private String villageName; // column 4
    private String occupation; // column 5
    private String bloodGroup; // column 6
    private String mobileNo; // column 7
    private String whatsAppNo; // column 8
    private String familyHead; // column 9
    private String wadiWastiName; // column 10
    private String gramPanchayatWardNo; // column 11
    private String vidhanSabhaWardNo; // column 12
    private String jilaParishadGat; // column 13
    private String information; // column 14
    private Date birthday;

    public KaryaKarta() {

    }

    private KaryaKarta(Parcel in) {
        fullName = in.readString();
        villageName = in.readString();
        occupation = in.readString();
        bloodGroup = in.readString();
        mobileNo = in.readString();
        whatsAppNo = in.readString();
        familyHead = in.readString();
        wadiWastiName = in.readString();
        gramPanchayatWardNo = in.readString();
        vidhanSabhaWardNo = in.readString();
        jilaParishadGat = in.readString();
        information = in.readString();
    }

    public static final Creator<KaryaKarta> CREATOR = new Creator<KaryaKarta>() {
        @Override
        public KaryaKarta createFromParcel(Parcel in) {
            return new KaryaKarta(in);
        }

        @Override
        public KaryaKarta[] newArray(int size) {
            return new KaryaKarta[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getWhatsAppNo() {
        return whatsAppNo;
    }

    public void setWhatsAppNo(String whatsAppNo) {
        this.whatsAppNo = whatsAppNo;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public String getWadiWastiName() {
        return wadiWastiName;
    }

    public void setWadiWastiName(String wadiWastiName) {
        this.wadiWastiName = wadiWastiName;
    }

    public String getGramPanchayatWardNo() {
        return gramPanchayatWardNo;
    }

    public void setGramPanchayatWardNo(String gramPanchayatWardNo) {
        this.gramPanchayatWardNo = gramPanchayatWardNo;
    }

    public String getVidhanSabhaWardNo() {
        return vidhanSabhaWardNo;
    }

    public void setVidhanSabhaWardNo(String vidhanSabhaWardNo) {
        this.vidhanSabhaWardNo = vidhanSabhaWardNo;
    }

    public String getJilaParishadGat() {
        return jilaParishadGat;
    }

    public void setJilaParishadGat(String jilaParishadGat) {
        this.jilaParishadGat = jilaParishadGat;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(villageName);
        parcel.writeString(occupation);
        parcel.writeString(bloodGroup);
        parcel.writeString(mobileNo);
        parcel.writeString(whatsAppNo);
        parcel.writeString(familyHead);
        parcel.writeString(wadiWastiName);
        parcel.writeString(gramPanchayatWardNo);
        parcel.writeString(vidhanSabhaWardNo);
        parcel.writeString(jilaParishadGat);
        parcel.writeString(information);
    }
}
