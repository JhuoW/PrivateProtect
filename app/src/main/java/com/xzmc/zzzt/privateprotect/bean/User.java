package com.xzmc.zzzt.privateprotect.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zw on 17/5/8.
 */

public class User implements Parcelable {


    private String ID;
    private String name;
    private String image;
    private String sex;

    private String address;

    private String phone;
    private String expertise;//领域
    private String briefIntroduction;//简介
    private String role;//角色
    private String office;//科室
    private String position;//职位




    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getExpertise() {
        return expertise;
    }
    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
    public String getBriefIntroduction() {
        return briefIntroduction;
    }
    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getOffice() {
        return office;
    }
    public void setOffice(String office) {
        this.office = office;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public User() {
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(sex);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(expertise);
        dest.writeString(briefIntroduction);
        dest.writeString(role);
        dest.writeString(office);
        dest.writeString(position);
    }

    private User(Parcel in) {
        ID = in.readString();
        name = in.readString();
        image = in.readString();
        sex = in.readString();
        address = in.readString();
        phone = in.readString();
        expertise = in.readString();
        briefIntroduction = in.readString();
        role = in.readString();
        office = in.readString();
        position = in.readString();
    }
}
