package com.example.saraiisraeli.give_n_take.models;

public class Item {
    private String itemName,itemLocation,itemMoreInfo,photoStr;
// without photo for now..
// only the name, location and a general explanation about the item.
    public Item(String itemName, String itemLocation, String itemMoreInfo,String photoStr) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemMoreInfo = itemMoreInfo;
        this.photoStr = photoStr;
    }

    public Item() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemMoreInfo() {
        return itemMoreInfo;
    }

    public void setItemMoreInfo(String itemMoreInfo) {
        this.itemMoreInfo = itemMoreInfo;
    }

    public String getPhotoStr() {
        return photoStr;
    }

    public void setPhotoStr(String photoStr) {
        this.photoStr = photoStr;
    }
}
