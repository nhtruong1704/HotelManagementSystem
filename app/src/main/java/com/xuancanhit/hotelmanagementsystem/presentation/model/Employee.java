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

    @SerializedName("EmpPosition")
    @Expose
    private String empPosition;

    @SerializedName("EmpSalary")
    @Expose
    private String empSalary;

    @SerializedName("EmpPhone")
    @Expose
    private String empPhone;

    @SerializedName("EmpAvatar")
    @Expose
    private String empAvatar;




    protected Employee(Parcel in) {
        empId = in.readString();
        empName = in.readString();
        empPosition = in.readString();
        empSalary = in.readString();
        empPhone = in.readString();
        empAvatar = in.readString();

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
        parcel.writeString(empPosition);
        parcel.writeString(empSalary);
        parcel.writeString(empPhone);
        parcel.writeString(empAvatar);

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

    public String getEmpPosition() {
        return empPosition;
    }

    public void setEmpPosition(String empPosition) {
        this.empPosition = empPosition;
    }

    public String getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(String empSalary) {
        this.empSalary = empSalary;
    }

    public String getEmpAvatar() {
        return empAvatar;
    }

    public void setEmpAvatar(String empAvatar) {
        this.empAvatar = empAvatar;
    }


}
