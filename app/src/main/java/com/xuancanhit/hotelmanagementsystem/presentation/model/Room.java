package com.xuancanhit.hotelmanagementsystem.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room implements Parcelable {
    @SerializedName("RoomId")
    @Expose
    private String roomId;

    @SerializedName("RoomName")
    @Expose
    private String roomName;

    @SerializedName("RoomPrice")
    @Expose
    private String roomPrice;

    @SerializedName("RoomDes")
    @Expose
    private String roomDes;

    @SerializedName("RoomImage")
    @Expose
    private String roomImage;

    @SerializedName("RoomHousekeepingId")
    @Expose
    private String roomHousekeepingId;

    protected Room(Parcel in) {
        roomId = in.readString();
        roomName = in.readString();
        roomPrice = in.readString();
        roomDes = in.readString();
        roomImage = in.readString();
        roomHousekeepingId = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(roomId);
        parcel.writeString(roomName);
        parcel.writeString(roomPrice);
        parcel.writeString(roomDes);
        parcel.writeString(roomImage);
        parcel.writeString(roomHousekeepingId);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomDes() {
        return roomDes;
    }

    public void setRoomDes(String roomDes) {
        this.roomDes = roomDes;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomHousekeepingId() {
        return roomHousekeepingId;
    }

    public void setRoomHousekeepingId(String roomHousekeepingId) {
        this.roomHousekeepingId = roomHousekeepingId;
    }
}
