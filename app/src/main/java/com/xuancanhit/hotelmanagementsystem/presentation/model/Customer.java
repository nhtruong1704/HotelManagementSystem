package com.xuancanhit.hotelmanagementsystem.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer implements Parcelable {
    @SerializedName("CusId")
    @Expose
    private String cusId;
    @SerializedName("CusName")
    @Expose
    private String cusName;
    @SerializedName("CusPhone")
    @Expose
    private String cusPhone;
    @SerializedName("CusAddress")
    @Expose
    private String cusAddress;
    @SerializedName("CusEmail")
    @Expose
    private String cusEmail;
    @SerializedName("CusIsVip")
    @Expose
    private String cusIsVip;
    @SerializedName("CusPassword")
    @Expose
    private String cusPassword;
    @SerializedName("CusAvatar")
    @Expose
    private String cusAvatar;
    @SerializedName("CusDOB")
    @Expose
    private String cusDOB;
    @SerializedName("CusGender")
    @Expose
    private String cusGender;


    @SerializedName("CusRoomId")
    @Expose
    private String cusRoomId;
    @SerializedName("CusFoodOrderId")
    @Expose
    private String cusFoodOrderId;

    protected Customer(Parcel in) {
        cusId = in.readString();
        cusName = in.readString();
        cusPhone = in.readString();
        cusAddress = in.readString();
        cusEmail = in.readString();
        cusIsVip = in.readString();
        cusPassword = in.readString();
        cusAvatar = in.readString();
        cusDOB = in.readString();
        cusGender = in.readString();
        cusRoomId = in.readString();
        cusFoodOrderId = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cusId);
        parcel.writeString(cusName);
        parcel.writeString(cusPhone);
        parcel.writeString(cusAddress);
        parcel.writeString(cusEmail);
        parcel.writeString(cusIsVip);
        parcel.writeString(cusPassword);
        parcel.writeString(cusAvatar);
        parcel.writeString(cusDOB);
        parcel.writeString(cusGender);
        parcel.writeString(cusRoomId);
        parcel.writeString(cusFoodOrderId);
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public String getCusIsVip() {
        return cusIsVip;
    }

    public void setCusIsVip(String cusIsVip) {
        this.cusIsVip = cusIsVip;
    }

    public String getCusPassword() {
        return cusPassword;
    }

    public void setCusPassword(String cusPassword) {
        this.cusPassword = cusPassword;
    }

    public String getCusAvatar() {
        return cusAvatar;
    }

    public void setCusAvatar(String cusAvatar) {
        this.cusAvatar = cusAvatar;
    }

    public String getCusDOB() {
        return cusDOB;
    }

    public void setCusDOB(String cusDOB) {
        this.cusDOB = cusDOB;
    }

    public String getCusGender() {
        return cusGender;
    }

    public void setCusGender(String cusGender) {
        this.cusGender = cusGender;
    }

    public String getCusRoomId() {
        return cusRoomId;
    }

    public void setCusRoomId(String cusRoomId) {
        this.cusRoomId = cusRoomId;
    }

    public String getCusFoodOrderId() {
        return cusFoodOrderId;
    }

    public void setCusFoodOrderId(String cusFoodOrderId) {
        this.cusFoodOrderId = cusFoodOrderId;
    }
}
