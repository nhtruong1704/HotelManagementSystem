package com.xuancanhit.hotelmanagementsystem.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employee implements Parcelable {
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("EmpName")
    @Expose
    private String empName;
    @SerializedName("EmpPhone")
    @Expose
    private String empPhone;
    @SerializedName("EmpAddress")
    @Expose
    private String empAddress;
    @SerializedName("EmpEmail")
    @Expose
    private String empEmail;
    @SerializedName("EmpIsVip")
    @Expose
    private String empIsVip;

    @SerializedName("EmpAvatar")
    @Expose
    private String empAvatar;
    @SerializedName("EmpDOB")
    @Expose
    private String empDOB;
    @SerializedName("EmpGender")
    @Expose
    private String empGender;




    protected Employee(Parcel in) {
        empId = in.readString();
        empName = in.readString();
        empPhone = in.readString();
        empAddress = in.readString();
        empEmail = in.readString();
        empIsVip = in.readString();
        empAvatar = in.readString();
        empDOB = in.readString();
        empGender = in.readString();

    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(empId);
        parcel.writeString(empName);
        parcel.writeString(empPhone);
        parcel.writeString(empAddress);
        parcel.writeString(empEmail);
        parcel.writeString(empIsVip);
        parcel.writeString(empAvatar);
        parcel.writeString(empDOB);
        parcel.writeString(empGender);

    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpIsVip() {
        return empIsVip;
    }

    public void setEmpIsVip(String empIsVip) {
        this.empIsVip = empIsVip;
    }



    public String getEmpAvatar() {
        return empAvatar;
    }

    public void setEmpAvatar(String empAvatar) {
        this.empAvatar = empAvatar;
    }

    public String getEmpDOB() {
        return empDOB;
    }

    public void setEmpDOB(String empDOB) {
        this.empDOB = empDOB;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

}
