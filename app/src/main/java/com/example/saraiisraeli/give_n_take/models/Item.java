package com.example.saraiisraeli.give_n_take.models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private static final String TAG = "";
    private String itemName,itemLocation,itemDescription;
    private String photoURL;
// without photo for now..
// only the name, location and a general explanation about the item.
    public Item(String itemName, String itemLocation, String itemMoreInfo,String photoURL) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemDescription = itemMoreInfo;
        this.photoURL = photoURL;
    }

    public Item(String itemName, String itemLocation, String itemMoreInfo, Object photoURL) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemDescription = itemMoreInfo;
    }

    public Item() {
    }

    public Map<String,Object> ItemToMap()
    {
        Log.d(TAG, "Start Method: toMap");
        HashMap<String, Object> result = new HashMap<>();
        result.put("itemName",itemName);
        result.put("itemLocation",itemLocation);
        result.put("itemDescription",itemDescription);
        result.put("photoURL",photoURL);

        Log.d(TAG, "End Method: toMap");
        return result;
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
        return itemDescription;
    }

    public void setItemMoreInfo(String itemMoreInfo) {
        this.itemDescription = itemMoreInfo;
    }

    public String getPhotoStr() {
        return photoURL;
    }

    public void setPhotoStr(String photoStr) {
        this.photoURL = photoStr;
    }
}
