package com.xuancanhit.hotelmanagementsystem.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodItem implements Parcelable {
    @SerializedName("FoodItemId")
    @Expose
    private String foodItemId;

    @SerializedName("FoodItemName")
    @Expose
    private String foodItemName;

    @SerializedName("FoodItemPrice")
    @Expose
    private String foodItemPrice;

    @SerializedName("FoodItemDes")
    @Expose
    private String foodItemDes;

    @SerializedName("FoodItemImage")
    @Expose
    private String foodItemImage;

    @SerializedName("FoodItemChefId")
    @Expose
    private String foodItemChefId;

    protected FoodItem(Parcel in) {
        foodItemId = in.readString();
        foodItemName = in.readString();
        foodItemPrice = in.readString();
        foodItemDes = in.readString();
        foodItemImage = in.readString();
        foodItemChefId = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodItemId);
        parcel.writeString(foodItemName);
        parcel.writeString(foodItemPrice);
        parcel.writeString(foodItemDes);
        parcel.writeString(foodItemImage);
        parcel.writeString(foodItemChefId);
    }

    public String getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public String getFoodItemPrice() {
        return foodItemPrice;
    }

    public void setFoodItemPrice(String foodItemPrice) {
        this.foodItemPrice = foodItemPrice;
    }

    public String getFoodItemDes() {
        return foodItemDes;
    }

    public void setFoodItemDes(String foodItemDes) {
        this.foodItemDes = foodItemDes;
    }

    public String getFoodItemImage() {
        return foodItemImage;
    }

    public void setFoodItemImage(String foodItemImage) {
        this.foodItemImage = foodItemImage;
    }

    public String getFoodItemChefId() {
        return foodItemChefId;
    }

    public void setFoodItemChefId(String foodItemChefId) {
        this.foodItemChefId = foodItemChefId;
    }
}
