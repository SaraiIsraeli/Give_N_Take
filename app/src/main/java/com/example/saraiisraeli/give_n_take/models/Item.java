package com.example.saraiisraeli.give_n_take.models;

public class Item {
    private String itemName,itemLocation,itemMoreInfo;
// without photo for now..
// only the name, location and a general explanation about the item.
    public Item(String itemName, String itemLocation, String itemMoreInfo) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemMoreInfo = itemMoreInfo;
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
}
