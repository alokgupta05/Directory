package com.ajit.bjp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Village implements Parcelable {
    public String villageName;
    public String scheme;
    public String year;
    public String details;
    public String sanctionedAmt;
    public String distances;
    public String statusVillage;
    public String remarks;

    public Village(){

    }


    protected Village(Parcel in) {
        villageName = in.readString();
        scheme = in.readString();
        year = in.readString();
        details = in.readString();
        sanctionedAmt = in.readString();
        distances = in.readString();
        statusVillage = in.readString();
        remarks = in.readString();
    }

    public static final Creator<Village> CREATOR = new Creator<Village>() {
        @Override
        public Village createFromParcel(Parcel in) {
            return new Village(in);
        }

        @Override
        public Village[] newArray(int size) {
            return new Village[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(villageName);
        dest.writeString(scheme);
        dest.writeString(year);
        dest.writeString(details);
        dest.writeString(sanctionedAmt);
        dest.writeString(distances);
        dest.writeString(statusVillage);
        dest.writeString(remarks);
    }
}
